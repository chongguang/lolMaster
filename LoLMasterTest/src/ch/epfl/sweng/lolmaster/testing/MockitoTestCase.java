package ch.epfl.sweng.lolmaster.testing;

import android.test.InstrumentationTestCase;

/**
 * Base class for InstrumentationTestCase that needs Mockito.
 * 
 * Dirty hack, but hey, it was on the midterm test suite.
 * 
 * @author fKunstner
 */
public class MockitoTestCase extends InstrumentationTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
			.getTargetContext().getCacheDir().getPath());
	}

}
