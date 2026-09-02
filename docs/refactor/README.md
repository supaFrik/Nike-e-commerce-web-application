# Refactor Log — Nike Ecommerce

Quy ước ponytail: mỗi lần refractor ghi 1 file `YYYY-MM-DD-NN-ten.md` vào `docs/refactor/`.

- `NN` tăng dần trong ngày (01, 02, 03...).
- Mỗi file gồm: Ngày / Phạm vi / Loại / Vấn đề / Thay đổi (diff) / Vérification / Rủi ro & `ponytail:` ceiling.
- Template tham khảo 3 file đầu (2026-09-02): prune-dependencies, inline-checkout-strategy, dedup-productbuilder-vnpay.

## Mục lục

| # | Ngày | Tên | Phạm vi |
|---|------|-----|---------|
| 01 | 2026-09-02 | Prune Dependencies | `pom.xml` |
| 02 | 2026-09-02 | Inline CheckoutPaymentHandler | `features/checkout/service/` |
| 03 | 2026-09-02 | Dedup ProductBuilderService + VNPayPaymentService | `features/admin/product/service/`, `infras/payment/vnpay/` |
| 04 | 2026-09-02 | Prune Administrator Assets | `src/main/resources/administrator/` |
| 05 | 2026-09-02 | Remove Entire administrator Bundle | `src/main/resources/administrator/` (27 MB) |
| 06 | 2026-09-02 | Unify String Helpers | `shared/util/StringUtil` + 3 services |
| 07 | 2026-09-02 | Remove Validation Duplication | `features/checkout/service/CheckoutService` |
| 08 | 2026-09-02 | Dedup GlobalExceptionHandler | `shared/exception/GlobalExceptionHandler` |
| 09 | 2026-09-02 | Remove Dead Shopper Abstraction | `features/user/request+entity+service` (4 files) |

Thêm dòng mới vào bảng mỗi lần tạo file. Không xóa lịch sử — chỉ append.
