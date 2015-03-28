package ch.epfl.sweng.lolmaster.api.directapi.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyLong;  
import static org.mockito.Mockito.verify;  
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;
import ch.epfl.sweng.lolmaster.api.directapi.LolDirectApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.api.dto.LolSummoner;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApi;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;

import com.google.gson.Gson;

import constant.Region;
import constant.staticdata.ChampData;
import dto.League.League;
import dto.MatchHistory.MatchSummary;
import dto.MatchHistory.PlayerHistory;
import dto.Stats.PlayerStatsSummary;
import dto.Stats.PlayerStatsSummaryList;
import dto.Summoner.MasteryPages;
import dto.Summoner.Summoner;

/**
 * Tests for the LoldirectApi class
 * 
 * @author fKunstner
 * 
 */
public class LolDirectApiStaticChampionTest extends MockitoTestCase {
    private static final String CHAMPION_LIST_FILE_NAME = "Riot_StaticChampionList.json";
    private static final int MAX_MATCH_HISTORY_REQUEST = 15;

    private RiotApi mRiotApi;
    private MashapeApi mMashapeApi;
    private LolDirectApi mLolApi;
    private dto.Static.ChampionList mChampionList;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRiotApi = mock(RiotApi.class);
        when(mRiotApi.clone()).thenReturn(mRiotApi);
        doNothing().when(mRiotApi).setKey(anyString());
        mMashapeApi = new MashapeApi("Key1", Region.GLOBAL);

        String json = JsonAssetsReader.getFileContent(getInstrumentation()
            .getContext(), CHAMPION_LIST_FILE_NAME);

        mChampionList = new Gson()
            .fromJson(json, dto.Static.ChampionList.class);

        when(mRiotApi.getDataChampionList()).thenReturn(mChampionList);
        when(mRiotApi.getDataChampionList(null, null, true, ChampData.IMAGE))
            .thenReturn(mChampionList);

        mLolApi = new LolDirectApi(mRiotApi, mMashapeApi,
            ApiKeysManager.getApiKeys());
    }

    // Changed internals of getDataChampionList, test no longer uptodate.
    public void deprecatedTestGetDataChampionList() throws IOException,
        ApiException {

        Map<LolId, LolChampionData> championById;
        championById = mLolApi.getDataChampionList();

        assertNotNull(championById);
        assertFalse(championById.isEmpty());

        Collection<dto.Static.Champion> championSet = mChampionList.getData()
            .values();
        for (dto.Static.Champion champion : championSet) {
            LolId champId = new LolId(champion.getId());
            assertTrue(championById.containsKey(champId));

            LolChampionData lolChampion = new LolChampionData(champion);
            assertEquals(lolChampion.getName(), championById.get(champId)
                .getName());
            assertEquals(lolChampion.getId(), championById.get(champId).getId());
        }

        assertEquals(championSet.size(), championById.size());
    }
    
    //test to get correct SummonerByName
    public void testGetSummonerByName() throws ApiException, RiotApiException {
    	
    	Summoner mSummoner=mock(Summoner.class);
    	LolSummoner mLolSummoner=new LolSummoner(mSummoner);
        Map<String, Summoner> riotSummoners = new HashMap<String, Summoner>();
        riotSummoners.put("clfei", mSummoner);

    	when(mRiotApi.getSummonerByName("CLFEI")).thenReturn(riotSummoners);

    	when(mSummoner.getId()).thenReturn((long) 1);
    	
        mLolSummoner=mLolApi.getSummonerByName("CLFEI");
        assertEquals("fail to get SummonerByName from api", mLolSummoner.getId().getValue(), 1);
    
    }
  //test to get correct MatchHistoryBySummonerId
    public void testGetMatchHistoryBySummonerId() throws ApiException, RiotApiException {
        
    	List<LolMatchSummary> mList;
    	PlayerHistory ph = mock(PlayerHistory.class);
    	MatchSummary mMatchSummary = mock(MatchSummary.class);
    	
    	List<MatchSummary> ms=new ArrayList<MatchSummary>();
    	ms.add(mMatchSummary);
        
        when(mRiotApi.getRegion()).thenReturn("global");
        
        when(mRiotApi.getMatchHistory(Region.valueOf("GLOBAL"), 1, null, null, 1, MAX_MATCH_HISTORY_REQUEST+1))
        	.thenReturn(ph);
        
    	when(ph.getMatches()).thenReturn(ms);
    	
    	when(mMatchSummary.getMatchId()).thenReturn((long) 1);
    	
    	mList=mLolApi.getMatchHistoryBySummonerId(1, 1);
    	
        assertEquals("fail to get correct region from api", mRiotApi.getRegion(), "global");
        assertNotNull("get a null MatchHistoryBySummonerId", mList);
        assertEquals("fail to get correct MatchHistoryBySummonerId from api", mList.size(), 1);
        
        for (LolMatchSummary m : mList) {
        	assertEquals("fail to get correct MatchHistoryBySummonerId from api", m.getMatchId(), 1);
        }
    }
    //test to get LeaguesBySummonerIds
    public void testGetLeaguesBySummonerIds() throws ApiException, RiotApiException {
    	LolId mLolId=new LolId(1);
    	List<League> mlist=new ArrayList<League>();
    	mlist.add(new League());
    	
    	Map<LolId, LolLeagues> mapIdToLolLeagues=new HashMap<LolId, LolLeagues>();
    	Map<String, List<League>> mapIdToLeagues =new HashMap<String, List<League>>();
    	mapIdToLeagues.put("1", mlist);
    	when(mRiotApi.getLeagueBySummoners(1)).thenReturn(mapIdToLeagues);
    	
    	mapIdToLolLeagues=mLolApi.getLeaguesBySummonerIds(mLolId);
    	
        assertNotNull("fail to get LeaguesBySummonerIds from api", mapIdToLolLeagues.get(mLolId));
    
    }
    
    //test  LeaguesBySummonerIds
    public void testGetMasteryPageBySummonerIds() throws ApiException, RiotApiException {
    	LolId mLolId=new LolId(1);
    	Map<String, MasteryPages> masteries =
                new HashMap<String, MasteryPages>();
    	MasteryPages mMasteryPages= mock(MasteryPages.class);
    	masteries.put("1", mMasteryPages);
    	
    	when(mRiotApi.getMasteryPages(anyLong())).thenReturn(masteries);
    	
    	mLolApi.getMasteryPageBySummonerIds(mLolId);
    	
    	verify(mRiotApi).getMasteryPages(anyLong());
    }
    
  //test  LeaguesBySummonerIds
    public void testGetStatsBySummonerId() throws ApiException, RiotApiException {
    	final int mNumberOfNormalWins=10;
    	final int mNumberOfRankedWins=11;

    	LolId mLolId=new LolId(1);
    	PlayerStatsSummaryList allStats = mock(PlayerStatsSummaryList.class);
    	LolStats mLolStats;
    	PlayerStatsSummary stats1=mock(PlayerStatsSummary.class);
    	PlayerStatsSummary stats2=mock(PlayerStatsSummary.class);
    	List<PlayerStatsSummary> mlist=new ArrayList<PlayerStatsSummary>();
    	mlist.add(stats1);
    	mlist.add(stats2);
    	
    	when(mRiotApi.getPlayerStatsSummary(1)).thenReturn(allStats);
    	when(allStats.getPlayerStatSummaries()).thenReturn(mlist);
    	when(stats1.getPlayerStatSummaryType()).thenReturn("Unranked");
    	when(stats2.getPlayerStatSummaryType()).thenReturn("RankedSolo5x5");
    	when(stats1.getWins()).thenReturn(mNumberOfNormalWins);
    	when(stats2.getWins()).thenReturn(mNumberOfRankedWins);
    	
    	mLolStats=mLolApi.getStatsBySummonerId(mLolId);
    	
    	assertEquals("fail to get correct NormalWins by StatsBySummonerId", 
    					mNumberOfNormalWins, mLolStats.getNumberOfNormalWins());
    	
    	assertEquals("fail to get correct RankedWIns by StatsBySummonerId", 
    					mNumberOfRankedWins, mLolStats.getNumberOfRankedWins());
    }
}
