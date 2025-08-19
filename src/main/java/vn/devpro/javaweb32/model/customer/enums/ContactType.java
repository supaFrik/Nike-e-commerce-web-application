package vn.devpro.javaweb32.model.customer.enums;

public enum ContactType {
    PRIMARY("Primary"),
    SECONDARY("Secondary"),
    WORK("Work"),
    PERSONAL("Personal");

    private final String displayContactType;

    ContactType(String displayContactType) {
        this.displayContactType = displayContactType;
    }

    @Override
    public String toString() {
        return displayContactType;
    }
}
