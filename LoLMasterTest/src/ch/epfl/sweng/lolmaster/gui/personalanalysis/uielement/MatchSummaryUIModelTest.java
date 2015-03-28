package ch.epfl.sweng.lolmaster.gui.personalanalysis.uielement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant.LolParticipantStats;
import ch.epfl.sweng.lolmaster.database.MatchSummaryModel;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement.MatchSummaryUIModel;
import dto.MatchHistory.ParticipantIdentity;
import android.test.AndroidTestCase;

/**
 * @author lcg31439
 *
 */
public class MatchSummaryUIModelTest extends AndroidTestCase {
	
	private MatchSummaryModel msm;
	private LolId summonerId;
	private LolMatchSummary matchSummary;
	private LolChampionData champion;
    private LolItem item0;
    private LolItem item1;
    private LolItem item2;
    private LolItem item3;
    private LolItem item4;
    private LolItem item5;
    private LolItem item6;
    private LolSummonerSpell spell1;
    private LolSummonerSpell spell2;
	
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
	private static final String MOCK_CHAMPION_NAME = "Yasuo";
	private static final String MOCK_SPELL1_NAME = "spell1";
	private static final String MOCK_SPELL2_NAME = "spell2";
	private static final String MOCK_ITEM0_NAME = "item0";
	private static final String MOCK_ITEM1_NAME = "item1";
	private static final String MOCK_ITEM2_NAME = "item2";
	private static final String MOCK_ITEM3_NAME = "item3";
	private static final String MOCK_ITEM4_NAME = "item4";
	private static final String MOCK_ITEM5_NAME = "item5";
	private static final String MOCK_ITEM6_NAME = "item6";

	private static final int[] MOCK_SPELLS = {MOCK_SPELL1, MOCK_SPELL2};	
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
		
		champion = mock(LolChampionData.class);
		when(champion.getName()).thenReturn(MOCK_CHAMPION_NAME);
		spell1 = mock(LolSummonerSpell.class);
		when(spell1.getName()).thenReturn(MOCK_SPELL1_NAME);
		spell2 = mock(LolSummonerSpell.class);
		when(spell2.getName()).thenReturn(MOCK_SPELL2_NAME);
		item0 = mock(LolItem.class);
		when(item0.getName()).thenReturn(MOCK_ITEM0_NAME);
		item1 = mock(LolItem.class);
		when(item1.getName()).thenReturn(MOCK_ITEM1_NAME);
		item2 = mock(LolItem.class);
		when(item2.getName()).thenReturn(MOCK_ITEM2_NAME);
		item3 = mock(LolItem.class);
		when(item3.getName()).thenReturn(MOCK_ITEM3_NAME);
		item4 = mock(LolItem.class);
		when(item4.getName()).thenReturn(MOCK_ITEM4_NAME);
		item5 = mock(LolItem.class);
		when(item5.getName()).thenReturn(MOCK_ITEM5_NAME);
		item6 = mock(LolItem.class);
		when(item6.getName()).thenReturn(MOCK_ITEM6_NAME);
	}

	public void testMatchResult() {
		LolSummonerSpell[] spells = {spell1, spell2};
		LolItem[] items = {item0, item1, item2, item3, item4, item5, item6};
		MatchSummaryUIModel ui = new MatchSummaryUIModel(msm, champion, items, spells);
		assertNotNull(ui);
		assertEquals(ui.matchResult(), "Victory");	
		assertEquals(ui.getMatch().getMatchId(), MOCK_MATCH_ID);	
		assertEquals(ui.getChampion().getName(), MOCK_CHAMPION_NAME);
		assertEquals(ui.getSpells()[0].getName(), MOCK_SPELL1_NAME);	
		assertEquals(ui.getItems()[0].getName(), MOCK_ITEM0_NAME);	
	}

}
