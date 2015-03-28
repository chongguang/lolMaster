package ch.epfl.sweng.lolmaster.gui;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.sweng.lolmaster.R;
import ch.epfl.sweng.lolmaster.gui.LoadingTask.ProgressUpdate;

/**
 * An AsyncTask that has built-in support for progress bar and exception
 * handling.
 * 
 * @author fKunstner
 * 
 * @param <PARAM>
 * @param <RESULT>
 */
public abstract class LoadingTask<PARAM, RESULT> extends
    AsyncTask<PARAM, ProgressUpdate, RESULT> {
    private static final int LOADING_BAR_LAYOUT =
        R.layout.activity_in_game_load_players;
    private static final int LOADING_LAYOUT = R.layout.loading_screen;
    private static final int PROGRESS_BAR =
        R.id.in_game_load_players_progress_bar;
    private static final int TEXT_VIEW = R.id.in_game_load_players_player_name;
    private String mCancelMessage = null;
    private Activity mActivity;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private ProgressUpdater mProgressUpdater;
    private boolean isOnLoadingBar = false;

    public <T extends Activity> LoadingTask(Activity activity) {
        mActivity = activity;
        mProgressUpdater = new ProgressUpdater();
    }

    protected void cancel(String message) {
        mCancelMessage = message;
        cancel(false);
    }

    @Override
    protected void onCancelled(RESULT result) {
        Log.v(getClass().getName(), "onPreExecute");
        if (mCancelMessage != null) {
            Toast.makeText(mActivity, mCancelMessage, Toast.LENGTH_LONG).show();
        }
        getActivity().finish();
    }

    @Override
    protected void onPreExecute() {
        Log.v(getClass().getName(), "onPreExecute");
        mActivity.setContentView(LOADING_LAYOUT);
    }

    @Override
    protected final void onProgressUpdate(ProgressUpdate... progressUpdates) {
        ProgressUpdate progress = progressUpdates[0];
        Log.v(getClass().getName(), "onProgressUpdate(" + progress + ")");

        if (!isOnLoadingBar) {
            mActivity.setContentView(LOADING_BAR_LAYOUT);
            mTextView = (TextView) mActivity.findViewById(TEXT_VIEW);
            mProgressBar = (ProgressBar) mActivity.findViewById(PROGRESS_BAR);
            isOnLoadingBar = true;
        }

        mTextView.setText(progress.getMessage());
        mProgressBar.setMax(progress.getMax());
        mProgressBar.setProgress(progress.getProgress());
    }

    /**
     * Access the activity that was provided at construction.
     * 
     * @return the activity that was provided at construction.
     */
    protected Activity getActivity() {
        return mActivity;
    }

    /**
     * Return the {@see ProgressUpdater} of the {@see LoadingTask}.
     * 
     * This class is a helper for the {@see
     * LoadingTask#publishProgress(Object...)} function.
     * 
     * The progress updater knows how many times you called {@see
     * ProgressUpdater#update()} and will display a progress bar loaded at
     * {@code #progress/max}
     * 
     * If you know how many updates you want to do before hand, call {@see
     * ProgressUpdater#setMaxUpdate(int)} to have a more precise loading bar.
     * Otherwise, {@code max = #progress + 1}
     * 
     * You can also call {@see ProgressUpdater#update(String)} to specify a
     * message to describe the current step that is being computed.
     * 
     * @return the {@see ProgressUpdater} of the {@see LoadingTask}.
     */
    protected ProgressUpdater getProgressUpdater() {
        return mProgressUpdater;
    }

    /**
     * A utility class to send {@see
     * LoadingTask#publishProgress(ProgressUpdate)} easier.
     * 
     * @author fKunstner
     */
    protected class ProgressUpdater {
        private int mMax;
        private int mProgress;

        protected ProgressUpdater() {
            mProgress = -1; // We haven't started yet.
            mMax = 0;
        }

        /**
         * Sets the max number of update you plan to do.
         * 
         * This is not a hard limit, if you exceed it it will be computed to be
         * the number of {@code update + 1}
         * 
         * @param max
         *            The max number of updates you plan to do.
         * @throws IllegalArgumentException
         *             if {@code max <= 0}
         */
        public void setMaxUpdate(int max) {
            if (max <= 0) {
                throw new IllegalArgumentException(
                    "The max number of progress updates needs to be > 0");
            }
            mMax = max;
        }

        /**
         * Increments the counter of updates, without showing an error message.
         * 
         * A shortcut for {@see ProgressUpdater#update("")}
         */
        public void update() {
            update("");
        }

        /**
         * Increments the counter of updates and show the {@code message}
         * describing the current step.
         * 
         * @param message
         *            A message describing the current step being computed.
         */
        public void update(String message) {
            mProgress++;
            publishProgress(new ProgressUpdate(mProgress, getMax(), message));
        }

        private int getMax() {
            if (mMax < mProgress) {
                return mProgress + 1;
            } else {
                return mMax;
            }
        }
    }

    /**
     * A wrapper class to send progress status via {@see
     * LoadingTask#publishProgress(ProgressUpdate)}
     * 
     * @author fKunstner
     */
    public static class ProgressUpdate {
        private int mProgress;
        private String mMessage;
        private int mMax;

        public ProgressUpdate(int progress, int max, String message) {
            mProgress = progress;
            mMax = max;
            mMessage = message;
        }

        public int getProgress() {
            return mProgress;
        }

        public String getMessage() {
            return mMessage;
        }

        public int getMax() {
            return mMax;
        }
    }

}
