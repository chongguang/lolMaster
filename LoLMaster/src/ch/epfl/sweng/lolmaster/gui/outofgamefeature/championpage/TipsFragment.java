package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage;

import android.app.Activity;
import android.content.Intent;
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

/**
 * Fragment displaying the tips of the champion
 * 
 * @author ygrimault
 * 
 */
public class TipsFragment extends Fragment {

    private Activity mActivity;
    private TextView mText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.tips_pageview, container,
                false);

        // We need to get the tips of the champion that is concerned
        mActivity = getActivity();
        Intent intent = mActivity.getIntent();
        String championName = intent.getExtras().getString("name");
        String championAllyTips = intent.getExtras().getString("ally_tips");
        String championEnemyTips = intent.getExtras().getString("enemy_tips");

        // First we create the Html text that will display correctly the text
        String htmlTipsText = "\t<b>Tips when playing " + championName
                + " :</b><br>" + championAllyTips + "<br><br>"
                + "<b>Tips when playing against " + championName + " :</b><br>"
                + championEnemyTips;

        // Then we can display it properly
        mText = (TextView) fragment.findViewById(R.id.tips_text);
        if (mText != null) {
            mText.setText(Html.fromHtml(htmlTipsText));
        } else {
            Log.w(OutOfGameChampion.class.getName(),
                    "Could not access the TextView displaying tips.");
        }

        return fragment;
    }

}
