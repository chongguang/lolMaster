package ch.epfl.sweng.lolmaster.api.dto.test;

import android.test.InstrumentationTestCase;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;

import com.google.gson.Gson;

import dto.Stats.PlayerStatsSummaryList;

/**
 * Tests for the LolStats class
 * 
 * @author fKunstner
 */
public class LolStatsTest extends InstrumentationTestCase {
	private static final String DEFAULT_RANKED_STATS_FILE_NAME = "Riot_StatsSummary_Ranked.json";
	private static final String DEFAULT_UNRANKED_STATS_FILE_NAME = "Riot_StatsSummary_Unranked.json";
    private static final int EXPECTED_NORMAL_PLAYER_UNRANKED_WINS = 48;
	private static final int EXPECTED_NORMAL_PLAYER_RANKED_WINS = 0;
    private static final int EXPECTED_RANKED_PLAYER_UNRANKED_WINS = 210;
    private static final int EXPECTED_RANKED_PLAYER_RANKED_WINS = 13;
	private LolStats mLolRankedStats;
	private LolStats mLolUnrankedStats;
	private PlayerStatsSummaryList mRankedStats;
	private PlayerStatsSummaryList mUnrankedStats;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		String json = JsonAssetsReader.getFileContent(getInstrumentation().getContext(),
			DEFAULT_RANKED_STATS_FILE_NAME);
		mRankedStats = new Gson().fromJson(json, PlayerStatsSummaryList.class);

		json = JsonAssetsReader.getFileContent(getInstrumentation().getContext(),
			DEFAULT_UNRANKED_STATS_FILE_NAME);
		mUnrankedStats = new Gson()
			.fromJson(json, PlayerStatsSummaryList.class);

		mLolRankedStats = new LolStats(mRankedStats);
		mLolUnrankedStats = new LolStats(mUnrankedStats);
	}

	public void testGetNumberOfRankedWins() {
		assertEquals(EXPECTED_NORMAL_PLAYER_RANKED_WINS, mLolUnrankedStats.getNumberOfRankedWins());
		assertEquals(EXPECTED_RANKED_PLAYER_RANKED_WINS, mLolRankedStats.getNumberOfRankedWins());
	}

	public void testGetNumberOfNormalWins() {
		assertEquals(EXPECTED_NORMAL_PLAYER_UNRANKED_WINS, mLolUnrankedStats.getNumberOfNormalWins());
		assertEquals(EXPECTED_RANKED_PLAYER_UNRANKED_WINS, mLolRankedStats.getNumberOfNormalWins());
	}
}
