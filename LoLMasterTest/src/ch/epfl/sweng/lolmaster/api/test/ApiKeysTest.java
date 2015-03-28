package ch.epfl.sweng.lolmaster.api.test;

import android.test.AndroidTestCase;
import ch.epfl.sweng.lolmaster.api.ApiKeys;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;

/**
 * @author lcg31439
 * 
 */
public class ApiKeysTest extends AndroidTestCase {

	public void testKeyAccess() {
		ApiKeys keys = ApiKeysManager.getApiKeys();
		assertNotNull("Mashape key should not be null", keys.getMashapeKey());
		assertNotNull("Riot key should not be null", keys.getRiotKey());
	}
	
	// Write more tests
}
