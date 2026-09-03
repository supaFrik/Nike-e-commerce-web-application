# BEFORE Benchmark — Product List Sort Bottleneck (50k dataset)

- **Ngày:** 2026-09-03
- **Dataset:** 50k (seed_50k.sql — deterministic, `scripts/seed_50k.sql`)
- **Endpoint:** `GET /products/list/data?categoryId=3&sort=newest&page=0` → `ProductRepository.findProductList`
- **Trạng thái:** BEFORE 2 runs + AFTER 2 runs load measured (same workload/dataset A 50k), index V7 applied, EXPLAIN Sort eliminated
- **Kết luận:** DB core 16.6→0.025ms (664x), k6 p95 25.27→5.04 (−80%) avg 4 runs, Hikari/CPU/GC không saturate — bottleneck xác nhận là DB Sort. Validity trung bình (2 vs 2, chưa 5-run), Grafana correlate đã ghi.

## 1. Dataset

| Table | COUNT(*) | information_schema.table_rows | Data | Index |
|-------|----------|-------------------------------|------|-------|
| products | 50120 | 49656 | 9.4 MB | 3.1 MB |
| product_colors | 50120 | 49950 | 4.7 MB | 1.5 MB |
| product_images | 50120 | 49864 | 8.9 MB | 1.5 MB |
| product_variants | 100480 | 99706 | 9.5 MB | 8.9 MB |

Seed: `scripts/seed_50k.sql` — deterministic từ `n` (không `RAND()`), 4 categories đều (~12.5k/cat), `create_date` phân tán 730 ngày `2023-01-01→2024-12-30`, `sale_price < price`, timestamps deterministic. Procedure: rollback → seed 1 lần → `ANALYZE TABLE` → BEFORE/AFTER cùng dataset A (không re-seed giữa chừng).

Verify:
```powershell
Get-Content scripts/seed_50k.sql | docker exec -i nikeecommercewebapplication-mysql-db-1 mysql -uroot -p${MYSQL_ROOT_PASSWORD} nike_store
docker exec nikeecommercewebapplication-mysql-db-1 mysql -uroot -p${MYSQL_ROOT_PASSWORD} nike_store -e "SELECT table_name, table_rows FROM information_schema.tables WHERE table_schema='nike_store' ORDER BY table_rows DESC;"
```

## 2. Repository → SQL (đang benchmark)

`ProductListPageController.listData:47` → `ProductListService.getProductList:24` (PageRequest 0,20 sort `createDate DESC`) → `ProductRepository.findProductList:53`:

```sql
SELECT new ProductListItemView(p.id, ..., MIN(pi.url), SIZE(p.colors))
FROM Product p LEFT JOIN p.category c WHERE (:categoryId IS NULL OR c.id=:categoryId)
-- countQuery: SELECT COUNT(p) ...
```

Actual SQL MySQL (từ JPQL) tương đương:

```sql
SELECT p.id, p.name, p.price, p.sale_price,
  (SELECT MIN(pi.url) FROM product_images pi JOIN product_colors pc ON pi.color_id=pc.id
   WHERE pc.product_id=p.id AND pi.is_main=1) AS imageUrl
FROM products p JOIN category c ON p.category_id=c.id
WHERE p.category_id=3 ORDER BY p.create_date DESC LIMIT 20;
```

Indexes hiện có (`SHOW INDEX FROM products`): `PRIMARY(id)`, `idx_products_category_id(category_id)`, `idx_products_status(product_status)` — **không có index trên `create_date`**.

## 3. EXPLAIN ANALYZE — BEFORE (FACT, 50k)

**Query tối giản (core sort):**
```sql
EXPLAIN ANALYZE SELECT p.id FROM products p WHERE p.category_id=3 ORDER BY p.create_date DESC LIMIT 20;
```
```
-> Limit: 20 row(s)  (cost=2639 rows=20) (actual time=16.6..16.6 rows=20 loops=1)
    -> Sort: p.create_date DESC, limit input to 20 row(s) per chunk  (cost=2639 rows=22048) (actual time=16.6..16.6 rows=20 loops=1)
        -> Index lookup on p using idx_products_category_id (category_id=3)  (cost=2639 rows=22048) (actual time=8.21..15.7 rows=12518 loops=1)
```

**Query full (với JOIN + subquery — đúng ProductRepository):**
```
-> Limit: 20 row(s)  (cost=2639 rows=20) (actual time=12.1..12.1 rows=20 loops=1)
    -> Sort: p.create_date DESC (cost=2639 rows=22048) (actual time=12.1..12.1 rows=20 loops=1)
        -> Index lookup on p using idx_products_category_id (cost=2639 rows=22048) (actual time=0.194..10.9 rows=12518 loops=1)
-> Select #2 (subquery; dependent)  loops=20
    -> Aggregate: min(pi.url) (actual 0.008ms/loop)
        -> Nested loop pc (idx_product_colors_product_id) + pi (idx_product_images_color_id, is_main=1)
```

So với 112 rows BEFORE: cost 13→2639, rows 18→12518, actual 0.56→16.6ms, Sort vẫn tồn tại. Threshold >5ms đã vượt → hypothesis validated.

Lấy lại:
```powershell
docker exec nikeecommercewebapplication-mysql-db-1 mysql -uroot -p${MYSQL_ROOT_PASSWORD} nike_store -e "EXPLAIN ANALYZE SELECT p.id FROM products p WHERE p.category_id=3 ORDER BY p.create_date DESC LIMIT 20;" 2>&1
docker exec nikeecommercewebapplication-mysql-db-1 mysql -uroot -p${MYSQL_ROOT_PASSWORD} nike_store -e "SHOW INDEX FROM products;" 2>&1
```

## 4. k6 BEFORE — 2 runs (50k, load workload `10→50→100→0`, sleep 1s)

Run 1 (2026-09-03 1st):
`product_list_duration avg=18.39 med=17.23 p90=23.65 p95=26.56 max=50.23` — http_req_duration cùng, http_reqs 4742 RPS 39.33, iterations 4742, vus_max 100, error 0%, checks 14226
Run 2 (same, cooldown ~10m):
`avg=17.38 med=16.54 p90=22.24 p95=23.99 max=35.83` — http_reqs 4744 RPS 39.41, iterations 4744
Avg 2 runs: p95 25.27ms, med 16.88ms, avg 17.88ms, RPS 39.37. So với 112 rows baseline smoke p95 2.28 → +23ms DB sort cost.

**Grafana BEFORE snapshot (Last 15m, Uptime 1.2h, at Before):**
System CPU mean 0.0197 last 0.0105 max 0.157, Process CPU mean 0.00161 last 0.000334 max 0.0109, Load 0.124 last 0.228 max 0.626, CPU 12 cores, Heap: G1 Eden 58.3 MiB mean (20 last, 124 max) / G1 Old 65.5 MiB mean (67.1 last), Survivor 6.53 MiB, Metaspace 110 MiB, Classes 20546, Threads Daemon 22.9 mean (22 last), GC count mean 0.016 total 1, GC duration mean 82µs max 3.00ms total 5.00ms, Hikari Active 0 max 0 Idle 10 Pending 0, k6_http_reqs_total / k6_http_req_failed_rate visible with test_type=load tag, HTTP Response Time No data (uri UNKNOWN).

## 5. Optimization — Applied (V7)

```sql
-- V7__add_index_products_category_create_date.sql
CREATE INDEX idx_products_category_create_date ON products (category_id, create_date DESC);
-- + ANALYZE TABLE products
```

EXPLAIN AFTER (50k, same queries):
```
Core:  -> Limit 20 (cost=2451) -> Covering index lookup idx_products_category_create_date (category_id=3) actual 0.025..0.028 rows=20 — no Sort
Full:  -> Limit 20 (cost=2639) -> Index lookup idx_products_category_create_date actual 0.19..0.19 rows=20 — no Sort + subquery 0.008ms/loop
```
Sort eliminated, core 16.6ms→0.025ms (664x), full 12.1ms→0.19ms (63x), cost 2639→2451. `SHOW INDEX` confirms `idx_products_category_create_date (category_id ASC, create_date DESC)` cardinality 47545.

## 6. k6 AFTER — 2 runs (50k, same workload/dataset A, V7 applied)

Run 1 After: `avg=3.82 med=3.72 p90=4.36 p95=4.76 max=7.53` — http_reqs 4812 RPS 39.89, iterations 4812
Run 2 After: `avg=4.05 med=3.88 p90=4.78 p95=5.33 max=28.24` — http_reqs 4812 RPS 39.88, iterations 4812
Avg 2 runs After: p95 5.04ms, med 3.80ms, avg 3.93ms, RPS 39.88, max 17.88 avg. Grafana Last 15m at time of run2 After: System CPU mean 0.0223 last 0.00417 max 0.158, Process CPU mean 0.00222 last 0.000667 max 0.0106, Load 0.172 last 0.0913 max 0.684, CPU 12 cores, Heap Used 3.4% Non-Heap 13.7% Uptime 1.0h Start 1h ago, Hikari Active mean 0.18 last 0 max 6 Idle 9.82 last 10 Pending 0, GC count mean 0.023 total 1.4 duration mean 62.3µs max 600µs total 3.80ms. Previous After run1 similar. HTTP Response Time No data (uri UNKNOWN) both.

## 7. BEFORE vs AFTER (measured, 2 vs 2 runs — validity trung bình)

| Metric | Before (avg 2 runs) | After (avg 2 runs) | Improvement | Notes |
|--------|---------------------|--------------------|-------------|-------|
| k6 p50 (med) | 16.88ms | 3.80ms | −77.5% | |
| k6 p95 | 25.27ms | 5.04ms | −80.0% | threshold p95<500 |
| k6 avg | 17.88ms | 3.93ms | −78.0% | |
| k6 max | 43.03ms (avg) | 17.88ms (avg) | −58.4% | run2 spike 28ms |
| Throughput (RPS) | 39.37/s | 39.88/s | +1.3% | sleep 1s caps RPS |
| Error rate | 0.00% | 0.00% | — | |
| Iterations | 4743 avg | 4812 avg | — | same 120s |
| DB actual core | 16.6ms | 0.025ms | −99.8% | EXPLAIN core |
| DB actual full | 12.1ms | 0.19ms | −98.4% | with subquery |
| DB cost core | 2639 | 2451 | −7.1% | cost est limited |
| Rows examined | 12518 | 20 | — | index prunes |
| Access type | Index lookup + Sort | Index lookup no Sort | — | |
| Sort eliminated? | Yes | No | — | FACT |
| Hikari Pending max | 0 | 0 | — | Grafana 15m both |
| Hikari Active max | 0 | 6 | — | Before 0, After 6 peak |
| System CPU mean/last/max | 0.0197/0.0105/0.157 | 0.0223/0.00417/0.158 | — | Before vs After snapshot |
| Process CPU mean/last/max | 0.00161/0.000334/0.0109 | 0.00222/0.000667/0.0106 | — | |
| GC count/total | 1 / 5.00ms | 1.4 / 3.8ms | — | G1 Evacuation; Before duration 82µs mean 3ms max, After 62.3µs mean 600µs max |
| Heap Used | — (Eden 58.3MiB Old 65.5MiB) | 3.4% (Eden/heap 3.4%) | — | not saturate |
| Uptime | 1.2h | 1.0h | — | After snapshot earlier |

Grafana SpringBoot APM Dashboard Before (1.2h uptime) vs After (1.0h uptime, 15m window): Hikari Pending 0 both, CPU low both, GC negligible — bottleneck was DB Sort, not pool/CPU. Response Time No data both (uri UNKNOWN). k6 metrics visible with test_type=load tag. Before heap detail: Eden 58.3MiB, Old 65.5MiB, Survivor 6.53MiB, Metaspace 110MiB, Classes 20546, Threads Daemon 22.9, After heap 3.4% — consistent.

## 8. Validity Check

**Đã có FACT:**
- 50k dataset A frozen (50120 COUNT(*), 49656 table_rows), không re-seed
- EXPLAIN core 16.6→0.025ms Sort eliminated validated same queries
- k6 2 vs 2 same workload `10→50→100→0` sleep 1s: p95 25.27→5.04 (−80%), p50 16.88→3.80 (−77%)
- Grafana: Hikari Pending 0, Active max 6/10, CPU ~0, GC 3.8ms total — không saturate, improvement từ DB

**Hạn chế (chưa claim 5-run CV):**
- Chỉ 2 vs 2, chưa thực hiện 5-run avg + stddev <10%
- Workload ramp `10→50→100→0` không steady isolation, chưa pin Docker CPU
- Throughput sleep-capped nên RPS không phản ánh capacity gain

**Yếu tố giữ nguyên:** dataset A 50k, workload `http://app:8080` nike-network, V7 chỉ 1 DDL, buffer pool warm, no code/config change.

## 9. Next Steps

1. Chạy thêm 3 BEFORE + 3 AFTER same `load` workload (đủ 5+5), cooldown 60s, ghi p50/p95/p99/RPS.
2. Fix `uri` tag để Grafana Response Time có data (cần `management.metrics.tags` hoặc custom tag).
3. Update §7 avg 5 vs 5 + stddev.
4. Không rollback/re-seed. V7 giữ.
5. CV bullet khi đủ 5+5: "Measured /products/list/data p95 25.3ms→5.0ms (−80%) at 100 VU peak / 120s on 50k products (12.5k/cat) after composite index (category_id, create_date), EXPLAIN 16.6→0.02ms Sort eliminated, Hikari/CPU not saturate, same workload/dataset, Flyway V7"

---
*Evidence-based: không fabricate After, không claim improved X% khi chưa đo. 10k actual <1.89ms chưa đủ threshold — 50k actual 16.6ms mới đủ.*
