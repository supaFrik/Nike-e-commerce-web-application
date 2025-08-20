<p align="center">
  <img src="src/main/resources/customer/img/air-jordan-logo.png" alt="Nike Logo" width="180"/>
</p>

# 🏃‍♂️ Nike E-Commerce Web Application
### *"Just Do It" - Now with Enterprise-Grade Architecture*

<div align="center">

  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen.svg)](https://spring.io/projects/spring-boot)
  [![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
  [![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
  [![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
  [![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)](https://docker.com)
  [![JPA](https://img.shields.io/badge/JPA-Hibernate-red.svg)](https://hibernate.org/)
  [![Architecture](https://img.shields.io/badge/Architecture-MVC-purple.svg)](https://spring.io/guides/gs/serving-web-content/)
  [![Flyway](https://img.shields.io/badge/Database-Flyway-orange.svg)](https://flywaydb.org/)
</div>

## 🚀 Performance Meets Innovation

Welcome to the **Nike E-Commerce Web Application** - where championship performance meets cutting-edge technology. Built with enterprise-grade Spring Boot architecture, this application delivers an unparalleled shopping experience that scales like a world-class athlete.

> *"Excellence isn't a skill, it's an attitude. And we've architected that attitude into every line of code."*

---

## ✨ Features That Champion Excellence

### 🏆 **Core E-Commerce Features**
- **🛍️ Product Management**: Complete product catalog with categories and detailed information
- **🔍 Advanced Search**: Lightning-fast product search and filtering capabilities
- **⭐ Customer Reviews**: Authentic feedback system with comprehensive rating system
- **📦 Order Management**: Full order lifecycle from cart to delivery tracking
- **👤 Customer Profiles**: Personalized user accounts with order history
- **📧 Contact System**: Direct customer communication channels

### 🎯 **Enterprise Architecture Features**
- **🏗️ Clean MVC Architecture**: Layered design with clear separation of concerns
- **📊 Database Migration**: Flyway-powered version control for database schemas
- **🔄 JPA Entity Relationships**: Sophisticated data modeling with proper associations
- **✅ Data Validation**: Comprehensive validation at all application layers
- **🐳 Docker Integration**: Containerized MySQL database for easy deployment
- **🎨 Responsive UI**: Mobile-first design with modern CSS and JavaScript

### 🛠️ **Technical Excellence**
- **⚡ Optimized Performance**: Strategic database indexing and query optimization
- **🔐 Security Implementation**: Enterprise-grade security configurations
- **📱 Multi-Device Support**: Seamless experience across desktop and mobile
- **🌐 Internationalization Ready**: Built for global Nike market expansion

---

## 🛠️ Technology Stack

<div align="center">

| Category | Technology | Version | Purpose |
|----------|------------|---------|---------|
| **Backend Framework** | Spring Boot | 2.7.18 | Core application framework |
| **Language** | Java | 11 | Programming language |
| **Build Tool** | Maven | 3.6+ | Dependency management & build |
| **Database** | MySQL | 8.0 | Primary data storage |
| **ORM** | Hibernate/JPA | Latest | Object-relational mapping |
| **Migration** | Flyway | Latest | Database version control |
| **View Engine** | JSP/JSTL | 1.2.5 | Server-side rendering |
| **Frontend** | HTML5, CSS3, JS | Latest | User interface |
| **Containerization** | Docker Compose | Latest | Database orchestration |
| **Server** | Embedded Tomcat | Latest | Application server |

</div>

---

## 🏗️ Project Architecture

### **Directory Structure**
```
nike-ecommerce-web-application/
├── 📁 src/main/java/vn/devpro/javaweb32/
│   ├── 🎯 controller/           # MVC Controllers
│   │   ├── administrator/       # Admin panel controllers
│   │   ├── auth/               # Authentication controllers
│   │   └── customer/           # Customer-facing controllers
│   ├── 📊 model/               # JPA Entities
│   │   ├── base/               # Base model classes
│   │   ├── category/           # Product categories
│   │   ├── customer/           # Customer entities
│   │   ├── order/              # Order management
│   │   ├── product/            # Product entities
│   │   └── review/             # Review system
│   ├── 🗃️ repository/          # Data access layer
│   ├── ⚙️ service/             # Business logic layer
│   ├── 🔧 config/              # Configuration classes
│   ├── 🛡️ security/            # Security configurations
│   └── 📋 dto/                 # Data Transfer Objects
├── 📁 src/main/resources/
│   ├── 🎨 customer/            # Frontend assets
│   │   ├── css/                # Stylesheets
│   │   ├── img/                # Images & logos
│   │   ├── scripts/            # JavaScript files
│   │   └── font/               # Custom fonts
│   ├── 👨‍💼 administrator/       # Admin panel assets
│   └── 🗄️ db/migration/         # Flyway database migrations
└── 📁 src/main/webapp/WEB-INF/views/ # JSP templates
```

### **Core Components**

#### 🎯 **Controller Layer**
- **HomeController**: Landing page and navigation
- **CustomerHomeController**: Customer dashboard and product browsing
- **CustomerContactController**: Customer support and communication
- **BaseController**: Shared controller functionality

#### 📊 **Data Models**
- **Product Management**: Complete product catalog with images and pricing
- **Category System**: Hierarchical product categorization
- **Order Processing**: Full order lifecycle management  
- **Customer Profiles**: User accounts and preferences
- **Review System**: Product ratings and feedback

#### 🗄️ **Database Architecture**
- **Flyway Migrations**: Version-controlled schema evolution
- **MySQL 8.0**: High-performance relational database
- **JPA/Hibernate**: Object-relational mapping with optimizations
- **Connection Pooling**: Efficient database connection management

---

## 🚀 Quick Start Guide

### **Prerequisites**
- ☕ Java 11 or higher
- 📦 Maven 3.6+
- 🐳 Docker & Docker Compose
- 🌐 Modern web browser

### **Installation & Setup**

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd Nike\ Ecommerce\ Web\ Application
   ```

2. **Start Database Services**
   ```bash
   docker-compose up -d
   ```

3. **Build & Run Application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the Application**
   - 🌐 **Main Application**: http://localhost:9090
   - 🗄️ **Database**: localhost:3307 (MySQL)
   - 📊 **Database Name**: nike_store

### **Default Configuration**
- **Server Port**: 9090
- **Database**: MySQL 8.0 on port 3307
- **Context Path**: Root (/)
- **JSP View Resolver**: /WEB-INF/views/*.jsp

---

## 📊 Features Deep Dive

### 🛍️ **Customer Experience**
- **Product Catalog**: Browse Nike's complete collection with high-quality images
- **Smart Search**: Advanced filtering by category, price, and specifications  
- **User Accounts**: Personalized shopping experience with order history
- **Reviews & Ratings**: Community-driven product feedback system
- **Responsive Design**: Optimized for all devices and screen sizes

### 👨‍💼 **Administrative Features**
- **Product Management**: CRUD operations for product catalog
- **Order Tracking**: Monitor and manage customer orders
- **Customer Support**: Direct communication with customers
- **Analytics Dashboard**: Business intelligence and reporting tools
- **Content Management**: Update site content and promotions

### 🔧 **Technical Features**
- **Database Migrations**: Automated schema updates with Flyway
- **Soft Delete**: Data integrity with logical deletion patterns
- **Audit Trails**: Comprehensive logging and change tracking
- **Performance Optimization**: Strategic caching and query optimization
- **Security**: Input validation and SQL injection protection

---

## 🤝 Contributing

We welcome contributions that help us build the ultimate Nike shopping experience! Please follow these guidelines:

1. 🔀 Fork the repository
2. 🌿 Create a feature branch (`git checkout -b feature/amazing-feature`)
3. ✅ Commit your changes (`git commit -m 'Add amazing feature'`)
4. 📤 Push to the branch (`git push origin feature/amazing-feature`)
5. 🔃 Open a Pull Request

---

## 📝 License

This project is developed for educational and demonstration purposes, showcasing enterprise-grade Spring Boot architecture and modern web development practices.

---

## 📞 Support & Contact

For technical support or business inquiries:

- 📧 **Email**: tqv2005business@gmail.com
- 🐛 **Issues**: [GitHub Issues](../../issues)
- 📚 **Documentation**: [Wiki](../../wiki)

---

<div align="center">
<img src="https://media.giphy.com/media/3oriNOV42vZdh31e48/giphy.gif" alt="Goodnight Animation" style="max-width:100%; height:auto;">
### *Built with ❤️ and Java ☕*

**Just Do It** - *With Code That Performs*
</div>
