package ch.epfl.sweng.lolmaster.api;

import java.util.List;
import java.util.Map;

import main.java.riotapi.RiotApi;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.directapi.LolDirectApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolInProgressGame;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryPage;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolRunePage;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.api.dto.LolSummoner;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApi;
import constant.Region;

/**
 * Provides methods to get informations about League of Legend
 * 
 * @author fKunstner
 */
public abstract class LolApi {
    private static final Region DEFAULT_REGION = Region.EUW;
    private RiotApi mRiotApi;
    private MashapeApi mMashapeApi;
    private ApiKeys mApiKeys;
    private static LolApi mockApi;

    /**
     * Get a LolApi
     * 
     * @return
     */
    public static LolApi getApi() {
        if (mockApi != null) {
            return mockApi;
        }

        ApiKeys apiKeys = ApiKeysManager.getApiKeys();
        RiotApi riotApi = new RiotApi(apiKeys.getRiotKey(), DEFAULT_REGION);
        MashapeApi mashapeApi =
            new MashapeApi(apiKeys.getMashapeKey(), DEFAULT_REGION);
        return new LolDirectApi(riotApi, mashapeApi, apiKeys);
    }

    protected LolApi(RiotApi riotApi, MashapeApi mashapeApi, ApiKeys apiKeys) {
        mRiotApi = riotApi.clone();
        mMashapeApi = mashapeApi.duplicate();
        mApiKeys = apiKeys;
    }

    protected void rotateMashapeKey() {
        String key = mApiKeys.getMashapeKey();
        Log.d(this.getClass().getName(), "Using MashapeKey:" + key);
        mMashapeApi.setKey(key);
    }

    protected void rotateRiotkey() {
        String key = mApiKeys.getRiotKey();
        Log.d(this.getClass().getName(), "Using RiotKey:" + key);
        mRiotApi.setKey(key);

    }

    /**
     * Set the region to query for further informations.
     * 
     * @param region
     *            The region
     * @throws ApiException
     *             if the region could not be set
     */
    public void setRegion(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("A null Region is not allowed.");
        }
        getRiotApi().setRegion(region);
        getMashapeApi().setRegion(region);
        assertRegionSync();
    }

    public String getRegion() {
        assertRegionSync();
        return getRiotApi().getRegion();
    }

    protected RiotApi getRiotApi() {
        return mRiotApi;
    }

    protected MashapeApi getMashapeApi() {
        return mMashapeApi;
    }

    private void assertRegionSync() {
        String riotRegion = getRiotApi().getRegion();
        String mashapeRegion = getMashapeApi().getRegion();
        if (!riotRegion.equals(mashapeRegion)) {
            throw new AssertionError(
                "Region of inner api component is not sync. Riot region: "
                    + riotRegion + ", Mashape region: " + mashapeRegion);
        }
    }

    // Mashape Api - In progress game
    /**
     * Get informations about a game in progress.
     * 
     * @param summonerName
     * @return a {@see LolInProgressGame} containing informations about the
     *         current game, or null if the champion is not in game.
     * @throws ApiException
     *             if the information could not be retrieved.
     */
    public abstract LolInProgressGame getInProgessGame(String summonerName)
        throws ApiException;

    // Riot Api - Champion
    /**
     * Get a list of all free to play champions Ids
     * 
     * @return a {@code List<Long>} containing the ids of the current free to
     *         play champions.
     * @throws ApiException
     *             if the information could not be retrieved.
     */
    public abstract List<LolId> getFreeToPlayChampions() throws ApiException;

    // Riot Api - Summoner
    /**
     * Get informations about a summoner by it's name.
     * 
     * @param summonerName
     *            The name of the summoner to look up.
     * @return a {@see LolSummoner} containing informations about the requested
     *         summoner.
     * @throws ApiException
     *             if the information could not be retrieved.
     */
    public abstract LolSummoner getSummonerByName(String summonerName)
        throws ApiException;

    // Riot Api - Static data
    /**
     * Get static informations about all champions
     * 
     * @return A map of each champion's Id to it's {@see LolChampionData} object
     *         containing static informations.
     * @trows ApiException if the information could not be retrieved.
     * 
     */
    public abstract Map<LolId, LolChampionData> getDataChampionList()
        throws ApiException;

    /**
     * Get champion's static information by id
     * @param id the id of the champion
     * @return a champion's static information in form of a {@see LolChampionData}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract LolChampionData getDataChampionById(int id)
        throws ApiException;

    /**
     * Get leagues by summoners ID
     * @param summonerId the Ids of summoners
     * @return A map of summoner's Id to it's {@see LolLeagues}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract Map<LolId, LolLeagues> getLeaguesBySummonerIds(
        LolId... summonerId) throws ApiException;

    /**
     * Get Stats by summoner ID
     * @param summonerId the ID of the summoner
     * @return stats of a summoner in form of a {@see LolStats}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract LolStats getStatsBySummonerId(LolId summonerId)
        throws ApiException;

    /**
     * Get Rune pages by summoners IDs
     * @param summonerIds the Ids of the summoners
     * @return A Map of summoner's ID to it's {@see LolRunePage}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract Map<LolId, LolRunePage> getRunesBySummonerIds(
        LolId... summonerIds) throws ApiException;

    /**
     * Get the Mastery Pages by summoenrs IDs
     * @param summonerIds The Ids of the summoenrs
     * @return A Map of summoenr's ID to it's {@see LolMasteryPage}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract Map<LolId, LolMasteryPage> getMasteryPageBySummonerIds(
        LolId... summonerIds) throws ApiException;

    /**
     * Get static information on all summoner's spell
     * @return A Map of Summoner's spell ID to it's {@see LolSummonerSpell}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract Map<LolId, LolSummonerSpell> getSpells()
        throws ApiException;

    /**
     * Get the summoner's last 10 match summaries
     * 
     * @param summonerName
     * @return
     * @throws ApiException
     */
    public abstract List<LolMatchSummary> getMatchHistoryBySummonerId(
        int summonerId, int beginIndex) throws ApiException;

    /**
     * Get static information on all items
     * @return A Map of item's ID to it's {@see LolItem}
     * @throws ApiException if the information could not be retrieved
     */
    public abstract Map<LolId, LolItem> getItems() throws ApiException;

    /**
     * set mockapi for test
     * 
     * @param mockapi
     */
    public static void injectApi(LolApi mockapi) {
        mockApi = mockapi;
    }
}
