package vn.devpro.javaweb32.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "product_colors")
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    //Black, Grey, Orange,...
    private String colorName;

    @Column(length = 30, nullable = false)
    // Nike Air Max Dn8/Black
    private String folderPath;

    @Column(length = 30, nullable = false)
    // AIR+MAX+DN8
    private String baseImage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public ProductColor() {
    }

    public ProductColor(Long id, String colorName, String folderPath, String baseImage, Product product) {
        this.id = id;
        this.colorName = colorName;
        this.folderPath = folderPath;
        this.baseImage = baseImage;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String name) {
        this.colorName = colorName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getBaseImage() {
        return baseImage;
    }

    public void setBaseImage(String baseImage) {
        this.baseImage = baseImage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return this.colorName;
    }
}
