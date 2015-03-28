package ch.epfl.sweng.lolmaster.gui.currentgamefeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import ch.epfl.sweng.lolmaster.BuildConfig;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.LolApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolInProgressGame;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryPage;
import ch.epfl.sweng.lolmaster.api.dto.LolPlayer;
import ch.epfl.sweng.lolmaster.api.dto.LolRunePage;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.gui.LoadingTask;
import ch.epfl.sweng.lolmaster.gui.MainActivity;
import constant.Region;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class InGameActivity extends Activity {

    public static final String SUMMONER_NAME = "summonerName";
    public static final String REGION = "region";

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            KeyguardManager km =
                (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock =
                km.newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
            getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        Intent intent = getIntent();
        String summonerName = intent.getStringExtra(MainActivity.SUMMONER_NAME);
        String regionName = intent.getStringExtra(MainActivity.REGION);

        new DownloadDataTask(summonerName, regionName).execute(summonerName,
            regionName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.in_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @author ajjngeor (alain george : 217451)
     * 
     */
    private class DownloadDataTask extends
        LoadingTask<String, GameForSummonerWrapper> {
        private static final int TOTAL_REQ_NUMBER = 10;
        private String mSummonerName;
        private String mRegion;

        public DownloadDataTask(String summonerName, String region) {
            super(InGameActivity.this);
            mSummonerName = summonerName;
            mRegion = region;
            getProgressUpdater().setMaxUpdate(TOTAL_REQ_NUMBER);
        }

        @Override
        protected GameForSummonerWrapper doInBackground(String... args) {
            String summonerName = mSummonerName;
            String regionName = mRegion;

            LolApi api = LolApi.getApi();
            api.setRegion(Region.valueOf(regionName.toUpperCase()));

            LolInProgressGame gameInProgress = null;
            GameForSummonerWrapper currentGame = null;

            try {
                gameInProgress = api.getInProgessGame(summonerName);

                if (gameInProgress == null) {
                    cancel("Unable to find game for player " + summonerName);
                    return null;
                }

                currentGame =
                    new GameForSummonerWrapper(gameInProgress, summonerName);
                setTeamsChampion(currentGame.getAlliedTeam(),
                    currentGame.getEnemyTeam(), api, gameInProgress);
                setTeamsRank(currentGame.getAlliedTeam(),
                    currentGame.getEnemyTeam(), api);
                setTeamsStats(currentGame.getAlliedTeam(),
                    currentGame.getEnemyTeam(), api);
                setTeamsSpells(currentGame.getAlliedTeam(),
                    currentGame.getEnemyTeam(), api, gameInProgress);
                setTeamsRunes(currentGame.getAlliedTeam(),
                    currentGame.getEnemyTeam(), api);
                setTeamsMasteries(currentGame.getAlliedTeam(),
                    currentGame.getEnemyTeam(), api);

            } catch (ApiException e) {
                Log.e(this.getClass().getName(), e.getMessage(), e);
                cancel(e.getMessage());
                return null;
            }

            return currentGame;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(GameForSummonerWrapper wrapper) {
            if (wrapper == null) {
                finish();
                return;
            }

            setContentView(R.layout.activity_in_game);
            InGamePlayerAdapter alliedInGamePlayerAdapter =
                new InGamePlayerAdapter(InGameActivity.this,
                    wrapper.getAlliedTeam(), mRegion);
            InGamePlayerAdapter enemyInGamePlayerAdapter =
                new InGamePlayerAdapter(InGameActivity.this,
                    wrapper.getEnemyTeam(), mRegion);

            ListView alliedListView =
                (ListView) findViewById(R.id.allied_team_listview);
            ListView enemyListView =
                (ListView) findViewById(R.id.enemy_team_listview);

            alliedListView.setAdapter(alliedInGamePlayerAdapter);
            enemyListView.setAdapter(enemyInGamePlayerAdapter);
        }

        private void setTeamsChampion(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam, LolApi api, LolInProgressGame game)
            throws ApiException {

            Map<LolId, LolChampionData> championsData =
                api.getDataChampionList();

            for (InGamePlayer player : alliedTeam) {
                LolId championId =
                    game.getChampionByPlayer(player.getInternalName());
                player.setChampion(championsData.get(championId));
            }
            for (InGamePlayer player : enemyTeam) {
                LolId championId =
                    game.getChampionByPlayer(player.getInternalName());
                player.setChampion(championsData.get(championId));
            }
        }

        private void setTeamsRank(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam, LolApi api) throws ApiException {

            LolId[] ids = groupIds(alliedTeam, enemyTeam);

            Map<LolId, LolLeagues> mapIdToLolLeagues =
                api.getLeaguesBySummonerIds(ids);

            for (InGamePlayer player : alliedTeam) {
                player.setLeagues(mapIdToLolLeagues.get(player.getId()));
            }
            for (InGamePlayer player : enemyTeam) {
                player.setLeagues(mapIdToLolLeagues.get(player.getId()));
            }
        }

        private void setTeamsStats(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam, LolApi api) throws ApiException {

            for (InGamePlayer player : alliedTeam) {
                player.setPlayerStats(api.getStatsBySummonerId(player.getId()));
                getProgressUpdater().update(player.getName());
            }
            for (InGamePlayer player : enemyTeam) {
                player.setPlayerStats(api.getStatsBySummonerId(player.getId()));
                getProgressUpdater().update(player.getName());
            }
        }

        private void setTeamsSpells(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam, LolApi api, LolInProgressGame game)
            throws ApiException {
            Map<LolId, LolSummonerSpell> spellById = api.getSpells();

            for (InGamePlayer player : alliedTeam) {
                LolId spell1Id =
                    game.getChampionSelectionByPlayer(player.getInternalName())
                        .getSpell1Id();
                player.setSpell1(spellById.get(spell1Id));
                LolId spell2Id =
                    game.getChampionSelectionByPlayer(player.getInternalName())
                        .getSpell2Id();
                player.setSpell2(spellById.get(spell2Id));
            }
            for (InGamePlayer player : enemyTeam) {
                LolId spell1Id =
                    game.getChampionSelectionByPlayer(player.getInternalName())
                        .getSpell1Id();
                player.setSpell1(spellById.get(spell1Id));
                LolId spell2Id =
                    game.getChampionSelectionByPlayer(player.getInternalName())
                        .getSpell2Id();
                player.setSpell2(spellById.get(spell2Id));
            }
        }

        private void setTeamsRunes(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam, LolApi api) throws ApiException {
            LolId[] ids = groupIds(alliedTeam, enemyTeam);

            Map<LolId, LolRunePage> runePages = api.getRunesBySummonerIds(ids);
            for (InGamePlayer player : alliedTeam) {
                player.setRunes(runePages.get(player.getId()));
            }
            for (InGamePlayer player : enemyTeam) {
                player.setRunes(runePages.get(player.getId()));
            }
        }

        private void setTeamsMasteries(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam, LolApi api) throws ApiException {
            LolId[] ids = groupIds(alliedTeam, enemyTeam);
            Map<LolId, LolMasteryPage> masteryPages =
                api.getMasteryPageBySummonerIds(ids);
            for (InGamePlayer player : alliedTeam) {
                player.setMasteries(masteryPages.get(player.getId()));
            }
            for (InGamePlayer player : enemyTeam) {
                player.setMasteries(masteryPages.get(player.getId()));
            }
        }

        private LolId[] groupIds(List<InGamePlayer> alliedTeam,
            List<InGamePlayer> enemyTeam) {
            LolId[] ids = new LolId[alliedTeam.size() + enemyTeam.size()];

            int i = 0;
            for (InGamePlayer player : alliedTeam) {
                ids[i] = player.getId();
                i++;
            }
            for (InGamePlayer player : enemyTeam) {
                ids[i] = player.getId();
                i++;
            }
            return ids;
        }
    }

    /**
     * A wrapper for a {@see LolInProgressGame} and a
     * {@code String summonerName}
     * 
     * @author fKunstner
     */
    private static class GameForSummonerWrapper {
        private final String summonerName;

        private List<InGamePlayer> mAlliedTeam = new ArrayList<InGamePlayer>();
        private List<InGamePlayer> mEnemyTeam = new ArrayList<InGamePlayer>();

        public GameForSummonerWrapper(LolInProgressGame game, String name) {
            summonerName = name;
            for (LolPlayer player : game.getTeamOfPlayer(summonerName)
                .getPlayers()) {
                mAlliedTeam.add(new InGamePlayer(player));
            }
            for (LolPlayer player : game.getEnemyTeamOfPlayer(summonerName)
                .getPlayers()) {
                mEnemyTeam.add(new InGamePlayer(player));
            }
        }
        
        /**
         * get allied team of the current game
         * @return allied team of the current game
         */
        public List<InGamePlayer> getAlliedTeam() {
            return mAlliedTeam;
        }

        /**
         * get enemy team of the current game
         * @return enemy team of the current game
         */
        public List<InGamePlayer> getEnemyTeam() {
            return mEnemyTeam;
        }
    }
}
