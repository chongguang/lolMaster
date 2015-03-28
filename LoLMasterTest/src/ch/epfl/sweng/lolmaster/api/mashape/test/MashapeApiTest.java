package ch.epfl.sweng.lolmaster.api.mashape.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;

import android.util.Log;
import ch.epfl.sweng.lolmaster.api.ApiKeys;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;
import ch.epfl.sweng.lolmaster.api.dto.LolTeam;
import ch.epfl.sweng.lolmaster.api.mashape.ConnectionUtils;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApi;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApiException;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;
import constant.Region;

/**
 * Tests for the MashapeApi class
 * 
 * @author fKunstner
 */
public class MashapeApiTest extends MockitoTestCase {

	private MashapeApi mApi;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mApi = new MashapeApi("Key1", Region.EUW);
	}

	// This test takes 3minutes under certain conditions
	public void ignoreTestCompleteMashapeCall() {
		ApiKeys keys = ApiKeysManager.getApiKeys();
		mApi = new MashapeApi(keys.getMashapeKey(), Region.NA);

		String summonerName;
		try {
			summonerName = getAnInGameSummonerName();
			if (summonerName == null) {
				fail("Could not find an in game summoner to test.");
			}
		} catch (ParseException e) {
			Log.w(
				this.getClass().getName(),
				"Could not test complete mashape call. Failed to get an in game summoner to test."
					+ e.getMessage());
			return;
		} catch (IOException e) {
			Log.w(
				this.getClass().getName(),
				"Could not test complete mashape call. Failed to get an in game summoner to test."
					+ e.getMessage());
			return;
		}

		InProgressGameInfo gameInfo;
		try {
			gameInfo = mApi.getInProgessGameInfo(summonerName);
		} catch (MashapeApiException e) {
			Log.w(this.getClass().getName(),
				"Could not retrieve information about current game for player "
					+ summonerName + ". " + e.getMessage());
			return;
		}

		if (gameInfo == null || gameInfo.getGame() == null) {
			Log.w(this.getClass().getName(), "No game found for player "
				+ summonerName + ". ");
		}

		LolTeam team1 = new LolTeam(gameInfo.getGame().getTeamOne());
		LolTeam team2 = new LolTeam(gameInfo.getGame().getTeamTwo());
		assertTrue("Fetched game should contain summoner " + summonerName,
			team1.hasPlayer(summonerName) || team2.hasPlayer(summonerName));
	}

	private String getAnInGameSummonerName() throws IOException, ParseException {
		String featuredGamesUrl = "http://spectator.na.lol.riotgames.com:8088/observer-mode/rest/featured";
		HttpURLConnection connection = ConnectionUtils.openConnection(
			featuredGamesUrl, new HashMap<String, String>());
		String jsonContent = ConnectionUtils.getContent(connection);

		Pattern pattern = Pattern.compile("\"summonerName\":\"(..*?)\"");
		Matcher matcher = pattern.matcher(jsonContent);
		if (matcher.find()) {
			return matcher.group(1);
		}

		throw new ParseException("Did not found a summoner name");
	}

	public void setKey() {
		mApi.setKey("KEY_1");
		assertEquals("KEY_1", mApi.getKey());
	}

	public void testSetRegion() {
		for (Region r : Region.values()) {
			mApi.setRegion(r);
			assertEquals(r.getName(), mApi.getRegion());
		}
	}

	public void testNullinput() {
		try {
			mApi.setKey(null);
			mApi.setRegion(null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
	}

	public void testNullRequest() throws MashapeApiException {
		assertNull(mApi.getInProgessGameInfo(""));
		assertNull(mApi.getInProgessGameInfo(null));
	}
}
