package vn.demo.nike.features.catalog.product.enums;

public enum ProductStatus {
    ACTIVE("Just in"),
    DRAFT("Coming soon"),
    FEW_LEFT("Few left"),
    OUT_OF_STOCK("Sold out"),
    DISCONTINUED("No longer available");

    private final String displayName;

    ProductStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProductStatus fromDisplayName(String displayName) {
        if (displayName == null) {
            return ACTIVE;
        }

        String normalized = normalize(displayName);
        for (ProductStatus status : ProductStatus.values()) {
            if (normalize(status.displayName).equals(normalized) || status.name().equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }

        return switch (normalized) {
            case "inorder", "instock", "availablenow", "available" -> ACTIVE;
            case "unavailable", "draft", "comingsoon" -> DRAFT;
            case "fewleft", "lowstock" -> FEW_LEFT;
            case "outofstock", "soldout" -> OUT_OF_STOCK;
            case "discontinued", "nolongeravailable" -> DISCONTINUED;
            default -> ACTIVE;
        };
    }

    @Override
    public String toString() {
        return displayName;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
}
