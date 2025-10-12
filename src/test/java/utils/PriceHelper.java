package utils;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class PriceHelper {
    /**
     * Compares two BigDecimal values safely with rounding.
     * Returns true if both are equal up to 2 decimal places.
     */
    public static boolean areEqual(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return false; // Null safety check
        }
        return a.setScale(2, RoundingMode.HALF_UP)
                .equals(b.setScale(2, RoundingMode.HALF_UP));
    }

}
