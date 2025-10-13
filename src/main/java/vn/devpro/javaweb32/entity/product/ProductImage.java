package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.customer.Customer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "product_images",
        indexes = { @Index(name = "idx_pi_product_color", columnList = "product_id,color_id") })
public class ProductImage extends BaseEntity {

    @Column(length = 1024, nullable = false)
    private String path;

    @Column(length = 255, nullable = true)
    private String title;

    @Column(length = 512, nullable = true)
    private String altText;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail = Boolean.FALSE;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    /**
     * NOTE: BaseEntity đã có "status". Đừng thêm
     * DÙng 'visible' cho mục đích nhất định. Chỉ thêm khi baseEntity k còn hiệu lực
     */
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Customer createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private ProductColor color;

    public ProductImage() {}

    public ProductImage(String path, String title, String altText, Boolean isThumbnail, Integer orderIndex, Boolean visible, Customer createdBy, Product product, ProductColor color) {
        this.path = path;
        this.title = title;
        this.altText = altText;
        this.isThumbnail = isThumbnail;
        this.orderIndex = orderIndex;
        this.visible = visible;
        this.createdBy = createdBy;
        this.product = product;
        this.color = color;
    }

    public ProductImage(Long id, Date createDate, Date updateDate, String status, String path, String title, String altText, Boolean isThumbnail, Integer orderIndex, Boolean visible, Customer createdBy, Product product, ProductColor color) {
        super(id, createDate, updateDate, status);
        this.path = path;
        this.title = title;
        this.altText = altText;
        this.isThumbnail = isThumbnail;
        this.orderIndex = orderIndex;
        this.visible = visible;
        this.createdBy = createdBy;
        this.product = product;
        this.color = color;
    }

    public String getUrl() {
        return this.path;
    }

    public Long getId() { return super.getId(); }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAltText() { return altText; }

    public void setAltText(String altText) { this.altText = altText; }

    public Boolean getIsThumbnail() { return isThumbnail; }

    public void setIsThumbnail(Boolean isThumbnail) { this.isThumbnail = isThumbnail; }

    public Integer getOrderIndex() { return orderIndex; }

    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

    public Boolean getVisible() { return visible; }

    public void setVisible(Boolean visible) { this.visible = visible; }

    public Customer getCreatedBy() { return createdBy; }

    public void setCreatedBy(Customer createdBy) { this.createdBy = createdBy; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public ProductColor getColor() { return color; }

    public void setColor(ProductColor color) { this.color = color; }
}
