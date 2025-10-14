package vn.devpro.javaweb32.entity.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.customer.Customer;
import vn.devpro.javaweb32.entity.product.enums.ProductStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(length = 25, nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private BigDecimal salePrice;

    @Column(length = 1000, nullable = false)
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
    private ProductStatus productStatus;

//    @Column(nullable = true)
//    private BigDecimal discount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductVariant> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("product")
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductColor> colors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @Transient
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_image_id")
    private ProductImage mainImage;

    public Product() {
    }


    public int getColorCount() {
        if (variants == null || variants.isEmpty()) {
            return 0;
        }
        java.util.Set<ProductColor> colors = new java.util.HashSet<>();
        for (ProductVariant variant : variants) {
            if (variant.getColor() != null) {
                colors.add(variant.getColor());
            }
        }
        return colors.size();
    }

    public void addRelationalProductImage(ProductImage productImage) {
        images.add(productImage);
        productImage.setProduct(this);
    }

    public String getImageUrl() {
        if (imageUrl != null) {
            return imageUrl;
        }
        if (mainImage != null) {
            try {
                return mainImage.getUrl();
            } catch (NoSuchMethodError | NullPointerException ex) {
                // Trả về path nếu URL k có
                return mainImage.getPath();
            }
        }
        if (images != null && !images.isEmpty() && images.get(0) != null) {
            return images.get(0).getUrl();
        }
        return null;
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

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
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

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ProductImage getMainImage() {
        return mainImage;
    }

    public void setMainImage(ProductImage mainImage) {
        this.mainImage = mainImage;
    }

    public Integer getTotalStock() {
        if (variants == null || variants.isEmpty()) return null;
        int sum = 0; boolean any = false;
        for (ProductVariant v : variants) {
            if (v != null && v.getStock() != null) { sum += v.getStock(); any = true; }
        }
        return any ? sum : null;
    }

    public BigDecimal getEffectivePrice() {
        if (salePrice != null) return salePrice;
        return price;
    }
}
