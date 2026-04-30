# Admin Product Add Backend Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the backend-only admin product creation flow with multipart JSON plus image upload, local file storage, and safe DTO-based persistence.

**Architecture:** Keep `AdminProductController` as a thin multipart boundary that parses `productData` and delegates the use case. Split orchestration, storage, and DTO concerns so `AdminProductService` stops acting like a god class and the catalog entities remain persistence targets only.

**Tech Stack:** Spring Boot 3.2, Spring MVC multipart support, Jakarta Validation, JPA, MySQL, JUnit 5, MockMvc, MockMultipartFile

---

## File Structure

### New files

- `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductCreateRequest.java`
- `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductColorRequest.java`
- `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductImageRequest.java`
- `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductVariantRequest.java`
- `src/main/java/vn/demo/nike/features/admin/product/dto/AdminCreatedProductResponse.java`
- `src/main/java/vn/demo/nike/features/admin/product/storage/ProductImageStorageProperties.java`
- `src/main/java/vn/demo/nike/features/admin/product/storage/ProductImageStorageService.java`
- `src/main/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageService.java`
- `src/main/java/vn/demo/nike/features/admin/product/exception/InvalidProductImageMappingException.java`
- `src/main/java/vn/demo/nike/features/admin/product/exception/InvalidProductImageFileException.java`
- `src/test/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageServiceTest.java`
- `src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java`
- `src/test/java/vn/demo/nike/features/admin/product/api/AdminProductControllerTest.java`

### Modified files

- `src/main/java/vn/demo/nike/features/admin/product/api/AdminProductController.java`
- `src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java`
- `src/main/resources/application.properties`
- `src/main/resources/application-dev.properties`
- `src/main/resources/application-prod.properties`
- `src/main/java/vn/demo/nike/shared/exception/GlobalExceptionHandler.java`

### Existing files to reference while implementing

- `src/main/java/vn/demo/nike/features/catalog/product/domain/Product.java`
- `src/main/java/vn/demo/nike/features/catalog/product/domain/ProductColor.java`
- `src/main/java/vn/demo/nike/features/catalog/product/domain/ProductImage.java`
- `src/main/java/vn/demo/nike/features/catalog/product/domain/ProductVariant.java`
- `src/main/java/vn/demo/nike/features/catalog/category/repository/CategoryRepository.java`
- `src/main/java/vn/demo/nike/features/catalog/product/repository/ProductRepository.java`

### Task 1: Add Admin Request and Response DTOs

**Files:**
- Create: `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductCreateRequest.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductColorRequest.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductImageRequest.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/dto/AdminProductVariantRequest.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/dto/AdminCreatedProductResponse.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java`

- [ ] **Step 1: Write the failing service test for bean-valid payload handling**

```java
@Test
void shouldRejectReferencedImageWhenClientKeyIsBlank() {
    AdminProductImageRequest image = new AdminProductImageRequest(
            " ",
            "Hero",
            "Hero alt",
            0,
            true
    );

    Set<ConstraintViolation<AdminProductImageRequest>> violations = validator.validate(image);

    assertThat(violations).isNotEmpty();
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -Dtest=AdminProductServiceTest#shouldRejectReferencedImageWhenClientKeyIsBlank test`

Expected: FAIL because admin DTO classes do not exist yet.

- [ ] **Step 3: Write minimal DTO implementation with Jakarta Validation**

```java
public record AdminProductImageRequest(
        @NotBlank(message = "Image clientKey is required")
        String clientKey,
        @Size(max = 255)
        String title,
        @Size(max = 512)
        String altText,
        @NotNull(message = "Image orderIndex is required")
        Integer orderIndex,
        @NotNull(message = "Image hero flag is required")
        Boolean isMainForColor
) {}
```

```java
public record AdminProductCreateRequest(
        @NotBlank(message = "Product name must not be blank")
        @Size(max = 255)
        String name,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be >= 0")
        BigDecimal price,
        @DecimalMin(value = "0.0", message = "Sale price must be >= 0")
        BigDecimal salePrice,
        @NotBlank(message = "Description must not be blank")
        String description,
        @NotBlank(message = "Type must not be blank")
        String type,
        @NotNull(message = "Category is required")
        Long categoryId,
        @NotNull(message = "Product status is required")
        ProductStatus productStatus,
        @NotEmpty(message = "Product must have at least one color")
        @Valid
        List<AdminProductColorRequest> colors
) {}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -Dtest=AdminProductServiceTest#shouldRejectReferencedImageWhenClientKeyIsBlank test`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/vn/demo/nike/features/admin/product/dto src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java
git commit -m "feat: add admin product create dto contract"
```

### Task 2: Add Local Storage Configuration and Storage Service

**Files:**
- Create: `src/main/java/vn/demo/nike/features/admin/product/storage/ProductImageStorageProperties.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/storage/ProductImageStorageService.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageService.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/exception/InvalidProductImageFileException.java`
- Modify: `src/main/resources/application.properties`
- Modify: `src/main/resources/application-dev.properties`
- Modify: `src/main/resources/application-prod.properties`
- Test: `src/test/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageServiceTest.java`

- [ ] **Step 1: Write the failing storage test for invalid image files**

```java
@Test
void shouldRejectEmptyMultipartFile() {
    MockMultipartFile file = new MockMultipartFile(
            "files",
            "hero.png",
            "image/png",
            new byte[0]
    );

    assertThatThrownBy(() -> storageService.store(file, "nike-air-max", "black"))
            .isInstanceOf(InvalidProductImageFileException.class)
            .hasMessageContaining("empty");
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -Dtest=LocalProductImageStorageServiceTest#shouldRejectEmptyMultipartFile test`

Expected: FAIL because storage service and exception do not exist yet.

- [ ] **Step 3: Write minimal storage implementation**

```java
public interface ProductImageStorageService {
    String store(MultipartFile file, String productSlug, String colorSlug);
}
```

```java
@ConfigurationProperties(prefix = "app.storage.product-images")
public record ProductImageStorageProperties(Path rootDirectory) {}
```

```java
@Service
@RequiredArgsConstructor
public class LocalProductImageStorageService implements ProductImageStorageService {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp", "avif");

    private final ProductImageStorageProperties properties;

    @Override
    public String store(MultipartFile file, String productSlug, String colorSlug) {
        if (file.isEmpty()) {
            throw new InvalidProductImageFileException("Uploaded image is empty");
        }
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new InvalidProductImageFileException("Uploaded file must be an image");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase(Locale.ROOT))) {
            throw new InvalidProductImageFileException("Uploaded image extension is not allowed");
        }
        String generatedName = UUID.randomUUID() + "." + extension.toLowerCase(Locale.ROOT);
        Path relative = Path.of("products", productSlug, colorSlug, generatedName);
        Path target = properties.rootDirectory().resolve(relative).normalize();
        Files.createDirectories(target.getParent());
        file.transferTo(target);
        return relative.toString().replace('\\', '/');
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -Dtest=LocalProductImageStorageServiceTest test`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/vn/demo/nike/features/admin/product/storage src/main/java/vn/demo/nike/features/admin/product/exception src/main/resources/application*.properties src/test/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageServiceTest.java
git commit -m "feat: add local product image storage"
```

### Task 3: Refactor AdminProductService Into Backend Create-Product Orchestration

**Files:**
- Modify: `src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java`
- Create: `src/main/java/vn/demo/nike/features/admin/product/exception/InvalidProductImageMappingException.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java`

- [ ] **Step 1: Write the failing service test for file-clientKey mapping**

```java
@Test
void shouldRejectReferencedImageWhenUploadedFileIsMissing() {
    AdminProductCreateRequest request = AdminProductCreateRequestFactory.singleColorSingleImage();

    List<MultipartFile> files = List.of();
    List<String> fileClientKeys = List.of();

    assertThatThrownBy(() -> adminProductService.createProduct(request, files, fileClientKeys))
            .isInstanceOf(InvalidProductImageMappingException.class)
            .hasMessageContaining("hero-black-1");
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -Dtest=AdminProductServiceTest#shouldRejectReferencedImageWhenUploadedFileIsMissing test`

Expected: FAIL because `createProduct(request, files, fileClientKeys)` does not exist yet.

- [ ] **Step 3: Write minimal orchestration implementation**

```java
@Transactional
public AdminCreatedProductResponse createProduct(
        AdminProductCreateRequest request,
        List<MultipartFile> files,
        List<String> fileClientKeys
) {
    validateSalePrice(request);
    validateDuplicateColors(request.colors());

    Map<String, MultipartFile> uploadedFiles = toFileMap(files, fileClientKeys);
    Category category = categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new CategoryNotFoundException(request.categoryId()));

    Product product = new Product();
    product.setName(request.name().trim());
    product.setDescription(request.description().trim());
    product.setType(request.type().trim());
    product.setPrice(request.price());
    product.setSalePrice(request.salePrice());
    product.setProductStatus(request.productStatus());
    product.setCategory(category);

    List<ProductColor> colors = request.colors().stream()
            .map(colorRequest -> mapColor(product, colorRequest, uploadedFiles))
            .toList();

    product.setColors(colors);
    Product saved = productRepository.save(product);
    return new AdminCreatedProductResponse(saved.getId(), saved.getName(), saved.getCategory().getName(), saved.getProductStatus().name());
}
```

```java
private Map<String, MultipartFile> toFileMap(List<MultipartFile> files, List<String> fileClientKeys) {
    if (files.size() != fileClientKeys.size()) {
        throw new InvalidProductImageMappingException("files and fileClientKeys must have the same size");
    }
    Map<String, MultipartFile> mapped = new HashMap<>();
    for (int i = 0; i < files.size(); i++) {
        String clientKey = fileClientKeys.get(i);
        if (mapped.put(clientKey, files.get(i)) != null) {
            throw new InvalidProductImageMappingException("Duplicate uploaded file clientKey: " + clientKey);
        }
    }
    return mapped;
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -Dtest=AdminProductServiceTest test`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java src/main/java/vn/demo/nike/features/admin/product/exception/InvalidProductImageMappingException.java src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java
git commit -m "refactor: split admin product create orchestration"
```

### Task 4: Add Colorway Image and Variant Validation Rules

**Files:**
- Modify: `src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java`

- [ ] **Step 1: Write the failing test for hero image validation**

```java
@Test
void shouldRejectColorwayWhenHeroImageCountIsNotExactlyOne() {
    AdminProductCreateRequest request = AdminProductCreateRequestFactory.singleColorWithoutHero();

    assertThatThrownBy(() -> adminProductService.createProduct(request, filesFor(request), clientKeysFor(request)))
            .isInstanceOf(InvalidProductColorException.class)
            .hasMessageContaining("Black");
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -Dtest=AdminProductServiceTest#shouldRejectColorwayWhenHeroImageCountIsNotExactlyOne test`

Expected: FAIL because the service does not enforce exactly one hero image yet.

- [ ] **Step 3: Write minimal validation implementation**

```java
private void validateColorImages(AdminProductColorRequest colorRequest) {
    if (colorRequest.images() == null || colorRequest.images().isEmpty()) {
        throw new InvalidProductColorException(colorRequest.colorName());
    }
    long heroCount = colorRequest.images().stream()
            .filter(image -> Boolean.TRUE.equals(image.isMainForColor()))
            .count();
    if (heroCount != 1) {
        throw new InvalidProductColorException(colorRequest.colorName());
    }
}

private void validateDuplicateSizes(AdminProductColorRequest colorRequest) {
    Set<String> seen = new HashSet<>();
    for (AdminProductVariantRequest variant : colorRequest.variants()) {
        String sizeKey = variant.size().trim().toLowerCase(Locale.ROOT);
        if (!seen.add(sizeKey)) {
            throw new InvalidSizeException(sizeKey);
        }
    }
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -Dtest=AdminProductServiceTest test`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java
git commit -m "test: enforce colorway image and variant rules"
```

### Task 5: Update AdminProductController to Multipart @RequestPart Boundary

**Files:**
- Modify: `src/main/java/vn/demo/nike/features/admin/product/api/AdminProductController.java`
- Modify: `src/main/java/vn/demo/nike/shared/exception/GlobalExceptionHandler.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/api/AdminProductControllerTest.java`

- [ ] **Step 1: Write the failing controller test for multipart create**

```java
@Test
void shouldAcceptMultipartProductCreateRequest() throws Exception {
    MockMultipartFile productData = new MockMultipartFile(
            "productData",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            """
            {"name":"Nike Air Max","description":"desc","type":"MEN","categoryId":1,"productStatus":"ACTIVE","price":190.00,"colors":[]}
            """.getBytes(StandardCharsets.UTF_8)
    );
    MockMultipartFile file = new MockMultipartFile("files", "hero.webp", "image/webp", "img".getBytes());

    mockMvc.perform(multipart("/admin/api/products")
                    .file(productData)
                    .file(file)
                    .param("fileClientKeys", "hero-black-1"))
            .andExpect(status().isOk());
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -Dtest=AdminProductControllerTest#shouldAcceptMultipartProductCreateRequest test`

Expected: FAIL because controller currently expects `@RequestBody`, not multipart request parts.

- [ ] **Step 3: Write minimal controller implementation**

```java
@PostMapping(path = "/admin/api/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<AdminCreatedProductResponse> createProduct(
        @RequestPart("productData") String productData,
        @RequestPart("files") List<MultipartFile> files,
        @RequestPart("fileClientKeys") List<String> fileClientKeys
) throws JsonProcessingException {
    AdminProductCreateRequest request = objectMapper.readValue(productData, AdminProductCreateRequest.class);
    return ResponseEntity.ok(adminProductService.createProduct(request, files, fileClientKeys));
}
```

- [ ] **Step 4: Run test to verify it passes**

Run: `mvn -Dtest=AdminProductControllerTest test`

Expected: PASS

- [ ] **Step 5: Commit**

```bash
git add src/main/java/vn/demo/nike/features/admin/product/api/AdminProductController.java src/main/java/vn/demo/nike/shared/exception/GlobalExceptionHandler.java src/test/java/vn/demo/nike/features/admin/product/api/AdminProductControllerTest.java
git commit -m "feat: expose multipart admin product create endpoint"
```

### Task 6: Add Focused Exception Mapping and Final Verification

**Files:**
- Modify: `src/main/java/vn/demo/nike/shared/exception/GlobalExceptionHandler.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/api/AdminProductControllerTest.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java`
- Test: `src/test/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageServiceTest.java`

- [ ] **Step 1: Write the failing controller test for malformed JSON**

```java
@Test
void shouldReturnBadRequestWhenProductDataIsMalformedJson() throws Exception {
    MockMultipartFile productData = new MockMultipartFile(
            "productData",
            "",
            MediaType.APPLICATION_JSON_VALUE,
            "{bad-json}".getBytes(StandardCharsets.UTF_8)
    );

    mockMvc.perform(multipart("/admin/api/products").file(productData))
            .andExpect(status().isBadRequest());
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `mvn -Dtest=AdminProductControllerTest#shouldReturnBadRequestWhenProductDataIsMalformedJson test`

Expected: FAIL because malformed JSON is not mapped to a safe 400 response yet.

- [ ] **Step 3: Write minimal exception handling and run full focused suite**

```java
@ExceptionHandler({
        JsonProcessingException.class,
        InvalidProductImageFileException.class,
        InvalidProductImageMappingException.class,
        InvalidProductColorException.class,
        InvalidSizeException.class,
        InvalidSalePriceException.class
})
public ResponseEntity<ErrorResponse> handleAdminProductCreateErrors(Exception ex) {
    return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
}
```

Run:

```bash
mvn -Dtest=AdminProductControllerTest,AdminProductServiceTest,LocalProductImageStorageServiceTest test
```

Expected: PASS

- [ ] **Step 4: Commit**

```bash
git add src/main/java/vn/demo/nike/shared/exception/GlobalExceptionHandler.java src/test/java/vn/demo/nike/features/admin/product/api/AdminProductControllerTest.java src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java src/test/java/vn/demo/nike/features/admin/product/storage/LocalProductImageStorageServiceTest.java
git commit -m "test: harden admin product create error handling"
```

## Self-Review

- Spec coverage:
  - multipart controller boundary: covered by Task 5
  - admin DTO separation: covered by Task 1
  - local storage: covered by Task 2
  - file-to-clientKey mapping: covered by Task 3
  - colorway image and variant rules: covered by Task 4
  - safe error handling and focused verification: covered by Task 6
- Placeholder scan:
  - removed generic "add validation later" language
  - every task contains exact files, commands, and minimal code direction
- Type consistency:
  - plan consistently uses `AdminProductCreateRequest`, `AdminCreatedProductResponse`, `ProductImageStorageService`, and `createProduct(request, files, fileClientKeys)`
