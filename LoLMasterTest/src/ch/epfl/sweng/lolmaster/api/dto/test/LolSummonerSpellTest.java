package ch.epfl.sweng.lolmaster.api.dto.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.testing.MockitoTestCase;
import dto.Static.SummonerSpell;

/**
 * Tests for the LolSummonerSpell class
 * 
 * @author fKunstner
 */
public class LolSummonerSpellTest extends MockitoTestCase {

    private static final Integer VALID_ID = 10;
	private LolSummonerSpell mLolSummonerSpell;
	private SummonerSpell mSummonerSpell;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		mSummonerSpell = mock(SummonerSpell.class);
		when(mSummonerSpell.getName()).thenReturn("SpellName1");
		when(mSummonerSpell.getId()).thenReturn(VALID_ID);
		mLolSummonerSpell = new LolSummonerSpell(mSummonerSpell);
	}

	public void testGetters() {
		assertEquals(mSummonerSpell.getName(), mLolSummonerSpell.getName());
		assertEquals(new LolId(mSummonerSpell.getId()),
			mLolSummonerSpell.getSpellId());
	}
}
