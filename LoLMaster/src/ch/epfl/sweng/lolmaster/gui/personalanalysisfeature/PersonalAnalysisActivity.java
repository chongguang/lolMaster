package ch.epfl.sweng.lolmaster.gui.personalanalysisfeature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.lolmaster.BuildConfig;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;
import ch.epfl.sweng.lolmaster.gui.MainActivity;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement.MatchSummaryAdapter;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement.MatchSummaryUIModel;
import dto.Static.Image;

/**
 * @author lcg31439
 * 
 */
public class PersonalAnalysisActivity extends Activity {

    private String summonerName;
    private String regionName;
    private List<MatchSummaryUIModel> historyData;

    private Map<LolId, LolChampionData> champions;

    public static final String DEFAUT_CHAMPION_NAME_INPUT = "All";
    public static final int PRECISION = 100;
    private static final int ITEM_IMAGE_SIZE_RATIO = 11;
    private static final double KDA_TEXT_SIZE_RATIO = 4;

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

        historyData = new ArrayList<MatchSummaryUIModel>();
        champions = new HashMap<LolId, LolChampionData>();
        summonerName = intent.getStringExtra(MainActivity.SUMMONER_NAME);
        regionName = intent.getStringExtra(MainActivity.REGION);
        setContentView(R.layout.loading_screen);
        new DownloadDataTask(this, summonerName, regionName).execute();
    }

    /**
     * Start the activity by showing the pop up
     * 
     * @param view
     */
    public void startStatisticActivity(View view) {
        showStatisticPopUp();
    }

    /**
     * This is the pop up for user to specify champion and dates for statistic
     * analysis
     */
    private void showStatisticPopUp() {

        // Create the pop up and set title
        AlertDialog.Builder helpBuilder =
            new AlertDialog.Builder(PersonalAnalysisActivity.this);
        helpBuilder.setTitle(R.string.statistic_pop_up_title);

        // Create a text input with auto-completion pour specify champion name
        final AutoCompleteTextView championInput =
            new AutoCompleteTextView(PersonalAnalysisActivity.this);
        championInput.setId(R.id.summoner_name_analysis_input);

        List<LolChampionData> allChampions =
            new ArrayList<LolChampionData>(
                PersonalAnalysisActivity.this.champions.values());
        List<String> allChampionNames = new ArrayList<String>();
        for (LolChampionData c : allChampions) {
            allChampionNames.add(c.getName());
        }
        String[] championNameArray = new String[allChampionNames.size() + 1];
        championNameArray = allChampionNames.toArray(championNameArray);
        championNameArray[allChampionNames.size()] = DEFAUT_CHAMPION_NAME_INPUT;

        ArrayAdapter<String> adapter =
            new ArrayAdapter<String>(PersonalAnalysisActivity.this,
                android.R.layout.simple_list_item_1, championNameArray);

        championInput.setAdapter(adapter);
        championInput.setHint(DEFAUT_CHAMPION_NAME_INPUT);
        championInput.setEms(TRIM_MEMORY_BACKGROUND);
        // Create a label
        TextView championNameLabel =
            new TextView(PersonalAnalysisActivity.this);
        championNameLabel.setText(R.string.statistic_champion_name_input_label);

        // Create the layout to include the label and the text input
        LinearLayout globalLayout =
            new LinearLayout(PersonalAnalysisActivity.this);
        globalLayout.setOrientation(LinearLayout.VERTICAL);
        globalLayout.addView(championNameLabel);
        globalLayout.addView(championInput);
        globalLayout.setHorizontalGravity(TRIM_MEMORY_BACKGROUND);
        helpBuilder.setView(globalLayout);

        // Create date pickers
        final EditText beginDate = new EditText(PersonalAnalysisActivity.this);
        final Calendar beginCalendar = Calendar.getInstance();
        beginDate.setHint(dateToString(beginCalendar.getTime()));
        setUpDatepicker(beginDate, beginCalendar);
        beginDate.setFocusable(false);
        TextView beginDateLabel = new TextView(PersonalAnalysisActivity.this);
        beginDateLabel.setText(R.string.statistic_begin_time_input_label);

        final EditText endDate = new EditText(PersonalAnalysisActivity.this);
        final Calendar endCalendar = Calendar.getInstance();
        endDate.setHint(dateToString(endCalendar.getTime()));
        setUpDatepicker(endDate, endCalendar);
        endDate.setFocusable(false);
        TextView endDateLabel = new TextView(PersonalAnalysisActivity.this);
        endDateLabel.setText(R.string.statistic_end_time_input_label);

        globalLayout.addView(beginDateLabel);
        globalLayout.addView(beginDate);
        globalLayout.addView(endDateLabel);
        globalLayout.addView(endDate);

        helpBuilder.setView(globalLayout);

        // Create the button of analysis
        helpBuilder.setPositiveButton(R.string.statistic_pop_up_analyse_button,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Calendar beginCal = Calendar.getInstance();
                    beginCal.set(beginCalendar.get(Calendar.YEAR),
                        beginCalendar.get(Calendar.MONTH),
                        beginCalendar.get(Calendar.DAY_OF_MONTH));

                    Calendar endCal = Calendar.getInstance();
                    endCal.set(endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH));

                    Date beginDate = beginCal.getTime();
                    Date endDate = endCal.getTime();
                    String championName = championInput.getText().toString();

                    AlertDialog.Builder analysisResultBuilder =
                        new AlertDialog.Builder(PersonalAnalysisActivity.this);
                    TextView result =
                        new TextView(PersonalAnalysisActivity.this);
                    result.setText(statisticAnalysis(championName, beginDate,
                        endDate));
                    analysisResultBuilder.setView(result);
                    analysisResultBuilder
                        .setTitle(R.string.statistic_analysis_result_title);
                    analysisResultBuilder.setPositiveButton(
                        R.string.statistic_analysis_result_close_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                int which) {
                                // Do nothing, just close the pop up
                            }
                        });
                    analysisResultBuilder.show();
                }
            });

        // Create the button of cancel
        helpBuilder.setNegativeButton(R.string.statistic_pop_up_cancle_button,
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

    /**
     * Function to convert an object of date to string
     * 
     * @param d
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private String dateToString(Date d) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(d);
    }

    /**
     * This is where we do the statistic analysis
     * 
     * @param championName
     * @param beginDate
     * @param endDate
     * @return
     */
    private String statisticAnalysis(String championName, Date beginDate,
        Date endDate) {
        List<MatchSummaryUIModel> filteredHistory =
            new ArrayList<MatchSummaryUIModel>();
        int championId = -1;

        // If the user didn't enter a name or entered "all", we will analysis
        // all the champions
        // otherwise we find the champion's id by searching in the static
        // champion data list
        if ("".equals(championName) || "all".equalsIgnoreCase(championName)) {
            championId = 0;
        } else {
            for (Entry<LolId, LolChampionData> entry : champions.entrySet()) {
                if (entry.getValue().getName().equals(championName)) {
                    championId = entry.getKey().getValue();
                }
            }
        }
        if (championId < 0) {
            return "Can't find champion " + championName
                + ". Please enter the name correctly.";
        }

        if (endDate.compareTo(beginDate) <= 0) {
            return "Begin time should be smaller than end time.";
        }

        // Filtering all the match history to find those that satisfies the
        // user's input
        for (MatchSummaryUIModel m : historyData) {
            long creationLong = m.getMatch().getMatchCreation();
            Date creation = new Date(creationLong);
            if ((m.getMatch().getChampionId() == championId || championId == 0)
                && creation.compareTo(endDate) <= 0
                && beginDate.compareTo(creation) <= 0) {
                filteredHistory.add(m);
            }
        }

        return doTheAnalysis(filteredHistory, beginDate, endDate, championName,
            championId);
    }

    /**
     * Function for constructing the statistic output string
     * 
     * @param history
     * @param beginDate
     * @param endDate
     * @param championName
     * @param championId
     * @return
     */
    private String doTheAnalysis(List<MatchSummaryUIModel> history,
        Date beginDate, Date endDate, String championName, int championId) {

        long totalkill = 0;
        long totalDeath = 0;
        long totalAssist = 0;
        long totalGoldEarned = 0;
        for (MatchSummaryUIModel m : history) {
            totalkill += m.getMatch().getKills();
            totalDeath += m.getMatch().getDeaths();
            totalAssist += m.getMatch().getAssists();
            totalGoldEarned += m.getMatch().getGoldEarned();
        }

        double averageKDA =
            totalDeath == 0 ? totalAssist + totalkill
                : (double) (totalAssist + totalkill) / (double) totalDeath;
        averageKDA = (double) Math.round(averageKDA * PRECISION) / PRECISION;

        double averageGold = (double) totalGoldEarned / (double) history.size();
        averageGold = (double) Math.round(averageGold * PRECISION) / PRECISION;

        String result =
            "Between " + dateToString(beginDate) + " and "
                + dateToString(endDate);
        result += "\nYou played " + history.size() + " RANKED matches";
        if (championId != 0) {
            result += " on " + championName;
        }
        result += "\nThe average K/D/A ratio is " + averageKDA;
        result += "\nThe average gold earned is " + averageGold;
        return result;
    }

    /**
     * Configuration of date pickers on the pop up of statistic user inputs
     * 
     * @param dateView
     * @param myCalendar
     */
    private void setUpDatepicker(final EditText dateView,
        final Calendar myCalendar) {
        final DatePickerDialog.OnDateSetListener date =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateView.setText(dateToString(myCalendar.getTime()));
                }
            };

        dateView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PersonalAnalysisActivity.this, date,
                    myCalendar.get(Calendar.YEAR), myCalendar
                        .get(Calendar.MONTH), myCalendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * This is an asyncTask to execute the Riot query and DB query
     * 
     * @author lcg31439
     */
    private class DownloadDataTask extends MatchHistoryAsyncTask {

        public DownloadDataTask(Activity activity, String theSummonerName,
            String region) {
            super(activity, theSummonerName, region);
        }

        @Override
        protected void onPostExecute(List<MatchSummaryUIModel> param) {
        	historyData = param;

            if (historyData == null || historyData.isEmpty()) {
                String errorMessage =
                    "No match history found for user "
                        + getRequestedSummoner()
                        + ". (Match history is only available for ranked players)";
                Toast.makeText(PersonalAnalysisActivity.this, errorMessage,
                    Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            PersonalAnalysisActivity.this.champions = getChampionCache();
            setContentView(R.layout.activity_personal_analysis);

            ListView listView =
                (ListView) findViewById(R.id.personal_analysis_list);

            // Show the user's match histories
            MatchSummaryAdapter adapter =
                new MatchSummaryAdapter(PersonalAnalysisActivity.this,
                    android.R.layout.simple_list_item_1, historyData);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                    matchDetailPopUp((MatchSummaryUIModel) parent
                        .getItemAtPosition(position));
                }
            });

        }

        /**
         * Private function to show details of the match when click on a row of
         * the list
         * 
         * @param data
         */
        @SuppressLint("InflateParams")
        private void matchDetailPopUp(MatchSummaryUIModel data) {
            AlertDialog.Builder helpBuilder =
                new AlertDialog.Builder(PersonalAnalysisActivity.this);
            helpBuilder.setTitle(R.string.match_history_pop_up_title);

            LolItem[] theItems = data.getItems();

            Drawable[] itemIcons = new Drawable[theItems.length];
            for (int i = 0; i < theItems.length; i++) {
                if (theItems[i] != null) {
                    Image image = theItems[i].getImageData();
                    itemIcons[i] =
                        ImageFetcher.getImage(getBaseContext(),
                            image.getGroup(), image.getFull());
                } else {
                    itemIcons[i] =
                        ImageFetcher.getEmptyItemSlotImage(getBaseContext());
                }
            }

            View globalView =
                getLayoutInflater().inflate(R.layout.match_summary_items, null);
            ImageView[] itemIconViews = new ImageView[theItems.length];

            int[] viewId =            		
            {R.id.matchSummaryItem1, R.id.matchSummaryItem2,
                R.id.matchSummaryItem3, R.id.matchSummaryItem4,
                R.id.matchSummaryItem5, R.id.matchSummaryItem6,
                R.id.matchSummaryItem7 };

            int itemImageSize = getItemImageSize();
            for (int i = 0; i < viewId.length; i++) {
                itemIconViews[i] =
                    (ImageView) globalView.findViewById(viewId[i]);
                itemIconViews[i].setImageDrawable(itemIcons[i]);
				itemIconViews[i].setLayoutParams(new LinearLayout.LayoutParams(
					itemImageSize, itemImageSize));
            }

            TextView kdaTextView =
                (TextView) globalView.findViewById(R.id.kdaTextView);
            kdaTextView.setText(data.kdaInfo());
            kdaTextView
                .setTextSize((float) (itemImageSize / KDA_TEXT_SIZE_RATIO));

            helpBuilder.setView(globalView);
            AlertDialog helpDialog = helpBuilder.create();
            helpDialog.show();

        }

        private int getItemImageSize() {
            WindowManager wm =
                (WindowManager) PersonalAnalysisActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x / ITEM_IMAGE_SIZE_RATIO;

            return width;
        }
    }
}
