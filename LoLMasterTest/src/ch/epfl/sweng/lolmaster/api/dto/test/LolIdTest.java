package ch.epfl.sweng.lolmaster.api.dto.test;

import android.test.AndroidTestCase;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.dto.LolId;

/**
 * Tests for the LolId class
 * 
 * @author fKunstner
 */
public class LolIdTest extends AndroidTestCase {
    private static final int INT_VALID_ID = 1;
    private static final int INT_ZERO_ID = 0;
    private static final int INT_NEGATIVE_ID = -1;
    private static final int INT_MAX_ID = Integer.MAX_VALUE;
    private static final long LONG_VALID_ID = INT_VALID_ID;
    private static final long LONG_ZERO_ID = INT_ZERO_ID;
    private static final long LONG_NEGATIVE_ID = INT_NEGATIVE_ID;
    private static final long LONG_MAX_ID = INT_MAX_ID;
    private static final String STRING_VALID_ID = Integer
        .toString(INT_VALID_ID);
    private static final String STRING_ZERO_ID = Integer.toString(INT_ZERO_ID);
    private static final String STRING_NEGATIVE_ID = Integer
        .toString(INT_NEGATIVE_ID);
    private static final String STRING_MAX_ID = Integer.toString(INT_MAX_ID);

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateLolId() {
        assertCanCreateLolIdFrom(INT_ZERO_ID);
        assertCanCreateLolIdFrom(INT_VALID_ID);
        assertCanCreateLolIdFrom(INT_MAX_ID);
        assertCanCreateLolIdFrom(LONG_ZERO_ID);
        assertCanCreateLolIdFrom(LONG_VALID_ID);
        assertCanCreateLolIdFrom(LONG_MAX_ID);
        assertCanCreateLolIdFrom(STRING_ZERO_ID);
        assertCanCreateLolIdFrom(STRING_VALID_ID);
        assertCanCreateLolIdFrom(STRING_MAX_ID);
        assertCannotCreateLolIdFrom(INT_NEGATIVE_ID);
        assertCannotCreateLolIdFrom(LONG_NEGATIVE_ID);
        assertCannotCreateLolIdFrom(STRING_NEGATIVE_ID);
    }

    private void assertCanCreateLolIdFrom(long i) {
        try {
            new LolId(i);
        } catch (IllegalArgumentException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            fail("Could not create a LolId with [" + i + "]");
        }
    }

    private void assertCannotCreateLolIdFrom(long i) {
        try {
            new LolId(i);
            fail("Could create a LolId with [" + i
                + "], wich should not be valid.");
        } catch (IllegalArgumentException e) {
            Log.i(this.getClass().getName(),
                "Correctly could not create a LolId from [" + i + "]", e);
        }
    }
    
    private void assertCanCreateLolIdFrom(String i) {
        try {
            new LolId(i);
        } catch (IllegalArgumentException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            fail("Could not create a LolId with [" + i + "]");
        }
    }

    private void assertCannotCreateLolIdFrom(String i) {
        try {
            new LolId(i);
            fail("Could create a LolId with [" + i
                + "], wich should not be valid.");
        } catch (IllegalArgumentException e) {
            Log.i(this.getClass().getName(),
                "Correctly could not create a LolId from [" + i + "]", e);
        }
    }

    public void testValidEquals() {
        LolId a = new LolId(INT_VALID_ID);
        LolId b = new LolId(LONG_VALID_ID);
        LolId c = new LolId(STRING_VALID_ID);

        assertTrue(a + "(int) and " + a + "(int) should be equals.",
            a.equals(a));
        assertTrue(a + "(int) and " + b + "(long) should be equals.",
            a.equals(b));
        assertTrue(a + "(int) and " + c + "(string) should be equals.",
            a.equals(c));
        assertTrue(b + "(long) and " + b + "(long) should be equals.",
            b.equals(b));
        assertTrue(b + "(long) and " + c + "(string) should be equals.",
            b.equals(c));
        assertTrue(c + "(string) and " + c + "(string) should be equals.",
            a.equals(c));
    }

    public void testGetValue() {
        assertTrue("GetValue did not return expected value.", new LolId(
            INT_VALID_ID).getValue() == INT_VALID_ID);
        assertTrue("GetValue did not return expected value.", new LolId(
            STRING_VALID_ID).getValue() == INT_VALID_ID);
        assertTrue("GetValue did not return expected value.", new LolId(
            LONG_VALID_ID).getValue() == INT_VALID_ID);
    }

}
