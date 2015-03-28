package ch.epfl.sweng.lolmaster.assetsmanagers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import android.content.Context;
import android.util.Log;
import constant.Region;

/**
 * The user preference. Used to store data across multiple launches of the
 * application.
 * 
 * Stores the userName and the region of the user.
 * 
 * @author fKunstner
 * 
 */
public final class UserPreferences {
    private static final String DEFAULT_USERNAME = "";
    private static final Region DEFAULT_REGION = Region.EUW;
    private static final UserPreferences DEFAULT_PREFS = new UserPreferences(
        DEFAULT_USERNAME, DEFAULT_REGION);
    private static final String USERNAME_KEY = "username";
    private static final String REGION_KEY = "region";
    private static final String USER_PREFERENCES_FILENAME =
        "userPreferences.property";
    private String mUserName;
    private Region mRegion;

    private UserPreferences(String userName, Region region) {
        mUserName = userName;
        mRegion = region;
    }

    public String getUserName() {
        return mUserName;
    }

    public Region getRegion() {
        return mRegion;
    }

    public static void storeUserPreferences(Context context, String username,
        Region region) {
        Properties properties = new Properties();

        properties.setProperty(USERNAME_KEY, username);
        properties.setProperty(REGION_KEY, region.name());

        try {
            OutputStream out =
                context.openFileOutput(USER_PREFERENCES_FILENAME,
                    Context.MODE_PRIVATE);
            properties.store(out, "");
        } catch (IOException e) {
            Log.e(UserPreferences.class.getName(),
                "Unable to store user preferences.", e);
        }
    }

    public static UserPreferences getUserPreferences(Context context) {
        Properties properties = new Properties();

        try {
            InputStream in = context.openFileInput(USER_PREFERENCES_FILENAME);
            properties.load(in);
            in.close();
        } catch (IOException e) {
            Log.e(UserPreferences.class.getName(),
                "Unable to get user preferences.", e);
            return DEFAULT_PREFS;
        }

        String userName = properties.getProperty(USERNAME_KEY, "");
        String region = properties.getProperty(REGION_KEY, Region.EUW.name());

        if (!validRegion(region)) {
            return DEFAULT_PREFS;
        } else {
            return new UserPreferences(userName, Region.valueOf(region));
        }
    }

    private static boolean validRegion(String region) {
        try {
            Region.valueOf(region);
        } catch (IllegalArgumentException e) {
            Log.e(UserPreferences.class.getName(), "Invalid region name.", e);
            return false;
        }
        return true;
    }
}
