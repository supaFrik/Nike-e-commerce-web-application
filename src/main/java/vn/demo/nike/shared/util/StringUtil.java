package vn.demo.nike.shared.util;

public final class StringUtil {

    private StringUtil() {}

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }

    public static String trimToNull(String value) {
        if (value == null) return null;
        String t = value.trim();
        return t.isEmpty() ? null : t;
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
