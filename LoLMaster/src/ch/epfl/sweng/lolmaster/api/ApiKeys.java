package ch.epfl.sweng.lolmaster.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ch.epfl.sweng.lolmaster.assetsmanagers.ConfigurationException;

import android.content.Context;

/**
 * A manager for keys stored in the file system
 * 
 * @author fKunstner
 */
public class ApiKeys {
    private final String[] mRiotKeys;
    private final String[] mMashapeKeys;
    private int mRiotKeyIndex = 0;
    private int mMashapeKeyIndex = 0;

    public ApiKeys(Context context) {
        mRiotKeys = ApiKeyLoader.loadRiotKeys(context);
        mMashapeKeys = ApiKeyLoader.loadMashapeKeys(context);
    }

    public synchronized String getRiotKey() {
        mRiotKeyIndex = (mRiotKeyIndex + 1) % mRiotKeys.length;
        return mRiotKeys[mRiotKeyIndex];
    }

    public synchronized String getMashapeKey() {
        mMashapeKeyIndex = (mMashapeKeyIndex + 1) % mMashapeKeys.length;
        return mMashapeKeys[mMashapeKeyIndex];
    }

    /**
     * Loads keys from .properties files
     * 
     * @author fKunstner
     */
    private static final class ApiKeyLoader {
        private static final String RIOT_KEYS_FILENAME = "riotKeys.properties";
        private static final String MASHAPE_KEYS_FILENAME =
            "mashapeKeys.properties";

        private ApiKeyLoader() {

        }

        public static String[] loadRiotKeys(Context context) {
            Properties readProperties = loadFile(context, RIOT_KEYS_FILENAME);

            // Check validity
            List<String> riotKeys = new ArrayList<String>();
            for (Object v : readProperties.values()) {
                if (!ValidationRules.validRiotKey(v.toString())) {
                    throw new ConfigurationException(
                        "Riot api key is invalid : " + v + " in "
                            + RIOT_KEYS_FILENAME);
                } else {
                    riotKeys.add(v.toString());
                }
            }

            // Check if not empty
            if (riotKeys.isEmpty()) {
                throw new ConfigurationException("No riot api key found in "
                    + RIOT_KEYS_FILENAME);
            }

            riotKeys = removeDuplicate(riotKeys);

            return riotKeys.toArray(new String[riotKeys.size()]);
        }

        public static String[] loadMashapeKeys(Context context) {
            Properties readProperties =
                loadFile(context, MASHAPE_KEYS_FILENAME);

            // Check validity
            List<String> mashapeKeys = new ArrayList<String>();
            for (Object v : readProperties.values()) {
                if (!ValidationRules.validMashapeKey(v.toString())) {
                    throw new ConfigurationException(
                        "Mashape api key is invalid : " + v + " in "
                            + MASHAPE_KEYS_FILENAME);
                } else {
                    mashapeKeys.add(v.toString());
                }
            }

            // Check if not empty
            if (mashapeKeys.isEmpty()) {
                throw new ConfigurationException("No mashape api key found in "
                    + MASHAPE_KEYS_FILENAME);
            }

            mashapeKeys = removeDuplicate(mashapeKeys);

            return mashapeKeys.toArray(new String[mashapeKeys.size()]);

        }

        private static List<String> removeDuplicate(List<String> keys) {
            List<String> noDuplicateKeys = new ArrayList<String>();
            for (String key : keys) {
                if (!noDuplicateKeys.contains(key)) {
                    noDuplicateKeys.add(key);
                }
            }
            return noDuplicateKeys;
        }

        private static Properties loadFile(Context context, String fileName) {
            Properties properties = new Properties();

            try {
                InputStream in =
                    context.getResources().getAssets().open(fileName);
                properties.load(in);
                in.close();
            } catch (IOException e) {
                throw new ConfigurationException("Unable to load api keys.", e);
            }

            return properties;
        }

        /**
         * Helper class to check validity of passed keys.
         * 
         * @author fKunstner
         */
        private static final class ValidationRules {
            private static final int RIOTKEY_LENGHT = 36;
            private static final int[] RIOTKEY_DASH_POSITIONS = new int[] {8,
                13, 18, 23 };
            private static final int MASHAPEKEY_LENGHT = 50;

            private ValidationRules() {

            }

            /**
             * Provide basic rules checking
             * 
             * @param key
             * @return
             */
            private static boolean validRiotKey(String key) {
                if (key.length() != RIOTKEY_LENGHT) {
                    return false;
                }

                boolean valid = true;

                // Has dashes at specified positions
                for (int i = 0; i < RIOTKEY_DASH_POSITIONS.length; i++) {
                    valid &= key.charAt(RIOTKEY_DASH_POSITIONS[i]) == '-';
                }
                // Contains only lower case
                valid &= key.equals(key.toLowerCase());

                return valid;
            }

            private static boolean validMashapeKey(String key) {
                return key.length() == MASHAPEKEY_LENGHT;
                // Additionnal rules may be
                // m at position 11
                // jsn at position 35-38
            }

        }
    }
}
