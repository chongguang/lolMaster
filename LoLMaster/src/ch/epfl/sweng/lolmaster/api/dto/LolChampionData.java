package ch.epfl.sweng.lolmaster.api.dto;

import java.util.ArrayList;
import java.util.List;

import dto.Static.Champion;
import dto.Static.ChampionSpell;
import dto.Static.Stats;

/**
 * @author fKunstner
 */
public class LolChampionData extends LolDTO {

    private final Champion mChampion;

    public LolChampionData(Champion champion) {
        mChampion = champion;
    }

    /**
     * get champion's name
     * @return champion's name
     */
    public String getName() {
        return mChampion.getName();
    }

    /**
     * get champion's image
     * @return champion's image
     */
    public LolImage getImage() {
        return new LolImage(mChampion.getImage());
    }

    /**
     * get champion's ID
     * @return champion's {@see LolId} 
     */
    public LolId getId() {
        return new LolId(mChampion.getId());
    }

    /**
     * get champion's lore
     * @return champion's lore
     */
    public String getLore() {
        return mChampion.getLore();
    }

    /**
     * get champion's blurb
     * @return champion's blurb
     */
    public String getBlurb() {
        return mChampion.getBlurb();
    }

    /**
     * get champion's title
     * @return champion's title
     */
    public String getTitle() {
        return mChampion.getTitle();
    }

    /**
     * get champion's passive spell
     * @return champion's passive spell
     */
    public LolPassive getPassive() {
        return new LolPassive(mChampion.getPassive());
    }

    /**
     * get champion's spells
     * @return champion's spells
     */
    public List<LolChampionSpell> getSpells() {
        List<ChampionSpell> lolSpells = mChampion.getSpells();
        List<LolChampionSpell> mSpells = new ArrayList<LolChampionSpell>();

        if (lolSpells != null) {
            for (ChampionSpell spell : lolSpells) {
                mSpells.add(new LolChampionSpell(spell));
            }
        }
        return mSpells;
    }

    /**
     * get champion's stats
     * @return champion's stats
     */
    public Stats getStats() {
        return mChampion.getStats();
    }

    /**
     * get champion's Allied tips, synergy with this champion
     * @return champion's allied tips
     */
    public String getAllytips() {
        StringBuilder allyTips = new StringBuilder();
        for (String tip : mChampion.getAllytips()) {
            allyTips.append(tip + "\n");
        }
        return allyTips.toString();
    }

    /**
     * get champion's enemy tips, how to counter this champion
     * @return champion's enemy tips
     */
    public String getEnnemytips() {
        StringBuilder enemyTips = new StringBuilder();
        for (String tip : mChampion.getEnemytips()) {
            enemyTips.append(tip + "\n");
        }
        return enemyTips.toString();
    }
}
