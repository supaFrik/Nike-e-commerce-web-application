package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;
import javax.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private String status;

    /**
     * Quan hệ nhiều ảnh → 1 sản phẩm
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Quan hệ nhiều ảnh → 1 màu sắc
     */
    @ManyToOne
    @JoinColumn(name = "product_color_id")
    private ProductColor color;

    // ========== GETTER/SETTER ==========

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }
}
