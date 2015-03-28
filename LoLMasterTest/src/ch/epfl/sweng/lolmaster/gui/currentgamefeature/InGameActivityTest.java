/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.currentgamefeature;
import android.test.ActivityInstrumentationTestCase2;
/**
 * @author lifei
 *
 */
public class InGameActivityTest extends ActivityInstrumentationTestCase2<InGameActivity> {

	/**
	 * @param activityClass
	 */
//	private String sanitizedName;
//	private InGameActivity mInGameActivity;
//	private Intent mIntent;
//	private Instrumentation mInstrumentation;
//  private static final int CONNECTION_TIME_OUT = 1000;
    
	public InGameActivityTest() {
		super(InGameActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		//sanitizedName = getRandomInGamePlayer();
		
		//assertNotNull("fail to get random sanitizedName", sanitizedName);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

/*	
	//test start InGameActivity by intent
	public void testStartInGame() throws Exception {
		
		mIntent = new Intent();
		mIntent.setClassName("ch.epfl.sweng.lolmaster.currentGameFeature.InGameActivity", 
				InGameActivity.class.getName()); 
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
			
		mIntent.putExtra(InGameActivity.SUMMONER_NAME, sanitizedName);
		mIntent.putExtra(InGameActivity.REGION, "NA");
		
		//could not launch in 45 s
		setActivityIntent(mIntent);
			
		mInGameActivity = getActivity();
		
		onView(withId(R.id.allied_team_listview)).check(matches(isDisplayed()));
		
	}
	
	private static String getRandomInGamePlayer() throws ApiException {
    	String featuredGamesUrl = "http://spectator.na.lol.riotgames.com/observer-mode/rest/featured";
   		HttpURLConnection connection;
   		String jsonContent;
   		try {
   			connection = ConnectionUtils.openConnection(featuredGamesUrl);
    		connection.setConnectTimeout(CONNECTION_TIME_OUT);
    		jsonContent = ConnectionUtils.getContent(connection);
    	} catch (IOException e) {
   			throw new ApiException("Unable to connect to the server.", e);
   		}
    	
  		Pattern pattern = Pattern.compile("\"summonerName\":\"(..*?)\"");
    	Matcher matcher = pattern.matcher(jsonContent);
    	if (matcher.find()) {
    		return matcher.group(1);
    	}
       	return null;
    }*/
	
}
