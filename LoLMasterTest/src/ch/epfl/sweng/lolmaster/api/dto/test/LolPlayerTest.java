package ch.epfl.sweng.lolmaster.api.dto.test;

import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolPlayer;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.Player;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Tests for the LolPlayer class
 * 
 * @author fKunstner
 */
public class LolPlayerTest extends MockitoTestCase {
	private static final String DEFAULT_PLAYERS_FILE = "mashape_default_players.json";
	private List<Player> mDefaultPlayers;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		String json = JsonAssetsReader.getFileContent(getInstrumentation()
			.getContext(), DEFAULT_PLAYERS_FILE);

		mDefaultPlayers = new Gson().fromJson(json,
			new TypeToken<List<Player>>() {
			}.getType());
	}

	public void testGetValues() {
		for (Player p : mDefaultPlayers) {
			assertEquals(p.getSummonerInternalName(),
				new LolPlayer(p).getSummonerInternalName());
			assertEquals(p.getSummonerName(),
				new LolPlayer(p).getSummonerName());
			assertEquals(new LolId(p.getSummonerId()),
				new LolPlayer(p).getSummonerId());
		}
	}

	public void testHashCode() throws IOException {
		for (Player p : mDefaultPlayers) {
			assertEquals(new LolPlayer(p), new LolPlayer(p));
			assertEquals(new LolPlayer(p).hashCode(),
				new LolPlayer(p).hashCode());
		}
	}
}
