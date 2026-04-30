package vn.demo.nike.features.catalog.product.domain.enums;

public enum  InventoryStatus {
    IN_ORDER("In stock"),
    FEW_LEFT("Few left"),
    OUT_OF_STOCK("Out of stock"),
    DISCONTINUED("Discontinued");

    private final String displayName;

    InventoryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
