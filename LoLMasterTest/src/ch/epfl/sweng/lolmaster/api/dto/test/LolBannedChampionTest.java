package ch.epfl.sweng.lolmaster.api.dto.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ch.epfl.sweng.lolmaster.api.dto.LolBannedChampion;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolTeam.TeamId;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.BannedChampion;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;

/**
 * Tests for the LolBannedChampion class
 * 
 * @author fKunstner
 */
public class LolBannedChampionTest extends MockitoTestCase {

    private static final Integer VALID_ID = 1;
	private LolBannedChampion mLolBannedChampion;
	private BannedChampion mBannedChampion;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mBannedChampion = mock(BannedChampion.class);
		when(mBannedChampion.getChampionId()).thenReturn(VALID_ID);
		when(mBannedChampion.getTeamId()).thenReturn(
			Integer.valueOf(TeamId.TEAM1.getId()));
		mLolBannedChampion = new LolBannedChampion(mBannedChampion);
	}

	public void testGetters() {
		assertEquals(new LolId(mBannedChampion.getChampionId()),
			mLolBannedChampion.getChampionId());
		assertEquals(TeamId.TEAM1, mLolBannedChampion.getTeamId());
	}

}
