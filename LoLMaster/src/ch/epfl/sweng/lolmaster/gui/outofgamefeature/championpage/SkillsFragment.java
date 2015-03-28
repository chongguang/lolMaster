package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.OutOfGameChampion;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage.ChampionPage.SpellKey;

/**
 * Fragment displaying the spells' name of the champion
 * 
 * @author ygrimault
 * 
 */
public class SkillsFragment extends Fragment {

    private Activity mActivity;
    private Resources mResources;
    private String mPackage;
    private TextView viewPassive;
    private TextView viewTitlePassive;
    private TextView viewSpell;
    private TextView viewTitleSpell;
    private String titlePassive;
    private String descPassive;
    private String titleSpell;
    private String descSpell;
    private Drawable imgPassive;
    private Drawable imgSpell;
    private int viewId;
    private int viewTitleId;
    private static final int WIDTH = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.skills_pageview, container,
                false);

        // We need to get the information on the spells of the champion that is
        // concerned
        mActivity = getActivity();

        // First, we set the Passive and its image
        PassiveHtml passiveHtml = new PassiveHtml(mActivity);

        viewTitlePassive = (TextView) fragment.findViewById(R.id.passive_title);
        viewPassive = (TextView) fragment.findViewById(R.id.passive);

        // First the title with the image
        if (viewTitlePassive != null) {
            titlePassive = passiveHtml.getPassiveTitle();
            viewTitlePassive.setText(Html.fromHtml(titlePassive));

            imgPassive = passiveHtml.getPassiveImage();
            imgPassive.setBounds(0, 0, WIDTH, WIDTH);
            viewTitlePassive.setCompoundDrawables(imgPassive, null, null, null);
        } else {
            Log.w(OutOfGameChampion.class.getName(),
                    "Could not access the TextView displaying the passive title.");
        }

        // Then the description
        if (viewPassive != null) {
            descPassive = passiveHtml.getPassiveString();
            viewPassive.setText(Html.fromHtml(descPassive));
        } else {
            Log.w(OutOfGameChampion.class.getName(),
                    "Could not access the TextView displaying the passive.");
        }

        // Now we take care of the spells.
        // We need to get the ids of the TextView for each spell dynamically
        mResources = mActivity.getResources();
        mPackage = mActivity.getPackageName();

        // Now we can do a for loop on the enums
        for (SpellKey sKey : SpellKey.values()) {
            SpellHtml spellHtml = new SpellHtml(sKey, mActivity);
            String spellKey = sKey.getKey();

            // First we set the title view
            viewTitleId = mResources.getIdentifier(spellKey + "_title", "id",
                    mPackage);
            viewTitleSpell = (TextView) fragment.findViewById(viewTitleId);

            if (viewTitleSpell != null) {
                titleSpell = spellHtml.getSpellTitle();
                viewTitleSpell.setText(Html.fromHtml(titleSpell));

                imgSpell = spellHtml.getSpellImage();
                imgSpell.setBounds(0, 0, WIDTH, WIDTH);
                viewTitleSpell.setCompoundDrawables(imgSpell, null, null, null);
            } else {
                Log.w(OutOfGameChampion.class.getName(),
                        "Could not access the TextView displaying the title spell.");
            }

            // Now we can set the rest of the spell
            viewId = mResources.getIdentifier(spellKey, "id", mPackage);
            viewSpell = (TextView) fragment.findViewById(viewId);

            if (viewSpell != null) {
                descSpell = spellHtml.getSpellString();
                viewSpell.setText(Html.fromHtml(descSpell));
            } else {
                Log.w(OutOfGameChampion.class.getName(),
                        "Could not access the TextView displaying the spell.");
            }
        }

        return fragment;
    }
}
