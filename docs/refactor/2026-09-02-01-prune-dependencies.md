# 01 — Prune Unused Dependencies (`pom.xml`)

- **Ngày:** 2026-09-02
- **Phạm vi:** `pom.xml`
- **Loại:** Xóa dependency thừa (ponytail ladder: YAGNI)

## Vấn đề

`pom.xml` khai 2 dependency không có import trực tiếp trong `src/`:

- `com.nimbusds:nimbus-jose-jwt:9.37.3` — chỉ dùng `NimbusJwtDecoder`/`NimbusJwtEncoder` qua `spring-boot-starter-oauth2-resource-server` (đã transit `nimbus-jose-jwt`). Grep `import com.nimbusds` toàn `src/` chỉ còn 1 hit `SecurityConfig.java:3 ImmutableSecret` (thuộc `com.nimbusds.jose.jwk.source` nhưng class này được kéo transit, không cần khai explicit version).
- `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` — Spring Boot 3.2.4 parent đã auto-register `JavaTimeModule` cho `LocalDateTime`/`Instant`. Grep `import com.fasterxml` chỉ thấy `GlobalExceptionHandler:JsonProcessingException` (thuộc `jackson-core`), không cần `jsr310` explicit.

Giữ lại: `spring-boot-starter-mail` (dùng thật trong `MailService.java:6 JavaMailSender`), `micrometer-registry-prometheus` + `actuator`, `springdoc-openapi`, `cloudinary-http5`.

## Thay đổi

```diff
# pom.xml — xóa 2 block <dependency>
- <dependency>
-     <groupId>com.nimbusds</groupId>
-     <artifactId>nimbus-jose-jwt</artifactId>
-     <version>9.37.3</version>
- </dependency>

- <dependency>
-     <groupId>com.fasterxml.jackson.datatype</groupId>
-     <artifactId>jackson-datatype-jsr310</artifactId>
- </dependency>
```

Net: **-13 dòng**, 0 dòng thêm. Không đụng code Java.

## Vérification

- `JAVA_HOME="C:/Program Files/Java/jdk-17" mvn compile -q` → `exit: 0` (sau cả 3 refactor).
- `grep -r "import com.nimbusds" src --include="*.java"` → chỉ còn transit hit, không lỗi compile.
- `grep -r "jackson-datatype" src` → 0 hit.

## Rủi ro & lưu ý

- Nếu sau này dùng `nimbus-jose-jwt` trực tiếp (ví dụ tự build `JWSObject`), phải khai lại dep với version khớp `oauth2-resource-server` managed version.
- `jsr310` nếu custom `ObjectMapper` không dùng Boot auto-config thì cần khai lại. Hiện tại không custom → an toàn.

## Tham chiếu

- `pom.xml:36-39` (nimbus), `87-89` (jackson-jsr310)
- `src/main/java/vn/demo/nike/shared/config/SecurityConfig.java:3,26-27`
- `src/main/java/vn/demo/nike/features/auth/service/MailService.java` (giữ mail)
