# 2026-04-25 Admin Product Add Backend Design

**Topic:** Backend design for administrator product creation with multipart image upload.

## Goal

Implement a production-shaped backend contract for `admin product add` in `Nike Ecommerce Web Application`.

This backend design must support:
- creating a product with product metadata
- creating multiple colorways
- creating variants per colorway
- uploading multiple images per colorway
- marking exactly one hero image per colorway
- storing uploaded files on the local filesystem
- storing only relative image paths in the database

The backend is the source of truth for this contract. Frontend integration is out of scope for this milestone.

## Scope

Included:
- admin-only multipart API contract
- admin-specific request and response DTOs
- business validation rules for product creation
- local file storage strategy for product images
- orchestration flow for mapping JSON metadata and uploaded files into the catalog aggregate

Excluded:
- frontend JSP or JavaScript changes
- product edit flow
- image deletion or replacement flow
- external object storage such as S3 or Cloudinary
- background cleanup for orphaned files created by partial failures

## Endpoint Contract

Endpoint:
- `POST /admin/api/products`

Content type:
- `multipart/form-data`

Request parts:
- `productData`: JSON string for the product aggregate metadata
- `files`: repeated multipart file parts
- `fileClientKeys`: repeated text parts aligned by index with `files`

The controller must use `@RequestPart` directly for all three parts.

## Admin DTO Boundary

Admin product creation must not reuse catalog-facing DTOs.

Create dedicated DTOs under the admin product feature:
- `AdminProductCreateRequest`
- `AdminProductColorRequest`
- `AdminProductImageRequest`
- `AdminProductVariantRequest`
- `AdminCreatedProductResponse`

Logical request shape:

- `AdminProductCreateRequest`
  - `name`
  - `description`
  - `type`
  - `categoryId`
  - `productStatus`
  - `price`
  - `salePrice`
  - `colors`

- `AdminProductColorRequest`
  - `colorName`
  - `hexCode`
  - `displayOrder`
  - `images`
  - `variants`

- `AdminProductImageRequest`
  - `clientKey`
  - `title`
  - `altText`
  - `orderIndex`
  - `isMainForColor`

- `AdminProductVariantRequest`
  - `sku`
  - `size`
  - `stock`
  - `active`

## Validation Rules

### Bean Validation

At DTO level:
- product name, description, and type must not be blank
- category id, product status, and price must not be null
- price must be greater than or equal to zero
- sale price must be greater than or equal to zero when present
- colors must not be empty
- each color must have a non-blank color name
- each image must have a non-blank client key
- each variant must have a non-blank size
- stock must be greater than or equal to zero

### Business Validation

At service level:
- `salePrice <= price`
- category must exist
- no duplicate color names within the same product
- no duplicate sizes within the same colorway
- each colorway must contain at least one image
- each colorway must contain exactly one hero image
- every image `clientKey` in JSON must map to exactly one uploaded file
- no uploaded file may exist without a referenced `clientKey`
- file count must equal `fileClientKeys` count

## Upload Mapping Strategy

The API uses a client-generated key for stable file-to-metadata mapping.

Flow:
1. Controller receives `productData`, `files`, and `fileClientKeys`.
2. Service validates that `files.size()` equals `fileClientKeys.size()`.
3. Service builds `Map<String, MultipartFile>` from `fileClientKey -> uploaded file`.
4. Each `AdminProductImageRequest.clientKey` resolves to one uploaded file.
5. The storage service saves the file and returns a relative path.
6. The relative path is assigned to `ProductImage.path`.

The backend must not infer mapping from multipart file order alone after request binding.

## Storage Strategy

Files are stored on the local filesystem. The database stores only relative paths.

Configuration:
- add an application property for product image storage root
- example logical value: `uploads/products`

Recommended relative path structure:
- `products/{product-slug}/{color-slug}/{generated-file-name}`

Storage rules:
- reject empty files
- allow only configured image extensions such as `jpg`, `jpeg`, `png`, `webp`, `avif`
- check that multipart content type starts with `image/`
- never trust the original filename for persistence naming
- generate a safe filename, preferably UUID-based
- never store absolute filesystem paths in the database

## Domain Mapping

Existing domain model remains the persistence target:
- `Product`
- `ProductColor`
- `ProductVariant`
- `ProductImage`

Important domain alignment:
- images belong to `ProductColor`
- `ProductImage.isMainForColor` is the hero image flag for a colorway
- stock remains variant-level, not product-level

## Service Design

`AdminProductService` should become an orchestration service, not a god class.

Responsibilities:
- parse and coordinate the admin create-product use case
- validate business rules
- resolve category
- map `clientKey` to uploaded files
- delegate file writes to a storage service
- assemble the product aggregate
- persist through `ProductRepository`
- return a compact admin response DTO

Non-responsibilities:
- low-level file IO details
- controller multipart parsing
- returning JPA entities directly

## Error Handling

Expected failure categories:
- malformed JSON in `productData`
- missing multipart parts
- invalid validation payload
- category not found
- invalid sale price
- duplicate color or size
- missing or extra file mappings
- invalid file type or empty file

Failures should produce explicit, safe messages suitable for admin operators without leaking filesystem internals.

## Testing Strategy

Primary tests for this milestone:

- service tests for create-product business rules
- storage tests for local file validation and relative path generation
- controller tests for multipart binding and bad-request handling

Minimum cases:
- reject sale price greater than price
- reject duplicate color names
- reject duplicate sizes within a color
- reject color with no images
- reject color with zero or multiple hero images
- reject missing file for referenced client key
- reject extra uploaded file without a referenced key
- create product successfully when payload and files are valid

## Milestones

1. Contract cleanup
   - add admin-specific DTOs and response model
   - update controller boundary to multipart `@RequestPart`

2. Upload infrastructure
   - add storage configuration
   - implement local product image storage service

3. Create-product use case
   - validate metadata and file mapping
   - save files
   - persist product aggregate

4. Hardening
   - add focused tests
   - improve exception mapping and validation messages

## Success Criteria

This backend milestone is successful when:
- admin product creation is exposed as a multipart endpoint
- the endpoint accepts one JSON metadata field and multiple image files
- each colorway can own multiple images and exactly one hero image
- uploaded files are saved locally with safe generated names
- the database stores relative image paths only
- entities are not returned directly from the controller
- core validation rules are covered by tests
