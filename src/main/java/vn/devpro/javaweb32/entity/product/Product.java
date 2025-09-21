package vn.devpro.javaweb32.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.customer.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(length = 25, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(length = 255, nullable = true)
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "user_create_id")
    private Customer userCreateProduct;

    @ManyToOne
    @JoinColumn(name = "user_update_id")
    private Customer userUpdateProduct;

    @Column(length = 255, nullable = true)
    private String seo;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    private List<ProductColor> colors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @Transient
    private String imageUrl;

    @Column(nullable = false)
    private boolean favourites;


    public String getImageUrl() {
        if (imageUrl != null) {
            return imageUrl;
        }
        if (images != null && !images.isEmpty() && images.get(0) != null) {
            return images.get(0).getUrl();
        }
        return null;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Customer getUserCreateProduct() {
        return userCreateProduct;
    }
    public void setUserCreateProduct(Customer userCreateProduct) {
        this.userCreateProduct = userCreateProduct;
    }
    public Customer getUserUpdateProduct() {
        return userUpdateProduct;
    }
    public void setUserUpdateProduct(Customer userUpdateProduct) {
        this.userUpdateProduct = userUpdateProduct;
    }
    public String getSeo() {
        return seo;
    }
    public void setSeo(String seo) {
        this.seo = seo;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public List<ProductColor> getColors() {
        return colors;
    }

    public void setColors(List<ProductColor> colors) {
        this.colors = colors;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getColorCount() {
        if (variants == null || variants.isEmpty()) {
            return 0;
        }
        // Use a set to count unique colors
        java.util.Set<ProductColor> colors = new java.util.HashSet<>();
        for (ProductVariant variant : variants) {
            if (variant.getColor() != null) {
                colors.add(variant.getColor());
            }
        }
        return colors.size();
    }

    public boolean getFavourites() {
        return favourites;
    }

    public void setFavourites(boolean favourites) {
        this.favourites = favourites;
    }

    public void addRelationalProductImage(ProductImage productImage) {
        images.add(productImage);
        productImage.setProduct(this);
    }
}
