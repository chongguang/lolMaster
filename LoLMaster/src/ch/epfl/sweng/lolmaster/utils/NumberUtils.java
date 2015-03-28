package ch.epfl.sweng.lolmaster.utils;

/**
 * Utility class for common Numbers handling functions.
 * 
 * @author fKunstner
 */
public final class NumberUtils {
    private static final int ROUNDING_BASE = 10;

    private NumberUtils() {

    }

    /**
     * Truncate a number to the specified decimal.
     * 
     * roundTo(10.111, 2) == 10.11
     * 
     * For edge cases, {@see Math#floor(double)}
     * 
     * @param d
     *            The number to round
     * @param decimal
     *            The number of decimal to keep
     * @return
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     */
    public static double truncateTo(double doubleToRound, int decimal) {
        if (decimal < 0) {
            throw new IllegalArgumentException(
                "Decimal needs to be >= 0. Provided: [" + decimal + "]");
        }

        double factor = Math.pow(ROUNDING_BASE, decimal);
        double stat = Math.floor(doubleToRound * factor) / factor;
        return stat;
    }
}
