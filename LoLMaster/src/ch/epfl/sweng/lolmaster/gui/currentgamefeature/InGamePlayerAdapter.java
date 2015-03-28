/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.currentgamefeature;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;
import ch.epfl.sweng.lolmaster.gui.MainActivity;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage.ChampionPage;
import ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.PersonalAnalysisActivity;
import ch.epfl.sweng.lolmaster.utils.NumberUtils;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class InGamePlayerAdapter extends ArrayAdapter<InGamePlayer> {

    private InGamePlayer mPlayer;

    private Context mContext;

    private String mRegion;

    private static final double MAX_HEIGHT_RATIO = 0.07;
    private static final double MAX_WIDTH_RATIO = 0.066;

    public InGamePlayerAdapter(Context context, List<InGamePlayer> players,
        String region) {
        super(context, R.layout.in_game_player_item, players);
        this.mContext = context;
        mRegion = region;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        mPlayer = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView =
                inflater.inflate(R.layout.in_game_player_item, parent, false);

            viewHolder.playerName =
                (TextView) convertView.findViewById(R.id.playerName);
            viewHolder.playerChampion =
                (ImageView) convertView.findViewById(R.id.playerChampion);
            viewHolder.playerSpell1 =
                (ImageView) convertView.findViewById(R.id.playerSpell1);
            viewHolder.playerSpell2 =
                (ImageView) convertView.findViewById(R.id.playerSpell2);
            viewHolder.playerTier =
                (ImageView) convertView.findViewById(R.id.playerTier);
            viewHolder.playerDivision =
                (TextView) convertView.findViewById(R.id.playerDivision);
            viewHolder.playerNormalWins =
                (TextView) convertView.findViewById(R.id.playerNormalWins);
            viewHolder.playerRankedWins =
                (TextView) convertView.findViewById(R.id.playerRankedWins);
            viewHolder.playerMastery =
                (TextView) convertView.findViewById(R.id.playerMastery);
            viewHolder.playerRuneButton =
                (Button) convertView.findViewById(R.id.runeButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Drawable champIcon =
            ImageFetcher.getImage(getContext(), mPlayer.getChampion()
                .getImage());
        Drawable spell1Icon =
            ImageFetcher.getImage(getContext(), mPlayer.getSpell1().getImage());
        Drawable spell2Icon =
            ImageFetcher.getImage(getContext(), mPlayer.getSpell2().getImage());
        Drawable tierIcon =
            ImageFetcher.getRankIcon(getContext(), getTierString(mPlayer));

        viewHolder.playerName.setText(mPlayer.getName());
        viewHolder.playerChampion.setImageDrawable(champIcon);
        viewHolder.playerSpell1.setImageDrawable(spell1Icon);
        viewHolder.playerSpell2.setImageDrawable(spell2Icon);
        viewHolder.playerTier.setImageDrawable(tierIcon);
        viewHolder.playerDivision.setText(getDivisionString(mPlayer));
        viewHolder.playerNormalWins.setText(getNormalWinsString(mPlayer));
        viewHolder.playerRankedWins.setText(getRankedWinsString(mPlayer));
        viewHolder.playerMastery.setText(mPlayer.getMasteries()
            .getTreeDescription());
        viewHolder.playerRuneButton.setText(mPlayer.getRunes().getName());
        viewHolder.playerRuneButton
            .setOnClickListener(new View.OnClickListener() {
                final private InGamePlayer currentPlayer = mPlayer;

                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), getRunesString(currentPlayer),
                        Toast.LENGTH_SHORT).show();
                }
            });
        viewHolder.playerChampion
            .setOnClickListener(new View.OnClickListener() {
                final private InGamePlayer currentPlayer = mPlayer;

                @Override
                public void onClick(View view) {
                    Intent intent =
                        new Intent(mContext.getApplicationContext(),
                            ChampionPage.class);

                    intent.putExtra("name", currentPlayer.getChampion()
                        .getName());
                    intent.putExtra("id", currentPlayer.getChampion().getId()
                        .getValue());
                    mContext.startActivity(intent);
                }
            });
        viewHolder.playerName.setOnClickListener(new View.OnClickListener() {
            final private InGamePlayer currentPlayer = mPlayer;

            @Override
            public void onClick(View view) {
                Intent intent =
                    new Intent(mContext.getApplicationContext(),
                        PersonalAnalysisActivity.class);

                intent.putExtra(MainActivity.SUMMONER_NAME,
                    currentPlayer.getName());
                intent.putExtra(MainActivity.REGION, mRegion);
                mContext.startActivity(intent);
            }
        });
        setLineSize(convertView, viewHolder);

        return convertView;
    }

    private String getTierString(InGamePlayer player) {
        if (player.getLeagues() != null
            && player.getLeagues().getRankedSoloQueueLeague() != null) {
            return player.getLeagues().getRankedSoloQueueLeague().getTier();
        }
        return "unranked";
    }

    private String getDivisionString(InGamePlayer player) {
        if (player.getLeagues() != null
            && player.getLeagues().getRankedSoloQueueLeague() != null) {
            return player.getLeagues().getRankedSoloQueueLeague().getDivision();
        }
        return " ";
    }

    private String getNormalWinsString(InGamePlayer player) {
        return player.getPlayerStats() == null ? "0" : String.valueOf(player
            .getPlayerStats().getNumberOfNormalWins());
    }

    private String getRankedWinsString(InGamePlayer player) {
        return player.getPlayerStats() == null ? "0" : String.valueOf(player
            .getPlayerStats().getNumberOfRankedWins());
    }

    private String getRunesString(InGamePlayer player) {
        StringBuilder runeString = new StringBuilder();

        Map<String, Double> runeStat = player.getRunes().getStatsMap();
        for (Entry<String, Double> entry : runeStat.entrySet()) {
            Double stat = NumberUtils.truncateTo(entry.getValue(), 2);

            runeString.append(stat);
            runeString.append(entry.getKey());
            runeString.append("\n");
        }
        return runeString.toString();
    }

    private void setLineSize(View convertView, ViewHolder viewHolder) {

        WindowManager wm =
            (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int maxHeight = (int) (width * MAX_HEIGHT_RATIO);
        int maxWidth = (int) (height * MAX_WIDTH_RATIO);
        int imageSize = Math.min(maxHeight, maxWidth);

        viewHolder.playerRuneButton.setMinimumHeight(imageSize);
    }

    /**
     * @author ajjngeor (alain george : 217451)
     * 
     */
    private static class ViewHolder {

        private TextView playerName;
        private ImageView playerChampion;
        private ImageView playerSpell1;
        private ImageView playerSpell2;
        private ImageView playerTier;
        private TextView playerDivision;
        private TextView playerNormalWins;
        private TextView playerRankedWins;
        private Button playerRuneButton;
        private TextView playerMastery;
    }

}
