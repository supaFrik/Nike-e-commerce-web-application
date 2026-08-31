# Docker Commands --- Nike Ecommerce Web Application

Tài liệu ghi chú các lệnh Docker dùng để chạy và kiểm tra toàn bộ stack
của project.

## 1. Architecture

Project hiện tại chạy các service chính:

``` text
Spring Boot App
      │
      ├── MySQL
      │
      └── Actuator
             │
             ↓
          Prometheus
             │
             ↓
           Grafana

MySQL
   │
   ↓
MySQL Exporter
   │
   ↓
Prometheus
```

Các service:

  Service           Container                                      Port
  ----------------- ------------------------------------------ --------
  Spring Boot App   `nikeecommercewebapplication-app-1`          `8080`
  MySQL             `nikeecommercewebapplication-mysql-db-1`     `3306`
  Prometheus        `nike-prometheus`                            `9090`
  Grafana           `nike-grafana`                               `3001`
  MySQL Exporter    `nike-mysql-exporter`                        `9104`

------------------------------------------------------------------------

## 2. Start toàn bộ stack

Đây là lệnh chính nên dùng:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml up -d --build
```

Lệnh này:

-   Build lại Spring Boot image.
-   Start MySQL.
-   Start Spring Boot App.
-   Start Prometheus.
-   Start Grafana.
-   Start MySQL Exporter.

Sau khi chạy, kiểm tra:

``` powershell
docker ps
```

Trạng thái mong muốn:

``` text
MySQL             Up (healthy)
Spring Boot       Up
Prometheus        Up
Grafana           Up
MySQL Exporter    Up
```

------------------------------------------------------------------------

## 3. Start mà không build lại App

Nếu không thay đổi source code hoặc Dockerfile:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml up -d
```

Dùng lệnh này thường xuyên hơn `--build` để tránh build không cần thiết.

------------------------------------------------------------------------

## 4. Restart monitoring stack

Nếu chỉ thay đổi cấu hình Prometheus, Grafana hoặc MySQL Exporter:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml restart prometheus grafana mysql-exporter
```

Nếu chỉ sửa `prometheus.yml` hoặc `alerting-rules.yml`:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml restart prometheus
```

Nếu chỉ sửa Grafana provisioning:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml restart grafana
```

------------------------------------------------------------------------

## 5. Rebuild Spring Boot App

Nếu chỉ thay đổi code Spring Boot:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml up -d --build app
```

Không cần rebuild Prometheus hoặc Grafana.

------------------------------------------------------------------------

## 6. Check container status

``` powershell
docker ps
```

Xem cả container đã stopped:

``` powershell
docker ps -a
```

Đặc biệt chú ý các trạng thái:

``` text
Up
Up (healthy)
Restarting
Exited
```

Nếu Prometheus hoặc Exporter hiện `Restarting`, xem log ngay.

------------------------------------------------------------------------

## 7. Check logs

### Prometheus

``` powershell
docker logs nike-prometheus --tail 50
```

Theo dõi realtime:

``` powershell
docker logs -f nike-prometheus
```

### MySQL Exporter

``` powershell
docker logs nike-mysql-exporter --tail 50
```

Realtime:

``` powershell
docker logs -f nike-mysql-exporter
```

### Grafana

``` powershell
docker logs nike-grafana --tail 50
```

### Spring Boot

``` powershell
docker logs nikeecommercewebapplication-app-1 --tail 50
```

### MySQL

``` powershell
docker logs nikeecommercewebapplication-mysql-db-1 --tail 50
```

------------------------------------------------------------------------

## 8. Check Spring Boot Actuator

Từ Windows:

``` powershell
curl http://localhost:8080/actuator/health
```

Kiểm tra Prometheus metrics:

``` powershell
curl http://localhost:8080/actuator/prometheus
```

Nếu endpoint hoạt động, Prometheus có thể scrape metrics từ Spring Boot.

------------------------------------------------------------------------

## 9. Check MySQL Exporter

Kiểm tra exporter có expose metrics:

``` powershell
docker exec nike-mysql-exporter sh -c "wget -qO- http://127.0.0.1:9104/metrics | grep mysql_up"
```

Kết quả đúng:

``` text
# HELP mysql_up Whether the MySQL server is up.
# TYPE mysql_up gauge
mysql_up 1
```

Ý nghĩa:

``` text
mysql_up 1
```

→ MySQL Exporter kết nối được MySQL.

``` text
mysql_up 0
```

→ Exporter đang chạy nhưng không kết nối được MySQL.

Nếu container exporter đang `Restarting`, không thể `docker exec` vào
nó. Hãy xem:

``` powershell
docker logs nike-mysql-exporter --tail 50
```

------------------------------------------------------------------------

## 10. Check Prometheus

Health check:

``` powershell
docker exec nike-prometheus wget -qO- http://localhost:9090/-/healthy
```

Sau đó mở:

``` text
http://localhost:9090
```

Vào:

``` text
Status → Targets
```

Kiểm tra các target.

Mục tiêu là các target cần thiết đều:

``` text
UP
```

Kiểm tra alert:

``` text
http://localhost:9090/alerts
```

Hoặc:

``` text
Status → Rules
```

------------------------------------------------------------------------

## 11. Check Docker network

Các monitoring service phải cùng Docker network:

``` powershell
docker network inspect nike-network
```

Các container cần giao tiếp nội bộ qua hostname Docker, ví dụ:

``` text
mysql-db
mysql-exporter
prometheus
grafana
```

Không dùng `localhost` để một container truy cập container khác.

Ví dụ:

``` text
Prometheus → mysql-exporter:9104
Grafana → prometheus:9090
MySQL Exporter → mysql-db:3306
```

`localhost` bên trong container chỉ trỏ về chính container đó.

------------------------------------------------------------------------

## 12. Important: hostname vs localhost

Từ Windows browser:

``` text
http://localhost:9090
http://localhost:3001
http://localhost:8080
```

Từ container:

``` text
http://prometheus:9090
http://grafana:3000
http://mysql-exporter:9104
mysql-db:3306
```

Ví dụ Prometheus scrape MySQL Exporter:

``` yaml
scrape_configs:
  - job_name: mysql
    static_configs:
      - targets:
          - mysql-exporter:9104
```

Không dùng:

``` yaml
targets:
  - localhost:9104
```

vì `localhost` sẽ trỏ tới container Prometheus.

------------------------------------------------------------------------

## 13. MySQL Exporter credentials

File:

``` text
docker/mysql-exporter/.my.cnf
```

Ví dụ:

``` ini
[client]
user=exporter
password=YOUR_PASSWORD
host=mysql-db
port=3306
```

Kiểm tra file từ PowerShell:

``` powershell
Get-Content .\docker\mysql-exporter\.my.cnf
```

Kiểm tra user trong MySQL:

``` powershell
docker exec -it nikeecommercewebapplication-mysql-db-1 mysql -uroot -p
```

Trong MySQL:

``` sql
SELECT user, host
FROM mysql.user
WHERE user = 'exporter';
```

Kiểm tra grants:

``` sql
SHOW GRANTS FOR 'exporter'@'%';
```

Exporter cần có quyền phù hợp để collect MySQL metrics.

------------------------------------------------------------------------

## 14. Check mounted configuration

Khi gặp lỗi kiểu:

``` text
is a directory
```

hoặc:

``` text
no configuration found
```

kiểm tra host path.

Ví dụ:

``` powershell
Get-Item .\docker\prometheus\prometheus.yml |
    Format-List FullName,PSIsContainer,Length
```

Và:

``` powershell
Get-Item .\docker\prometheus\alerting-rules.yml |
    Format-List FullName,PSIsContainer,Length
```

Kết quả phải có:

``` text
PSIsContainer : False
```

Nếu:

``` text
PSIsContainer : True
```

thì path đang là directory thay vì file.

------------------------------------------------------------------------

## 15. Check Compose configuration

Trước khi chạy stack, có thể kiểm tra Compose đã resolve configuration
đúng chưa:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml config
```

Check service list:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml config --services
```

Expected:

``` text
prometheus
grafana
mysql-db
mysql-exporter
app
```

------------------------------------------------------------------------

## 16. Stop toàn bộ stack

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml down
```

Lệnh này stop và remove containers/network được Compose quản lý.

------------------------------------------------------------------------

## 17. Stop và xóa volumes

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml down -v
```

**Cẩn thận:** `-v` xóa volumes.

Không nên dùng lệnh này thường xuyên.

Đặc biệt project đang có:

``` yaml
volumes:
  grafana_data:
```

Xóa volume có thể làm mất dữ liệu Grafana như dashboards và datasource
đã lưu trong volume.

Chỉ dùng `down -v` khi thực sự muốn reset environment.

------------------------------------------------------------------------

## 18. Full reset khi Docker environment bị lỗi

Nếu stack bị lỗi nghiêm trọng và muốn dựng lại:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml down
```

Sau đó:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml up -d --build
```

Chỉ khi cần reset persistent data mới dùng:

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml down -v
```

------------------------------------------------------------------------

## 19. Các URL cần nhớ

  Service               URL
  --------------------- ---------------------------------------------
  Spring Boot           `http://localhost:8080`
  Actuator Health       `http://localhost:8080/actuator/health`
  Actuator Prometheus   `http://localhost:8080/actuator/prometheus`
  Prometheus            `http://localhost:9090`
  Prometheus Alerts     `http://localhost:9090/alerts`
  Grafana               `http://localhost:3001`
  MySQL Exporter        `http://localhost:9104/metrics`

`mysql-exporter:9104` là hostname nội bộ Docker. Không cần mở URL này
trực tiếp trên browser để hệ thống hoạt động.

------------------------------------------------------------------------

## 20. Daily workflow

Thông thường chỉ cần:

### Start project

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml up -d --build
```

### Check

``` powershell
docker ps
```

### Nếu có vấn đề

``` powershell
docker logs nike-prometheus --tail 50
docker logs nike-mysql-exporter --tail 50
docker logs nike-grafana --tail 50
docker logs nikeecommercewebapplication-app-1 --tail 50
```

### Sau khi sửa monitoring config

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml restart prometheus grafana mysql-exporter
```

### Stop

``` powershell
docker compose -f docker-compose.yml -f docker/monitoring.yml down
```

------------------------------------------------------------------------

## 21. Quick troubleshooting order

Khi monitoring không hoạt động, kiểm tra theo đúng thứ tự:

``` text
1. docker ps
       ↓
2. MySQL = Up (healthy)?
       ↓
3. App = Up?
       ↓
4. MySQL Exporter = Up?
       ↓
5. mysql_up = 1?
       ↓
6. Prometheus = Up?
       ↓
7. Prometheus → Status → Targets = UP?
       ↓
8. Prometheus → Rules / Alerts
       ↓
9. Grafana datasource
       ↓
10. Grafana dashboard
```

Đừng nhảy thẳng vào Grafana khi Prometheus hoặc Exporter đang lỗi.
Monitoring stack có dependency chain; phải kiểm tra từ dưới lên trên.
