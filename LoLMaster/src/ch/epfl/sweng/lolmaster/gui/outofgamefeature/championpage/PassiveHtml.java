package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;

/**
 * Generalize the creation of the String that'll be display for a passive
 * 
 * @author ygrimault
 * 
 */
public class PassiveHtml {

    private Activity mActivity;
    private Intent mIntent;

    private String mName;
    private String mDescription;
    private String mFull;
    private String mGroup;
    private String mPassiveTitle;
    private String mPassiveString;

    /**
     * 
     * @param mIntent
     *            Intent from which we fetch the information we need
     */
    public PassiveHtml(Activity activity) {
        mActivity = activity;
        mIntent = activity.getIntent();

        mName = mIntent.getExtras().getString("passive_name");
        mDescription = mIntent.getExtras().getString("passive_desc");
        mFull = mIntent.getExtras().getString("passive_full");
        mGroup = mIntent.getExtras().getString("passive_group");

        createPassiveTitle();
        createPassiveString();
    }

    /**
     * Returns the String for the passive's title
     * 
     * @return
     */
    public String getPassiveTitle() {
        if (mPassiveTitle != null) {
            return mPassiveTitle;
        } else {
            return "Object not initialized. Thus, you get a useless string.";
        }
    }

    /**
     * Returns the String for the passive
     * 
     * @return
     */
    public String getPassiveString() {
        if (mPassiveString != null) {
            return mPassiveString;
        } else {
            return "Object not initialized. Thus, you get a useless string.";
        }
    }

    /**
     * Creates the html code that'll be use to display the passive's title
     */
    private void createPassiveTitle() {
        final StringBuilder stBuild = new StringBuilder("");

        stBuild.append("<h2>");
        stBuild.append("<br>");
        stBuild.append("Passive : " + mName);
        stBuild.append("</h2>");

        mPassiveTitle = new String(stBuild);
    }

    /**
     * Creates the html code that'll be use to display all information needed
     * for the passive
     */
    private void createPassiveString() {
        final StringBuilder sBuild = new StringBuilder("");
        sBuild.append(mDescription);
        mPassiveString = new String(sBuild);
    }

    /**
     * Returns the image of the Passive, already scaled
     * 
     * @return
     */
    public Drawable getPassiveImage() {
        return ImageFetcher.getImage(mActivity, mGroup, mFull);
    }
}
