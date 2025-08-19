<p align="center">
  <img src="../Nike Ecommerce Web Application/src/main/resources/customer/img/air-jordan-logo.png" alt="Nike Logo" width="180"/>
</p>

# 🏃‍♂️ Nike E-Commerce Web Application
### *"Just Do It" - Now with Enterprise-Grade Architecture*

<div align="center">

  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.1.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
  [![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
  [![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
  [![JPA](https://img.shields.io/badge/JPA-Hibernate-red.svg)](https://hibernate.org/)
  [![Architecture](https://img.shields.io/badge/Architecture-MVC-purple.svg)](https://spring.io/guides/gs/serving-web-content/)
  [![Security](https://img.shields.io/badge/Security-Enhanced-success.svg)](#security-features)
</div>

## 🚀 Performance Meets Innovation

Welcome to the **Nike E-Commerce Web Application** - where championship performance meets cutting-edge technology. Built with enterprise-grade Spring Boot architecture, this application delivers an unparalleled shopping experience that scales like a world-class athlete.

> *"Excellence isn't a skill, it's an attitude. And we've architected that attitude into every line of code."*

---

<!-- Shopping GIF in Features Section -->
<p align="center">
  <img src="https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif" alt="Shopping Experience" width="250"/>
</p>

## ✨ Features That Champion Excellence

### 🏆 **Enhanced Entity Management System**
- **🔐 Unified BaseModel**: Comprehensive auditing with created/updated timestamps
- **🗑️ Soft Delete**: Data integrity with logical deletion instead of hard deletes
- **📧 JPA Auditing**: Automatic timestamp management with `@CreatedDate` and `@LastModifiedDate`
- **🏠 Flexible Architecture**: Support for both unified BaseModel and specialized base classes
- **👤 Entity Relationships**: Proper JPA mappings with cascade controls and fetch strategies

### 🛍️ **Premium Shopping Experience**
- **⚡ Lightning-Fast Performance**: Optimized database queries with strategic indexing
- **🔍 Advanced Search & Filtering**: Find your perfect Nike gear in milliseconds
- **📱 Responsive Design**: Seamless experience across all devices
- **🏪 Product Categories**: Comprehensive Nike collections with proper entity relationships
- **⭐ Customer Reviews**: Authentic feedback system with rating capabilities

### 🎯 **Enterprise Architecture Features**
- **🏗️ Clean Architecture**: Layered design with separation of concerns
- **✅ Bean Validation**: Comprehensive data validation at all layers
- **🔄 Relationship Management**: Bidirectional entity relationships with cascade controls
- **📊 Performance Optimized**: Strategic indexing and lazy loading
- **🎨 DTO Pattern**: Secure data transfer with dedicated DTOs

---

## 🛠️ Technology Stack

<div align="center">

| Category | Technology | Version | Purpose |
|----------|------------|---------|---------|
| **Backend Framework** | Spring Boot | 2.1.4 | Core application framework |
| **Language** | Java | 11 | Programming language |
| **Build Tool** | Maven | 3.6+ | Dependency management & build |
| **Database** | MySQL | 8.0+ | Primary data storage |
| **ORM** | Hibernate/JPA | 2.1.4 | Object-relational mapping |
| **View Engine** | JSP/JSTL | 1.2 | Server-side rendering |
| **Frontend** | HTML5, CSS3, JS | Latest | User interface |
| **Validation** | Bean Validation | 2.0 | Data validation |

</div>

---

## 🏗️ Architecture Overview

### **Unified BaseModel Design**
Our refactored architecture introduces a comprehensive `BaseModel` class that provides:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    @Column(name = "deleted_at")
    private Instant deletedAt;
    
    // Soft delete functionality
    public boolean isDeleted() { return deletedAt != null; }
    public void softDelete() { this.deletedAt = Instant.now(); }
    public void restore() { this.deletedAt = null; }
}
```

### **Key Architectural Improvements**

#### ✅ **Fixed Issues:**
- **Unified Entity Base**: Replaced fragmented base classes with comprehensive `BaseModel`
- **Proper JPA Annotations**: Added missing `@Entity`, `@Table`, and column mappings
- **Consistent ID Types**: Standardized all entities to use `Long` for primary keys
- **Audit Field Configuration**: Fixed `updatedAt` field to be properly updatable
- **Soft Delete Implementation**: Added proper column annotations and utility methods

#### 🎯 **Benefits:**
- **Consistency**: All entities follow the same base structure
- **Maintainability**: Single source of truth for common entity fields
- **Auditability**: Automatic tracking of creation and modification times
- **Data Integrity**: Soft delete prevents accidental data loss
- **Performance**: Proper indexing and lazy loading strategies

### **Entity Relationship Mapping**

```
BaseModel (Abstract)
├── Product
│   ├── @ManyToOne → Category
│   └── @ElementCollection → sizes
├── Category
│   └── @OneToMany → Product
├── Customer
│   ├── @OneToMany → Order
│   └── @OneToMany → Review
├── Order
│   ├── @ManyToOne → Customer
│   └── @OneToMany → OrderItem
└── Review
    ├── @ManyToOne → Customer
    └── @ManyToOne → Product
```

---

## 🚀 Quick Start Guide

### **Prerequisites**
- ☕ Java 11 or higher
- 🔧 Maven 3.6+
- 🗄️ MySQL 8.0+
- 🌐 Any modern web browser

### **Installation Steps**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-repo/nike-ecommerce.git
   cd nike-ecommerce/Javaweb32/Javaweb32
   ```

2. **Database Configuration**
   ```bash
   # Create MySQL database
   mysql -u root -p
   CREATE DATABASE nike_ecommerce;
   ```

3. **Configure Application Properties**
   ```properties
   # src/main/resources/application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/nike_ecommerce
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - 🏠 **Customer Portal**: http://localhost:8080
   - 🔧 **Admin Portal**: http://localhost:8080/admin

---

## 📚 API Documentation

### **Core Entities**

#### **Product Entity**
```java
@Entity
@Table(name = "products")
public class Product extends BaseModel {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Automatic auditing fields from BaseModel
    // + soft delete functionality
}
```

#### **Soft Delete Operations**
```java
// Soft delete a product
product.softDelete();
productRepository.save(product);

// Restore a deleted product
product.restore();
productRepository.save(product);

// Check if product is deleted
if (product.isDeleted()) {
    // Handle deleted product logic
}
```

---

## 🎯 Project Structure

```
src/main/java/vn/devpro/javaweb32/
├── 📁 config/                    # Configuration classes
│   └── JpaAuditingConfig.java   # JPA auditing configuration
├── 📁 model/                     # Entity models
│   ├── 📁 base/                 # Base entity classes
│   │   ├── BaseModel.java       # ✨ New unified base model
│   │   ├── AuditableEntity.java # Legacy auditing entity
│   │   └── SoftDeletableEntity.java # Legacy soft delete entity
│   ├── 📁 product/              # Product-related entities
│   │   ├── Product.java         # ✅ Refactored with BaseModel
│   │   ├── Category.java        # ✅ Refactored with BaseModel
│   │   └── Brand.java
│   ├── 📁 customer/             # Customer-related entities
│   ├── 📁 order/               # Order-related entities
│   └── 📁 review/              # Review-related entities
├── 📁 controller/               # MVC controllers
├── 📁 service/                  # Business logic layer
└── 📁 repository/              # Data access layer
```

---

## 🛡️ Security Features

- **🔒 Input Validation**: Bean validation on all entity fields
- **🛡️ SQL Injection Protection**: JPA prevents SQL injection attacks
- **🔐 Session Management**: Secure session handling
- **📝 Audit Trail**: Complete tracking of data changes
- **🗑️ Data Recovery**: Soft delete allows data recovery

---

## 🎨 Best Practices Implemented

### **Entity Design**
- ✅ Consistent base model inheritance
- ✅ Proper JPA annotations and mappings
- ✅ Strategic use of fetch types (LAZY/EAGER)
- ✅ Cascade operations for related entities
- ✅ Soft delete for data integrity

### **Performance Optimization**
- ✅ Database indexing on frequently queried fields
- ✅ Lazy loading for related entities
- ✅ Proper column definitions and constraints
- ✅ Optimized relationship mappings

### **Code Quality**
- ✅ Clean, readable, and maintainable code
- ✅ Proper separation of concerns
- ✅ Comprehensive error handling
- ✅ Consistent naming conventions

---

## 🔄 Migration Guide

If you're migrating from the old fragmented base classes to the new `BaseModel`:

### **Before (Old Structure)**
```java
public class Product extends BaseEntity {
    // Only had id field, no auditing
}
```

### **After (New Structure)**
```java
@Entity
@Table(name = "products")
public class Product extends BaseModel {
    // Now includes id, createdAt, updatedAt, deletedAt
    // Plus automatic auditing and soft delete
}
```

### **Database Migration**
```sql
-- Add audit columns to existing tables
ALTER TABLE products
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD COLUMN deleted_at TIMESTAMP NULL;

-- Add indexes for performance
CREATE INDEX idx_products_deleted_at ON products(deleted_at);
CREATE INDEX idx_products_created_at ON products(created_at);
```

---

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/amazing-feature`
3. **Commit** your changes: `git commit -m 'Add amazing feature'`
4. **Push** to the branch: `git push origin feature/amazing-feature`
5. **Open** a Pull Request

---

## 📧 Contact & Support

- **Developer**: VieTrinh AKA supaFrik
- **Email**: tqv2005business@gmail.com
- **Documentation**: [Wiki Pages](../../wiki)
- **Issues**: [GitHub Issues](../../issues)

---

<div align="center">

### *"Your potential is endless. Your code should be too."*

**Built with ❤️ by the Only Mighty SpaFrikky**

[⬆ Back to Top](#nike-e-commerce-web-application)

</div>

<!-- Motivational GIF at the End -->
<p align="center">
  <img src="https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif" alt="Just Do It Motivation" width="220"/>
</p>
