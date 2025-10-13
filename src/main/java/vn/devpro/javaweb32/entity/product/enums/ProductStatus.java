package vn.devpro.javaweb32.entity.product.enums;

public enum ProductStatus {
    ACTIVE("In Order"), // Có sẵn
    DRAFT("Unavailable"),
    FEW_LEFT("Few Left"), // Còn ít
    OUT_OF_STOCK("Out Of Stock"), // Hết hàng
    DISCONTINUED("Discontinued"); // Ngưng bán

    ProductStatus(String displayName) {
    }
}
