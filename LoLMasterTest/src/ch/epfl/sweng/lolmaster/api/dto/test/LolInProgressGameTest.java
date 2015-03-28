package ch.epfl.sweng.lolmaster.api.dto.test;

import java.util.List;

import android.test.InstrumentationTestCase;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionSelection;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolInProgressGame;
import ch.epfl.sweng.lolmaster.api.dto.LolTeam;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.ChampionSelection;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;

import com.google.gson.Gson;

/**
 * Tests for the LolId class
 * 
 * @author fKunstner
 */
public class LolInProgressGameTest extends InstrumentationTestCase {
	private static final String DEFAULT_IN_PROGRESS_GAME_FILE_NAME = "MashapeApi_GetInProgressGame.json";
	private LolInProgressGame mInProgressGame;
	private InProgressGameInfo mGameInfo;

	@Override
	public void setUp() throws Exception {
		String json = JsonAssetsReader.getFileContent(getInstrumentation()
			.getContext(), DEFAULT_IN_PROGRESS_GAME_FILE_NAME);
		mGameInfo = new Gson().fromJson(json, InProgressGameInfo.class);
		mInProgressGame = new LolInProgressGame(mGameInfo);
		super.setUp();
	}

	public void testGetChampionSelectionByPlayer() {
		List<ChampionSelection> refSelections = mGameInfo.getGame()
			.getChampionSelections();
		for (ChampionSelection refSelection : refSelections) {
			String summonerName = refSelection.getSummonerInternalName();

			LolChampionSelection refLolSelection = new LolChampionSelection(
				refSelection);

			LolChampionSelection lolSelection = mInProgressGame
				.getChampionSelectionByPlayer(summonerName);
			assertEquals(refLolSelection, lolSelection);
		}

		assertEquals(null, mInProgressGame.getChampionSelectionByPlayer(null));
		assertEquals(null, mInProgressGame.getChampionSelectionByPlayer(""));
	}

	public void testGetChampionByPlayer() {
		List<ChampionSelection> refSelections = mGameInfo.getGame()
			.getChampionSelections();

		for (ChampionSelection refSelection : refSelections) {
			String summonerName = refSelection.getSummonerInternalName();

			LolId refId = new LolId(refSelection.getChampionId());

			LolId id = mInProgressGame.getChampionByPlayer(summonerName);

			assertEquals(refId, id);
		}

		assertEquals(null, mInProgressGame.getChampionByPlayer(null));
		assertEquals(null, mInProgressGame.getChampionByPlayer(""));
	}

	public void testGetTeam() {
		LolTeam team1 = new LolTeam(mGameInfo.getGame().getTeamOne());
		LolTeam team2 = new LolTeam(mGameInfo.getGame().getTeamTwo());
		assertEquals(team1, mInProgressGame.getTeam1());
		assertEquals(team2, mInProgressGame.getTeam2());
	}

	public void testGetPlayerTeam() {
		String[] team1Players = {"aesenar", "changename", "ntdarkstar",
			"bolqrcheto", "oceethemastermnd" };
		String[] team2Players = {"robzornlft", "fizzonyajayce", "kingsenpai",
			"vulca", "fendris" };

		LolTeam refTeam1 = new LolTeam(mGameInfo.getGame().getTeamOne());
		LolTeam refTeam2 = new LolTeam(mGameInfo.getGame().getTeamTwo());
		for (String p : team1Players) {
			LolTeam allyTeam = mInProgressGame.getTeamOfPlayer(p);
			assertEquals(refTeam1, allyTeam);
			LolTeam enemyTeam = mInProgressGame.getEnemyTeamOfPlayer(p);
			assertEquals(refTeam2, enemyTeam);
		}
		for (String p : team2Players) {
			LolTeam allyTeam = mInProgressGame.getTeamOfPlayer(p);
			assertEquals(refTeam2, allyTeam);
			LolTeam enemyTeam = mInProgressGame.getEnemyTeamOfPlayer(p);
			assertEquals(refTeam1, enemyTeam);
		}

		assertNull(mInProgressGame.getTeamOfPlayer(""));
		assertNull(mInProgressGame.getEnemyTeamOfPlayer(""));
	}
}
