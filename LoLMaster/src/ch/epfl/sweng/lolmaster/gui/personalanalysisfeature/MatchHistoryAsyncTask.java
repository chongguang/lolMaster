package ch.epfl.sweng.lolmaster.gui.personalanalysisfeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.LolApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.database.DBManager;
import ch.epfl.sweng.lolmaster.database.MatchSummaryModel;
import ch.epfl.sweng.lolmaster.gui.LoadingTask;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement.MatchSummaryUIModel;
import constant.Region;

/**
 * AsyncTask that loads the match history of a user
 * 
 * @author fKunstner
 */
public abstract class MatchHistoryAsyncTask extends
    LoadingTask<String, List<MatchSummaryUIModel>> {
    public static final int NB_MATCH_HISTORY_PER_REQUEST = 15;
    public static final int MAX_NB_REQUEST = 10;

    private LolApi mApi;
    private DBManager mDB;
    private Map<LolId, LolChampionData> mChampionCache = null;
    private String mSummonerName;
    private String mRegion;

    public MatchHistoryAsyncTask(Activity activity, String summonerName,
        String region) {
        super(activity);
        mApi = LolApi.getApi();
        mDB = new DBManager(activity);
        mSummonerName = summonerName;
        mRegion = region;

    }

    protected Map<LolId, LolChampionData> getChampionCache() {
        return mChampionCache;
    }

    protected String getRequestedSummoner() {
        return mSummonerName;
    }

    @Override
    protected List<MatchSummaryUIModel> doInBackground(String... params) {
        List<MatchSummaryUIModel> historyUI =
            new ArrayList<MatchSummaryUIModel>();

        mApi.setRegion(Region.valueOf(mRegion));

        int summonerId;
        try {
            summonerId =
                mApi.getSummonerByName(mSummonerName).getId().getValue();
        } catch (ApiException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            cancel(e.getMessage());
            return historyUI;
        }

        try {
            updateMatchHistory(summonerId, mRegion);
        } catch (ApiException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            cancel(e.getMessage());
            return historyUI;
        }

        List<MatchSummaryModel> matchHistory =
            mDB.getMatchHistory(summonerId, mRegion);

        Map<LolId, LolChampionData> champions;
        Map<LolId, LolSummonerSpell> spells;
        Map<LolId, LolItem> items;
        try {
            champions = mApi.getDataChampionList();
            mChampionCache = champions;
            spells = mApi.getSpells();
            items = mApi.getItems();
        } catch (ApiException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            cancel(e.getMessage());
            return historyUI;
        }

        // Transform data to UI
        for (MatchSummaryModel match : matchHistory) {
            historyUI.add(fromDataToUI(match, champions, spells, items));
        }

        // Sort the list by match creation time
        Collections.sort(historyUI, new Comparator<MatchSummaryUIModel>() {
            public int compare(MatchSummaryUIModel lhs, MatchSummaryUIModel rhs) {
                Long time1 = lhs.getMatch().getMatchCreation();
                Long time2 = rhs.getMatch().getMatchCreation();
                return time2.compareTo(time1);
            }
        });

        return historyUI;
    }

    /**
     * Method to select the correct champion/spell/item to pass to the
     * MatchSummaryUIModel constructor.
     * 
     * @param match
     * @param champions
     * @param spells
     * @param items
     * @return
     */
    private static MatchSummaryUIModel fromDataToUI(MatchSummaryModel match,
        Map<LolId, LolChampionData> champions,
        Map<LolId, LolSummonerSpell> spells, Map<LolId, LolItem> items) {

        LolId championId = new LolId(match.getChampionId());
        LolChampionData champion = champions.get(championId);

        LolItem[] playerItems = new LolItem[match.getItems().length];
        for (int i = 0; i < match.getItems().length; i++) {
            LolId itemId = new LolId(match.getItems()[i]);
            playerItems[i] = items.get(itemId);
        }
        LolSummonerSpell[] summonerSpells =
            new LolSummonerSpell[match.getSpells().length];
        for (int i = 0; i < match.getSpells().length; i++) {
            LolId spellId = new LolId(match.getSpells()[i]);
            summonerSpells[i] = spells.get(spellId);
        }

        return new MatchSummaryUIModel(match, champion, playerItems,
            summonerSpells);
    }

    /**
     * Downloads all unknown matches of a given summoner (with a limit of {@see
     * MatchHistoryAsyncTask#MAX_NB_REQUEST}), and stores it into the database.
     * 
     * @param summonerId
     * @param region
     * @throws ApiException
     */
    private void updateMatchHistory(int summonerId, String region)
        throws ApiException {

        long latestMatchCreationInDB =
            mDB.getLastMatchCreation(summonerId, region);

        List<LolMatchSummary> matchSummaries =
            getMatchHistoryForSummonerUpTo(summonerId, latestMatchCreationInDB);

        mDB.saveMatchHistory(matchSummaries, summonerId, region);
    }

    /**
     * Downloads a summoner match history until we encounter a match that has
     * been played before {@code latestMathCreationInDB}
     * 
     * @param summonerId
     * @param latestMatchCreationInDB
     * @return
     * @throws ApiException
     */
    private List<LolMatchSummary> getMatchHistoryForSummonerUpTo(
        int summonerId, long latestMatchCreationInDB) throws ApiException {
        List<LolMatchSummary> matchSummaries = new ArrayList<LolMatchSummary>();

        long lastSeenMatchCreation = 0;
        int i = 0;

        boolean exceededMaxRequest = i > MAX_NB_REQUEST;
        boolean alreadyStoredInDB = false;
        boolean reachedEnd = false;

        while (!exceededMaxRequest && !alreadyStoredInDB && !reachedEnd) {
            int startIndex = NB_MATCH_HISTORY_PER_REQUEST * i;

            getProgressUpdater().update(
                "Loading matches " + startIndex + " to "
                    + (startIndex + NB_MATCH_HISTORY_PER_REQUEST));

            List<LolMatchSummary> temp;
            temp = mApi.getMatchHistoryBySummonerId(summonerId, startIndex);

            for (LolMatchSummary matchSummary : temp) {
                lastSeenMatchCreation = matchSummary.getMatchCreation();
                if (lastSeenMatchCreation > latestMatchCreationInDB) {
                    matchSummaries.add(matchSummary);
                } else {
                	alreadyStoredInDB = true;
                }
            }

            i++;
            exceededMaxRequest = i > MAX_NB_REQUEST;
            reachedEnd = temp.size() < NB_MATCH_HISTORY_PER_REQUEST;
        }
        return matchSummaries;
    }

    @Override
    protected abstract void onPostExecute(List<MatchSummaryUIModel> results);
}
