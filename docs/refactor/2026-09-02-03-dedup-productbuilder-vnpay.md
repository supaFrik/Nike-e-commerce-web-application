# 03 — Dedup ProductBuilderService + VNPayPaymentService

- **Ngày:** 2026-09-02
- **Phạm vi:** `features/admin/product/service/ProductBuilderService.java`, `infras/payment/vnpay/service/VNPayPaymentService.java`
- **Loại:** Xóa duplication, dùng lại helper nội bộ (ladder: reuse → stdlib)

## Vấn đề

### ProductBuilderService (~70 dòng lặp)

`rebuildColorBlocks` (create) và `rebuildColorBlocksKeepingExistingIds` (update) cùng lặp `colorRequest → variantRequest / imageRequest`, chỉ khác: create luôn `new`, update `getOrDefault` theo `normalize(colorName)`/`normalize(size)`/`existingImageId` và track `kept*` + `removeIf`. Helper `rebuildVariantsKeepingExistingIds`/`rebuildImagesKeepingExistingIds` lặp cùng pattern.

Thêm 1 field mới phải sửa 4 nơi.

### VNPayPaymentService (song sinh + dead code)

`buildHashData` (457-476) và `buildEncodedQuery` (478-497) cùng sort keys + loop + skip empty + `URLEncoder.encode(value)`, chỉ khác `buildEncodedQuery` encode thêm key. `readInternalAmount()` (499-502) `@SuppressWarnings("unused")` không ai gọi.

## Thay đổi

### ProductBuilderService — trích helper chung

```java
// create: dùng newVariant / newImage
// update: dùng syncVariants / syncImages (getOrDefault + kept + removeIf)
// chung: applyColorFields, fillVariant, fillImage

// ponytail: shared helpers — create vs sync reuse same field mapping; add new field here once
private void applyColorFields(ProductColor c, AdminProductColorRequest req, Product p) { ... }
private void fillVariant(ProductVariant v, AdminProductVariantRequest req, ProductColor c) { ... }
private void fillImage(ProductImage img, ProductColor c, AdminProductImageRequest req,
                       Map<String, MultipartFile> uploadedFiles,
                       Map<Long, ProductImage> existingImages, String productName) { ... }
```

`rebuildColorBlocks` giờ gọi `applyColorFields` + `newVariant`/`newImage`. `rebuildColorBlocksKeepingExistingIds` gọi `applyColorFields` + `syncVariants`/`syncImages`. Thêm field chỉ sửa `fill*`.

Net: ~-40 dòng trùng, cấu trúc giữ nguyên, behavior không đổi.

### VNPayPaymentService — gộp query builders

```java
private String buildHashData(Map<String, String> params) {
    // ponytail: hash needs raw keys, query needs encoded keys — shared sort+loop in buildSortedQuery
    return buildSortedQuery(params, false);
}
private String buildEncodedQuery(Map<String, String> params) { return buildSortedQuery(params, true); }
private String buildSortedQuery(Map<String, String> params, boolean encodeKey) {
    List<String> keys = new ArrayList<>(params.keySet()); Collections.sort(keys);
    StringBuilder out = new StringBuilder();
    for (String k : keys) {
        String v = params.get(k);
        if (v == null || v.isEmpty()) continue;
        if (!out.isEmpty()) out.append('&');
        String key = encodeKey ? URLEncoder.encode(k, StandardCharsets.UTF_8) : k;
        out.append(key).append('=').append(URLEncoder.encode(v, StandardCharsets.UTF_8));
    }
    return out.toString();
}
// xóa: readInternalAmount()
```

Net: -1 method dead, -1 duplicate loop (~30 dòng).

## Vérification

- `mvn compile -q` → pass.
- So sánh before/after: hash/query khác nhau đúng 1 chỗ encode key — behavior giữ nguyên.
- `ProductBuilderService` create vs update đều qua `fill*` — thêm field chỉ sửa 1 nơi.

## Khi nào tách lại

- `ProductBuilderService`: nếu create/update diverge mạnh (ví dụ audit log chỉ cho update), tách lại 2 path nhưng giữ `fill*` chung.
- `VNPayPaymentService`: nếu hash spec đổi (ví dụ không sort hoặc encode khác), tách lại 2 method.

## Tham chiếu

- `features/admin/product/service/ProductBuilderService.java:29-174`
- `infras/payment/vnpay/service/VNPayPaymentService.java:457-502`
