<p align="center">
  <img src="src/main/resources/static/images/air-jordan-logo.png" alt="Nike Logo" width="140"/>
</p>

<h1 align="center">🏃‍♂️ Nike E-Commerce Web Application</h1>
<p align="center"><em>"Just Do It" - Now with Enterprise-Grade Architecture</em></p>

<p align="center">
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg" alt="Spring Boot"/></a>
  <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-17-orange.svg" alt="Java"/></a>
  <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Maven-3.6+-blue.svg" alt="Maven"/></a>
  <a href="https://www.mysql.com/"><img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL"/></a>
  <a href="https://docker.com"><img src="https://img.shields.io/badge/Docker-Compose-blue.svg" alt="Docker"/></a>
  <a href="https://hibernate.org/"><img src="https://img.shields.io/badge/JPA-Hibernate-red.svg" alt="JPA"/></a>
  <a href="https://spring.io/guides/gs/serving-web-content/"><img src="https://img.shields.io/badge/Architecture-MVC-purple.svg" alt="Architecture"/></a>
  <a href="https://flywaydb.org/"><img src="https://img.shields.io/badge/Database-Flyway-orange.svg" alt="Flyway"/></a>
  <a href="https://junit.org/junit5/"><img src="https://img.shields.io/badge/Testing-JUnit%205-green.svg" alt="JUnit 5"/></a>
  <a href="https://spring.io/projects/spring-security"><img src="https://img.shields.io/badge/Security-Spring%20Security-brightgreen.svg" alt="Spring Security"/></a>
</p>

---

## 🚀 Overview

Welcome to the **Nike E-Commerce Web Application** – where championship performance meets cutting-edge technology. Built with enterprise-grade Spring Boot architecture, this application delivers an unparalleled shopping experience with comprehensive customer and admin functionalities.

This full-stack e-commerce platform provides a complete online shopping solution with modern web technologies, secure authentication, robust testing framework, and scalable architecture designed for high performance and maintainability.

---

## 📑 Table of Contents
- [✨ Features](#-features)
- [🛠 Tech Stack](#-tech-stack)
- [🏗 Architecture](#-architecture)
- [🎨 Frontend Technologies](#-frontend-technologies)
- [⚙️ Backend Architecture](#️-backend-architecture)
- [🔒 Security Framework](#-security-framework)
- [🧪 Testing & Quality Assurance](#-testing--quality-assurance)
- [⚠️ Exception Handling](#️-exception-handling)
- [🚀 Getting Started](#-getting-started)
- [📊 Database Schema](#-database-schema)
- [🔧 Configuration](#-configuration)
- [📱 API Endpoints](#-api-endpoints)
- [🎯 Usage](#-usage)
- [📁 Project Structure](#-project-structure)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

---

## ✨ Features

### 🛍️ Customer Features
- **User Registration & Authentication** - Secure signup/login with Spring Security & BCrypt encryption
- **Product Catalog** - Browse Nike products with advanced filtering, search, and sorting
- **Product Details** - Detailed product pages with multiple images, variants (size/color), and pricing
- **Shopping Cart** - Real-time cart management with variant-specific pricing
- **User Profile** - Comprehensive profile management with contact information
- **Product Reviews & Feedback** - Rate and review products with persistent feedback system
- **Responsive Design** - Mobile-first responsive interface with Bootstrap integration

### 👨‍💼 Admin Features
- **Product Management** - Full CRUD operations with multi-variant support (size, color, pricing)
- **Category Management** - Hierarchical category organization
- **Customer Management** - User account administration and role management
- **File Management** - Advanced image upload with automatic file organization
- **Dashboard Analytics** - Comprehensive admin dashboard with system insights
- **Content Management** - SEO-optimized product descriptions and metadata

### 🔒 Security & Authentication
- **Spring Security Integration** - Role-based access control (USER/ADMIN)
- **BCrypt Password Encryption** - Industry-standard password hashing
- **Session Management** - Secure session handling with timeout protection
- **CSRF Protection** - Cross-site request forgery prevention
- **Custom Authentication** - Email-based login with credential validation

---

## 🛠 Tech Stack

### Backend Framework
- **Java 17** - Modern Java with enhanced performance and language features
- **Spring Boot 2.7.18** - Production-ready application framework
- **Spring MVC** - Model-View-Controller web architecture
- **Spring Data JPA** - Simplified data access with repository pattern
- **Spring Security** - Comprehensive security framework
- **Hibernate** - Advanced ORM with MySQL dialect optimization
- **Maven** - Dependency management and build automation

### Database & Persistence
- **MySQL 8.0** - Primary relational database
- **Flyway** - Database migration and version control
- **JPA/Hibernate** - Object-relational mapping with entity relationships
- **Connection Pooling** - Optimized database connection management

### Frontend Technologies
- **JSP (Java Server Pages)** - Server-side rendering with JSTL integration
- **Bootstrap 5** - Responsive CSS framework with custom styling
- **JavaScript/jQuery** - Client-side interactivity and AJAX operations
- **SCSS** - Enhanced CSS preprocessing for maintainable stylesheets
- **Custom CSS** - Specialized styling for Nike brand consistency

### Testing & Quality
- **JUnit 5** - Modern testing framework with advanced assertions
- **Spring Boot Test** - Integration testing with test slices
- **Transactional Testing** - Database rollback for isolated test execution
- **Parameterized Tests** - Data-driven testing capabilities

### DevOps & Infrastructure
- **Docker Compose** - Containerized development environment
- **Maven Surefire** - Test execution and reporting
- **Embedded Tomcat** - Production-ready servlet container

---

## 🏗 Architecture

The application implements a **layered MVC architecture** with clear separation of concerns and dependency inversion:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│     Controllers (MVC + REST API)        │
│   - Customer Controllers                │
│   - Admin Controllers                   │
│   - Authentication Controllers          │
│   - API Controllers (JSON responses)    │
├─────────────────────────────────────────┤
│              Service Layer              │
│          Business Logic                 │
│   - AuthService (Authentication)        │
│   - ProductService (Catalog mgmt)       │
│   - CartService (Shopping cart)         │
│   - FileStorageService (File handling)  │
├─────────────────────────────────────────┤
│           Repository Layer              │
│         Data Access Objects            │
│   - Spring Data JPA Repositories       │
│   - Custom Query Methods               │
│   - Transaction Management             │
├─────────────────────────────────────────┤
│             Entity Layer                │
│          Domain Models                  │
│   - Customer, Product, Order entities  │
│   - JPA Relationships & Constraints    │
│   - Audit Fields (created/updated)     │
└─────────────────────────────────────────┘
```

---

## 🎨 Frontend Technologies

### View Layer Architecture
- **JSP Templates** - Server-side rendering with shared layouts
- **Component-based Design** - Reusable header, footer, and navigation components
- **Responsive Layouts** - Mobile-first design with Bootstrap grid system
- **Custom SCSS** - Modularized stylesheets for maintainable CSS

### UI Components
- **Product Catalog** - Grid/list view with filtering and pagination
- **Shopping Cart** - Dynamic cart updates with variant pricing
- **User Forms** - Validation-enhanced registration and profile forms
- **Admin Interface** - Data tables with CRUD operations
- **File Upload** - Multi-file upload with preview functionality

### Client-Side Features
- **AJAX Integration** - Asynchronous cart operations and form submissions
- **Form Validation** - Client-side validation with server-side verification
- **Image Handling** - Lazy loading and responsive image optimization
- **Interactive Elements** - Dynamic product variant selection

---

## ⚙️ Backend Architecture

### MVC Implementation
- **Controllers** - 11+ specialized controllers handling web and API requests
- **Services** - 13+ business logic services with transaction management
- **Repositories** - Spring Data JPA repositories with custom queries
- **DTOs** - Data Transfer Objects for API communication and form binding

### Key Services
- **AuthService** - User registration, login, and credential management
- **ProductService** - Product catalog operations and variant management
- **CartService** - Shopping cart persistence and calculations
- **FileStorageService** - Image upload, processing, and organization
- **CurrentUserService** - Session management and user context

### Data Management
- **Entity Relationships** - Complex JPA relationships (OneToMany, ManyToOne)
- **Variant-based Pricing** - Size and color-specific product pricing
- **Audit Trail** - Created/updated timestamps on all entities
- **Cascade Operations** - Proper entity lifecycle management

---

## 🔒 Security Framework

### Spring Security Configuration
```java
@EnableWebSecurity
- BCrypt password encoding
- Role-based authorization (USER/ADMIN)
- Custom UserDetailsService implementation
- Session management with timeout handling
```

### Authentication Features
- **Email-based Login** - User authentication via email address
- **Password Validation** - Strong password requirements with pattern matching
- **Account Management** - Account locking and enabling capabilities
- **Role Management** - Hierarchical role assignment and verification

### Access Control
- **URL-based Security** - Method-level access control
- **CSRF Protection** - Token-based request validation
- **Session Security** - Secure session cookie configuration
- **API Security** - REST endpoint protection with role verification

---

## 🧪 Testing & Quality Assurance

### Testing Framework
- **JUnit 5** - Modern testing with DisplayName annotations and advanced assertions
- **Spring Boot Test** - Full application context testing with test slices
- **Integration Tests** - Database integration with @Transactional rollback
- **Unit Tests** - Service layer testing with dependency injection

### Test Coverage
```java
@SpringBootTest
class SignupPersistenceTest {
    @Test
    @DisplayName("Register creates Customer + Credential with encoded password")
    @Transactional
    void registerPersistsEntities() {
        // Comprehensive user registration testing
        // Password encoding verification
        // Database persistence validation
    }
}
```

### Testing Capabilities
- **Authentication Testing** - User registration and login validation
- **Password Security** - Encryption and matching verification
- **Data Persistence** - Entity relationship testing
- **Exception Handling** - Error scenario validation
- **Duplicate Prevention** - Constraint violation testing

---

## ⚠️ Exception Handling

### Comprehensive Error Management
The application implements robust exception handling across all layers:

### Custom Exception Types
- **IllegalArgumentException** - Input validation errors
- **DataIntegrityViolationException** - Database constraint violations
- **UsernameNotFoundException** - Authentication failures
- **IOException** - File operation errors

### Validation & Error Handling
```java
// AuthService Exception Examples
throw new IllegalArgumentException("Username is required");
throw new IllegalArgumentException("Email already exists");
throw new IllegalArgumentException("Password must be at least 8 chars...");
```

### Error Handling Patterns
- **Service Layer Validation** - Input validation with descriptive error messages
- **Database Constraint Handling** - Graceful handling of unique constraint violations
- **File Operation Errors** - Comprehensive IOException handling for uploads
- **Authentication Failures** - Secure error messages preventing information disclosure

### Global Error Handling
- **@ControllerAdvice** - Global exception handling across controllers
- **Custom Error Pages** - User-friendly error page rendering
- **Logging Integration** - Comprehensive error logging for debugging
- **Transaction Rollback** - Automatic rollback on service layer exceptions

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher (OpenJDK recommended)
- **Maven 3.6+** for dependency management
- **Docker & Docker Compose** for database setup
- **Git** for version control

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/Nike-e-commerce-web-application.git
   cd Nike-e-commerce-web-application
   ```

2. **Database Setup**
   ```bash
   # Start MySQL container
   docker-compose up -d
   
   # Verify database is running
   docker-compose ps
   ```

3. **Application Build**
   ```bash
   # Clean and compile
   mvn clean compile
   
   # Run tests
   mvn test
   
   # Package application
   mvn package -DskipTests
   ```

4. **Run Application**
   ```bash
   # Development mode
   mvn spring-boot:run
   
   # Or run packaged JAR
   java -jar target/Nike-Ecommerce-Application.jar
   ```

5. **Access Application**
   - **Customer Portal**: http://localhost:9090
   - **Admin Dashboard**: http://localhost:9090/admin
   - **API Endpoints**: http://localhost:9090/api/*

---

## 📊 Database Schema

### Core Entities
- **Customer Management**: `customers`, `credentials`, `addresses`, `contact_info`
- **Product Catalog**: `products`, `product_variants`, `product_images`, `product_colors`, `categories`
- **Shopping & Orders**: `cart_items`, `orders`, `order_items`
- **Content**: `reviews`, `feedback`

### Advanced Features
- **Variant-based Pricing** - Size and color-specific pricing in `product_variants`
- **Image Management** - Color-specific image organization
- **Audit Fields** - Created/updated timestamps on all entities
- **Referential Integrity** - Foreign key constraints with cascade options

---

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=9090
server.servlet.context-path=/

# Database Configuration
spring.datasource.url=jdbc:mysql://127.0.0.1:3307/nike_store?useSSL=false
spring.datasource.username=root
spring.datasource.password=trinhquocviet2005
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA & HibernateConfiguration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# View Resolution
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB

# Security Configuration
spring.security.enabled=true
logging.level.org.springframework.security=DEBUG
```

---

## 📱 API Endpoints

### Customer Endpoints
- `GET /` - Landing page with featured products
- `GET /products` - Product catalog with filtering
- `GET /product/{id}` - Detailed product view
- `POST /api/cart/add` - Add item to cart (AJAX)
- `GET /cart` - Shopping cart view
- `POST /auth/register` - User registration
- `POST /auth/login` - User authentication
- `GET /profile` - User profile management

### Admin Endpoints
- `GET /admin` - Admin dashboard
- `GET /admin/product/list` - Product management interface
- `POST /admin/product/add-save` - Create new product
- `GET /admin/product/edit/{id}` - Edit product form
- `POST /admin/product/edit-save` - Update product
- `GET /admin/product/delete/{id}` - Delete product

### REST API
- `GET /api/products` - JSON product catalog
- `POST /api/cart/add` - Add to cart (JSON response)
- `GET /api/cart/items` - Cart contents (JSON)

---

## 🎯 Usage Examples

### Customer Workflow
1. **Registration** - Create account with email verification
2. **Browse Products** - Filter by category, price, size, color
3. **Product Selection** - Choose variants and add to cart
4. **Cart Management** - Review items with variant-specific pricing
5. **Profile Management** - Update personal information

### Admin Workflow
1. **Product Creation** - Add products with multiple variants
2. **Image Management** - Upload and organize product images
3. **Category Management** - Organize product hierarchy
4. **Customer Management** - Monitor user accounts and activities

---

## 📁 Enhanced Project Structure

```
Nike-e-commerce-web-application/
├── src/
│   ├── main/
│   │   ├── java/vn/devpro/javaweb32/
│   │   │   ├── entity/          # JPA Entities with relationships
│   │   │   │   ├── customer/    # Customer, Credential, Address
│   │   │   │   ├── product/     # Product, ProductVariant, ProductColor, ProductImage
│   │   │   │   ├── cart/        # CartItem with variant pricing
│   │   │   │   └── order/       # Order processing entities
│   │   │   ├── repository/      # Spring Data JPA repositories
│   │   │   ├── service/         # Business logic layer
│   │   │   │   ├── administrator/ # Admin-specific services
│   │   │   │   ├── customer/    # Customer-facing services
│   │   │   │   └── impl/        # Service implementations
│   │   │   ├── controller/      # MVC Controllers
│   │   │   │   ├── customer/    # Customer portal controllers
│   │   │   │   ├── administrator/ # Admin panel controllers
│   │   │   │   ├── auth/        # Authentication controllers
│   │   │   │   ├── product/     # Product & API controllers
│   │   │   │   └── cart/        # Shopping cart controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── customer/    # Customer-facing DTOs
│   │   │   │   └── administrator/ # Admin DTOs
│   │   │   ├── config/          # Configuration classes
│   │   │   │   ├── SecurityConfig.java # Spring Security setup
│   │   │   │   ├── MvcConfig.java      # MVC configuration
│   │   │   │   └── GlobalModelAttributes.java # Global model data
│   │   │   └── common/          # Utilities and constants
│   │   ├── resources/
│   │   │   ├── static/          # Frontend assets
│   │   │   │   ├── css/         # Custom stylesheets
│   │   │   │   ├── js/          # JavaScript files
│   │   │   │   └── images/      # Static images
│   │   │   ├── db/migration/    # Flyway migration scripts
│   │   │   └── application.properties # Application configuration
│   │   └── webapp/WEB-INF/views/
│   │       ├── customer/        # Customer-facing JSP pages
│   │       │   ├── index.jsp    # Landing page
│   │       │   ├── product-list.jsp # Product catalog
│   │       │   ├── product-detail.jsp # Product details
│   │       │   ├── checkout.jsp # Shopping cart
│   │       │   └── profile.jsp  # User profile
│   │       ├── administrator/   # Admin interface JSP pages
│   │       │   ├── home.jsp     # Admin dashboard
│   │       │   ├── product/     # Product management pages
│   │       │   └── category/    # Category management
│   │       └── common/          # Shared JSP components
│   └── test/
│       └── java/vn/devpro/javaweb32/
│           └── auth/            # Authentication tests
│               └── SignupPersistenceTest.java # Comprehensive auth testing
├── docker-compose.yml           # Database containerization
├── pom.xml                     # Maven dependencies & plugins
└── README.md                   # Comprehensive documentation
```

---

## 🤝 Contributing

We welcome contributions! Please follow these enhanced guidelines:

### Development Workflow
1. **Fork & Clone** - Create your feature branch
2. **Environment Setup** - Follow installation instructions
3. **Code Standards** - Maintain existing patterns and conventions
4. **Testing** - Write tests for new features using JUnit 5
5. **Documentation** - Update README and code comments
6. **Pull Request** - Submit with detailed description

### Code Quality Standards
- **Java Conventions** - Follow Oracle Java coding standards
- **Spring Boot Patterns** - Maintain MVC architecture principles
- **Exception Handling** - Implement comprehensive error handling
- **Security Best Practices** - Follow Spring Security guidelines
- **Test Coverage** - Maintain high test coverage for critical paths

---

## 🐛 Troubleshooting & Support

### Common Issues & Solutions

1. **Database Connection Failures**
   ```bash
   # Check Docker container status
   docker-compose ps
   
   # Restart database
   docker-compose down && docker-compose up -d
   
   # Verify connection settings in application.properties
   ```

2. **Authentication Issues**
   ```bash
   # Clear browser cache and cookies
   # Check user credentials in database
   # Verify BCrypt password encoding
   ```

3. **File Upload Problems**
   ```bash
   # Check file size limits in application.properties
   # Verify upload directory permissions
   # Review server logs for IOException details
   ```

4. **Test Execution Failures**
   ```bash
   # Run tests with verbose output
   mvn test -X
   
   # Run specific test class
   mvn test -Dtest=SignupPersistenceTest
   ```

### Support Resources
- **GitHub Issues** - Report bugs and feature requests
- **Documentation** - Comprehensive inline code documentation
- **Logs** - Check application logs for detailed error information
- **Community** - Engage with project contributors

---

## 📄 License & Attribution

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for complete details.

### Third-Party Dependencies
- **Spring Framework** - Apache License 2.0
- **Bootstrap** - MIT License
- **MySQL Connector** - GPL License with commercial exception
- **JUnit 5** - Eclipse Public License 2.0

### Acknowledgments
- Built with modern Spring Boot ecosystem
- Follows enterprise architecture patterns
- Implements security best practices
- Comprehensive testing framework integration

---

<p align="center">
  <strong>🚀 Built with ❤️ using Enterprise Java & Modern Web Technologies 🚀</strong>
</p>

<p align="center">
  <em>"Just Do It" - Nike E-Commerce Platform with Championship-Grade Architecture</em>
</p>

<p align="center">
  <strong>Ready for Production • Scalable • Secure • Well-Tested</strong>
</p>
