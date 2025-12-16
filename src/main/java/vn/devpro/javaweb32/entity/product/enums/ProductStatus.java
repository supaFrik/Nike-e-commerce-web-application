package vn.devpro.javaweb32.entity.product.enums;

public enum ProductStatus {
    ACTIVE("In Order"), // Có sẵn
    DRAFT("Unavailable"),
    FEW_LEFT("Few Left"), // Còn ít
    OUT_OF_STOCK("Out Of Stock"), // Hết hàng
    DISCONTINUED("Discontinued"); // Ngưng bán

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Convert display name to enum (e.g., "In Order" → ACTIVE)
     */
    public static ProductStatus fromDisplayName(String displayName) {
        if (displayName == null) return ACTIVE;
        for (ProductStatus status : ProductStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }
        return ACTIVE; // default fallback
    }
}
