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

    @Override
    public String toString() {
        return displayName;
    }
}
