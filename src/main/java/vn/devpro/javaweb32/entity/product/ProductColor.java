package vn.devpro.javaweb32.entity.product;

import vn.devpro.javaweb32.common.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "product_colors",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "color_name"}))
public class ProductColor extends BaseEntity {

    @Column(name = "color_name", length = 100, nullable = false)
    private String colorName;

    @Column(name = "hex_code", length = 7, nullable = true)
    private String hexCode;

    @Column(name = "swatch_path", length = 1024, nullable = true)
    private String swatchPath;

    // Tách biệt với BaseEntity.status, này cho mục đích khác
    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductColor() {}

    public ProductColor(String colorName, String hexCode) {
        this.colorName = colorName;
        this.hexCode = hexCode;
    }

    public Long getId() { return super.getId(); }

    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }

    public String getHexCode() { return hexCode; }
    public void setHexCode(String hexCode) { this.hexCode = hexCode; }

    public String getSwatchPath() { return swatchPath; }
    public void setSwatchPath(String swatchPath) { this.swatchPath = swatchPath; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
