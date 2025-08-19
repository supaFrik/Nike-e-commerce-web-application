package vn.devpro.javaweb32.model.customer;

public enum Role {
    CUSTOMER("Customer"),
    ADMIN("Administrator"),
    MODERATOR("Moderator");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
