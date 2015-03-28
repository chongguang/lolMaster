package ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;

/**
 * @author lcg31439
 * 
 */
public class MatchSummaryAdapter extends ArrayAdapter<MatchSummaryUIModel> {
    private static final int[] SPELL_VIEWS_ID = {R.id.match_history_spell_1,
        R.id.match_history_spell_2 };

    public MatchSummaryAdapter(Context context, int resource,
        List<MatchSummaryUIModel> matchHistory) {
        super(context, resource, matchHistory);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        MatchSummaryUIModel matchSummary = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView =
                LayoutInflater.from(getContext()).inflate(
                    R.layout.match_summary, parent, false);
        }
        // Lookup view for data population
        // Populate the champion icon view
        Drawable champIcon =
            ImageFetcher.getImage(getContext(), matchSummary.getChampion()
                .getImage());
        ImageView championIconView =
            (ImageView) convertView
                .findViewById(R.id.match_history_champion_icon);
        championIconView.setImageDrawable(champIcon);

        // Populate the spell views
        Drawable[] spellIcons = new Drawable[matchSummary.getSpells().length];
        ImageView[] spellIconViews = new ImageView[spellIcons.length];
        for (int i = 0; i < spellIcons.length; i++) {
            spellIcons[i] =
                ImageFetcher.getImage(getContext(),
                    matchSummary.getSpells()[i].getImage());

            spellIconViews[i] =
                (ImageView) convertView.findViewById(SPELL_VIEWS_ID[i]);

            spellIconViews[i].setImageDrawable(spellIcons[i]);
        }

        // Populate the multiInfo view
        TextView matchResult =
            (TextView) convertView.findViewById(R.id.match_history_result);
        matchResult.setText(matchSummary.matchResult());
        int matchResultColor =
            matchSummary.getMatch().isWinner() ? Color.GREEN : Color.RED;
        matchResult.setTextColor(matchResultColor);

        // multiInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        TextView creation =
            (TextView) convertView
                .findViewById(R.id.match_history_creation_time);
        creation.setText(matchSummary.creationTimeToString());

        return convertView;
    }

}
