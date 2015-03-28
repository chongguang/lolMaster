package ch.epfl.sweng.lolmaster;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.ApiException;
import ch.epfl.sweng.lolmaster.api.ApiKeysManager;
import ch.epfl.sweng.lolmaster.api.directapi.LolStaticDataApi;

/**
 * The base of the Application. Does all the hard work on start.
 * 
 * @author fKunstner
 */
public class LolMasterApplication extends Application {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime()
        .availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    private static final String STARTING_MESSAGE = "Starting ...";
    private static final String FINISHED_MESSAGE = "Finished.";
    private static final String UNABLE_TO_POPULATE_CACHE_MESSAGE =
        "Unable to populate cache.";

    private final BlockingQueue<Runnable> mWorkQueue =
        new LinkedBlockingQueue<Runnable>();
    private final ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(
        NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME,
        KEEP_ALIVE_TIME_UNIT, mWorkQueue);

    @Override
    public void onCreate() {
        ApiKeysManager.onCreate(getApplicationContext());

        mThreadPool.execute(new ItemCachePopulator());
        mThreadPool.execute(new SpellCachePopulator());
        mThreadPool.execute(new ChampionCachePopulator());
        mThreadPool.execute(new MasteryCachePopulator());
        mThreadPool.execute(new RuneCachePopulator());
    }

    /**
     * Base class for Simple threaded call to the Riot Api to preload
     * information.
     * 
     * @author fKunstner
     */
    private static abstract class CachePopulator implements Runnable {
        private void onStart() {
            Log.v(getClass().getName(), STARTING_MESSAGE);
        }

        public final void run() {
            onStart();
            try {
                populateCache();
            } catch (ApiException e) {
                Log.e(this.getClass().getName(),
                    UNABLE_TO_POPULATE_CACHE_MESSAGE, e);
            }
            onFinish();
        }

        private void onFinish() {
            Log.v(this.getClass().getName(), FINISHED_MESSAGE);
        }

        protected abstract void populateCache() throws ApiException;
    }

    /**
     * Simple threaded call to the Riot Api to preload champion information
     * 
     * @author fKunstner
     */
    private static class ChampionCachePopulator extends CachePopulator {
        @Override
        protected void populateCache() throws ApiException {
            LolStaticDataApi.Champion.get();
        }
    }

    /**
     * Simple threaded call to the Riot Api to preload items information
     * 
     * @author fKunstner
     */
    private static class ItemCachePopulator extends CachePopulator {
        @Override
        protected void populateCache() throws ApiException {
            LolStaticDataApi.Item.get();
        }
    }

    /**
     * Simple threaded call to the Riot Api to preload spells information
     * 
     * @author fKunstner
     */
    private static class SpellCachePopulator extends CachePopulator {
        @Override
        protected void populateCache() throws ApiException {
            LolStaticDataApi.Item.get();
        }
    }

    /**
     * Simple threaded call to the Riot Api to preload masteries information
     * 
     * @author fKunstner
     */
    private static class MasteryCachePopulator extends CachePopulator {
        @Override
        public void populateCache() throws ApiException {
            LolStaticDataApi.Mastery.get();
        }
    }

    /**
     * Simple threaded call to the Riot Api to preload runes information
     * 
     * @author fKunstner
     */
    private static class RuneCachePopulator extends CachePopulator {
        @Override
        protected void populateCache() throws ApiException {
            LolStaticDataApi.Rune.get();
        }
    }
}
