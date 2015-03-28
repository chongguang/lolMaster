package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.LolApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolImage;
import ch.epfl.sweng.lolmaster.gui.LoadingTask;
import ch.epfl.sweng.lolmaster.gui.MainActivity;

/**
 * Activity that displays the list of champions, and opens a new activity
 * whenever the user click on one champion
 * 
 * @author ygrimault
 * 
 */
public class OutOfGameActivity extends Activity {

    private Map<LolId, LolChampionData> championDataListById = null;

    /**
     * Get championMap from API, store in Map, and display
     * 
     * @author lifei
     * @param
     */
    public class DownloadDataTask extends LoadingTask<Void, Map<String, LolId>> {

        public DownloadDataTask() {
            super(OutOfGameActivity.this);
        }

        @Override
        protected Map<String, LolId> doInBackground(Void... noArgs) {

            try {
                LolApi api = LolApi.getApi();
                championDataListById = api.getDataChampionList();
            } catch (ApiException e) {
                Log.e(this.getClass().getName(), e.getMessage(), e);
                cancel(e.getMessage());
                return null;
            }
            return dataRemaping(championDataListById);
        }

        /**
         * Displays the list of champions in a GridView
         */
        protected void onPostExecute(Map<String, LolId> dataList) {
            if (dataList == null || dataList.isEmpty()) {
                finish();
                return;
            }

            setContentView(R.layout.activity_out_of_game);
            setChampionList(dataList);
        }

    }

    /**
     * We only need the ID and the name of the champion. However, I change the
     * order in order to be able to sort it later.
     * 
     * @param lolChampionDataById
     * @return
     */
    private Map<String, LolId> dataRemaping(
        Map<LolId, LolChampionData> lolChampionDataById) {
        Map<String, LolId> lolChampionIds = new HashMap<String, LolId>();
        LolChampionData champData;
        String champName;

        if (lolChampionDataById != null) {
            for (Entry<LolId, LolChampionData> entry : lolChampionDataById
                .entrySet()) {
                champData = entry.getValue();
                champName = champData.getName();
                lolChampionIds.put(champName, entry.getKey());
            }
        } else {
            String errorMessage =
                "There seems to be an error with the champions' list.";
            Log.w(OutOfGameChampion.class.getName(), errorMessage);
        }
        return lolChampionIds;
    }

    private void setChampionList(final Map<String, LolId> championNamesList) {

        if (championNamesList == null) {
            String errorMessage =
                "There seems to be an error with the champions' list.";
            Log.w(OutOfGameChampion.class.getName(), errorMessage);
            Intent mainActivityIntent =
                new Intent(OutOfGameActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            return;
        }

        // First we get the GridView
        GridView gridChampionLayout =
            (GridView) findViewById(R.id.outOfGameLayout);

        // Then, we need to create a list of names so it displays the views in
        // the order we want
        List<String> championNames = new ArrayList<String>();
        for (String key : championNamesList.keySet()) {
            championNames.add(key);
        }
        Collections.sort(championNames);
        List<OutOfGameChampion> listChampion =
            new ArrayList<OutOfGameChampion>();
        for (String championName : championNames) {
            LolId champId = championNamesList.get(championName);
            LolImage champImage = championDataListById.get(champId).getImage();
            listChampion.add(new OutOfGameChampion(championName, champId,
                champImage));
        }
        gridChampionLayout.setAdapter(new ChampionAdapter(
            OutOfGameActivity.this, listChampion));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        new DownloadDataTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.out_of_game, menu);
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

}
