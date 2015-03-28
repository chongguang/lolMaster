/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.currentgamefeature;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryPage;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryTree;
import ch.epfl.sweng.lolmaster.api.dto.LolPlayer;
import ch.epfl.sweng.lolmaster.api.dto.LolRune;
import ch.epfl.sweng.lolmaster.api.dto.LolRunePage;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import constant.Region;
import constant.staticdata.MasteryListData;
import dto.League.League;
import dto.League.LeagueEntry;
import dto.Static.MasteryTree;
import dto.Static.Rune;
import dto.Summoner.Mastery;
import dto.Summoner.MasteryPage;

/**
 * @author lifei
 * 
 */
public class InGamePlayerTest extends TestCase {

	private InGamePlayer mInGamePlayer;
	private LolId mlolId;
	
	private static final int ITER_NB1 = 30;
	private static final int RUNE_VALUE = 120;
	private static final int ITER_NB2 = 9;
	private static final int ITER_NB3 = 3;
	private static final int MASTERY1 = 4311;
	private static final int MASTERY2 = 4162;
	private static final int MASTERY_RANK1 = 21;
	private static final int MASTERY_RANK2 = 9;

	protected void setUp() throws Exception {
		super.setUp();
		LolPlayer mplayer = mock(LolPlayer.class);
		mlolId = new LolId(1);
		when(mplayer.getSummonerName()).thenReturn("mockSummonerName");
		when(mplayer.getSummonerId()).thenReturn(mlolId);
		when(mplayer.getSummonerInternalName()).thenReturn("mockSummonerInternalName");

		mInGamePlayer = new InGamePlayer(mplayer);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	// test we can get correct name, full name, internal name and id
	public void testGet() {

		assertEquals("fail to get correct Name from InGamePlayer.class",
			"mockSummoner...", mInGamePlayer.getName());
		assertEquals("fail to get correct full Name from InGamePlayer.class",
			"mockSummonerName", mInGamePlayer.getFullName());
		assertEquals(
			"fail to get correct getInternalName from InGamePlayer.class",
			"mockSummonerInternalName", mInGamePlayer.getInternalName());
		assertEquals("fail to get correct mlolId from InGamePlayer.class",
			mlolId.toString(), mInGamePlayer.getId().toString());
	}

	// test we can set and get correct champion data
	public void testSetChampion() {
		// set mock
		LolChampionData mockLolChampionData = mock(LolChampionData.class);
		when(mockLolChampionData.getName()).thenReturn("yasuo");
		mInGamePlayer.setChampion(mockLolChampionData);
		// test
		assertEquals(
			"fail to set or get correct champion from InGamePlayer.class",
			"yasuo", mInGamePlayer.getChampion().getName());
	}

	public void testSetStats() {
		LolStats mockLolStats = mock(LolStats.class);
		mInGamePlayer.setPlayerStats(mockLolStats);

		assertNotNull("failed to set Player stats from InGamePlayer",
			mInGamePlayer.getPlayerStats());
	}

	public void testSetNormalWin() {
		// set mock
		LolStats mockLolStats = mock(LolStats.class);
		when(mockLolStats.getNumberOfNormalWins()).thenReturn(1);
		mInGamePlayer.setPlayerStats(mockLolStats);
		// test
		assertEquals(
			"fail to set or get correct numberOfNormalWins from InGamePlayer.class",
			1, mInGamePlayer.getPlayerStats().getNumberOfNormalWins());
	}

	public void testSetRankedWin() {
		LolStats mockLolStats = mock(LolStats.class);
		when(mockLolStats.getNumberOfRankedWins()).thenReturn(1);
		mInGamePlayer.setPlayerStats(mockLolStats);

		assertEquals(
			"fail to set or get ccorret numberOfRankedWins from InGamePlayer.class",
			1, mInGamePlayer.getPlayerStats().getNumberOfRankedWins());
	}

	// test we can set and get correct LolSummonerSpell
	public void testSetSpell() {
		// set mock
		mlolId = new LolId(1);
		LolSummonerSpell mockLolSummonerSpell = mock(LolSummonerSpell.class);
		when(mockLolSummonerSpell.getName()).thenReturn("mockLolSummonerSpell");
		when(mockLolSummonerSpell.getSpellId()).thenReturn(mlolId);
		mInGamePlayer.setSpell1(mockLolSummonerSpell);
		// test testSetSpell1
		assertEquals(
			"fail to set or get correct name of LolSummonerSpell from InGamePlayer.class",
			"mockLolSummonerSpell", mInGamePlayer.getSpell1().getName());
		assertEquals(
			"fail to set or get correct id of LolSummonerSpell from InGamePlayer.class",
			mlolId, mInGamePlayer.getSpell1().getSpellId());

		// set mock
		mlolId = new LolId(2);
		LolSummonerSpell mockLolSummonerSpell2 = mock(LolSummonerSpell.class);
		when(mockLolSummonerSpell2.getName()).thenReturn(
			"mockLolSummonerSpell2");
		when(mockLolSummonerSpell2.getSpellId()).thenReturn(mlolId);
		mInGamePlayer.setSpell2(mockLolSummonerSpell2);
		// test testSetSpell2
		assertEquals(
			"fail to set or get correct name of LolSummonerSpell from InGamePlayer.class",
			"mockLolSummonerSpell2", mInGamePlayer.getSpell2().getName());
		assertEquals(
			"fail to set or get correct id of LolSummonerSpell from InGamePlayer.class",
			mlolId, mInGamePlayer.getSpell2().getSpellId());
	}

	public void testSetLeagues() {
		List<League> leagues = new ArrayList<League>();

		League mockSoloRanked = mock(League.class);
		when(mockSoloRanked.getQueue()).thenReturn("RANKED_SOLO_5x5");

		League mockTeam5Ranked = mock(League.class);
		when(mockTeam5Ranked.getQueue()).thenReturn("RANKED_TEAM_5x5");

		League mockTeam3Ranked = mock(League.class);
		when(mockTeam3Ranked.getQueue()).thenReturn("RANKED_TEAM_3x3");

		leagues.add(mockSoloRanked);
		leagues.add(mockTeam5Ranked);
		leagues.add(mockTeam3Ranked);

		LolLeagues lolLeagues = new LolLeagues(leagues);

		mInGamePlayer.setLeagues(lolLeagues);

		assertNotNull("fail set leagues test", mInGamePlayer.getLeagues());
		assertNotNull("fail set SoloRanked league test from InGamePlayer",
			mInGamePlayer.getLeagues().getRankedSoloQueueLeague());
		assertNotNull("fail set team5Ranked league test from InGamePlayer",
			mInGamePlayer.getLeagues().getRankedTeam5());
		assertNotNull("fail set team3Ranked league test from InGamePlayer",
			mInGamePlayer.getLeagues().getRankedTeam3());

	}

	public void testSetLeaguesBadQueueName() {
		List<League> leagues = new ArrayList<League>();

		League mockLeague = mock(League.class);
		when(mockLeague.getQueue()).thenReturn("awrwa");
		leagues.add(mockLeague);

		mInGamePlayer.setLeagues(new LolLeagues(leagues));

		assertNull("fail soloRanked queue null test from InGamePlayer",
			mInGamePlayer.getLeagues().getRankedSoloQueueLeague());
		assertNull("fail team5Ranked queue null test from InGamePlayer",
			mInGamePlayer.getLeagues().getRankedTeam5());
		assertNull("fail team3Ranked queue null test from InGamePlayer",
			mInGamePlayer.getLeagues().getRankedTeam3());
	}

	public void testSetLeagueTier() {
		List<League> leagues = new ArrayList<League>();

		League mockLeague = mock(League.class);
		when(mockLeague.getTier()).thenReturn("GOLD");
		when(mockLeague.getQueue()).thenReturn("RANKED_SOLO_5x5");

		leagues.add(mockLeague);

		mInGamePlayer.setLeagues(new LolLeagues(leagues));

		assertEquals("fail set Tier of SoloQueue from InGamePlayer", "GOLD",
			mInGamePlayer.getLeagues().getRankedSoloQueueLeague().getTier());
	}

	public void testSetLeagueDivision() {
		List<League> leagues = new ArrayList<League>();
		List<LeagueEntry> leagueEntries = new ArrayList<LeagueEntry>();

		LeagueEntry mockLeagueEntry = mock(LeagueEntry.class);
		when(mockLeagueEntry.getPlayerOrTeamId()).thenReturn("1");
		when(mockLeagueEntry.getDivision()).thenReturn("1");
		leagueEntries.add(mockLeagueEntry);

		League mockLeague = mock(League.class);
		when(mockLeague.getQueue()).thenReturn("RANKED_SOLO_5x5");
		when(mockLeague.getParticipantId()).thenReturn("1");
		when(mockLeague.getEntries()).thenReturn(leagueEntries);
		leagues.add(mockLeague);

		LolLeagues lolLeagues = new LolLeagues(leagues);

		mInGamePlayer.setLeagues(lolLeagues);

		assertEquals("fail set of leagueEntry & Division from InGamePlayer",
			"1", mInGamePlayer.getLeagues().getRankedSoloQueueLeague()
				.getDivision());

	}

	public void testSetSingletonRune() {
		List<LolRune> runes = new ArrayList<LolRune>();
		Rune mockRune = mock(Rune.class);
		when(mockRune.getSanitizedDescription()).thenReturn("+4 attack");
		for (int i = 0; i < ITER_NB1; i++) {
			runes.add(new LolRune(mockRune));
		}

		LolRunePage runePage = new LolRunePage("hello", runes);
		mInGamePlayer.setRunes(runePage);

		assertEquals("fail to check RunePage name in InGamePlayer", "hello",
			mInGamePlayer.getRunes().getName());
		assertEquals("fail to get good rune value in InGamePlayer",
			Double.valueOf(RUNE_VALUE),
			mInGamePlayer.getRunes().getStatsMap().get(" attack"));
		assertTrue("fail to get good rune description in InGamePlayer",
			mInGamePlayer.getRunes().getStatsMap().containsKey(" attack"));
	}

	public void testSetMultipleRune() {
		List<LolRune> runes = new ArrayList<LolRune>();

		Rune mockRune1 = mock(Rune.class);
		when(mockRune1.getSanitizedDescription()).thenReturn("+1 attack");

		Rune mockRune2 = mock(Rune.class);
		when(mockRune2.getSanitizedDescription()).thenReturn("+1 attack speed");

		Rune mockRune3 = mock(Rune.class);
		when(mockRune3.getSanitizedDescription())
			.thenReturn("+1 ability power");

		Rune mockRune4 = mock(Rune.class);
		when(mockRune4.getSanitizedDescription()).thenReturn(
			"+1 cooldown reduction");

		for (int i = 0; i < ITER_NB2; i++) {
			runes.add(new LolRune(mockRune1));
			runes.add(new LolRune(mockRune2));
			runes.add(new LolRune(mockRune3));
		}
		for (int i = 0; i < ITER_NB3; i++) {
			runes.add(new LolRune(mockRune4));
		}

		LolRunePage runePage = new LolRunePage("hello", runes);
		mInGamePlayer.setRunes(runePage);
		Map<String, Double> stats = mInGamePlayer.getRunes().getStatsMap();
		assertEquals("fail to check attack Value in RunePage in InGamePlayer",
			Double.valueOf(ITER_NB2), stats.get(" attack"));
		assertEquals(
			"fail to check attack speed Value in RunePage in InGamePlayer",
			Double.valueOf(ITER_NB2), stats.get(" attack speed"));
		assertEquals(
			"fail to check ability power Value in RunePage in InGamePlayer",
			Double.valueOf(ITER_NB2), stats.get(" ability power"));
		assertEquals(
			"fail to check cooldown reduction in RunePage in InGamePlayer",
			Double.valueOf(ITER_NB3), stats.get(" cooldown reduction"));
	}

	public void testSetMasteries() {
		MasteryPage mockMasteryPage = mock(MasteryPage.class);
		List<Mastery> customMasteries = new ArrayList<Mastery>();

		Mastery mockMastery1 = mock(Mastery.class);
		Mastery mockMastery2 = mock(Mastery.class);

		when(mockMastery1.getId()).thenReturn(MASTERY1);
		when(mockMastery1.getRank()).thenReturn(MASTERY_RANK1);

		when(mockMastery2.getId()).thenReturn(MASTERY2);
		when(mockMastery2.getRank()).thenReturn(MASTERY_RANK2);

		customMasteries.add(mockMastery1);
		customMasteries.add(mockMastery2);

		when(mockMasteryPage.getMasteries()).thenReturn(customMasteries);

		RiotApi api = new RiotApi(ApiKeysManager.getApiKeys().getRiotKey(),
			Region.EUW);
		MasteryTree masteryTree = null;
		try {
			masteryTree = api.getDataMasteryList(null, null,
				MasteryListData.ALL).getTree();
		} catch (RiotApiException e) {
			e.printStackTrace();
		}
		LolMasteryPage lolMasteryPage = new LolMasteryPage(mockMasteryPage,
			new LolMasteryTree(masteryTree));

		mInGamePlayer.setMasteries(lolMasteryPage);

		assertEquals("fail to check Masteries in InGamePlayer", "9/0/21",
			mInGamePlayer.getMasteries().getTreeDescription());
	}
}
