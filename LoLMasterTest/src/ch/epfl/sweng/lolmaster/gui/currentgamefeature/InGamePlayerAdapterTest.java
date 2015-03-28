/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.currentgamefeature;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import constant.Region;
import constant.staticdata.MasteryListData;

import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolImage;
import ch.epfl.sweng.lolmaster.api.dto.LolLeague;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryPage;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryTree;
import ch.epfl.sweng.lolmaster.api.dto.LolRune;
import ch.epfl.sweng.lolmaster.api.dto.LolRunePage;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import dto.Static.MasteryTree;
import dto.Static.Rune;
import dto.Summoner.Mastery;
import dto.Summoner.MasteryPage;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * @author ajjngeor (alain george : 217451)
 *
 */
public class InGamePlayerAdapterTest extends AndroidTestCase{

	private InGamePlayerAdapter mAdapter;
	
	private List<InGamePlayer> players;
	
	private static final String REGION = "EUW";
	
	private static final String NAME = "atlistorm";
	private static final String TIER = "SILVER";
	private static final String DIVISION = "II";
	private static final int NUMBER_OF_WIN = 10;
	private static final String RUNE_NAME = "hello";
	private static final String RUNE = "+4 attack";
	private static final String MASTERY_DESCRIPTION = "9/0/21";
	
	private static final String IMAGE_FULL = "Aatrox.png";
	private static final String IMAGE_GROUP = "champion";
	private static final String IMAGE_SPRITE = "champion0.png";
	
	private static final int ITER_NB = 30;
	private static final int MASTERY1_ID = 4311;
	private static final int MASTERY1_RANK = 21;
	private static final int MASTERY2_ID = 4162;
	private static final int MASTERY2_RANK = 9;
	
	public InGamePlayerAdapterTest() {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		players = new ArrayList<InGamePlayer>();
		
		InGamePlayer player1 = mock(InGamePlayer.class);
		when(player1.getName()).thenReturn(NAME);
		
		LolImage image = makeCustomImage();
		LolChampionData champion = mock(LolChampionData.class);
		when(champion.getImage()).thenReturn(image);
		
		LolSummonerSpell spell = mock(LolSummonerSpell.class);
		when(spell.getImage()).thenReturn(image);
		
		LolLeagues leagues = mock(LolLeagues.class);
		LolLeague league = mock(LolLeague.class);
		when(league.getTier()).thenReturn(TIER);
		when(league.getDivision()).thenReturn(DIVISION);
		when(leagues.getRankedSoloQueueLeague()).thenReturn(league);
		
		LolStats stats = mock(LolStats.class);
		when(stats.getNumberOfNormalWins()).thenReturn(NUMBER_OF_WIN);
		when(stats.getNumberOfRankedWins()).thenReturn(NUMBER_OF_WIN);
		
		LolRunePage runePage = makeCustomRunePage();
		
		LolMasteryPage masteryPage = makeCustomMasteryPage();
		
		when(player1.getChampion()).thenReturn(champion);
		when(player1.getLeagues()).thenReturn(leagues);
		when(player1.getPlayerStats()).thenReturn(stats);
		when(player1.getMasteries()).thenReturn(masteryPage);
		when(player1.getRunes()).thenReturn(runePage);
		when(player1.getSpell1()).thenReturn(spell);
		when(player1.getSpell2()).thenReturn(spell);
		
		players.add(player1);
		
		mAdapter = new InGamePlayerAdapter(getContext(), players, REGION);
		
		
	}
	private LolImage makeCustomImage() {
		LolImage image = mock(LolImage.class);
		when(image.getFull()).thenReturn(IMAGE_FULL);
		when(image.getGroup()).thenReturn(IMAGE_GROUP);
		when(image.getSprite()).thenReturn(IMAGE_SPRITE);
		
		return image;
	}
	private LolRunePage makeCustomRunePage() {
		List<LolRune> runes = new ArrayList<LolRune>();
		Rune mockRune = mock(Rune.class);
		when(mockRune.getSanitizedDescription()).thenReturn(RUNE);
		for (int i = 0; i < ITER_NB; i++) {
			runes.add(new LolRune(mockRune));
		}

		LolRunePage runePage = new LolRunePage(RUNE_NAME, runes);
		
		return runePage;
	}
	
	private LolMasteryPage makeCustomMasteryPage() {
		MasteryPage mockMasteryPage = mock(MasteryPage.class);
		List<Mastery> customMasteries = new ArrayList<Mastery>();

		Mastery mockMastery1 = mock(Mastery.class);
		Mastery mockMastery2 = mock(Mastery.class);

		when(mockMastery1.getId()).thenReturn(MASTERY1_ID);
		when(mockMastery1.getRank()).thenReturn(MASTERY1_RANK);

		when(mockMastery2.getId()).thenReturn(MASTERY2_ID);
		when(mockMastery2.getRank()).thenReturn(MASTERY2_RANK);

		customMasteries.add(mockMastery1);
		customMasteries.add(mockMastery2);

		when(mockMasteryPage.getMasteries()).thenReturn(customMasteries);

		RiotApi api = new RiotApi(ApiKeysManager.getApiKeys().getRiotKey(),
			Region.EUW);
		MasteryTree masteryTree = null;
		try {
			masteryTree = api.getDataMasteryList(null, null,
				MasteryListData.ALL).getTree();
		} catch (RiotApiException e) {
			e.printStackTrace();
		}
		LolMasteryPage lolMasteryPage = new LolMasteryPage(mockMasteryPage,
			new LolMasteryTree(masteryTree));
		
		return lolMasteryPage;
	}
	
	public void testGetView() {
		
		View view = mAdapter.getView(0, null, null);
		
	    TextView playerName =
            (TextView) view.findViewById(R.id.playerName);
        ImageView playerChampion =
            (ImageView) view.findViewById(R.id.playerChampion);
        ImageView playerSpell1 =
            (ImageView) view.findViewById(R.id.playerSpell1);
        ImageView playerSpell2 =
            (ImageView) view.findViewById(R.id.playerSpell2);
        ImageView playerTier =
            (ImageView) view.findViewById(R.id.playerTier);
        TextView playerDivision =
            (TextView) view.findViewById(R.id.playerDivision);
        TextView playerNormalWins =
            (TextView) view.findViewById(R.id.playerNormalWins);
        TextView playerRankedWins =
            (TextView) view.findViewById(R.id.playerRankedWins);
        TextView playerMastery =
            (TextView) view.findViewById(R.id.playerMastery);
        Button playerRuneButton =
            (Button) view.findViewById(R.id.runeButton);
        
        assertNotNull("View is null", view);
        assertNotNull("playerName textView is null", playerName);
        assertNotNull("champion imageView is null", playerChampion);
        assertNotNull("playerSpell1 imageView is null", playerSpell1);
        assertNotNull("playerSpell2 imageView is null", playerSpell2);
        assertNotNull("playerTier image View is null", playerTier);
        assertNotNull("playerDivision textView is null", playerDivision);
        assertNotNull("playerNormalWins textView is null", playerNormalWins);
        assertNotNull("playerRankedWins textView is null", playerRankedWins);
        assertNotNull("playerMasteries textView is null", playerMastery);
        assertNotNull("palyerRune ButtonView is null", playerRuneButton);
        
        assertEquals("player Name doesn't match", playerName.getText(), NAME);
        assertEquals("player Division doesn't match", playerDivision.getText(), DIVISION);
        assertEquals("player NormalWin doesn't match", playerNormalWins.getText(), String.valueOf(NUMBER_OF_WIN));
        assertEquals("player RankedWin doesn't match", playerRankedWins.getText(), String.valueOf(NUMBER_OF_WIN));
        assertEquals("player masteries doesn't match", playerMastery.getText(), MASTERY_DESCRIPTION);
        assertEquals("player runePage name doesn't match", playerRuneButton.getText(), RUNE_NAME);
        
        assertNotNull("player champion didn't recieve image", playerChampion.getDrawable());
        assertNotNull("player Spell1 didn't recieve image", playerSpell1.getDrawable());
        assertNotNull("player Spell2 didn't recieve image", playerSpell2.getDrawable());
        assertNotNull("player tier didn't recieve image", playerTier.getDrawable());
        
	}
}
