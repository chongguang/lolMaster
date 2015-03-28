package ch.epfl.sweng.lolmaster.gui.personalanalysis.uielement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolImage;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.database.MatchSummaryModel;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement.MatchSummaryAdapter;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement.MatchSummaryUIModel;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author lcg31439
 * 
 */
public class MatchSummaryAdapterTest extends AndroidTestCase {

	private List<MatchSummaryUIModel> mMatchHistory;
	private MatchSummaryAdapter mAdapter;

	private static final String IMAGE_FULL = "Aatrox.png";
	private static final String IMAGE_GROUP = "champion";
	private static final String IMAGE_SPRITE = "champion0.png";

	private static LolImage mImage;

	private static final long KILL = 10;
	private static final long ASSIST = 10;
	private static final long DEATH = 2;
	private static final boolean IS_WINNER = true;
	private static final long GOLD = 10000;
	private static final long MATCH_CREATION = 1415211509199L;

	private static final int NUMBER_OF_SPELL = 2;
	private static final int[] SPELL_VIEWS_ID = {R.id.match_history_spell_1,
		R.id.match_history_spell_2 };

	public MatchSummaryAdapterTest() {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();

		mMatchHistory = new ArrayList<MatchSummaryUIModel>();
		mImage = makeCustomImage();

		MatchSummaryModel match = makeCustomMatch();
		LolChampionData champion = makeCustomChampion();
		LolSummonerSpell[] spells = makeCustomSpells();

		MatchSummaryUIModel matchModel = new MatchSummaryUIModel(match,
			champion, null, spells);

		mMatchHistory.add(matchModel);

		mAdapter = new MatchSummaryAdapter(getContext(),
			android.R.layout.simple_list_item_1, mMatchHistory);

	}

	private MatchSummaryModel makeCustomMatch() {
		MatchSummaryModel match = mock(MatchSummaryModel.class);
		when(match.getDeaths()).thenReturn(DEATH);
		when(match.getAssists()).thenReturn(ASSIST);
		when(match.getKills()).thenReturn(KILL);
		when(match.isWinner()).thenReturn(IS_WINNER);
		when(match.getGoldEarned()).thenReturn(GOLD);
		when(match.getMatchCreation()).thenReturn(MATCH_CREATION);

		return match;
	}

	private LolChampionData makeCustomChampion() {
		LolChampionData champion = mock(LolChampionData.class);
		when(champion.getImage()).thenReturn(mImage);

		return champion;
	}

	private LolSummonerSpell[] makeCustomSpells() {
		LolSummonerSpell spell = mock(LolSummonerSpell.class);
		when(spell.getImage()).thenReturn(mImage);

		LolSummonerSpell[] spells = {spell, spell };

		return spells;
	}

	private LolImage makeCustomImage() {
		LolImage image = mock(LolImage.class);
		when(image.getFull()).thenReturn(IMAGE_FULL);
		when(image.getGroup()).thenReturn(IMAGE_GROUP);
		when(image.getSprite()).thenReturn(IMAGE_SPRITE);

		return image;
	}

	public void testGetView() {
		View view = mAdapter.getView(0, null, null);

		ImageView championIconView = (ImageView) view
			.findViewById(R.id.match_history_champion_icon);

		ImageView[] spellIconViews = new ImageView[NUMBER_OF_SPELL];
		for (int i = 0; i < spellIconViews.length; i++) {
			spellIconViews[i] = (ImageView) view
				.findViewById(SPELL_VIEWS_ID[i]);
		}

		TextView matchResult = (TextView) view
			.findViewById(R.id.match_history_result);

		TextView creation = (TextView) view
			.findViewById(R.id.match_history_creation_time);

		String matchResultText = mMatchHistory.get(0).matchResult();
		String matchCreationText = mMatchHistory.get(0).creationTimeToString();

		assertNotNull("championIconView ImageView is null", championIconView);
		for (int i = 0; i < spellIconViews.length; i++) {
			assertNotNull("spellIconView(" + i + ") ImageView is null",
				spellIconViews[i]);
		}
		assertNotNull("matchResult TextView is null", matchResult);
		assertNotNull("creation TextView is null", creation);

		assertNotNull("championIconView did not recieve image",
			championIconView.getDrawable());
		for (int i = 0; i < spellIconViews.length; i++) {
			assertNotNull("spellIconView(" + i + ") did not recieve image",
				spellIconViews[i].getDrawable());
		}
		assertEquals("matchResult text doesn't match", matchResult.getText(),
			matchResultText);
		assertEquals("matchCreation text doesn't match", creation.getText(),
			matchCreationText);

	}

}
