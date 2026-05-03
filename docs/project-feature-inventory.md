# Project Feature Inventory

This document is a code-driven inventory of the key features and technical capabilities currently present in the Nike E-commerce project.

It covers:
- business features
- backend modules
- frontend pages and JS modules
- security and infrastructure features
- DTO and Lombok usage
- persistence patterns
- current tests
- known technical debt

## 1. Project Overview

Project type:
- Spring Boot 3.2 WAR application
- Server-side rendered JSP storefront and admin panel
- MySQL + JPA/Hibernate persistence
- Spring Security authentication/authorization
- VNPAY payment integration

Core package style:
- package-by-feature under `src/main/java/vn/demo/nike/features`

Top-level feature modules:
- `admin`
- `cart`
- `catalog`
- `checkout`
- `home`
- `identity`
- `order`
- `payment`

Shared cross-cutting modules:
- `shared/config`
- `shared/controller`
- `shared/domain`
- `shared/dto`
- `shared/exception`
- `shared/util`

## 2. Business Features

### 2.1 Home / Landing

Main capability:
- renders the landing page and featured customer-facing storefront entry points

Key backend file:
- [HomeController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/home/api/HomeController.java)

Key UI pages:
- [index.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/user/index.jsp)

Key frontend features:
- hero section
- featured section
- shop-icons carousel
- running products carousel
- shop-by-sport carousel
- cart sidebar shell

### 2.2 Identity / Authentication / User Context

Main capability:
- login, signup, current-user resolution, role-based user identity

Feature contents:
- login page and signup page
- email-based Spring Security login
- signup service
- custom authenticated principal
- role enum
- current-user provider abstraction

Key files:
- [AuthController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/identity/auth/controller/AuthController.java)
- [SignUpService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/identity/auth/service/SignUpService.java)
- [User.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/identity/user/domain/User.java)
- [CustomUserPrincipal.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/identity/user/domain/CustomUserPrincipal.java)
- [SecurityCurrentUserProvider.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/identity/user/service/SecurityCurrentUserProvider.java)

Important request models:
- `LoginRequest`
- `SignupForm`
- `UserCreateRequest`
- `ContactForm`

Identity-related domain objects:
- `User`
- `Address`
- `Contact`
- `Role`

### 2.3 Catalog / Product Browsing

Main capability:
- category-based and sort-based product listing
- product detail view
- product summary/detail DTO mapping
- product aggregate with variants, colors, and images

Catalog subdomains:
- `catalog/category`
- `catalog/product`

Key controllers:
- [ProductListController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/catalog/product/api/ProductListController.java)
- [ProductDetailController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/catalog/product/api/ProductDetailController.java)

Key services:
- [CategoryService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/catalog/category/service/CategoryService.java)
- [ProductListService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/catalog/product/service/ProductListService.java)
- [ProductDetailPageService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/catalog/product/service/ProductDetailPageService.java)
- [ProductService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/catalog/product/service/ProductService.java)

Product aggregate model:
- `Product`
- `ProductColor`
- `ProductImage`
- `ProductVariant`
- `ProductStatus`

Category model:
- `Category`
- `CategoryView`

Catalog DTO/request/response features:
- product list item DTO
- product detail response DTO
- product summary response DTO
- color/image/variant create requests
- color/image/variant detail responses

Catalog UI pages:
- [product-list.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/user/product-list.jsp)
- [product-detail.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/user/product-detail.jsp)
- [search.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/user/search.jsp)

Current storefront filtering reality:
- category and sort are backed by the server
- some additional list-page filtering is client-side only
- not all UI filter labels are backed by full domain/query support yet

### 2.4 Cart

Main capability:
- add item to cart
- update cart quantity
- remove cart item
- compute cart count and summary
- render cart page

Key files:
- [CartController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/cart/api/CartController.java)
- [CartPageController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/cart/api/CartPageController.java)
- [CartService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/cart/service/CartService.java)
- [CartItem.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/cart/domain/CartItem.java)
- [CartItemRepository.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/cart/repository/CartItemRepository.java)

Cart request models:
- `AddToCartRequest`
- `UpdateCartItemQuantityRequest`

Cart response models:
- `AddToCartResponse`
- `CartCountResponse`
- `CartItemViewResponse`
- `CartSummaryResponse`

Cart UI pages:
- [cart.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/user/cart.jsp)

### 2.5 Checkout

Main capability:
- authenticated checkout entry
- checkout page view data assembly
- place checkout request handling
- shipping/address/payment method handling
- pluggable payment-handler contract

Key controllers:
- [CheckoutPageController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/checkout/api/CheckoutPageController.java)
- [CheckoutController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/checkout/api/CheckoutController.java)

Key services:
- [CheckoutService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/checkout/service/CheckoutService.java)
- [CheckoutPageViewService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/checkout/service/CheckoutPageViewService.java)
- [CheckoutPaymentHandler.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/checkout/service/CheckoutPaymentHandler.java)
- [CodCheckoutPaymentHandler.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/checkout/service/CodCheckoutPaymentHandler.java)

Checkout DTOs:
- `PlaceCheckoutRequest`
- `CheckoutPageViewData`
- `CheckoutItemSnapshotDto`
- `CheckoutInitiationResponse`

Checkout exceptions:
- `InvalidCheckoutRequestException`
- `ShippingMethodNotFoundException`
- `AddressNotFoundException`
- `UnauthenticatedCheckoutException`

Checkout UI pages:
- [checkout.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/user/checkout.jsp)

### 2.6 Order

Main capability:
- order page rendering
- order detail/overview view composition
- order line items
- order timeline/payment/shipping view models

Key files:
- [OrderPageController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/order/api/OrderPageController.java)
- [OrderPageViewService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/order/service/OrderPageViewService.java)
- [Order.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/order/domain/Order.java)
- [OrderItem.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/order/domain/OrderItem.java)
- [OrderRepository.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/order/repository/OrderRepository.java)

Order DTO/view features:
- `OrderPageView`
- `OrderOverviewView`
- `OrderItemView`
- `OrderPricingView`
- `OrderPaymentView`
- `OrderShippingView`
- `OrderTimelineStepView`
- `OrderActionView`
- `PaymentHistoryView`

Important JPA feature here:
- `@EntityGraph` usage in `OrderRepository` for eager loading order items

### 2.7 Payment

Main capability:
- VNPAY payment creation
- VNPAY return handling
- VNPAY IPN handling
- payment transaction persistence
- request signing/verification

Key files:
- [VNPayPaymentController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/api/VNPayPaymentController.java)
- [VNPayPaymentService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/service/VNPayPaymentService.java)
- [VNPaySignatureService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/service/VNPaySignatureService.java)
- [VNPayProperties.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/config/VNPayProperties.java)
- [PaymentTransaction.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/domain/PaymentTransaction.java)
- [PaymentTransactionRepository.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/repository/PaymentTransactionRepository.java)

Payment DTOs:
- `VNPayCreatePaymentResponse`
- `VNPayReturnResponse`
- `VNPayIpnResponse`

Payment enums:
- `PaymentMethod`
- `PaymentProvider`
- `PaymentStatus`

### 2.8 Admin Dashboard / Back Office

Main capability:
- admin landing page
- admin dashboard data API
- admin product inventory API
- admin category list API
- admin order list API

Key files:
- [AdminPageController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/api/AdminPageController.java)
- [AdminPageDataController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/page/api/AdminPageDataController.java)
- [AdminPageDataService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/page/service/AdminPageDataService.java)

Admin page DTOs:
- `AdminDashboardResponse`
- `AdminProductInventoryItemResponse`
- `AdminCategoryListItemResponse`
- `AdminOrderListItemResponse`

Admin UI pages:
- [dashboard.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/dashboard.jsp)
- [administrator/product/list.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/product/list.jsp)
- [administrator/category/list.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/category/list.jsp)
- [administrator/order/list.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/order/list.jsp)

### 2.9 Admin Product Management

Main capability:
- create product
- load product form data
- update product
- delete product
- wire category options to backend
- manage colorways, images, and variants
- local product image storage
- remove orphan images from DB and filesystem
- draft/publish admin form flow

Key files:
- [AdminProductController.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/product/api/AdminProductController.java)
- [AdminProductService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/product/service/AdminProductService.java)
- [ProductImageStorageService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/product/service/ProductImageStorageService.java)
- [LocalProductImageStorageService.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/product/service/LocalProductImageStorageService.java)
- [ProductImageStorageProperties.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/product/service/ProductImageStorageProperties.java)

Admin product DTOs:
- `AdminProductCreateRequest`
- `AdminProductColorRequest`
- `AdminProductImageRequest`
- `AdminProductVariantRequest`
- `AdminCreatedProductResponse`
- `AdminProductCategoryOptionResponse`
- `AdminProductFormResponse`

Admin product exception coverage:
- `ProductNotFoundException`
- `ProductColorNotFoundException`
- `InvalidUploadedImageException`
- `InvalidFileMappingException`
- `InvalidProductColorException`
- `InvalidSalePriceException`
- `InvalidSizeException`

Important current behavior:
- edit flow is built around full form snapshot update
- color block replacement is supported
- existing images can be preserved by id
- unreferenced images are deleted from state and storage
- inventory is managed at variant level

Admin product UI pages:
- [administrator/product/add.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/product/add.jsp)
- [administrator/product/edit.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/product/edit.jsp)
- [administrator/product/list.jsp](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/webapp/WEB-INF/views/administrator/product/list.jsp)

## 3. Frontend Feature Inventory

### 3.1 Customer JSP Pages

Current customer JSP pages:
- `index.jsp`
- `auth.jsp`
- `search.jsp`
- `product-list.jsp`
- `product-detail.jsp`
- `cart.jsp`
- `checkout.jsp`
- `order.jsp`
- `profile.jsp`
- `contact.jsp`

Shared layout JSPs:
- `layout/header.jsp`
- `layout/footer.jsp`
- `layout/css.jsp`
- `layout/js.jsp`
- `layout/order-process.jsp`

Imported partial JSPs:
- `imported/auth.jsp`
- `imported/cart.jsp`
- `imported/checkout.jsp`
- `imported/landing-page.jsp`
- `imported/order.jsp`
- `imported/product-detail.jsp`
- `imported/product-list.jsp`
- `imported/profile.jsp`

### 3.2 Admin JSP Pages

Current admin JSP pages:
- dashboard
- product list/add/edit
- category list/add/edit
- order list
- admin layout css/js/sidebar partials

### 3.3 Frontend JavaScript Architecture

Common runtime/bootstrap:
- [runtime.js](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/resources/static/js/common/runtime.js)
- [mobile-nav.js](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/resources/static/js/common/mobile-nav.js)
- `url-utils.js`

Customer component modules:
- `add-to-cart.js`
- `carousel.js`
- `color-options.js`
- `footer.js`
- `header.js`
- `overlay-panels.js`
- `sort-btn.js`
- `thumbnail-scope.js`

Customer page modules:
- `auth.js`
- `cart.js`
- `checkout-page.js`
- `checkout-payment.js`
- `checkout-shipping.js`
- `checkout-validation.js`
- `order.js`
- `product-detail-carousel.js`
- `product-detail-page.js`
- `product-list-filters.js`
- `product-list.js`
- `product.js`
- `profile.js`
- `search.js`
- `section-expand.js`

Admin JS modules:
- `shell.js`
- `components/sidebarmenu.js`
- `components/update-button.js`
- `pages/dashboard.js`
- `pages/product-list.js`
- `pages/product-form.js`
- `pages/product-add.js`
- `pages/edit-product.js`
- `pages/categories.js`
- `pages/orders.js`
- `pages/order-management.js`
- `pages/admin-product-list.js`

Important frontend engineering features already present:
- runtime context bootstrap through `window.APP_CTX`
- CSRF token bootstrap for JS API calls
- page-specific JS split under `static/js`
- reduced inline JS in JSPs
- admin/customer separation in JS folders

## 4. Technical Feature Inventory

### 4.1 Spring Boot and Core Framework Use

Libraries in use from `pom.xml`:
- `spring-boot-starter-web`
- `spring-boot-starter-security`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-validation`
- `mysql-connector-j`
- `jakarta.servlet.jsp.jstl-api`
- `jakarta.servlet.jsp.jstl`
- `tomcat-embed-jasper`
- `jackson-datatype-jsr310`
- `nimbus-jose-jwt`
- `spring-boot-devtools`
- `spring-boot-starter-test`
- `lombok`

### 4.2 Security Features

Configured in:
- [SecurityConfig.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/shared/config/SecurityConfig.java)

Security capabilities:
- Spring Security filter chain
- role-based protection of `/admin/**`
- public access for storefront assets and product pages
- custom login page
- email as username parameter
- logout handling with session invalidation
- invalid session redirect
- CSRF exception handling with forced session reset
- BCrypt password encoder
- `UserDetailsService` backed by `UserRepository`

### 4.3 Persistence / JPA Features

Persistence characteristics present in code:
- JPA entities under feature-based `domain` packages
- Spring Data repositories
- entity relationships across user/product/cart/order/payment domains
- `BaseEntity` shared base type
- enum persistence for roles/status/payment/shipping types
- repository-level custom queries
- paging and sorting usage in product list services
- `@EntityGraph` usage in order repository
- multipart-backed local file storage for product images

Main entities currently present:
- `User`
- `Address`
- `Contact`
- `Category`
- `Product`
- `ProductColor`
- `ProductImage`
- `ProductVariant`
- `CartItem`
- `Order`
- `OrderItem`
- `PaymentTransaction`

### 4.4 DTO / Request / Response Feature Use

This project already uses DTO-style boundaries in multiple areas.

Patterns present:
- request models for form/API input
- response models for API output
- view data DTOs for page rendering
- Java `record` DTOs for compact immutable response shapes
- builder-based DTO assembly in order/checkout areas

Examples of DTO styles:

Classic class DTOs:
- `AddToCartResponse`
- `CartSummaryResponse`
- `ProductDetailResponse`
- `PlaceCheckoutRequest`
- `VNPayReturnResponse`

Record DTOs:
- `AdminDashboardResponse`
- `AdminProductInventoryItemResponse`
- `AdminCategoryListItemResponse`
- `AdminOrderListItemResponse`
- `AdminProductCategoryOptionResponse`
- `AdminProductFormResponse`
- `ErrorResponse`

Nested record DTOs:
- `AdminProductFormResponse.AdminColorFormItem`
- `AdminProductFormResponse.AdminImageFormItem`
- `AdminProductFormResponse.AdminVariantFormItem`

Why this matters:
- avoids returning entities directly from admin APIs
- reduces accidental lazy-loading leakage
- creates explicit API contracts
- keeps view/API payload shapes stable

### 4.5 Lombok Feature Use

Lombok is used widely across entities, DTOs, services, and config classes.

Annotations found in the codebase:
- `@Getter`
- `@Setter`
- `@Builder`
- `@NoArgsConstructor`
- `@AllArgsConstructor`
- `@RequiredArgsConstructor`

Where Lombok is mainly used:
- entities
- request/response DTOs
- service classes with constructor injection
- configuration properties classes

What Lombok is doing here:
- removes boilerplate getters/setters/constructors/builders
- supports constructor injection with `@RequiredArgsConstructor`
- keeps entity and DTO code shorter

### 4.6 Configuration Properties and Config Classes

Config classes present:
- [SecurityConfig.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/shared/config/SecurityConfig.java)
- [StaticResourceConfig.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/shared/config/StaticResourceConfig.java)
- [VNPayProperties.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/payment/config/VNPayProperties.java)
- [ProductImageStorageProperties.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/features/admin/product/service/ProductImageStorageProperties.java)

Config features:
- `@ConfigurationProperties` for VNPAY settings
- `@ConfigurationPropertiesScan` at app boot level
- static resource mapping
- local upload directory configuration

### 4.7 Exception Handling Features

Shared exception handling:
- [GlobalControllerAdvice.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/shared/controller/GlobalControllerAdvice.java)
- [GlobalExceptionHandler.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/shared/exception/GlobalExceptionHandler.java)
- [ErrorResponse.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/main/java/vn/demo/nike/shared/dto/ErrorResponse.java)

Feature-local exception types exist in:
- `admin/product/exception`
- `cart/exception`
- `catalog/category/exception`
- `catalog/product/exception`
- `checkout/exception`
- `order/exception`

### 4.8 File Storage Features

Product image storage features:
- storage abstraction via `ProductImageStorageService`
- local filesystem implementation via `LocalProductImageStorageService`
- configurable root directory
- image deletion support
- orphan cleanup after admin edits/deletes

Storage path in config:
- `app.storage.product-images.root-directory`

## 5. Testing Features

Current automated tests in `src/test/java`:
- [AdminProductServiceTest.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/test/java/vn/demo/nike/features/admin/product/service/AdminProductServiceTest.java)
- [AdminPageDataServiceTest.java](C:/Users/aDMIN/Documents/Nike%20Ecommerce%20Web%20Application/src/test/java/vn/demo/nike/features/admin/page/service/AdminPageDataServiceTest.java)

Current test focus:
- admin product service behavior
- admin page data mapping behavior

Testing stack:
- Spring Boot Test
- JUnit 5

Current limitation:
- test coverage is still narrow relative to the project surface area

## 6. Known Legacy / Technical Debt

These are not fake issues. They are real repo smells worth noting.

- Some customer-side JS modules still overlap in responsibility, especially around old product-list behavior.
- `checkout.jsp` still has inline JS debt compared with the cleaner extracted approach used elsewhere.
- The storefront filter UI is ahead of the backend query model in some places.
- Dev configuration currently contains hard-coded local secrets and should be externalized.
- The repo contains some legacy JS files that are likely no longer the primary implementation path.
- Test coverage does not yet match the complexity of commerce-critical flows like checkout, payment, and security edge cases.

## 7. Summary

If someone asks, "What features does this project have?", the honest answer is:

Business features:
- storefront
- authentication
- catalog
- cart
- checkout
- order tracking/view
- admin dashboard
- admin catalog/inventory management
- VNPAY payment flow

Technical features:
- package-by-feature backend structure
- DTO and record-based API/view contracts
- Lombok-based boilerplate reduction
- Spring Security role-based access control
- JPA/Hibernate persistence
- JSP/JSTL server rendering
- page-specific JS module organization
- configuration properties for externalized settings
- shared exception handling
- local product image storage abstraction

That is the current feature inventory based on the codebase as it exists now.
