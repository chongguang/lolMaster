package ch.epfl.sweng.lolmaster.api.mashape.test;

import java.io.IOException;

import android.test.InstrumentationTestCase;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApiException;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApiException.MashapeError;

/**
 * Tests the mashape api exception.
 * 
 * @author fKunstner
 */
public class MashapeApiExceptionTest extends InstrumentationTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateMashapeApiException() {
        for (MashapeError error : MashapeError.values()) {
            IOException ioEx = new IOException(error.getErrorMessage());
            MashapeApiException ex = new MashapeApiException(error, ioEx);
            assertEquals(error.getErrorMessage(), ex.getMessage());
            assertEquals(error, ex.getError());
            assertEquals(ex.getCause(), ioEx);
        }

        for (MashapeError error : MashapeError.values()) {
            MashapeApiException ex = new MashapeApiException(error);
            assertEquals(error.getErrorMessage(), ex.getMessage());
            assertEquals(error, ex.getError());
        }
    }

    public void testMashapeError() {
        for (MashapeError error : MashapeError.values()) {
            assertEquals(error, MashapeError.getMashapeError(error.getCode()));
        }

        assertEquals(MashapeError.OK, MashapeError.valueOf("OK"));

        assertEquals(MashapeError.UNKNOWN_ERROR,
            MashapeError.getMashapeError(-1));
    }

    public void testMashapeErrorToStringIsOverriden()
        throws NoSuchMethodException {
        assertFalse(
            "toString should be overriden.",
            Object.class.getMethod("toString").getDeclaringClass()
                .equals("Object"));
        for (MashapeError e : MashapeError.values()) {
            assertFalse(e.toString().isEmpty());
        }
    }

}
