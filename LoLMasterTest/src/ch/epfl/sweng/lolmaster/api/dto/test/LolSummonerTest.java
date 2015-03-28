package ch.epfl.sweng.lolmaster.api.dto.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolSummoner;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;
import dto.Summoner.Summoner;

/**
 * Tests for the LolId class
 * 
 * @author fKunstner
 */
public class LolSummonerTest extends MockitoTestCase {

    private static final long NEGATIVE_LONG = Long.valueOf(-1);
    private static final long VALID_LONG = Long.valueOf(1);
    private LolSummoner mLolSummoner;
    private Summoner mSummoner;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mSummoner = mock(Summoner.class);
        when(mSummoner.getId()).thenReturn(VALID_LONG);
        when(mSummoner.getName()).thenReturn("SummonerName1");

        mLolSummoner = new LolSummoner(mSummoner);
    }

    public void testInvalidConstructor() {
        when(mSummoner.getId()).thenReturn(NEGATIVE_LONG);
        try {
            mLolSummoner = new LolSummoner(mSummoner);
            fail("Should have rejected input");
        } catch (IllegalArgumentException e) {
            Log.i(this.getClass().getName(), "Correctly rejected invalid input.", e);
        }
    }

    public void testGetters() {
        assertEquals(new LolId(mSummoner.getId()), mLolSummoner.getId());
        assertEquals(mSummoner.getName(), mLolSummoner.getName());
    }

}
