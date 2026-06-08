package vn.demo.nike.features.identity.auth.dto;

public record AvailabilityResponse (
        boolean usernameAvailable,
        boolean emailAvailable
) {
}
