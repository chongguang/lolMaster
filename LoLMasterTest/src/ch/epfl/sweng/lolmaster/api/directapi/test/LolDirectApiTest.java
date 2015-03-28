package ch.epfl.sweng.lolmaster.api.directapi.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.ApiKeys;
import ch.epfl.sweng.lolmaster.api.directapi.LolDirectApi;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApi;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;
import constant.Region;
import dto.Champion.Champion;
import dto.Champion.ChampionList;

/**
 * Tests for the LoldirectApi class
 * 
 * @author fKunstner
 * 
 */
public class LolDirectApiTest extends MockitoTestCase {

	private static final int NUM_PLAYERS = 10;
	private RiotApi mRiotApi;
	private MashapeApi mMashapeApi;
	private LolDirectApi mLolApi;
	private ApiKeys mApiKeys;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mRiotApi = new RiotApi("key1", Region.GLOBAL);
		mMashapeApi = new MashapeApi("Key1", Region.GLOBAL);
		mApiKeys = mock(ApiKeys.class);
		when(mApiKeys.getMashapeKey()).thenReturn("mashapeKey1");
		when(mApiKeys.getRiotKey()).thenReturn("riotKey1");
	}

	public void testSetRegion() {

		LolDirectApi lolApi = new LolDirectApi(mRiotApi, mMashapeApi, mApiKeys);
		for (Region r : Region.values()) {
			lolApi.setRegion(r);
			assertTrue(lolApi.getRegion().equals(r.getName()));
		}

		try {
			lolApi.setRegion(null);
			fail("Should reject null input.");
		} catch (IllegalArgumentException e) {

		}
	}

	public void testValidGetFreeToPlayChampions() throws ApiException,
		RiotApiException {

		// Generate mocked riotApi response
		List<Champion> champions = new ArrayList<Champion>();

		for (int i = 0; i < NUM_PLAYERS; i++) {
			Champion c = mock(Champion.class);
			when(c.getId()).thenReturn((long) i);
			champions.add(c);
		}

		ChampionList championList = mock(ChampionList.class);
		when(championList.getChampions()).thenReturn(champions);

		mMashapeApi = mock(MashapeApi.class);
		when(mMashapeApi.getKey()).thenReturn("");
		when(mMashapeApi.getRegion()).thenReturn("");
		when(mMashapeApi.duplicate()).thenReturn(mMashapeApi);

		mRiotApi = mock(RiotApi.class);
		when(mRiotApi.getFreeToPlayChampions()).thenReturn(championList);
		when(mRiotApi.clone()).thenReturn(mRiotApi);

		mApiKeys = mock(ApiKeys.class);
		when(mApiKeys.getRiotKey()).thenReturn("");

		mLolApi = new LolDirectApi(mRiotApi, mMashapeApi, mApiKeys);

		// Test lolApi response
		List<LolId> ids = mLolApi.getFreeToPlayChampions();
		for (int i = 0; i < NUM_PLAYERS; i++) {
			assertTrue("Champion " + i + " should be in the list.",
				ids.contains(new LolId(i)));
		}

	}
}
