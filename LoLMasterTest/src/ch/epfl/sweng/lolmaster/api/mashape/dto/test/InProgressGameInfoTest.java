package ch.epfl.sweng.lolmaster.api.mashape.dto.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.test.InstrumentationTestCase;
import ch.epfl.sweng.lolmaster.api.dto.LolTeam.TeamId;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.BannedChampion;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.ChampionSelection;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.Player;
import ch.epfl.sweng.lolmaster.testing.JsonAssetsReader;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

/**
 * Tests for the InProgressGameInfo DTO class
 * 
 * @author fKunstner
 */
public class InProgressGameInfoTest extends InstrumentationTestCase {
    private static final String DEFAULT_IN_PROGRESS_GAME_INFO_FILE =
        "MashapeApi_GetInProgressGame.json";
    private static final int MAX_NUM_PLAYERS = 10;
    private static final int NUM_PLAYER_TEAM = 5;
    private static final int NUM_BANNED_CHAMPION = 6;
    private static final Set<Long> SUMMONER_IDS_TEAM1 = new HashSet<Long>(
        Arrays.asList((long) 18996410, (long) 27666623, (long) 20621725,
            (long) 19645958, (long) 20669690));
    private static final Set<Long> SUMMONER_IDS_TEAM2 = new HashSet<Long>(
        Arrays.asList((long) 19251660, (long) 19349121, (long) 268667,
            (long) 23241039, (long) 21556668));
    private static final Set<Integer> BANNED_TEAM_ONE = new HashSet<Integer>(
        Arrays.asList(150, 39, 121));
    private static final Set<Integer> BANNED_TEAM_TWO = new HashSet<Integer>(
        Arrays.asList(126, 238, 84));
    private static final int[] SPELLS_1 = {4, 7, 4, 4, 4, 3, 4, 4, 14, 11 };
    private static final int[] SPELLS_2 = {7, 4, 12, 3, 7, 4, 12, 11, 4, 4 };
    private static final int[] CHAMPS_ID = {101, 236, 92, 40, 42, 412, 76, 59,
        61, 64 };
    private static final String[] SUMMONER_NAMES = {"fizzonyajayce",
        "ntdarkstar", "vulca", "robzornlft", "kingsenpai", "bolqrcheto",
        "aesenar", "oceethemastermnd", "changename", "fendris" };
    private List<ImmutableMap<String, ImmutableMap<String, Integer>>> mChampionSelection =
        initChampionSelection();

    private String mJson;
    private InProgressGameInfo mInProgressGameInfo;
    private InProgressGame mGame;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mJson =
            JsonAssetsReader.getFileContent(getInstrumentation().getContext(),
                DEFAULT_IN_PROGRESS_GAME_INFO_FILE);
        mInProgressGameInfo =
            new Gson().fromJson(mJson, InProgressGameInfo.class);
        mGame = mInProgressGameInfo.getGame();
    }

    private List<ImmutableMap<String, ImmutableMap<String, Integer>>> initChampionSelection() {
        List<ImmutableMap<String, ImmutableMap<String, Integer>>> selection =
            new ArrayList<ImmutableMap<String, ImmutableMap<String, Integer>>>();

        for (int i = 0; i < MAX_NUM_PLAYERS; i++) {
            ImmutableMap<String, Integer> champInfo =
                ImmutableMap.of("spell1", SPELLS_1[i], "spell2", SPELLS_2[i],
                    "champId", CHAMPS_ID[i]);
            selection.add(ImmutableMap.of(SUMMONER_NAMES[i], champInfo));
        }
        return selection;
    }

    public void testInProgressGameInfo() {
        assertNotNull(mInProgressGameInfo.getGame());
    }

    public void testInProgressGame() {
        assertEquals("CLASSIC", mGame.getGameMode());
        assertEquals("RANKED_GAME", mGame.getGameType());
        assertEquals(MAX_NUM_PLAYERS, mGame.getMaxNumPlayer());
        assertEquals("RANKED_SOLO_5x5", mGame.getQueueTypeName());
    }

    public void testTeam() {
        assertFalse(mGame.getTeamOne().isEmpty());
        assertTrue(mGame.getTeamOne().size() == NUM_PLAYER_TEAM);
        assertFalse(mGame.getTeamTwo().isEmpty());
        assertTrue(mGame.getTeamTwo().size() == NUM_PLAYER_TEAM);
    }

    public void testPlayer() {
        testPlayersTeamOne();
        testPlayersTeamTwo();
    }

    private void testPlayersTeamOne() {
        List<Player> team = mGame.getTeamOne();
        Set<String> summonerInternalNames =
            new HashSet<String>(Arrays.asList("aesenar", "changename",
                "ntdarkstar", "bolqrcheto", "oceethemastermnd"));
        Set<String> summonerNames =
            new HashSet<String>(Arrays.asList("Aesenar", "ChangeName",
                "nT Darkstar", "BoLqRcHeTo", "OCEETHEMASTERMND"));
        for (Player p : team) {
            assertTrue("Player " + p.getSummonerId() + " unknown in team one",
                SUMMONER_IDS_TEAM1.contains(p.getSummonerId()));
            assertTrue("Player " + p.getSummonerInternalName()
                + " unknown in team one",
                summonerInternalNames.contains(p.getSummonerInternalName()));
            assertTrue(
                "Player " + p.getSummonerName() + " unknown in team one",
                summonerNames.contains(p.getSummonerName()));
        }
    }

    private void testPlayersTeamTwo() {
        List<Player> team = mGame.getTeamTwo();
        Set<String> summonerInternalNames =
            new HashSet<String>(Arrays.asList("robzornlft", "fizzonyajayce",
                "kingsenpai", "vulca", "fendris"));
        Set<String> summonerNames =
            new HashSet<String>(Arrays.asList("Robzorn LFT",
                "Fizz on ya Jayce", "King Senpai", "Vulca", "Fendris"));
        for (Player p : team) {
            assertTrue("Player " + p.getSummonerId() + " unknown in team two",
                SUMMONER_IDS_TEAM2.contains(p.getSummonerId()));
            assertTrue("Player " + p.getSummonerInternalName()
                + " unknown in team two",
                summonerInternalNames.contains(p.getSummonerInternalName()));
            assertTrue(
                "Player " + p.getSummonerName() + " unknown in team two",
                summonerNames.contains(p.getSummonerName()));
        }
    }

    public void testBannedChampions() {
        assertFalse(mGame.getBannedChampions().isEmpty());
        assertTrue(mGame.getBannedChampions().size() == NUM_BANNED_CHAMPION);
    }

    public void testBannedChampion() {
        List<BannedChampion> bannedChampions = mGame.getBannedChampions();

        for (BannedChampion bc : bannedChampions) {
            if (bc.getTeamId() == TeamId.TEAM1.getId()) {
                assertTrue(BANNED_TEAM_ONE.contains(bc.getChampionId()));
            } else if (bc.getTeamId() == TeamId.TEAM2.getId()) {
                assertTrue(BANNED_TEAM_TWO.contains(bc.getChampionId()));
            } else {
                fail();
            }
        }

    }

    public void testChampionSelections() {
        assertFalse(mGame.getChampionSelections().isEmpty());
        assertTrue(mGame.getChampionSelections().size() == MAX_NUM_PLAYERS);
    }

    public void testChampionSelection() {
        List<ChampionSelection> championSelections =
            mGame.getChampionSelections();

        for (int i = 0; i < mChampionSelection.size(); i++) {
            assertTrue(mChampionSelection.get(i).containsKey(
                championSelections.get(i).getSummonerInternalName()));
            ImmutableMap<String, Integer> refValues =
                mChampionSelection.get(i).get(
                    championSelections.get(i).getSummonerInternalName());

            assertEquals((int) refValues.get("spell1"),
                championSelections.get(i).getSpell1Id());
            assertEquals((int) refValues.get("spell2"),
                championSelections.get(i).getSpell2Id());
            assertEquals((int) refValues.get("champId"), championSelections
                .get(i).getChampionId());
        }
    }
}
