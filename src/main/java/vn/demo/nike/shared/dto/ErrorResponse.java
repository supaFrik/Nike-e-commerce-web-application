package vn.demo.nike.shared.dto;

public record ErrorResponse(int status,
                            String message,
                            Long timestamp) {
}
