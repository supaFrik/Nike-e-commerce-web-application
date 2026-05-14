package vn.demo.shared.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class CurrencyUtils {

    private static final char GROUPING_SEPARATOR = ',';
    private static final String CURRENCY_SYMBOL = "₫";

    private CurrencyUtils() {
        // utility
    }

    /**
     * Format a numeric value as Vietnamese Dong using comma as thousands separator and appending ₫ with no space.
     * Examples: 7999000 -> "7,999,000₫"
     */
    public static String format(Object value) {
        if (value == null) {
            return "0" + CURRENCY_SYMBOL;
        }

        BigDecimal number;
        if (value instanceof BigDecimal) {
            number = (BigDecimal) value;
        } else if (value instanceof Number) {
            number = new BigDecimal(((Number) value).longValue());
        } else {
            try {
                number = new BigDecimal(value.toString());
            } catch (Exception ex) {
                return String.valueOf(value) + CURRENCY_SYMBOL;
            }
        }

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);
        // decimal separator won't be used because pattern has no fraction part
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        df.setGroupingUsed(true);
        df.setMaximumFractionDigits(0);
        df.setMinimumFractionDigits(0);

        String formatted = df.format(number);
        return formatted + CURRENCY_SYMBOL;
    }
}


