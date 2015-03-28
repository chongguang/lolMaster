/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.outofgamefeature;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.GridView;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.LolApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolImage;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.ChampionAdapter;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.OutOfGameActivity;

/**
 * @author Lifei test of OutOfGameActivity
 */

public class OutOfGameActivityTest extends
        ActivityInstrumentationTestCase2<OutOfGameActivity> {

    /**
     * @param activityClass
     */
    private OutOfGameActivity mOutOfGameActivity;

    // private static final int SHOW_TIME = 3000;

    public OutOfGameActivityTest() {
        super(OutOfGameActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // test the Champion we choose is showed correctly

    public void testCanDisplayOneChampion() throws ApiException,
            InterruptedException {
        LolApi mockapi = mock(LolApi.class);
        LolChampionData mockLolChampionData = mock(LolChampionData.class);
        LolImage mockLolImage = mock(LolImage.class);

        Map<LolId, LolChampionData> lolChampionDataById = new HashMap<LolId, LolChampionData>();

        LolId lolId = new LolId(1);
        lolChampionDataById.put(lolId, mockLolChampionData);

        when(mockapi.getDataChampionList()).thenReturn(lolChampionDataById);
        when(mockLolChampionData.getId()).thenReturn(lolId);
        when(mockLolChampionData.getName()).thenReturn("Yasuo");
        when(mockLolChampionData.getImage()).thenReturn(mockLolImage);
        when(mockLolImage.getFull()).thenReturn("yasuo_square_0.png");

        LolApi.injectApi(mockapi);

        mOutOfGameActivity = getActivity();

        onView(withId(ch.epfl.sweng.lolmaster.R.id.outOfGameLayout)).check(
                matches(isDisplayed()));

        // test yasuo is showed

        GridView gridChampionLayout = (GridView) mOutOfGameActivity
                .findViewById(ch.epfl.sweng.lolmaster.R.id.outOfGameLayout);

        assertNotNull("The OutOfGameActivity must contain a champion grid.",
                gridChampionLayout);

        assertFalse("The list of displayed campions must not be empty!",
                ((ChampionAdapter) gridChampionLayout.getAdapter()).isEmpty());

        assertEquals("The OutOfGameActivity must show the correct champions.",
                ((ChampionAdapter) gridChampionLayout.getAdapter())
                        .getChampionName(0), "Yasuo");

    }

}
