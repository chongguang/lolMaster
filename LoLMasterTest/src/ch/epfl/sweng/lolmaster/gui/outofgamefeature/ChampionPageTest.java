/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.outofgamefeature;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage.ChampionPage;
/**
 * @author lifei
 *
 */
public class ChampionPageTest extends ActivityInstrumentationTestCase2<ChampionPage> {

	/**
	 * @param activityClass
	 */
	private Intent mIntent;
	private ChampionPage mChampionPageActivity;
	
	public ChampionPageTest() {
		super(ChampionPage.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mIntent = new Intent();
		mIntent.setClassName("ch.epfl.sweng.lolmaster.outofgamefeature.championpage.ChampionPage", 
						ChampionPage.class.getName()); 
		//take "Annie" for example to test the ChampionPage
		mIntent.putExtra("name", "Annie");
		mIntent.putExtra("id", 1);
		setActivityIntent(mIntent);

		mChampionPageActivity=getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPreConditions() {
        assertNotNull(mChampionPageActivity);
    }
	
	//test we display the correct content in ChampionPage
	public void testOneChampion() {
		//check tips page is displayed
		onView(withId(R.id.champion_banner)).check(matches(isDisplayed()));
		//check tips page's content iscorrect
		onView(withId(R.id.tips_text))
    		.check(matches(withText(containsString("playing"))));
		onView(withId(R.id.tips_text))
			.check(matches(withText(containsString("Annie"))));
		
		//check skill page is displayed
		onView(withId(android.R.id.tabs)).perform(click());
		onView(withId(R.id.passive)).check(matches(isDisplayed()));
		//check skill page's content is correct
		onView(withId(R.id.passive))
        	.check(matches(withText(containsString("Annie's"))));
		
		//onData(allOf(is(instanceOf(String.class)), is("LORE"))).perform(click());
	}

}
