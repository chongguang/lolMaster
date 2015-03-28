package ch.epfl.sweng.lolmaster.api.dto;

import java.util.ArrayList;
import java.util.List;

import dto.Static.ChampionSpell;
import dto.Static.Image;
import dto.Static.SpellVars;

/**
 * Facilitates the use of champions' spells
 * 
 * @author ygrimault
 * 
 */
public final class LolChampionSpell extends LolDTO {

    private final ChampionSpell mSpell;
    private final Image mImage;

    public LolChampionSpell(ChampionSpell championSpell) {
        mSpell = championSpell;
        mImage = mSpell.getImage();
    }

    public String getCooldownBurn() {
        return mSpell.getCooldownBurn();
    }

    public String getCostBurn() {
        return mSpell.getCostBurn();
    }

    public String getCostType() {
        return mSpell.getCostType();
    }

    public String getDescription() {
        return mSpell.getDescription();
    }

    public List<String> getEffectBurn() {
        return mSpell.getEffectBurn();
    }

    public String getName() {
        return mSpell.getName();
    }

    public String getRangeBurn() {
        return mSpell.getRangeBurn();
    }

    public String getResource() {
        return mSpell.getResource();
    }

    public String getTooltip() {
        return mSpell.getTooltip();
    }

    public List<LolSpellVars> getVars() {
        List<SpellVars> lolVars = mSpell.getVars();
        List<LolSpellVars> mVars = new ArrayList<LolSpellVars>();

        if (lolVars != null) {
            for (SpellVars var : lolVars) {
                mVars.add(new LolSpellVars(var));
            }
        }

        return mVars;
    }

    public String getFull() {
        return mImage.getFull();
    }

    public String getGroup() {
        return mImage.getGroup();
    }
}
