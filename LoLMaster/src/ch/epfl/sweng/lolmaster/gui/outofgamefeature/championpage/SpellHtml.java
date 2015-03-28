package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import ch.epfl.sweng.lolmaster.api.dto.LolSpellVars;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage.ChampionPage.SpellKey;

/**
 * Generalize the creation of the String that'll be display for a spell
 * 
 * @author ygrimault
 * 
 */
public class SpellHtml {

    private Activity mActivity;
    private Intent intent;

    private String mKeyShortcut;
    private String mName;
    private String mCooldown;
    private String mCost;
    private String mResource;
    private String mRange;
    private String mTooltip;
    private String[] mEffects;
    private LolSpellVars[] mVarValues;

    private String mFull;
    private String mGroup;

    private String mSpellTitle;
    private String mSpellString;

    private static final int INDEX_LIM = 9;

    /**
     * Creates an object depending on the spell name : passive ?, q_spell,
     * w-spell, e_spell, or r_spell
     * 
     * @param spellKey
     * @param intent
     *            Intent from which we fetch the information we need
     */
    public SpellHtml(SpellKey spellKey, Activity activity) {
        mActivity = activity;
        intent = mActivity.getIntent();

        mKeyShortcut = spellKey.getKey();
        mName = intent.getExtras().getString(mKeyShortcut + "_name");
        mCooldown = intent.getExtras().getString(mKeyShortcut + "_cooldown");
        mCost = intent.getExtras().getString(mKeyShortcut + "_cost");
        mResource = intent.getExtras().getString(mKeyShortcut + "_resource");
        mRange = intent.getExtras().getString(mKeyShortcut + "_range");
        mTooltip = intent.getExtras().getString(mKeyShortcut + "_tooltip");
        mEffects = intent.getExtras().getStringArray(mKeyShortcut + "_effects");
        mVarValues = (LolSpellVars[]) intent.getSerializableExtra(mKeyShortcut
                + "_var_values");

        mFull = intent.getExtras().getString(mKeyShortcut + "_full");
        mGroup = intent.getExtras().getString(mKeyShortcut + "_group");

        normalizeStrings();
        createSpellTitle();
        createSpellString();
    }

    /**
     * Returns the String for the selected spell's title
     * 
     * @return
     */
    public String getSpellTitle() {
        if (mSpellTitle != null) {
            return mSpellTitle;
        } else {
            return "Object not initialized. Thus, you get a useless string";
        }
    }

    /**
     * Returns the String for the selected spell
     * 
     * @return
     */
    public String getSpellString() {
        if (mSpellString != null) {
            return mSpellString;
        } else {
            return "Object not initialized. Thus, you get a useless string";
        }
    }

    /**
     * Creates the html code that'll be use to display the spell's title
     */
    private void createSpellTitle() {
        StringBuilder stBuild = new StringBuilder("");

        String upperKey = String.valueOf(mKeyShortcut.charAt(0)).toUpperCase();
        stBuild.append("<h2>");
        stBuild.append("<br>");
        stBuild.append(upperKey + " : " + mName);
        stBuild.append("</h2>");

        mSpellTitle = new String(stBuild);
    }

    /**
     * Replace all keys by their values.
     */
    private void normalizeStrings() {
        // It just needs to not be null so we don't need to change everything
        if (mResource == null) {
            mResource = "";
        }

        mResource = replaceKeys(mResource, "cost", mCost);

        if (mEffects != null) {
            for (int idx = 0; idx < mEffects.length; idx++) {
                String coeffs = mEffects[idx];
                String key = "e" + idx;
                mResource = replaceKeys(mResource, key, coeffs);
                mTooltip = replaceKeys(mTooltip, key, coeffs);
            }
        }
        if (mVarValues != null) {
            for (LolSpellVars entry : mVarValues) {
                String key = entry.getKey();
                String values = entry.getStringValues();
                String type = entry.getType();

                mResource = replaceKeys(mResource, key, values);

                if ("attackdamage".equals(type)) {
                    mTooltip = replaceKeys(mTooltip, key, values, "red");
                } else if ("spelldamage".equals(type)) {
                    mTooltip = replaceKeys(mTooltip, key, values, "green");
                } else {
                    mTooltip = replaceKeys(mTooltip, key, values);
                }
            }
        }

        clearTooltip();
    }

    /**
     * Creates the html code that'll be use to display all information needed
     * for the spell
     */
    private void createSpellString() {
        final StringBuilder sBuild = new StringBuilder("");

        if (!mResource.isEmpty()) {
            sBuild.append("<b>Cooldown : </b>").append(mCooldown).append("s")
                    .append("<br>");
            sBuild.append("<b>Cost : </b>").append(mResource).append("<br>");
            sBuild.append("<b>Range : </b>").append(mRange).append("<br>");
            sBuild.append("<br>");
            sBuild.append(mTooltip);
        } else {
            sBuild.append(mTooltip);
        }

        mSpellString = new String(sBuild);
    }

    /**
     * Replace key in a string with the actual value.
     * 
     * @param desc
     * @param key
     * @param coeffs
     * @param color
     * @return
     */
    public String replaceKeys(String desc, String key, String coeffs,
            String color) {
        String htmlCoeffs = "<font color = \"" + color + "\">" + coeffs
                + "</font>";
        String finalDesc;

        finalDesc = desc
                .replaceAll(Pattern.quote("{{ " + key + " }}"), htmlCoeffs)
                .replaceAll(Pattern.quote("{{" + key + "}}"), htmlCoeffs)
                .replaceAll(Pattern.quote(" " + key), coeffs);

        return finalDesc;
    }

    public String replaceKeys(String desc, String key, String coeffs) {
        return replaceKeys(desc, key, coeffs, "black");
    }

    /**
     * Looks for every data that haven't been replaced. I don't think that they
     * come in differrent forms than {{ fi }} with i a number between 1 and 9
     */
    public void clearTooltip() {
        for (int i = 1; i < INDEX_LIM; i++) {
            String elem1 = "{{ e" + i + " }}";
            String elem2 = "{{ f" + i + " }}";
            mTooltip = mTooltip.replaceAll(Pattern.quote(elem1),
                    "INCOMPLETE_DATA");
            mTooltip = mTooltip.replaceAll(Pattern.quote(elem2),
                    "INCOMPLETE_DATA");
        }

        mTooltip = mTooltip.replaceAll(Pattern.quote("{{ "), "");
        mTooltip = mTooltip.replaceAll(Pattern.quote(" }}"), "");
    }

    public Drawable getSpellImage() {
        return ImageFetcher.getImage(mActivity, mGroup, mFull);
    }
}