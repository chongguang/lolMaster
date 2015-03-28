package ch.epfl.sweng.lolmaster.gui.outofgamefeature.championpage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.LolApi;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolChampionSpell;
import ch.epfl.sweng.lolmaster.api.dto.LolPassive;
import ch.epfl.sweng.lolmaster.api.dto.LolSpellVars;
import ch.epfl.sweng.lolmaster.assetsmanagers.ImageFetcher;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.OutOfGameActivity;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.OutOfGameChampion;

/**
 * Activity launched when the user click on a champion in the OutOfGameActivity
 * 
 * @author ygrimault
 * 
 */
public class ChampionPage extends FragmentActivity {

    private TabHost mTabHost;
    private ViewPager myPager;
    private TabsAdapter mTabsAdapter;
    private String championName;
    private int championId;
    private LolChampionData champData;
    private Intent intent;
    private static final int PADDING = 32;
    private static final float RATIO = (float) 4.5;

    private static final int Q_PLACE = 0;
    private static final int W_PLACE = 1;
    private static final int E_PLACE = 2;
    private static final int R_PLACE = 3;

    /**
     * Regroup the different possibilities for the spell key
     * 
     * @author ygrimault
     * 
     */
    public enum SpellKey {
        QSPELL("q_spell", Q_PLACE), WSPELL("w_spell", W_PLACE), ESPELL(
                "e_spell", E_PLACE), RSPELL("r_spell", R_PLACE);

        private final String skKey;
        private final int skPos;

        private SpellKey(String key, int pos) {
            skKey = key;
            skPos = pos;
        }

        public String getKey() {
            return this.skKey;
        }

        public int getPos() {
            return this.skPos;
        }
    }

    /**
     * Fetches the data in background, then keeps going
     * 
     * @author ygrimault
     * 
     */
    public class DownloadChampionData extends
            AsyncTask<Integer, Void, LolChampionData> {

        private void handleException(ApiException e) {
            String errorMessage = "Error when trying to fetch the data for "
                    + championName + ".";
            Log.w(OutOfGameChampion.class.getName(), errorMessage);
            toastErrorMessage(errorMessage);

            Intent outOfGameIntent = new Intent(ChampionPage.this,
                    OutOfGameActivity.class);
            startActivity(outOfGameIntent);
        }

        private void toastErrorMessage(String errorMessage) {
            final String message = errorMessage;
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), message,
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        protected LolChampionData doInBackground(Integer... params) {
            try {
                LolApi api = LolApi.getApi();
                int id = params[0];
                champData = api.getDataChampionById(id);
            } catch (ApiException e) {
                handleException(e);
                return null;
            }

            return champData;
        }

        protected void onPostExecute(LolChampionData championData) {
            if (championData == null) {
                return;
            }

            onFinishInflate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_page);

        // We need to retrieve the name and the data of the selected champion
        intent = getIntent();
        championName = intent.getExtras().getString("name");
        championId = intent.getExtras().getInt("id");
        new DownloadChampionData().execute(championId);
    }

    protected void onFinishInflate() {

        // We first need to set the data into the intent, so it's useable by the
        // tabs
        // NB : we can't give custom classes to the intent, so we need to
        // separate everything
        setDataIntent();

        // Now we display the banner on top of the tabs
        Drawable img = ImageFetcher.getChampionBanner(this, championName);
        ImageView imageView = (ImageView) findViewById(R.id.champion_banner);
        imageView.setImageDrawable(img);
        imageView = setBannerScale(imageView);

        // And don't we forget the tabs !
        mTabHost = (TabHost) findViewById(R.id.info_tab_host);
        mTabHost.setup();

        myPager = (ViewPager) findViewById(R.id.champion_info);
        mTabsAdapter = new TabsAdapter(this, mTabHost, myPager);

        mTabsAdapter.addTab(mTabHost.newTabSpec("one").setIndicator("Tips"),
                TipsFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("two").setIndicator("Skills"),
                SkillsFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("three").setIndicator("Lore"),
                LoreFragment.class, null);
    }

    /**
     * We need to adapt the banner to the screen size, without forgetting the
     * padding from the theme and the density of the screen
     * 
     * @param image
     * @return
     */
    protected ImageView setBannerScale(ImageView image) {
        Resources r = Resources.getSystem();
        DisplayMetrics display = r.getDisplayMetrics();

        float densityScreen = display.density;
        final int bannerWidth = (int) (display.widthPixels - (PADDING * densityScreen));
        final int bannerHeight = (int) (bannerWidth / RATIO + (PADDING * densityScreen));

        final int finalWidth = (int) bannerWidth;
        final int finalHeight = (int) bannerHeight;
        image.setLayoutParams(new LinearLayout.LayoutParams(finalWidth,
                finalHeight));

        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.champion_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDataIntent() {
        // Putting the tips into the intent
        intent.putExtra("ally_tips", stringRefactor(champData.getAllytips()));
        intent.putExtra("enemy_tips", stringRefactor(champData.getEnnemytips()));

        // Putting the passive into the intent
        LolPassive passive = champData.getPassive();
        intent.putExtra("passive_name", passive.getName());
        intent.putExtra("passive_desc", passive.getDescription());
        intent.putExtra("passive_full", passive.getFull());
        intent.putExtra("passive_group", passive.getGroup());

        // Putting the spells into the intent
        List<LolChampionSpell> spellList = champData.getSpells();
        for (SpellKey sKey : SpellKey.values()) {
            LolChampionSpell spell = spellList.get(sKey.getPos());
            setSpellIntent(spell, sKey.getKey());
        }

        // Putting the lore into the intent
        String lore = champData.getLore();
        intent.putExtra("lore", lore);
    }

    /**
     * It's better to have a proper list of tips (with -) than lines under lines
     * 
     * @param tips
     * @return
     */
    private String stringRefactor(String tips) {
        StringBuilder refTips = new StringBuilder();
        String[] tipsList = tips.split("\n");
        for (String tip : tipsList) {
            refTips.append("- ").append(tip).append("<br>");
        }
        return refTips.toString();
    }

    /**
     * Setting the intent for spells is a bit more complex than for other
     * things, so we generalize it
     * 
     * @param spell
     * @param mKeyShortcut
     * @param intent
     */
    private void setSpellIntent(LolChampionSpell spell, String mKeyShortcut) {
        intent.putExtra(mKeyShortcut + "_name", spell.getName());
        intent.putExtra(mKeyShortcut + "_cooldown", spell.getCooldownBurn());
        intent.putExtra(mKeyShortcut + "_cost", spell.getCostBurn());
        intent.putExtra(mKeyShortcut + "_resource", spell.getResource());
        intent.putExtra(mKeyShortcut + "_range", spell.getRangeBurn());
        intent.putExtra(mKeyShortcut + "_tooltip", spell.getTooltip());
        intent.putExtra(mKeyShortcut + "_effects", spell.getEffectBurn()
                .toArray(new String[spell.getEffectBurn().size()]));
        intent.putExtra(
                mKeyShortcut + "_var_values",
                spell.getVars().toArray(
                        new LolSpellVars[spell.getVars().size()]));
        intent.putExtra(mKeyShortcut + "_full", spell.getFull());
        intent.putExtra(mKeyShortcut + "_group", spell.getGroup());
    }

    /**
     * Class used to display the slides using fragments and tabs.
     * 
     * @author Copy-paste from
     *         http://www.rogcg.com/blog/2013/10/20/working-with-
     *         fragments-and-viewpager-on-android I only solved the checkstyle
     *         problems and added javadocs
     * 
     */
    public static class TabsAdapter extends FragmentPagerAdapter implements
            TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final List<TabInfo> mTabs = new ArrayList<TabInfo>();

        /**
         * To be honest, I don't know exactly what this class is for, but it
         * seems it helps creating the tabs
         */
        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String tTag, Class<?> tClass, Bundle tArgs) {
                tag = tTag;
                clss = tClass;
                args = tArgs;
            }

            public String getTag() {
                return tag;
            }
        }

        /**
         * There is one element that we need to create, but don't need to
         * display, and that's what this class does.
         */
        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost,
                ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);

            return Fragment.instantiate(mContext, info.clss.getName(),
                    info.args);

        }

        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }
}
