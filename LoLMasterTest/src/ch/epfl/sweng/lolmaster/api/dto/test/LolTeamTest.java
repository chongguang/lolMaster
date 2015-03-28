package ch.epfl.sweng.lolmaster.api.dto.test;

import java.util.ArrayList;
import java.util.List;

import android.test.InstrumentationTestCase;
import ch.epfl.sweng.lolmaster.api.dto.LolPlayer;
import ch.epfl.sweng.lolmaster.api.dto.LolTeam;
import ch.epfl.sweng.lolmaster.api.dto.LolTeam.TeamId;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.Player;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Tests for the LolTeam class
 * 
 * @author fKunstner
 */
public class LolTeamTest extends InstrumentationTestCase {
	private static final String DEFAULT_TEAM_JSON = "MashapeTeam.json";
	private static final String[] DEFAULT_PLAYERS = {"Tew\u00f2", "TBS Amades",
		"\u00c8at Sleep Gam\u00c9", "Gstv", "wejlen" };
	private String json;
	private List<Player> mPlayers;
	private LolTeam mLolTeam;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		json = JsonAssetsReader.getFileContent(getInstrumentation()
			.getContext(), DEFAULT_TEAM_JSON);
		mPlayers = new Gson().fromJson(json, new TypeToken<List<Player>>() {
		}.getType());
		mLolTeam = new LolTeam(mPlayers);
	}

	public void testContainsValidPlayer() {
		mLolTeam.getPlayers();
		for (int i = 0; i < DEFAULT_PLAYERS.length; i++) {
			assertTrue("LolTeam should contain player " + DEFAULT_PLAYERS[i],
				mLolTeam.hasPlayer(DEFAULT_PLAYERS[i]));
		}
		assertFalse(mLolTeam.hasPlayer(null));
		assertFalse(mLolTeam.hasPlayer(""));
	}

	public void testEquals() {
		List<LolPlayer> playerList = new ArrayList<LolPlayer>();
		for (Player p : mPlayers) {
			playerList.add(new LolPlayer(p));
		}
		assertTrue(playerList.equals(mLolTeam.getPlayers()));
		assertFalse(mLolTeam.getPlayers().equals(mPlayers));
	}

	public void testLolId() {
		for (TeamId id : TeamId.values()) {
			assertEquals(TeamId.getTeamId(id.getId()), id);
		}
		assertNull(TeamId.getTeamId(0));
	}
}
