package vn.demo.nike.features.catalog.product.domain.enums;

public enum ProductStatus {
    ACTIVE("In Order"),
    DRAFT("Unavailable"),
    FEW_LEFT("Few Left"),
    OUT_OF_STOCK("Out Of Stock"),
    DISCONTINUED("Discontinued");

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProductStatus fromDisplayName(String displayName) {
        if (displayName == null) return ACTIVE;
        for (ProductStatus status : ProductStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }
        return ACTIVE;
    }
}
