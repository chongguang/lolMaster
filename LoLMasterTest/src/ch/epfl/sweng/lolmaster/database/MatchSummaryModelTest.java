package ch.epfl.sweng.lolmaster.database;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant.LolParticipantStats;
import ch.epfl.sweng.lolmaster.database.MatchHistoryDBHelper.TableField;
import dto.MatchHistory.ParticipantIdentity;

/**
 * @author lcg31439
 *
 */
public class MatchSummaryModelTest extends AndroidTestCase {
	
	private MatchSummaryModel msm;
	private LolId summonerId;
	private LolMatchSummary matchSummary;
	private MatchSummaryModel m;
	private Cursor cursor;
	
	private static final int MOCK_SUMMONER_ID = 1;
	private static final long MOCK_MATCH_ID = 2;
	private static final long MOCK_MATCH_CREATION = 3;
	private static final String MOCK_REGION = "EUW";
	private static final int MOCK_PARTICIPANT_ID = 4;
	private static final int MOCK_CHAMPION_ID = 5;
	private static final int MOCK_SPELL1 = 6;
	private static final int MOCK_SPELL2 = 7;
	private static final boolean MOCK_IS_WINNER = true;
	private static final long MOCK_KILLS = 8;
	private static final long MOCK_DEATHS = 9;
	private static final long MOCK_ASSISTS = 10;
	private static final long MOCK_GOLD = 11;
	private static final long MOCK_ITEM_0 = 12;
	private static final long MOCK_ITEM_1 = 13;
	private static final long MOCK_ITEM_2 = 14;
	private static final long MOCK_ITEM_3 = 15;
	private static final long MOCK_ITEM_4 = 16;
	private static final long MOCK_ITEM_5 = 17;
	private static final long MOCK_ITEM_6 = 18;
	
	private static final int INDEX_MOCK_SUMMONER_ID = 1;
	private static final int INDEX_MOCK_MATCH_ID = 2;
	private static final int INDEX_MOCK_MATCH_CREATION = 3;
	private static final int INDEX_MOCK_REGION = 19;
	private static final int INDEX_MOCK_CHAMPION_ID = 5;
	private static final int INDEX_MOCK_SPELL1 = 6;
	private static final int INDEX_MOCK_SPELL2 = 7;
	private static final int INDEX_MOCK_IS_WINNER = 20;
	private static final int INDEX_MOCK_KILLS = 8;
	private static final int INDEX_MOCK_DEATHS = 9;
	private static final int INDEX_MOCK_ASSISTS = 10;
	private static final int INDEX_MOCK_GOLD = 11;
	private static final int INDEX_MOCK_ITEM_0 = 12;
	private static final int INDEX_MOCK_ITEM_1 = 13;
	private static final int INDEX_MOCK_ITEM_2 = 14;
	private static final int INDEX_MOCK_ITEM_3 = 15;
	private static final int INDEX_MOCK_ITEM_4 = 16;
	private static final int INDEX_MOCK_ITEM_5 = 17;
	private static final int INDEX_MOCK_ITEM_6 = 18;	

	private static final int[] MOCK_SPELLS = {MOCK_SPELL1, MOCK_SPELL2};
	private static final int[] INDEX_MOCK_SPELLS = {INDEX_MOCK_SPELL1, INDEX_MOCK_SPELL2};
	
	private static final int[] INDEX_MOCK_ITEMS = {INDEX_MOCK_ITEM_0, INDEX_MOCK_ITEM_1, INDEX_MOCK_ITEM_2, 
		INDEX_MOCK_ITEM_3, INDEX_MOCK_ITEM_4, INDEX_MOCK_ITEM_5, INDEX_MOCK_ITEM_6};	
	private static final long[] MOCK_ITEMS = {MOCK_ITEM_0, MOCK_ITEM_1, MOCK_ITEM_2, 
		MOCK_ITEM_3, MOCK_ITEM_4, MOCK_ITEM_5, MOCK_ITEM_6};

    public void setUp() throws Exception {		
		super.setUp();	
        
		summonerId = new LolId(MOCK_SUMMONER_ID);
		matchSummary = mock(LolMatchSummary.class);
		ParticipantIdentity playerIdentity = mock(ParticipantIdentity.class);		
		when(matchSummary.getParticipantIdentity(summonerId)).thenReturn(playerIdentity);

		when(matchSummary.getMatchId()).thenReturn(MOCK_MATCH_ID);
		when(matchSummary.getMatchCreation()).thenReturn(MOCK_MATCH_CREATION);
		when(matchSummary.getRegion()).thenReturn(MOCK_REGION);
		
		LolParticipant participant = mock(LolParticipant.class);
		when(playerIdentity.getParticipantId()).thenReturn(MOCK_PARTICIPANT_ID);
		when(matchSummary.getParticipant(MOCK_PARTICIPANT_ID)).thenReturn(participant);
		
		when(participant.getChampionId()).thenReturn(MOCK_CHAMPION_ID);
		when(participant.getSpells()).thenReturn(MOCK_SPELLS);
		LolParticipantStats stats = mock(LolParticipantStats.class);
		
		when(participant.getStats()).thenReturn(stats);
		when(stats.isWinner()).thenReturn(MOCK_IS_WINNER);
		when(stats.getKills()).thenReturn(MOCK_KILLS);
		when(stats.getDeaths()).thenReturn(MOCK_DEATHS);
		when(stats.getAssists()).thenReturn(MOCK_ASSISTS);
		when(stats.getGoldEarned()).thenReturn(MOCK_GOLD);
		when(stats.getItems()).thenReturn(MOCK_ITEMS);
		msm = new MatchSummaryModel(summonerId, matchSummary);
		
		
		cursor = mock(Cursor.class);
		when(cursor.getColumnIndexOrThrow(TableField.MATCH_ID.getName())).
			thenReturn(INDEX_MOCK_MATCH_ID);
		when(cursor.getColumnIndexOrThrow(TableField.SUMMONER_ID.getName())).
			thenReturn(INDEX_MOCK_SUMMONER_ID);
		when(cursor.getColumnIndexOrThrow(TableField.MATCH_CREATION.getName())).
			thenReturn(INDEX_MOCK_MATCH_CREATION);
		when(cursor.getColumnIndexOrThrow(TableField.REGION.getName())).
			thenReturn(INDEX_MOCK_REGION);
		when(cursor.getColumnIndexOrThrow(TableField.IS_WINNER.getName())).
			thenReturn(INDEX_MOCK_IS_WINNER);
		when(cursor.getColumnIndexOrThrow(TableField.CHAMPION_ID.getName())).
			thenReturn(INDEX_MOCK_CHAMPION_ID);
		when(cursor.getColumnIndexOrThrow(TableField.KILLS.getName())).
			thenReturn(INDEX_MOCK_KILLS);
		when(cursor.getColumnIndexOrThrow(TableField.DEATHS.getName())).
			thenReturn(INDEX_MOCK_DEATHS);
		when(cursor.getColumnIndexOrThrow(TableField.ASSISTS.getName())).
			thenReturn(INDEX_MOCK_ASSISTS);
		when(cursor.getColumnIndexOrThrow(TableField.GOLD_EARNED.getName())).
			thenReturn(INDEX_MOCK_GOLD);
		for (int i = 0; i < TableField.SPELLS.length; i++) {
			when(cursor.getColumnIndexOrThrow(TableField.SPELLS[i].getName())).
				thenReturn(INDEX_MOCK_SPELLS[i]);
		}
		for (int i = 0; i < TableField.ITEMS.length; i++) {
			when(cursor.getColumnIndexOrThrow(TableField.ITEMS[i].getName())).
				thenReturn(INDEX_MOCK_ITEMS[i]);
		}	
		
		
		when(cursor.getLong(INDEX_MOCK_MATCH_ID)).thenReturn(MOCK_MATCH_ID);
		when(cursor.getInt(INDEX_MOCK_SUMMONER_ID)).thenReturn(MOCK_SUMMONER_ID);
		when(cursor.getLong(INDEX_MOCK_MATCH_CREATION)).thenReturn(MOCK_MATCH_CREATION);
		when(cursor.getString(INDEX_MOCK_REGION)).thenReturn(MOCK_REGION);
		when(cursor.getInt(INDEX_MOCK_IS_WINNER)).thenReturn(1);
		when(cursor.getInt(INDEX_MOCK_CHAMPION_ID)).thenReturn(MOCK_CHAMPION_ID);
		when(cursor.getLong(INDEX_MOCK_KILLS)).thenReturn(MOCK_KILLS);
		when(cursor.getLong(INDEX_MOCK_DEATHS)).thenReturn(MOCK_DEATHS);
		when(cursor.getLong(INDEX_MOCK_ASSISTS)).thenReturn(MOCK_ASSISTS);
		when(cursor.getLong(INDEX_MOCK_GOLD)).thenReturn(MOCK_GOLD);
		for (int i = 0; i < TableField.SPELLS.length; i++) {
			when(cursor.getInt(INDEX_MOCK_SPELLS[i])).thenReturn(MOCK_SPELLS[i]);
		}
		for (int i = 0; i < TableField.ITEMS.length; i++) {
			when(cursor.getLong(INDEX_MOCK_ITEMS[i])).thenReturn(MOCK_ITEMS[i]);
		}	
    }
    public void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Test the constructor with mock objects
     */
	public void testConstractorWithObjects() {	
		msm = new MatchSummaryModel(summonerId, matchSummary);
		assertEquals(msm.getMatchId(), MOCK_MATCH_ID);
		assertEquals(msm.getSummonerId(), MOCK_SUMMONER_ID);
		assertEquals(msm.getMatchCreation(), MOCK_MATCH_CREATION);
		assertEquals(msm.getRegion(), MOCK_REGION);
		assertEquals(msm.isWinner(), MOCK_IS_WINNER);
		assertEquals(msm.getChampionId(), MOCK_CHAMPION_ID);
		assertEquals(msm.getKills(), MOCK_KILLS);
		assertEquals(msm.getDeaths(), MOCK_DEATHS);
		assertEquals(msm.getAssists(), MOCK_ASSISTS);
		assertEquals(msm.getGoldEarned(), MOCK_GOLD);
		assertTrue(msm.getSpells() == MOCK_SPELLS);
		assertTrue(msm.getItems() == MOCK_ITEMS);
	}
	
	/**
	 * Test the contentValue function
	 */
	public void testContentValues() {
		ContentValues cv = msm.contentValues();			
		assertEquals(cv.getAsLong(TableField.MATCH_ID.getName()).longValue(), MOCK_MATCH_ID);
		assertEquals(cv.getAsInteger(TableField.SUMMONER_ID.getName()).intValue(), MOCK_SUMMONER_ID);
		assertEquals(cv.getAsLong(TableField.MATCH_CREATION.getName()).longValue(), MOCK_MATCH_CREATION);
		assertEquals(cv.getAsString(TableField.REGION.getName()), MOCK_REGION);
		assertEquals(cv.getAsBoolean(TableField.IS_WINNER.getName()).booleanValue(), MOCK_IS_WINNER);
		assertEquals(cv.getAsInteger(TableField.CHAMPION_ID.getName()).intValue(), MOCK_CHAMPION_ID);
		assertEquals(cv.getAsLong(TableField.KILLS.getName()).longValue(), MOCK_KILLS);
		assertEquals(cv.getAsLong(TableField.DEATHS.getName()).longValue(), MOCK_DEATHS);
		assertEquals(cv.getAsLong(TableField.ASSISTS.getName()).longValue(), MOCK_ASSISTS);
		assertEquals(cv.getAsLong(TableField.GOLD_EARNED.getName()).longValue(), MOCK_GOLD);
		assertEquals(cv.getAsInteger(TableField.SPELL1.getName()).intValue(), MOCK_SPELL1);
		assertEquals(cv.getAsInteger(TableField.SPELL2.getName()).intValue(), MOCK_SPELL2);
		assertEquals(cv.getAsLong(TableField.ITEM0.getName()).longValue(), MOCK_ITEM_0);
		assertEquals(cv.getAsLong(TableField.ITEM1.getName()).longValue(), MOCK_ITEM_1);
		assertEquals(cv.getAsLong(TableField.ITEM2.getName()).longValue(), MOCK_ITEM_2);
		assertEquals(cv.getAsLong(TableField.ITEM3.getName()).longValue(), MOCK_ITEM_3);
		assertEquals(cv.getAsLong(TableField.ITEM4.getName()).longValue(), MOCK_ITEM_4);
		assertEquals(cv.getAsLong(TableField.ITEM5.getName()).longValue(), MOCK_ITEM_5);
		assertEquals(cv.getAsLong(TableField.ITEM6.getName()).longValue(), MOCK_ITEM_6);
	}
	
	/**
	 * Test constructor for getting data from DB
	 */
	public void testConstructorCursor() {
		m = new MatchSummaryModel(cursor);				
		assertEquals(m.getMatchId(), MOCK_MATCH_ID);
		assertEquals(m.getSummonerId(), MOCK_SUMMONER_ID);
		assertEquals(m.getMatchCreation(), MOCK_MATCH_CREATION);
		assertEquals(m.getRegion(), MOCK_REGION);
		assertEquals(m.isWinner(), MOCK_IS_WINNER);
		assertEquals(m.getChampionId(), MOCK_CHAMPION_ID);
		assertEquals(m.getKills(), MOCK_KILLS);
		assertEquals(m.getDeaths(), MOCK_DEATHS);
		assertEquals(m.getAssists(), MOCK_ASSISTS);
		assertEquals(m.getGoldEarned(), MOCK_GOLD);
		assertEquals(m.getSpells()[0], MOCK_SPELL1);
		assertEquals(m.getSpells()[1], MOCK_SPELL2);
		assertEquals(m.getItems()[0], MOCK_ITEM_0);
	}

}
