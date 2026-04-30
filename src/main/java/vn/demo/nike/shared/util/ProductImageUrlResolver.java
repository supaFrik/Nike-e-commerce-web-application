package vn.demo.nike.shared.util;

public final class ProductImageUrlResolver {

    private static final String PUBLIC_UPLOAD_PREFIX = "/uploads/products/";

    private ProductImageUrlResolver() {
    }

    public static String toPublicUrl(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return null;
        }

        String normalized = storedPath.trim().replace('\\', '/');

        if (normalized.startsWith(PUBLIC_UPLOAD_PREFIX)) {
            return normalized;
        }

        if (normalized.startsWith("uploads/products/")) {
            return "/" + normalized;
        }

        if (normalized.startsWith("/")) {
            return normalized;
        }

        return PUBLIC_UPLOAD_PREFIX + normalized;
    }
}
