package ch.epfl.sweng.lolmaster.api.dto.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;
import dto.Static.Champion;

/**
 * Tests for the LolBannedChampion class
 * 
 * @author fKunstner
 */
public class LolChampionDataTest extends MockitoTestCase {

	private static final Integer VALID_ID = 1;
    private LolChampionData mLolChampionData;
	private Champion mChampion;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mChampion = mock(Champion.class);
		when(mChampion.getId()).thenReturn(VALID_ID);
		when(mChampion.getName()).thenReturn("ChampionName1");

		mLolChampionData = new LolChampionData(mChampion);

	}

	public void testGetters() {
		assertEquals(new LolId(mChampion.getId()), mLolChampionData.getId());
		assertEquals(mChampion.getName(), mLolChampionData.getName());
	}

}
