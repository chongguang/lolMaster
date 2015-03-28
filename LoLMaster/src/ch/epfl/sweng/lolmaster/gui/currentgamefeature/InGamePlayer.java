/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.currentgamefeature;

import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolLeagues;
import ch.epfl.sweng.lolmaster.api.dto.LolMasteryPage;
import ch.epfl.sweng.lolmaster.api.dto.LolPlayer;
import ch.epfl.sweng.lolmaster.api.dto.LolRunePage;
import ch.epfl.sweng.lolmaster.api.dto.LolStats;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class InGamePlayer {

    private static final int S_NAME_LENGTH_LIMIT = 15;
    private static final String S_TRUNCATE_REPLACEMENT = "...";

    private LolId mPlayerId;
    private String mInternalName;
    private String mName;
    private String mFullName;
    private LolChampionData mChampion = null;
    private LolLeagues mLeagues = null;
    private LolStats mStats = null;
    private LolSummonerSpell mSpell1 = null;
    private LolSummonerSpell mSpell2 = null;
    private LolRunePage mRunes = null;
    private LolMasteryPage mMasteries = null;

    public InGamePlayer(LolPlayer player) {

        this.mFullName = player.getSummonerName();
        this.mName = truncate(this.mFullName);
        this.mPlayerId = player.getSummonerId();
        this.mInternalName = player.getSummonerInternalName();

    }

    private String truncate(String fullName) {
        if (fullName.length() < S_NAME_LENGTH_LIMIT) {
            return fullName;
        }

        return fullName.substring(0, S_NAME_LENGTH_LIMIT
            - S_TRUNCATE_REPLACEMENT.length())
            + S_TRUNCATE_REPLACEMENT;
    }
    
    /**
     * Get the player's ID
     * @return player's LolID
     */
    public LolId getId() {
        return mPlayerId;
    }
    
    /**
     * Get player's internal Name 
     * @return player's internal Name
     */
    public String getInternalName() {
        return mInternalName;
    }
    
    /**
     * Get player's name, can be truncated
     * @return player's name, can be truncated
     */
    public String getName() {
        return mName;
    }

    /**
     * get player's full name
     * @return player's full name
     */
    public String getFullName() {
        return mFullName;
    }

    /**
     * get player's current champion
     * @return player's current champion
     */
    public LolChampionData getChampion() {
        return mChampion;
    }

    /**
     * set player's current champion
     * @param champion LolChampionData to be set
     */
    public void setChampion(LolChampionData champion) {
        mChampion = champion;
    }

    /**
     * get player's current leagues
     * @return player's current leagues
     */
    public LolLeagues getLeagues() {
        return mLeagues;
    }

    /**
     * set player's current leagues
     * @param leagues LolLeagues to be set
     */
    public void setLeagues(LolLeagues leagues) {
        mLeagues = leagues;
    }

    /**
     * get player's current stats
     * @return player's current stats
     */
    public LolStats getPlayerStats() {
        return mStats;
    }

    /**
     * set player's current stat
     * @param stats LolSats to be set
     */
    public void setPlayerStats(LolStats stats) {
        mStats = stats;
    }

    /**
     * set first spell of player
     * @param lolSummonerSpell LolSummonerSpell to be set
     */
    public void setSpell1(LolSummonerSpell lolSummonerSpell) {
        mSpell1 = lolSummonerSpell;
    }

    /**
     * set second spell of player
     * @param lolSummonerSpell LolSummonerSpell to be set
     */
    public void setSpell2(LolSummonerSpell lolSummonerSpell) {
        mSpell2 = lolSummonerSpell;
    }

    /**
     * get player's first spell
     * @return player's first spell
     */
    public LolSummonerSpell getSpell1() {
        return mSpell1;
    }

    /**
     * get player's second spell
     * @return player's second spell
     */
    public LolSummonerSpell getSpell2() {
        return mSpell2;
    }

    /**
     * get player's current rune page
     * @return player's current rune page
     */
    public LolRunePage getRunes() {
        return mRunes;
    }

    /**
     * set player's current rune page
     * @param runePage LolRunePage to be set
     */
    public void setRunes(LolRunePage runePage) {
        mRunes = runePage;
    }

    /**
     * get player's current masteries
     * @return player's current masteries
     */
    public LolMasteryPage getMasteries() {
        return mMasteries;
    }

    /**
     * set player's masteries
     * @param masteries LolMasteryPage to be set
     */
    public void setMasteries(LolMasteryPage masteries) {
        mMasteries = masteries;
    }

}
