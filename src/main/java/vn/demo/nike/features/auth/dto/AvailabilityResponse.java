package vn.demo.nike.features.auth.dto;

public record AvailabilityResponse (
        boolean usernameAvailable,
        boolean emailAvailable
) {
}
