<p align="center">
  <img src="src/main/resources/static/images/air-jordan-logo.png" alt="Nike Logo" width="140"/>
</p>

<h1 align="center">🏃‍♂️ Nike E-Commerce Web Application</h1>
<p align="center"><em>"Just Do It" - Now with Enterprise-Grade Architecture</em></p>

<p align="center">
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg" alt="Spring Boot"/></a>
  <a href="https://www.oracle.com/java/"><img src="https://img.shields.io/badge/Java-11-orange.svg" alt="Java"/></a>
  <a href="https://maven.apache.org/"><img src="https://img.shields.io/badge/Maven-3.6+-blue.svg" alt="Maven"/></a>
  <a href="https://www.mysql.com/"><img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL"/></a>
  <a href="https://docker.com"><img src="https://img.shields.io/badge/Docker-Compose-blue.svg" alt="Docker"/></a>
  <a href="https://hibernate.org/"><img src="https://img.shields.io/badge/JPA-Hibernate-red.svg" alt="JPA"/></a>
  <a href="https://spring.io/guides/gs/serving-web-content/"><img src="https://img.shields.io/badge/Architecture-MVC-purple.svg" alt="Architecture"/></a>
  <a href="https://flywaydb.org/"><img src="https://img.shields.io/badge/Database-Flyway-orange.svg" alt="Flyway"/></a>
</p>

---

## 🚀 Overview

Welcome to the **Nike E-Commerce Web Application** – where championship performance meets cutting-edge technology. Built with enterprise-grade Spring Boot architecture, this application delivers an unparalleled shopping experience that scales like a world-class athlete.

---

## 📑 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [How it works](#how-it-works)
- [Contact](#contact)
- [Folder Structure](#folder-structure)
- [Contributing](#contributing)
- [Screenshots](#screenshots)
- [License](#license)

---

## ✨ Features
- Modern, responsive UI/UX
- Secure authentication & authorization
- Product catalog, search, and filtering
- Shopping cart & checkout
- Order management
- Admin dashboard
- Database migrations with Flyway
- Dockerized for easy deployment

## 🛠 Tech Stack

This project leverages a modern, enterprise-grade technology stack:

- **Backend:**
  - Java 11
  - Spring Boot 2.7.18
  - Spring MVC (Model-View-Controller)
  - Spring Data JPA (Hibernate)
  - Flyway (Database Migrations)
- **Frontend:**
  - JSP (JavaServer Pages)
  - HTML5, CSS3, JavaScript
- **Database:**
  - MySQL 8.0
- **Build & Dependency Management:**
  - Maven 3.6+
- **Containerization & Deployment:**
  - Docker & Docker Compose
- **Other:**
  - RESTful APIs
  - Responsive design principles
  - Modular folder structure for scalability

## 🚦 Getting Started

```bash
# Clone the repository
git clone https://github.com/supaFrik/Nike-e-commerce-web-application
cd nike-ecommerce-webapp

# Build the project
mvn clean install

# Run with Docker Compose
docker-compose up --build
```

- Configure your database in `src/main/resources/application.properties`.
- Access the app at `http://localhost:9090` after startup.

## 🖼 How it works

Experience a seamless shopping journey:
1. Browse the product catalog with advanced search and filtering.
2. Add your favorite Nike products to the cart.
3. Securely checkout and manage your orders.
4. Admins can manage products, orders, and users via the dashboard.

## 🗂 Folder Structure

A quick look at the main project folders:

```
Nike Ecommerce Web Application/
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   │   ├── static/
│   │   │   ├── db/
│   │   │   └── application.properties
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
├── pom.xml
├── docker-compose.yml
├── README.md
```

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request. For major changes, open an issue first to discuss what you would like to change.

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 🤝 Contact
- **Author:** [supaFrikky](mailto:tqv2005business@gmail.com)
- **Project Link:** [GitHub Repository](https://github.com/supaFrik/Nike-e-commerce-web-application)

---

<p align="center"><sub>© 2025 Nike E-Commerce Web Application. All rights reserved.</sub></p>
