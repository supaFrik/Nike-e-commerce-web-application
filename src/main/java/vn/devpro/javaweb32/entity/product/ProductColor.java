package vn.devpro.javaweb32.entity.product;

import javax.persistence.*;

@Entity
@Table(name = "colors")
public class ProductColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String hex;

    @Column(length = 30, nullable = false)
    private String imageUrl;

    public ProductColor() {
    }

    public ProductColor(Long id, String name, String hex, String imageUrl) {
        this.id = id;
        this.name = name;
        this.hex = hex;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
