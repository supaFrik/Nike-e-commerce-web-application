# Product Seed via Postman

This seed flow uses the existing API exactly as implemented:

1. `GET /login` obtains the login CSRF token from the rendered login page.
2. `POST /login` submits the existing Spring Security form login fields: `email`, `password`, and `_csrf`.
3. `GET /admin/category/add` verifies authenticated admin access and extracts CSRF from the admin layout.
4. `GET /admin/api/products/form-options` resolves category IDs.
5. `POST /admin/api/categories` creates a missing category, then the collection resolves categories again.
6. `GET /admin/api/page-data/products` checks whether the generated product name already exists.
7. `POST /admin/api/products` creates the product using `multipart/form-data`.

No Java source, entity mapping, repository, controller, service, Flyway migration, upload validation, or database script is required.

## Files

- `product-seed-data.json`: 120 deterministic product rows for Collection Runner/Newman.
- `product-seed.postman_collection.json`: Postman collection for login, CSRF handling, category resolution, duplicate skipping, and product creation.

## Required Variables

Set these when running the collection:

- `baseUrl`: default is `http://localhost:8080`.
- `adminEmail`: an existing admin user's email.
- `adminPassword`: that admin user's password.

The application protects `/admin/**` with form login and `ROLE_ADMIN`. A `POST /login` response alone is not treated as proof of success; the collection verifies login by loading `/admin/category/add` and checking that the admin page, not the login form, was returned.

## CSRF

The collection extracts CSRF from the actual rendered HTML:

- login page: `data-csrf-token` / `data-csrf-header` on `#appRuntime`, with hidden `_csrf` input as a fallback.
- admin page: `data-csrf-token` / `data-csrf-header` in the admin layout.

The observed header name is `X-CSRF-TOKEN`; mutating requests send that literal header name with the extracted token.

## Image Uploads

To keep Newman file upload stable, every generated product uses one fixed verified supported file:

- `src/main/resources/static/images/products/advertisement.png`

The dataset still stores the image path for traceability, but the collection uses the fixed path directly in the multipart file part. The file is sent as `files`, and its matching key is sent as `fileClientKeys`.

## Running One Debug Iteration

From the repository root:

```powershell
newman run postman/product-seed.postman_collection.json `
  -d postman/product-seed-data.json `
  --working-dir . `
  --env-var "baseUrl=http://localhost:8080" `
  --env-var "adminEmail=YOUR_ADMIN_EMAIL" `
  --env-var "adminPassword=YOUR_ADMIN_PASSWORD" `
  --iteration-count 1
```

Only run the full dataset after one iteration completes cleanly.

## Duplicate Behavior

The collection checks existing products by exact generated product name through `GET /admin/api/page-data/products`. If a product already exists in that API response, the product creation request is skipped for that iteration. That endpoint returns the admin product list exposed by the application, so duplicate detection is limited to the products returned by the existing API.

## Notes

- Category IDs are never hardcoded. They are resolved during the run.
- `InventoryStatus` is not sent because the create API DTO does not accept it.
- The product create request sends `productData`, `files`, and `fileClientKeys`, matching `AdminProductController`.

