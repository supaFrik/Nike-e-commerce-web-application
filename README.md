# Nike E-commerce Web Application

[![Java 17](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot 3.2.4](https://img.shields.io/badge/Spring%20Boot-3.2.4-6DB33F)](https://spring.io/projects/spring-boot)
[![MySQL 8](https://img.shields.io/badge/MySQL-8.0-4479A1)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-C71A36)](https://maven.apache.org/)
[![JSP](https://img.shields.io/badge/View-JSP%20%2B%20JSTL-blue)](https://jakarta.ee/specifications/tags/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](#license)

Nike E-commerce is a full-stack web application for a Nike-style online store, built with Spring Boot, MySQL, JPA, JSP, and Spring Security. It includes a customer storefront, cart and checkout flows, and an admin back office for catalog and inventory management.

## Project Description

This project was built to practice and evolve a production-oriented e-commerce architecture on the Java stack.

What the application does:
- Serves a customer-facing storefront with product browsing, search, cart, checkout, profile, and order flows
- Provides an admin area for managing products, categories, dashboard data, and inventory
- Supports product variants by color and size, with image management and variant-level stock
- Integrates with VNPAY configuration for payment processing flows

Why this stack was chosen:
- `Spring Boot 3.2` for a stable enterprise Java foundation
- `Spring MVC + JSP` for server-side rendered pages
- `Spring Data JPA + MySQL` for relational persistence
- `Spring Security` for authentication and role-based access control
- `Vanilla JS` for progressive enhancement without introducing frontend framework overhead

Current engineering focus:
- Package-by-feature organization
- DTO-based boundaries instead of exposing entities directly
- Admin product add/edit flows wired to real backend data
- Removing mock data and inline JavaScript from JSP pages

Known challenges and next improvements:
- `checkout.jsp` still contains one remaining inline script block that should be extracted
- Local development config currently contains hard-coded secrets and should be externalized
- Test coverage is still not broad enough for a production-grade commerce system

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation and Run](#installation-and-run)
- [How to Use](#how-to-use)
- [Documentation](#documentation)
- [Tests](#tests)
- [Contributing](#contributing)
- [Credits](#credits)
- [License](#license)

## Features

### Customer
- Product listing, search, and filtering
- Product detail with color, size, image, and stock-aware variant selection
- Cart management
- Checkout flow
- Authentication and profile pages
- Order-related flows

### Admin
- Dashboard with data loaded from backend
- Product inventory page wired to real database data
- Product add and edit flow with:
  - category options from backend
  - variant-level inventory
  - image upload and existing image retention
  - draft save and publish/update actions
  - remove controls for product, colorway, size, and image
- Category management pages
- Order list page

### Security
- Spring Security-based authentication
- Role separation between customer and admin areas
- CSRF-aware frontend runtime bootstrap
- DTO usage across admin API responses and requests

## Tech Stack

- Java 17
- Spring Boot 3.2.4
- Spring MVC
- Spring Data JPA
- Spring Security
- MySQL 8
- JSP + JSTL
- Maven
- JUnit 5 / Spring Boot Test
- Docker Compose for local MySQL

## Project Structure

```text
.
+-- docs/                         # Feature docs, UML, architecture notes
+-- src/
|   +-- main/
|   |   +-- java/vn/demo/nike/
|   |   |   +-- features/        # Package-by-feature modules
|   |   |   \-- config/          # Spring and application configuration
|   |   +-- resources/
|   |   |   +-- static/          # CSS, JS, images
|   |   |   \-- application*.properties
|   |   \-- webapp/WEB-INF/views/
|   |       +-- administrator/   # Admin JSP pages
|   |       \-- user/            # Customer JSP pages
|   \-- test/                    # Service and page data tests
+-- uploads/                     # Local product image storage
+-- docker-compose.yml
+-- pom.xml
\-- README.md
```

## Installation and Run

### Prerequisites

- JDK 17
- Maven 3.6+
- Docker Desktop or local MySQL 8

### 1. Clone the project

```bash
git clone <your-repository-url>
cd "Nike Ecommerce Web Application"
```

### 2. Start MySQL

Using Docker Compose:

```bash
docker compose up -d
```

Default local database container:
- Host: `localhost`
- Port: `3307`
- Database: `nike_store`

### 3. Configure environment

The project runs with `spring.profiles.active=dev` by default.

Recommended approach:
- Use `application-prod.properties` style environment variables for real credentials
- Do not commit real passwords or payment secrets

Suggested variables for safer local setup:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3307/nike_store?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_password"
```

### 4. Build the project

```bash
mvn clean compile
```

### 5. Run the application

```bash
mvn spring-boot:run
```

Application URLs:
- Customer site: `http://localhost:9090`
- Admin area: `http://localhost:9090/admin/home`

### 6. Package artifact

```bash
mvn clean package -DskipTests
```

Packaged artifact name:
- `target/nike-starter.war`

## How to Use

### Customer flow

1. Open the storefront at `http://localhost:9090`
2. Browse products or use search/filter pages
3. Select color and size on product detail
4. Add item to cart
5. Proceed through checkout

### Admin flow

1. Sign in with an account that has admin role
2. Open `http://localhost:9090/admin/home`
3. Go to product inventory
4. Create or edit product data
5. Manage:
   - basic information
   - category
   - colorways
   - sizes
   - variant stock
   - product images

### Admin product add/edit notes

- Inventory is managed at variant level, not product level
- Edit flow uses full replace for color blocks
- Existing images can be retained without re-uploading them
- Removed images are deleted from both database state and local file storage

## Documentation

Useful project docs in [`docs/`](docs):

- [`docs/admin-product-add-edit-flow.md`](docs/admin-product-add-edit-flow.md): admin product add/edit backend and frontend flow
- `docs/domain-model-and-database-map.md`: domain and database notes
- `docs/*.puml`: feature and architecture diagrams

## Tests

Run all tests:

```bash
mvn test
```

Run targeted service tests:

```bash
mvn -Dtest=AdminProductServiceTest,AdminPageDataServiceTest test
```

Useful verification commands:

```bash
mvn -q -DskipTests compile
mvn clean package -DskipTests
```

Current note:
- Some service-level tests exist for admin product and admin page data
- The project still needs broader test coverage for checkout, payment, security, and edge-case inventory flows

## Contributing

If you want to contribute:

1. Create a feature branch
2. Keep changes scoped to one concern
3. Do not expose entities directly in APIs
4. Prefer package-by-feature organization
5. Add or update tests when changing behavior
6. Avoid reintroducing mock data or inline JavaScript in JSP pages
7. Document important flow changes in `docs/`

Recommended branch naming:
- `feature/...`
- `fix/...`
- `refactor/...`

## Credits

This project appears to be maintained as a personal or small-team learning/productization effort.

You should list here:
- project owner name
- collaborators
- GitHub profiles
- any tutorial, article, or reference that materially influenced the implementation

Example:

```text
- Owner: Your Name
- GitHub: https://github.com/your-username
```

## License

This repository should include an explicit license file.

Suggested default:
- `MIT` if you want permissive reuse
- `GPL-3.0` if you want derivative work to remain open

Important brand note:
- Nike name and brand assets are trademarks of Nike, Inc.
- If this project is used publicly or commercially, replace branded assets and naming unless you have permission to use them
