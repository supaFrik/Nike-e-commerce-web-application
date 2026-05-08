package vn.demo.nike.shared.util;

public final class StringUtil {

    private StringUtil() {}

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase();
    }
}

