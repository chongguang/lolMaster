package ch.epfl.sweng.lolmaster.gui;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import ch.epfl.sweng.lolmaster.BuildConfig;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.assetsmanagers.UserPreferences;
import ch.epfl.sweng.lolmaster.gui.currentgamefeature.InGameActivity;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.OutOfGameActivity;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.PersonalAnalysisActivity;
import constant.Region;

/**
 * @author lcg31439
 * 
 */
public class MainActivity extends Activity {
    public final static String SUMMONER_NAME = "summonerName";
    public final static String REGION = "region";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = km
                    .newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
            getWindow()
                    .addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        setContentView(R.layout.activity_main);
    }

    /**
     * This is the function to start the feature of current game help. It uses a
     * pop-up to get the user's summoner name and region. The current game help
     * will show basic information of all the 10 players.
     * 
     * @param view
     */
    public void startInGameActivity(View view) {
        showPopUp(InGameActivity.class);
    }

    /**
     * The out of game help provides information of champions.
     * 
     * @param view
     */
    public void startOutOfGameActivity(View view) {
        Intent inGameActivityIntent = new Intent(MainActivity.this,
                OutOfGameActivity.class);
        startActivity(inGameActivityIntent);
    }

    /**
     * This is the function to start the feature of personal analysis. It uses a
     * pop-up to get the user's summoner name and region. The personal analysis
     * will provide statistic results based on the player's match history.
     * 
     * @param view
     */
    public void startPersonalAnalysisActivity(View view) {
        showPopUp(PersonalAnalysisActivity.class);
    }

    /**
     * This function creates a pop-up to get the player's summoner name and
     * region and then pass them to other activities.
     * 
     * @param activity
     */
    private void showPopUp(final Class<? extends Activity> activity) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle(R.string.popup_title);

        // Create the the EditText view for summoner name
        final EditText summonerNameInput = new EditText(this);
        summonerNameInput.setHint(R.string.popup_summoner_name_hint);
        summonerNameInput.setId(R.id.summoner_name_input);
        summonerNameInput.setSingleLine();

        // Get the list of regions
        List<String> regions = new ArrayList<String>();
        for (Region r : EnumSet.allOf(Region.class)) {

            // Global should not be an option - Only queried for data.
            if (!r.equals(Region.GLOBAL)) {
                regions.add(r.getName().toUpperCase());
            }
        }

        // Create the drop-down list of regions
        final Spinner regionsDropDownList = new Spinner(this);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, regions);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        regionsDropDownList.setAdapter(dataAdapter);

        // SET DEFAULTS
        UserPreferences userPreferences = UserPreferences
                .getUserPreferences(getApplicationContext());

        int defaultRegionPos = dataAdapter.getPosition(userPreferences
                .getRegion().name());
        regionsDropDownList.setSelection(defaultRegionPos);
        summonerNameInput.setText(userPreferences.getUserName());

        // Create the layout to include the 2 views
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(summonerNameInput);
        ll.addView(regionsDropDownList);
        helpBuilder.setView(ll);

        // Create the button of search
        helpBuilder.setPositiveButton(R.string.popup_search_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String summonerName = summonerNameInput.getText()
                                .toString();
                        String sanitizedName = summonerName.toLowerCase()
                                .replaceAll("\\s+", "");

                        if (sanitizedName.isEmpty()) {
                            String message = getString(R.string.error_no_summoner_name);
                            Toast.makeText(getApplicationContext(), message,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Intent intent = new Intent(MainActivity.this, activity);

                        Region region = Region.valueOf(regionsDropDownList
                                .getSelectedItem().toString().toUpperCase());

                        intent.putExtra(SUMMONER_NAME, sanitizedName);
                        intent.putExtra(REGION, region.name());

                        UserPreferences.storeUserPreferences(
                                getApplicationContext(), summonerName, region);

                        startActivity(intent);
                    }
                });

        // Create the button of cancel
        helpBuilder.setNegativeButton(R.string.popup_cancel_button,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });

        // Create the view of the pop-up
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }

}