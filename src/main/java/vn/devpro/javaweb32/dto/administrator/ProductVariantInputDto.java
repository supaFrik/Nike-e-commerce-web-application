package vn.devpro.javaweb32.dto.administrator;

import java.math.BigDecimal;

public class ProductVariantInputDto {
    private String size;
    private BigDecimal price;
    private Integer stock;
    private Integer colorIndex; // trỏ đến index của màu tương ứng trong danh sách colors

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(Integer colorIndex) {
        this.colorIndex = colorIndex;
    }
}
