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
</p>

---

## 🚀 Overview

Welcome to the **Nike E-Commerce Web Application** – where championship performance meets cutting-edge technology. Built with enterprise-grade Spring Boot architecture, this application delivers an unparalleled shopping experience with comprehensive customer and admin functionalities.

This full-stack e-commerce platform provides a complete online shopping solution with modern web technologies, secure authentication, and scalable architecture designed for high performance.

---

## 📑 Table of Contents
- [✨ Features](#-features)
- [🛠 Tech Stack](#-tech-stack)
- [🏗 Architecture](#-architecture)
- [���� Getting Started](#-getting-started)
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
- **User Registration & Authentication** - Secure signup/login with Spring Security
- **Product Catalog** - Browse Nike products with advanced filtering and search
- **Product Details** - Detailed product pages with multiple images, variants, and colors
- **Shopping Cart** - Add/remove items with real-time cart management
- **User Profile** - Manage personal information, addresses, and contact details
- **Order Management** - View order history and track purchases
- **Product Reviews** - Rate and review products
- **Responsive Design** - Mobile-friendly interface

### 👨‍💼 Admin Features
- **Product Management** - CRUD operations for products, categories, and variants
- **Customer Management** - View and manage customer accounts
- **Order Management** - Process and track customer orders
- **Dashboard** - Analytics and reporting capabilities
- **Content Management** - Manage product images, descriptions, and SEO

### 🔒 Security Features
- **Spring Security Integration** - Role-based access control
- **Password Encryption** - Secure password storage
- **Session Management** - Secure user sessions
- **CSRF Protection** - Cross-site request forgery prevention

---

## 🛠 Tech Stack

### Backend
- **Java 17** - Modern Java features and performance
- **Spring Boot 2.7.18** - Enterprise application framework
- **Spring MVC** - Web layer architecture
- **Spring Data JPA** - Data persistence layer
- **Spring Security** - Authentication and authorization
- **Hibernate** - ORM framework with MySQL dialect
- **Maven** - Dependency management and build tool

### Database
- **MySQL 8.0** - Primary database
- **Flyway** - Database migration and versioning
- **JPA/Hibernate** - Object-relational mapping

### Frontend
- **JSP (Java Server Pages)** - Server-side rendering
- **JSTL** - JSP Standard Tag Library
- **Bootstrap** - Responsive CSS framework
- **JavaScript** - Client-side functionality
- **SCSS** - Enhanced CSS preprocessing

### DevOps & Tools
- **Docker Compose** - Containerized database setup
- **Maven** - Build automation
- **Tomcat Embedded** - Application server

---

## 🏗 Architecture

The application follows a **layered MVC architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────┐
│               Presentation Layer         │
│        (Controllers & Views)            │
├─────────────────────────────────────────┤
│               Service Layer             │
│           (Business Logic)              │
├─────────────────────────────────────────┤
│            Repository Layer             │
│         (Data Access Objects)          │
├─────────────────────────────────────────┤
│              Entity Layer               │
│            (Domain Models)              │
└─────────────────────────────────────────┘
```

### Key Components:
- **Entities**: Customer, Product, Order, CartItem, Review, Category, etc.
- **Repositories**: Spring Data JPA repositories for data access
- **Services**: Business logic implementation
- **Controllers**: HTTP request handling (Web + REST API)
- **DTOs**: Data Transfer Objects for API communication
- **Configuration**: Security, JPA auditing, MVC configuration

---

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Docker & Docker Compose** (for database)
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/supaFrik/Nike-e-commerce-web-application.git
   cd Nike-e-commerce-web-application
   ```

2. **Start the database using Docker**
   ```bash
   docker-compose up -d
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - **Customer Portal**: http://localhost:9090
   - **Admin Dashboard**: http://localhost:9090/admin
   - **Database**: localhost:3307 (MySQL)

### Default Credentials
- **Database**: 
  - Host: localhost:3307
  - Database: nike_store
  - Username: root
  - Password: trinhquocviet2005

---

## 📊 Database Schema

The application uses MySQL with the following main entities:

- **Customer Management**: `customer`, `credential`, `address`, `contact_info`
- **Product Catalog**: `products`, `product_variant`, `product_image`, `product_color`, `category`
- **Order Processing**: `order`, `order_item`
- **Shopping Cart**: `cart_item`
- **Reviews**: `review`

Database migrations are managed with Flyway for version control and consistency.

---

## 🔧 Configuration

### Application Properties
Key configurations in `application.properties`:

```properties
# Server Configuration
server.port=9090

# Database Configuration
spring.datasource.url=jdbc:mysql://127.0.0.1:3307/nike_store
spring.datasource.username=root
spring.datasource.password=trinhquocviet2005

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# View Configuration
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```

### Docker Configuration
The `docker-compose.yml` sets up MySQL 8.0 with persistent data storage.

---

## 📱 API Endpoints

### Customer API
- `GET /` - Home page
- `GET /products` - Product catalog
- `GET /product/{id}` - Product details
- `POST /cart/add` - Add to cart
- `GET /cart` - View cart
- `POST /auth/register` - User registration
- `POST /auth/login` - User login

### Admin API
- `GET /admin` - Admin dashboard
- `GET /admin/products` - Product management
- `POST /admin/product` - Create product
- `PUT /admin/product/{id}` - Update product
- `DELETE /admin/product/{id}` - Delete product

---

## 🎯 Usage

### For Customers:
1. Register a new account or login
2. Browse products by categories
3. Use search and filters to find products
4. Add items to cart
5. Proceed to checkout
6. Manage profile and view order history

### For Administrators:
1. Access admin panel at `/admin`
2. Manage product catalog
3. Process customer orders
4. View analytics and reports
5. Manage customer accounts

---

## 📁 Project Structure

```
Nike-e-commerce-web-application/
├── src/
│   ├── main/
│   │   ├── java/vn/devpro/javaweb32/
│   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── customer/    # Customer-related entities
│   │   │   │   ├── product/     # Product-related entities
│   │   │   │   ├── order/       # Order management entities
│   │   │   │   └── cart/        # Shopping cart entities
│   │   │   ├── repository/      # Data access layer
│   │   │   ├── service/         # Business logic layer
│   │   │   │   ├── administrator/ # Admin services
│   │   │   │   └── customer/    # Customer services
│   │   │   ├── controller/      # Web controllers
│   │   │   │   ├── customer/    # Customer controllers
│   │   │   │   ├── product/     # Product controllers
│   │   │   │   ├── cart/        # Cart controllers
│   │   │   │   └── auth/        # Authentication controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── config/          # Configuration classes
│   │   │   └── common/          # Common utilities
│   │   ├── resources/
���   │   │   ├── static/          # CSS, JS, images
│   │   │   ├── db/migration/    # Flyway migrations
│   │   │   └── application.properties
│   │   └── webapp/WEB-INF/views/
│   │       ├── customer/        # Customer JSP pages
│   │       ├── administrator/   # Admin JSP pages
│   │       └── common/          # Shared components
├── docker-compose.yml           # Database setup
├── pom.xml                     # Maven configuration
└── README.md                   # Project documentation
```

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit your changes**: `git commit -m 'Add amazing feature'`
4. **Push to the branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Development Guidelines:
- Follow Java coding standards
- Write unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

---

## 🐛 Troubleshooting

### Common Issues:

1. **Database Connection Error**
   - Ensure Docker is running: `docker-compose ps`
   - Check database credentials in `application.properties`

2. **Port Already in Use**
   - Change server port in `application.properties`
   - Kill process using port: `netstat -ano | findstr :9090`

3. **Maven Build Fails**
   - Ensure Java 17 is installed and configured
   - Clean and reinstall dependencies: `mvn clean install -U`

---

## 📞 Contact

For questions, suggestions, or support:

- **GitHub Issues**: [Create an issue](https://github.com/supaFrik/Nike-e-commerce-web-application/issues)
- **Email**: [tqv2005business@gmail.com]
- **Documentation**: Check this README and code comments

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  <strong>Built with ❤️ using Spring Boot & Modern Web Technologies</strong>
</p>

<p align="center">
  <em>"Just Do It" - Nike E-Commerce Platform</em>
</p>
