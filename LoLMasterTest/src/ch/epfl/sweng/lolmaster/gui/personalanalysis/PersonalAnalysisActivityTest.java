package ch.epfl.sweng.lolmaster.gui.personalanalysis;

import android.test.ActivityInstrumentationTestCase2;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.PersonalAnalysisActivity;

/**
 * @author lcg31439
 *
 */
public class PersonalAnalysisActivityTest extends
	ActivityInstrumentationTestCase2<PersonalAnalysisActivity> {
	
//	private PersonalAnalysisActivity pActivity;
//	private Instrumentation mInstrumentation;
//	private Intent mIntent;
	
//	private static final int TIME_OUT = 5000;

	public PersonalAnalysisActivityTest() {
		super(PersonalAnalysisActivity.class);
	}

    protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		getInstrumentation();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
/*    
    /**
     * This test verifies that if the user enters a summoner name which doesn't exist,
     * the app will go back to main activity
     *//*
    public void testListViewOfMatchHistory() {
		
		String summonerNameInput = "KingOfINSA";
		mIntent = new Intent();
		mIntent.setClassName("ch.epfl.sweng.lolmaster.personalAnalysisFeature.PersonalAnalysisActivity", 
				PersonalAnalysisActivity.class.getName()); 
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			
		mIntent.putExtra(MainActivity.SUMMONER_NAME, summonerNameInput);
		mIntent.putExtra(MainActivity.REGION, "EUW");
		
		setActivityIntent(mIntent);
		
		Instrumentation.ActivityMonitor monitor = 
					mInstrumentation.addMonitor(PersonalAnalysisActivity.class.getName(), null, false);
			
		pActivity = getActivity();		
		//as summoner name doesn't exist, it should return to mainActivity
//		PersonalAnalysisActivity receiverActivity = (PersonalAnalysisActivity) 
//				monitor.waitForActivityWithTimeout(TIME_OUT);		
//		assertNotNull("PersonalAnalysisActivity should have been launched", receiverActivity); 
		
        onView(withId(R.id.personal_analysis_list)).check(matches(isDisplayed()));
		
		
    }  
    
    
    
    /**
     * This test verifies that if the user enters a summoner name that doesn't have ranked game records,
     * the app will go back to main activity
     *//*
    public void testSummonerNameWithNoRankedGame() {
		
		String summonerNameInput = "clfei";
		mIntent = new Intent();
		mIntent.setClassName("ch.epfl.sweng.lolmaster.personalAnalysisFeature.PersonalAnalysisActivity", 
				PersonalAnalysisActivity.class.getName()); 
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			
		mIntent.putExtra(MainActivity.SUMMONER_NAME, summonerNameInput);
		mIntent.putExtra(MainActivity.REGION, "EUW");
		
		setActivityIntent(mIntent);
        onView(withId(R.id.statisticAnalysis)).check(matches(isDisplayed()));
    }*/

}
