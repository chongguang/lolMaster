package ch.epfl.sweng.lolmaster.utils;

import junit.framework.TestCase;
import android.util.Log;

/**
 * Tests for the LolBannedChampion class
 * 
 * @author fKunstner
 */
public class NumberUtilsTest extends TestCase {
    private static final double NUMBER_TO_TRUNCATE = 1.1111;
    private static final double TRUNCATED_NO_DECIMAL = 1;
    private static final double TRUNCATED_ONE_DECIMAL = 1.1;
    private static final int NO_DECIMAL = 0;
    private static final int INVALID_DECIMAL = -1;
    private static final int ONE_DECIMAL = 1;

    public void testRoundToNoDecimal() {
        double result = NumberUtils.truncateTo(NUMBER_TO_TRUNCATE, NO_DECIMAL);
        double expectedResult = TRUNCATED_NO_DECIMAL;
        assertEquals(result, expectedResult);
    }

    public void testRoundToOneDecimal() {
        double result = NumberUtils.truncateTo(NUMBER_TO_TRUNCATE, ONE_DECIMAL);
        double expectedResult = TRUNCATED_ONE_DECIMAL;
        assertEquals(result, expectedResult);
    }

    public void testTruncateToInvalidDecimal() {
        try {
            NumberUtils.truncateTo(NUMBER_TO_TRUNCATE, INVALID_DECIMAL);
            fail("Should not be able to truncate to negative decimals.");
        } catch (IllegalArgumentException e) {
            // Everything is fine.
            Log.i(this.getClass().getName(),
                "truncateTo correctly failed on invalid decimal.", e);
        }
    }
}
