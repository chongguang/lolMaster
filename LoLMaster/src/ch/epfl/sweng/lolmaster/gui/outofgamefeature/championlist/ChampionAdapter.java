/**
 * 
 */
package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage.ChampionPage;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class ChampionAdapter extends BaseAdapter {

    private List<OutOfGameChampion> mChampions =
        new ArrayList<OutOfGameChampion>();

    private OutOfGameChampion mChampion;

    private LayoutInflater mInflater;

    private Context mContext;

    public ChampionAdapter(Context context, List<OutOfGameChampion> champions) {
        if (champions != null) {
            mChampions = champions;
        }
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mChampions.size();
    }

    @Override
    public Object getItem(int position) {
        return mChampions.get(position);
    }

    // We don't need to access the ID of the ChampLayouts,
    // as they are dynamically created.
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // We might need the champion's name though
    public String getChampionName(int position) {
        return mChampions.get(position).getName();
    }

    // We might need the champion's data
    public LolId getChampionData(int position) {
        return mChampions.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mChampion = (OutOfGameChampion) getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView =
                mInflater.inflate(R.layout.champion_image, parent, false);
            viewHolder.champImage =
                (ImageView) convertView
                    .findViewById(R.id.outOfGameChampionImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.champImage.setImageDrawable(ImageFetcher.getImage(mContext,
            mChampion.getImage()));
        viewHolder.champImage.setOnClickListener(new View.OnClickListener() {
            private final OutOfGameChampion champion = mChampion;

            @Override
            public void onClick(View view) {
                Intent intent =
                    new Intent(mContext.getApplicationContext(),
                        ChampionPage.class);

                intent.putExtra("name", champion.getName());
                intent.putExtra("id", champion.getId().getValue());
                mContext.startActivity(intent);
            }
        });
        return convertView;

    }

    /**
     * @author ajjngeor (alain george : 217451)
     * 
     */
    private static class ViewHolder {

        private ImageView champImage;
    }

}
