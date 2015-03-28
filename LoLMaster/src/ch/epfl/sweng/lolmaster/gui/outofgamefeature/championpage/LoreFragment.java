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
 * Fragment displaying the lore of the champion
 * 
 * @author ygrimault
 * 
 */
public class LoreFragment extends Fragment {

    private Activity mActivity;
    private TextView mLoreText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.lore_pageview, container,
                false);

        // We need to get the lore of the champion that is concerned
        mActivity = getActivity();
        Intent intent = mActivity.getIntent();
        String championLore = intent.getExtras().getString("lore");

        // We need to display the lore
        mLoreText = (TextView) fragment.findViewById(R.id.lore_text);
        if (mLoreText != null) {
            mLoreText.setText(Html.fromHtml(championLore));
        } else {
            Log.w(OutOfGameChampion.class.getName(),
                    "Could not access the TextView displaying the lore.");
        }

        return fragment;
    }
}
