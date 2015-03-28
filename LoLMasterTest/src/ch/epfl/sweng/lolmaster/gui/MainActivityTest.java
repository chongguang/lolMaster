package ch.epfl.sweng.lolmaster.gui;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.doesNotExist;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ch.epfl.sweng.lolmaster.R;

import com.google.android.apps.common.testing.ui.espresso.Espresso;

/**
 * @author Lifei 
 * black box UI test
 */
public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    /**
     * @param activityClass
     */
    //private MainActivity mMainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(false);
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        getActivity();
    }

    protected void tearDown() throws Exception {
    	goBackN();
        super.tearDown();
    }

    
    // UI test of InGame Button (mock input and cancel)
    public void testInGameUi() throws InterruptedException {

        onView(withId(R.id.inGameButton)).perform(click());
        onView(withId(R.id.summoner_name_input)).check(matches(isDisplayed()));
        
        // mock input
        onView(withId(R.id.summoner_name_input)).perform(clearText()).perform(typeText("Jagage"));

        onView(withId(R.id.summoner_name_input)).check(
                matches(withText("Jagage")));

        // mock cancel
        onView(withText(R.string.popup_cancel_button)).perform(click());
        onView(withId(R.id.summoner_name_input)).check(doesNotExist());
        
    }

    // test InGameActivity can return to mainActivity when input User not in game
    public void testUserNotInGame() throws InterruptedException {

        onView(withId(R.id.inGameButton)).perform(click());
        
        // mock input user not in game
        onView(withId(R.id.summoner_name_input)).perform(clearText()).perform(typeText("clfei"));

        onView(withText(R.string.popup_search_button)).perform(click());

        // check whether we can return to Main activity when input User not in game
        onView(withId(R.id.analysisButton)).check(matches(isDisplayed()));
        
        //test whether previous name is remembered
        onView(withId(R.id.inGameButton)).perform(click());
        onView(withId(R.id.summoner_name_input)).check(
                matches(withText("clfei")));
    }
    
    // test InGameActivity can return to mainActivity when input Invalid User
    public void testInvalidUserInGame() throws InterruptedException {

        onView(withId(R.id.inGameButton)).perform(click());
        // mock input invalid user
        onView(withId(ch.epfl.sweng.lolmaster.R.id.summoner_name_input))
                .perform(clearText()).perform(typeText("InvalidUser"));

        onView(withText(ch.epfl.sweng.lolmaster.R.string.popup_search_button))
                .perform(click());

        // check whether we can return to Main activity when input User not in game
        onView(withId(R.id.analysisButton)).check(matches(isDisplayed()));
        
        //test whether previous name is remembered
        onView(withId(R.id.inGameButton)).perform(click());
        onView(withId(R.id.summoner_name_input)).check(
                matches(withText("InvalidUser")));

    }

    // UI test that we can start outOfGameActivity
    // @Deprecated
    public void testStartOutOfGameUi() throws InterruptedException {
    	
        onView(withId(R.id.outOfGameButton)).perform(click());

        // check whether we start the new OutofGameActivity
        onView(withId(ch.epfl.sweng.lolmaster.R.id.outOfGameLayout)).check(
                matches(isDisplayed()));
    }



    /*   
    // UI test of PersonalAnalysis Button (mock input and cancel)

    public void testPersonalAnalysisUi() {

        onView(withId(R.id.analysisButton)).perform(click());
        
        // mock input
        onView(withId(R.id.summoner_name_input)).perform(clearText()).perform(typeText("Jagage"));

        onView(withId(R.id.summoner_name_input)).check(
                matches(withText("Jagage")));

        // mock cancle
        onView(withText(R.string.popup_cancel_button)).perform(click());

        onView(withId(R.id.summoner_name_input)).check(doesNotExist());

    }

    // test PersonalAnalysis can return to mainActivity when input invalid User
    //@Deprecated
    public void testInvalidUserinPersonalAnalysis() throws InterruptedException {
    	
        onView(withId(R.id.analysisButton)).perform(click());

        // mock input invalid user
        onView(withId(R.id.summoner_name_input)).perform(clearText()).perform(typeText("InvalidUser"));

        onView(withText(R.string.popup_search_button)).perform(click());

        // check whether we can return to Main activity when the user is invalid
        onView(withId(R.id.analysisButton)).check(matches(isDisplayed()));
        
        //test whether previous name is remembered
        onView(withId(R.id.analysisButton)).perform(click());
        onView(withId(R.id.summoner_name_input)).check(
                matches(withText("InvalidUser")));
        
    }
    
    // test PersonalAnalysis can return to mainActivity when User do not have rank
    //@Deprecated
    public void testNoRankUserinPersonalAnalysis() throws InterruptedException {
    	
        onView(withId(R.id.analysisButton)).perform(click());

        // mock input invalid user
        onView(withId(R.id.summoner_name_input)).perform(clearText()).perform(typeText("clfei"));

        onView(withText(R.string.popup_search_button)).perform(click());

        // check whether we can return to Main activity when the user do not have rank
        onView(withId(R.id.analysisButton)).check(matches(isDisplayed()));
        
    }
    
    
    // UI test that we can start and run PersonalAnalysis
    //@Deprecated
    public void testStartPersonalAnalysis() throws InterruptedException {
    	
        onView(withId(R.id.analysisButton)).perform(click());

        // mock input
        onView(withId(R.id.summoner_name_input)).perform(clearText()).perform(typeText("Jagage"));

        onView(withText(R.string.popup_search_button)).perform(click());

        // check whether we have started PersonalAnalysis
        onView(withId(R.id.statisticAnalysis)).check(matches(isDisplayed()));
 
        onView(withId(R.id.statisticAnalysis)).perform(click());

        onView(withId(R.id.summoner_name_analysis_input)).perform(clearText()).perform(typeText("Yasuo"));

        onView(withText(R.string.statistic_pop_up_analyse_button)).perform(click());
        
    }
    */

    private void goBackN() {
    	// how many times to hit back button
        final int steps = 10; 
        try {
        	for (int i = 0; i < steps; i++) {
                Espresso.pressBack();
        	}
        } catch (com.google.android.apps.common.testing.ui.espresso.NoActivityResumedException e) {
            Log.e("MainActivityTest", "Closed all activities", e);
        }
    }
    
}
