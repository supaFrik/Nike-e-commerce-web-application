package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;
import vn.devpro.javaweb32.entity.product.enums.InventoryStatus;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product_variants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "color_id", "size_label"}))
public class ProductVariant extends BaseEntity {

    @Column(length = 100)
    private String sku;

    @Column(name = "size_label", length = 50, nullable = false)
    private String sizeLabel; // e.g. "EU 42.5"

    @Column(name = "size_value", length = 50)
    private String sizeValue;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_status", length = 20)
    private InventoryStatus inventoryStatus = InventoryStatus.IN_ORDER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    private ProductColor color;

    public ProductVariant() {}

    public ProductVariant(String sizeLabel, BigDecimal price, Integer stock) {
        this.sizeLabel = sizeLabel;
        this.price = price == null ? BigDecimal.ZERO : price;
        this.stock = stock == null ? 0 : stock;
        updateInventoryStatus();
    }

    public void updateInventoryStatus() {
        if (Boolean.FALSE.equals(this.active)) {
            this.inventoryStatus = InventoryStatus.DISCONTINUED;
            return;
        }
        if (this.stock == null || this.stock <= 0) {
            this.inventoryStatus = InventoryStatus.OUT_OF_STOCK;
        } else if (this.stock <= 5) { // Chinh theo ý
            this.inventoryStatus = InventoryStatus.FEW_LEFT;
        } else {
            this.inventoryStatus = InventoryStatus.IN_ORDER;
        }
    }

    public Long getId() { return super.getId(); }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getSizeLabel() { return sizeLabel; }
    public void setSizeLabel(String sizeLabel) { this.sizeLabel = sizeLabel; }

    public String getSizeValue() { return sizeValue; }
    public void setSizeValue(String sizeValue) { this.sizeValue = sizeValue; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) {
        this.stock = stock;
        updateInventoryStatus();
    }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) {
        this.active = active;
        updateInventoryStatus();
    }

    public InventoryStatus getInventoryStatus() { return inventoryStatus; }
    public void setInventoryStatus(InventoryStatus inventoryStatus) { this.inventoryStatus = inventoryStatus; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public ProductColor getColor() { return color; }
    public void setColor(ProductColor color) { this.color = color; }
}
