package ch.epfl.sweng.lolmaster.api.dto;

import java.io.Serializable;

import dto.Static.SpellVars;

/**
 * Adaptation of the class SpellVars so we can pass it to Intents
 * 
 * @author ygrimault
 * 
 */
public class LolSpellVars extends LolDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mKey;
    private String mType;
    private Double[] mValues;

    public LolSpellVars(SpellVars spellVars) {
        mKey = spellVars.getKey();
        mType = spellVars.getLink();
        mValues = spellVars.getCoeff().toArray(
                new Double[spellVars.getCoeff().size()]);
    }

    /**
     * Get the key of the spell vars
     * 
     * @return the key of the spell vars
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Get the type of damage of the spell vars
     * 
     * @return the key of the spell vars
     */
    public String getType() {
        return mType;
    }

    /**
     * Get an array of values
     * 
     * @return values of the spell vars
     */
    public Double[] getValues() {
        return mValues;
    }

    /**
     * Get the values in string
     * 
     * @return values of the spell vars in string
     */
    public String getStringValues() {
        StringBuilder mBuffValues = new StringBuilder("");

        int l = mValues.length;
        for (int idx = 0; idx < l; idx++) {
            mBuffValues.append(mValues[idx].toString());
            if (idx != l - 1) {
                mBuffValues.append("/");
            }
        }

        return mBuffValues.toString();
    }
}
