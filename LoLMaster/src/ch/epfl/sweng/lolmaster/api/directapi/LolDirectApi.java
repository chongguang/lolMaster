package ch.epfl.sweng.lolmaster.api.directapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.ApiKeys;
import ch.epfl.sweng.lolmaster.api.LolApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolInProgressGame;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryPage;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryTree;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolRune;
import ch.epfl.sweng.lolmaster.api.dto.LolRunePage;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.api.dto.LolSummoner;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApi;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApiException;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo;
import constant.Region;
import constant.Season;
import constant.staticdata.ChampData;
import dto.Champion.Champion;
import dto.League.League;
import dto.MatchHistory.MatchSummary;
import dto.MatchHistory.PlayerHistory;
import dto.Static.MasteryTree;
import dto.Static.Rune;
import dto.Static.RuneList;
import dto.Stats.PlayerStatsSummaryList;
import dto.Summoner.MasteryPage;
import dto.Summoner.MasteryPages;
import dto.Summoner.RunePage;
import dto.Summoner.RunePages;
import dto.Summoner.RuneSlot;
import dto.Summoner.Summoner;

/**
 * @author fKunstner
 */
public final class LolDirectApi extends LolApi {
    private static final int MAX_MATCH_HISTORY_REQUEST = 15;
    private static final int MAX_SIMULTANEOUS_REQ = 10;
    private static final Season DEFAULT_SEASON = Season.FOUR;
    private static final String DATA_UNAVAILABLE_ERROR_MESSAGE =
        "Wasn't able to read data from the server.";

    public LolDirectApi(RiotApi riotApi, MashapeApi mashapeApi, ApiKeys apiKeys) {
        super(riotApi, mashapeApi, apiKeys);
    }

    // Mashape Api - In progress game
    @Override
    public LolInProgressGame getInProgessGame(String summonerName)
        throws ApiException {
        if (summonerName == null || summonerName.isEmpty()) {
            throw new IllegalArgumentException("Summoner name is null or empty");
        }
        rotateMashapeKey();

        InProgressGameInfo inProgressGameInfo = null;
        try {
            inProgressGameInfo =
                getMashapeApi().getInProgessGameInfo(summonerName);
        } catch (MashapeApiException e) {
            throw new ApiException(e);
        }

        if (inProgressGameInfo == null || inProgressGameInfo.getGame() == null) {
            return null;
        } else {
            return new LolInProgressGame(inProgressGameInfo);
        }
    }

    // Riot Api - Champions free
    @Override
    public List<LolId> getFreeToPlayChampions() throws ApiException {
        rotateRiotkey();

        List<Champion> championList = null;
        try {
            championList = getRiotApi().getFreeToPlayChampions().getChampions();
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }

        checkNotNull(championList);

        List<LolId> freeToPlayChampions = new ArrayList<LolId>();
        for (Champion champion : championList) {
            freeToPlayChampions.add(new LolId(champion.getId()));
        }

        checkNotEmpty(freeToPlayChampions);

        return freeToPlayChampions;
    }

    // Riot Api - Summoner
    @Override
    public LolSummoner getSummonerByName(String summonerName)
        throws ApiException {
        if (summonerName == null) {
            throw new IllegalArgumentException("Summoner name is null");
        }
        if (summonerName.isEmpty()) {
            throw new IllegalArgumentException("Summoner name is empty");
        }
        rotateRiotkey();

        Map<String, Summoner> riotSummoners = null;
        try {
            riotSummoners = getRiotApi().getSummonerByName(summonerName);
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }

        checkNotNull(riotSummoners);
        checkNotEmpty(riotSummoners);

        return new LolSummoner(riotSummoners.get(summonerName.toLowerCase()
            .replaceAll("\\s+", "")));
    }

    // List of champions
    @Override
    public Map<LolId, LolChampionData> getDataChampionList()
        throws ApiException {
        return LolStaticDataApi.Champion.get();
    }

    public LolChampionData getDataChampionById(int id) throws ApiException {
        rotateRiotkey();

        dto.Static.Champion championData = null;
        try {
            championData =
                getRiotApi().getDataChampion(id, (String) null, (String) null,
                    true, ChampData.ALL);
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }
        checkNotNull(championData);

        return new LolChampionData(championData);
    }

    @Override
    public Map<LolId, LolLeagues> getLeaguesBySummonerIds(LolId... summonerIds)
        throws ApiException {
        rotateRiotkey();
        getRiotApi().setSeason(DEFAULT_SEASON);
        checkMaxSimultaneousRequest(summonerIds.length);

        long[] ids = new long[summonerIds.length];
        for (int i = 0; i < summonerIds.length; i++) {
            ids[i] = (long) summonerIds[i].getValue();
        }

        Map<String, List<League>> mapIdToLeagues;
        try {
            mapIdToLeagues = getRiotApi().getLeagueBySummoners(ids);
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }

        Map<LolId, LolLeagues> mapIdToLolLeagues =
            new HashMap<LolId, LolLeagues>();
        for (Entry<String, List<League>> entry : mapIdToLeagues.entrySet()) {
            LolId leagueId = new LolId(entry.getKey());
            LolLeagues leagues = new LolLeagues(entry.getValue());
            mapIdToLolLeagues.put(leagueId, leagues);
        }

        return mapIdToLolLeagues;
    }

    @Override
    public Map<LolId, LolRunePage> getRunesBySummonerIds(LolId... summonerIds)
        throws ApiException {
        rotateRiotkey();
        checkMaxSimultaneousRequest(summonerIds.length);

        long[] ids = new long[summonerIds.length];
        for (int i = 0; i < summonerIds.length; i++) {
            ids[i] = (long) summonerIds[i].getValue();
        }
        Map<String, RunePages> runes = new HashMap<String, RunePages>();
        RuneList runeList;
        try {
            runes = getRiotApi().getRunePages(ids);
            runeList = LolStaticDataApi.Rune.get();
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }
        checkNotNull(runes);
        checkNotNull(runeList);

        Map<String, Rune> allRunes = runeList.getData();
        Map<String, RunePage> currentRunes = new HashMap<String, RunePage>();

        for (Entry<String, RunePages> entry : runes.entrySet()) {
            for (RunePage page : entry.getValue().getPages()) {
                if (page.isCurrent()) {
                    currentRunes.put(entry.getKey(), page);
                }
            }
        }

        Map<LolId, LolRunePage> currentRunesBySummoner =
            new HashMap<LolId, LolRunePage>();
        for (Entry<String, RunePage> entry : currentRunes.entrySet()) {
            List<LolRune> playerRunes = new ArrayList<LolRune>();
            String name = "";
            if (entry.getValue() != null && entry.getValue().getSlots() != null) {
                name = entry.getValue().getName();
                for (RuneSlot runeSlot : entry.getValue().getSlots()) {
                    LolId runeId = new LolId(runeSlot.getRuneId());
                    playerRunes.add(new LolRune(allRunes.get(String
                        .valueOf(runeId.getValue()))));
                }
            }
            currentRunesBySummoner.put(new LolId(entry.getKey()),
                new LolRunePage(name, playerRunes));

        }

        return currentRunesBySummoner;

    }

    @Override
    public Map<LolId, LolMasteryPage> getMasteryPageBySummonerIds(
        LolId... summonerIds) throws ApiException {

        rotateRiotkey();
        checkMaxSimultaneousRequest(summonerIds.length);

        long[] ids = new long[summonerIds.length];
        for (int i = 0; i < summonerIds.length; i++) {
            ids[i] = (long) summonerIds[i].getValue();
        }
        Map<String, MasteryPages> masteries =
            new HashMap<String, MasteryPages>();
        MasteryTree masteryTree;
        try {
            masteries = getRiotApi().getMasteryPages(ids);
            masteryTree = LolStaticDataApi.Mastery.get().getTree();
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }
        checkNotNull(masteries);
        checkNotNull(masteryTree);

        LolMasteryTree lolMasteryTree = new LolMasteryTree(masteryTree);

        Map<LolId, LolMasteryPage> currentMasteries =
            new HashMap<LolId, LolMasteryPage>();

        for (Entry<String, MasteryPages> entry : masteries.entrySet()) {
            for (MasteryPage page : entry.getValue().getPages()) {
                if (page.isCurrent()) {
                    LolId id = new LolId(entry.getKey());
                    LolMasteryPage masteryPage =
                        new LolMasteryPage(page, lolMasteryTree);
                    currentMasteries.put(id, masteryPage);
                }
            }
        }

        return currentMasteries;

    }

    @Override
    public LolStats getStatsBySummonerId(LolId summonerId) throws ApiException {
        rotateRiotkey();

        PlayerStatsSummaryList allStats = null;
        try {
            allStats =
                getRiotApi().getPlayerStatsSummary(summonerId.getValue());
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }
        checkNotNull(allStats);

        return new LolStats(allStats);
    }

    @Override
    public Map<LolId, LolSummonerSpell> getSpells() throws ApiException {
        return LolStaticDataApi.Spell.get();
    }

    @Override
    public List<LolMatchSummary> getMatchHistoryBySummonerId(int summoneId,
        int beginIndex) throws ApiException {
        rotateRiotkey();

        int endIndex = beginIndex + MAX_MATCH_HISTORY_REQUEST;

        PlayerHistory ph = null;
        try {
            ph =
                getRiotApi().getMatchHistory(
                    Region.valueOf(getRegion().toUpperCase()), summoneId, null,
                    null, beginIndex, endIndex);
        } catch (RiotApiException e) {
            throw new ApiException(e);
        }

        List<MatchSummary> ms = ph.getMatches();
        List<LolMatchSummary> lms = new ArrayList<LolMatchSummary>();

        if (ms != null) {
            for (MatchSummary m : ms) {
                lms.add(new LolMatchSummary(m));
            }
        }
        return lms;
    }

    private void checkMaxSimultaneousRequest(int requestNumber) {
        if (requestNumber > MAX_SIMULTANEOUS_REQ) {
            throw new IllegalArgumentException(
                "Maximum simultaneous execution is " + MAX_SIMULTANEOUS_REQ
                    + ". Received: " + requestNumber);
        }
    }

    @Override
    public Map<LolId, LolItem> getItems() throws ApiException {
        return LolStaticDataApi.Item.get();
    }

    private void checkNotNull(Object object) throws ApiException {
        if (object == null) {
            throw new ApiException(DATA_UNAVAILABLE_ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("rawtypes")
    private void checkNotEmpty(Collection collection) throws ApiException {
        if (collection.isEmpty()) {
            throw new ApiException(DATA_UNAVAILABLE_ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("rawtypes")
    private void checkNotEmpty(Map map) throws ApiException {
        if (map.isEmpty()) {
            throw new ApiException(DATA_UNAVAILABLE_ERROR_MESSAGE);
        }
    }

}
