package ch.epfl.sweng.lolmaster.api.dto.test;

import android.test.InstrumentationTestCase;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;

import com.google.gson.Gson;

import dto.MatchHistory.MatchSummary;

/**
 * Tests for the LolId class
 * 
 * @author fKunstner
 */
public class LolMatchSummaryTest extends InstrumentationTestCase {
	private static final String DEFAULT_MATCH_SUMMARY_FILE_NAME = "Riot_MatchSummary.json";
	private MatchSummary mMatchSummary;
	private LolMatchSummary mLolMatchSummary;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		String json = JsonAssetsReader.getFileContent(getInstrumentation()
			.getContext(), DEFAULT_MATCH_SUMMARY_FILE_NAME);
		mMatchSummary = new Gson().fromJson(json, MatchSummary.class);
		mLolMatchSummary = new LolMatchSummary(mMatchSummary);
	}

	public void testGetters() {
		assertEquals(mMatchSummary.getMatchCreation(),
			mLolMatchSummary.getMatchCreation());
		assertEquals(mMatchSummary.getMatchDuration(),
			mLolMatchSummary.getMatchDuration());
		assertEquals(mMatchSummary.getMatchId(), mLolMatchSummary.getMatchId());
		assertEquals(mMatchSummary.getMatchMode(),
			mLolMatchSummary.getMatchMode());
		assertEquals(mMatchSummary.getMatchType(),
			mLolMatchSummary.getMatchType());
		assertEquals(mMatchSummary.getParticipantIdentities(),
			mLolMatchSummary.getParticipantIdentities());
		// getParticipants now return a List<LolParticipant>
		// assertEquals(mMatchSummary.getParticipants(),
		// mLolMatchSummary.getParticipants());
		assertEquals(mMatchSummary.getRegion(), mLolMatchSummary.getRegion());
		assertEquals(mMatchSummary.getSeason(), mLolMatchSummary.getSeason());
	}
}
