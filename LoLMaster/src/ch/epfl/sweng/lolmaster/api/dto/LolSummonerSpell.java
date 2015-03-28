package ch.epfl.sweng.lolmaster.api.dto;

import dto.Static.SummonerSpell;

/**
 * A Summoner spell
 * 
 * @author fKunstner
 */
public class LolSummonerSpell extends LolDTO {
    private SummonerSpell mSpell;

    public LolSummonerSpell(SummonerSpell spell) {
        mSpell = spell;
    }

    /**
     * Get the spell's ID
     * @return the spell's {@see LolId}
     */
    public LolId getSpellId() {
        return new LolId(mSpell.getId());
    }

    /**
     * Get the spell's name
     * @return the spell's name
     */
    public String getName() {
        return mSpell.getName();
    }

    /**
     * Get the spell's image
     * @return the spell's image
     */
    public LolImage getImage() {
        return new LolImage(mSpell.getImage());
    }
}
