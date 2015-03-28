package ch.epfl.sweng.lolmaster.database;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant.LolParticipantStats;
import dto.MatchHistory.ParticipantIdentity;

/**
 * @author lcg31439
 *
 */
public class DBManagerTest extends AndroidTestCase {
	private DBManager dbManager;
	private MatchHistoryDBHelper testerHelper;
	
	private static final String REGION = "EUW";
	private static final int SUMMONER_ID = 1;
	private LolId lolSummonerId;
	private LolMatchSummary match1;
	private LolMatchSummary match2;
	private ParticipantIdentity playerIdentity1;
	private ParticipantIdentity playerIdentity2;
	private LolParticipant participant1;
	private LolParticipant participant2;
	private LolParticipantStats stats1;
	private LolParticipantStats stats2;
	
	private static final long MATCH_ID_1 = 11;
	private static final long MATCH_ID_2 = 22;	
	private static final long MATCH_CREATION_1 = 111;
	private static final long MATCH_CREATION_2 = 222;	
	private static final int PARTICIPANT_ID_1 = 1111;
	private static final int PARTICIPANT_ID_2 = 2222;	
	private static final int CHAMPION_ID_1 = 11111;
	private static final int CHAMPION_ID_2 = 22222;	
	private static final int[] SPELLS_1 = {0, 1};
	private static final int[] SPELLS_2 = {2, 3};	
	private static final boolean IS_WINNER_1 = true;
	private static final boolean IS_WINNER_2 = false;
	private static final long KILLS_1 = 33;
	private static final long KILLS_2 = 44;
	private static final long DEATH_1 = 333;
	private static final long DEATH_2 = 444;
	private static final long ASSISTS_1 = 3333;
	private static final long ASSISTS_2 = 4444;
	private static final long GOLD_1 = 33333;
	private static final long GOLD_2 = 44444;	
	private static final long[] ITEMS_1 = {0, 1, 2, 3, 4, 5, 6};
	private static final long[] ITEMS_2 = {10, 11, 12, 13, 14, 15, 16};	
	
	public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        dbManager = new DBManager(context);
        testerHelper = new MatchHistoryDBHelper(context);
		SQLiteDatabase database = testerHelper.getWritableDatabase();
		database.execSQL("delete from " + MatchHistoryDBHelper.TABLE_NAME);
		database.close();
        assertEquals(dbManager.getMatchHistory(SUMMONER_ID, REGION).size(), 0);
        
        lolSummonerId = new LolId(SUMMONER_ID);
        match1 = mock(LolMatchSummary.class);
        match2 = mock(LolMatchSummary.class);
        playerIdentity1 = mock(ParticipantIdentity.class);
        playerIdentity2 = mock(ParticipantIdentity.class);
        when(match1.getParticipantIdentity(lolSummonerId)).thenReturn(playerIdentity1);
        when(match2.getParticipantIdentity(lolSummonerId)).thenReturn(playerIdentity2);
        when(match1.getMatchId()).thenReturn(MATCH_ID_1);
        when(match2.getMatchId()).thenReturn(MATCH_ID_2);
        when(match1.getMatchCreation()).thenReturn(MATCH_CREATION_1);
        when(match2.getMatchCreation()).thenReturn(MATCH_CREATION_2);
        when(match1.getRegion()).thenReturn(REGION);
        when(match2.getRegion()).thenReturn(REGION);
        when(playerIdentity1.getParticipantId()).thenReturn(PARTICIPANT_ID_1);
        when(playerIdentity2.getParticipantId()).thenReturn(PARTICIPANT_ID_2);
        participant1 = mock(LolParticipant.class);
        participant2 = mock(LolParticipant.class);
        when(match1.getParticipant(PARTICIPANT_ID_1)).thenReturn(participant1);
        when(match2.getParticipant(PARTICIPANT_ID_2)).thenReturn(participant2);
        when(participant1.getChampionId()).thenReturn(CHAMPION_ID_1);
        when(participant2.getChampionId()).thenReturn(CHAMPION_ID_2);
        when(participant1.getSpells()).thenReturn(SPELLS_1);
        when(participant2.getSpells()).thenReturn(SPELLS_2);
        stats1 = mock(LolParticipantStats.class);
        stats2 = mock(LolParticipantStats.class);
        when(participant1.getStats()).thenReturn(stats1);
        when(participant2.getStats()).thenReturn(stats2);
        when(stats1.isWinner()).thenReturn(IS_WINNER_1);
        when(stats2.isWinner()).thenReturn(IS_WINNER_2);
        when(stats1.getKills()).thenReturn(KILLS_1);
        when(stats2.getKills()).thenReturn(KILLS_2);
        when(stats1.getDeaths()).thenReturn(DEATH_1);
        when(stats2.getDeaths()).thenReturn(DEATH_2);
        when(stats1.getAssists()).thenReturn(ASSISTS_1);
        when(stats2.getAssists()).thenReturn(ASSISTS_2);
        when(stats1.getGoldEarned()).thenReturn(GOLD_1);
        when(stats2.getGoldEarned()).thenReturn(GOLD_2);
        when(stats1.getItems()).thenReturn(ITEMS_1);
        when(stats2.getItems()).thenReturn(ITEMS_2);
        
	}

    public void tearDown() throws Exception {
        super.tearDown();
    	dbManager.deleteAllRecords();
    }
	
    /**
     * This test is a normal scenario to verify that all the DB manipulations works well together
     */
	public void testNormalScenario() {		
		List<LolMatchSummary> matchHistory = new ArrayList<LolMatchSummary>();
        matchHistory.add(match1);
        matchHistory.add(match2);
		List<MatchSummaryModel> h = dbManager.getMatchHistory(SUMMONER_ID, REGION);
		assertNotNull(h);
		assertEquals(h.size(), 0);
		dbManager.saveMatchHistory(matchHistory, SUMMONER_ID, REGION);
		h = dbManager.getMatchHistory(SUMMONER_ID, REGION);
		assertNotNull(h);
		assertEquals(h.size(), 2);
		assertEquals(MATCH_CREATION_2, dbManager.getLastMatchCreation(SUMMONER_ID, REGION));
		dbManager.deleteAllRecords();
		h = dbManager.getMatchHistory(SUMMONER_ID, REGION);
		assertNotNull(h);
		assertEquals(h.size(), 0);		
	}
	
	/**
	 * This test verifies that when we save records with same primary keys, they are just stored once
	 */
	public void testDuplicatedRecords() {
		dbManager.deleteAllRecords();
		List<LolMatchSummary> matchHistory = new ArrayList<LolMatchSummary>();
        matchHistory.add(match1);
        matchHistory.add(match1);
        List<MatchSummaryModel> h = dbManager.getMatchHistory(SUMMONER_ID, REGION);
		assertNotNull(h);
		assertEquals(h.size(), 0);
        dbManager.saveMatchHistory(matchHistory, SUMMONER_ID, REGION);
        h = dbManager.getMatchHistory(SUMMONER_ID, REGION);
		assertNotNull(h);
		assertEquals(h.size(), 1);
		dbManager.deleteAllRecords();
		h = dbManager.getMatchHistory(SUMMONER_ID, REGION);
		assertNotNull(h);
		assertEquals(h.size(), 0);		
	}

}
