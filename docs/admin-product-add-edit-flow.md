# Luong Product Add va Product Edit

## 1. Muc tieu

Tai lieu nay mo ta luong them san pham va chinh sua san pham trong khu vuc admin cua du an Nike E-commerce.

Muc tieu hien tai:

- Khong dung mock data cho form product
- Dung du lieu that tu database
- Product edit ho tro full-replace color block
- Inventory duoc quan ly theo variant, khong quan ly bang mot stock tong gia

## 2. JSP routes

### Product add

- `GET /admin/product/add`
- JSP: `src/main/webapp/WEB-INF/views/administrator/product/add.jsp`

Trang nay dung chung script:

- `src/main/resources/static/js/admin/pages/product-form.js`

Body:

- `data-form-type="add"`

### Product edit

- `GET /admin/product/edit/{productId}`
- JSP: `src/main/webapp/WEB-INF/views/administrator/product/edit.jsp`

Body:

- `data-form-type="edit"`
- `data-product-id="{productId}"`

## 3. API contracts

### 3.1. Form options

- `GET /admin/api/products/form-options`

Tra ve danh sach category de do vao select box.

### 3.2. Product form detail

- `GET /admin/api/products/{productId}`

Tra ve:

- thong tin product level
- categoryId
- danh sach colors
- danh sach images cua tung color
- danh sach variants cua tung color

DTO hien tai:

- `AdminProductFormResponse`

### 3.3. Create product

- `POST /admin/api/products`
- `Content-Type: multipart/form-data`

Phan multipart:

- `productData`: JSON blob
- `files`: cac file anh moi
- `fileClientKeys`: key de map file vao image metadata

### 3.4. Update product

- `PUT /admin/api/products/{productId}`
- `Content-Type: multipart/form-data`

Contract update dung cung shape voi create, nhung ho tro them:

- `existingImageId`

## 4. Product request shape

Request hien tai dung:

- `AdminProductCreateRequest`
- `AdminProductColorRequest`
- `AdminProductImageRequest`
- `AdminProductVariantRequest`

Product level:

- `name`
- `description`
- `type`
- `categoryId`
- `productStatus`
- `price`
- `salePrice`
- `colors`

Color block:

- `colorName`
- `hexCode`
- `displayOrder`
- `images`
- `variants`

Image block:

- `existingImageId`: dung khi giu anh cu
- `clientKey`: dung khi upload anh moi
- `title`
- `altText`
- `orderIndex`
- `isMainForColor`

Variant block:

- `sku`
- `size`
- `stock`
- `active`

## 5. Full-replace color block

Product edit hien tai dung chien luoc:

- full replace toan bo color block

Y nghia:

- mau nao khong xuat hien trong request moi thi bi xoa
- variant nao khong xuat hien trong request moi thi bi xoa
- image nao khong xuat hien trong request moi thi bi xoa

Anh cu duoc giu lai bang:

- `existingImageId`

Anh moi duoc them bang:

- `clientKey` + `files[]`

## 6. Xu ly image trong update

Service:

- `AdminProductService`

Storage abstraction:

- `ProductImageStorageService`
- `LocalProductImageStorageService`

Quy tac:

1. Validate request
2. Validate `existingImageId` phai thuoc dung product dang edit
3. Validate `clientKey` phai map duoc vao uploaded file
4. Tinh danh sach file cu can xoa
5. Save DB state moi
6. Sau khi transaction commit thanh cong moi xoa file cu tren disk

Neu xoa file vat ly that bai:

- chi log warning
- khong rollback business transaction

## 7. Inventory rules

Inventory hien tai duoc tinh va sua theo:

- `ProductVariant.stock`

Khong con dung:

- product-level mock `stockQuantity`

Frontend render:

- moi dong `color + size` co mot input stock rieng

## 8. Frontend state trong product form

Script:

- `src/main/resources/static/js/admin/pages/product-form.js`

State chinh:

- `categories`
- `colors`

Moi color chua:

- metadata color
- `images[]`
- `variants[]`

Image state co 2 loai:

- `kind = "existing"`
- `kind = "new"`

## 9. Trang thai hien tai

Da hoan thanh:

- product add doc category that tu backend
- product edit load du lieu that tu backend
- product add/edit luu bang multipart form
- edit full-replace color block
- inventory theo variant stock
- remove color, remove size, remove image tren client state

Chua hoan thanh:

- delete product that tu product list
- create/update category that tu admin category pages
- toi uu query de tranh N+1 cho admin page data

## 10. File chinh lien quan

- `src/main/java/vn/demo/nike/features/admin/product/api/AdminProductController.java`
- `src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java`
- `src/main/java/vn/demo/nike/features/admin/product/dto/*`
- `src/main/java/vn/demo/nike/features/admin/product/service/ProductImageStorageService.java`
- `src/main/java/vn/demo/nike/features/admin/product/service/LocalProductImageStorageService.java`
- `src/main/resources/static/js/admin/pages/product-form.js`
- `src/main/webapp/WEB-INF/views/administrator/product/add.jsp`
- `src/main/webapp/WEB-INF/views/administrator/product/edit.jsp`
