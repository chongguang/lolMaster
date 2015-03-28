package ch.epfl.sweng.lolmaster.api.mashape;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import ch.epfl.sweng.lolmaster.api.mashape.MashapeApiException.MashapeError;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import constant.Region;

/**
 * @author fKunstner
 */
public class MashapeApi {
    private static final String BASE_URL =
        "https://spectator-league-of-legends-v1.p.mashape.com/lol/";
    private static final String HEADER_KEY_NAME = "X-Mashape-Key";
    private Map<String, String> headers;
    private String mKey;
    private Region mRegion;

    public MashapeApi(String key, Region region) {
        setKey(key);
        setRegion(region);
        headers = new HashMap<String, String>();
        headers.put(HEADER_KEY_NAME, mKey);
    }

    public void setKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key is null or empty");
        }
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }

    public void setRegion(Region region) {
        if (region == null) {
            throw new IllegalArgumentException("Region is null");
        }
        mRegion = region;
    }

    public String getRegion() {
        return mRegion.getName();
    }

    /**
     * Get informations about a game being currently played, or null if none
     * could be found for the requested player.
     * 
     * @param summonerName
     *            The name of the summoner you want to find current game data
     *            for.
     * @return the requested {@code InProgressGameInfo}, or null if none could
     *         be found.
     * @throws MashapeApiException
     *             If the information could not be retrieved.
     */
    public InProgressGameInfo getInProgessGameInfo(String summonerName)
        throws MashapeApiException {
        if (summonerName == null || summonerName.isEmpty()) {
            return null;
        }

        String version = "v1";
        String section = "spectator";
        String function = "by-name";
        String arg = summonerName;
        String url = getUrl(version, section, function, arg);

        HttpURLConnection connection;
        try {
            connection = ConnectionUtils.openConnection(url, headers);
        } catch (IOException e) {
            throw new MashapeApiException(MashapeError.UNKNOWN_ERROR, e);
        }

        int responseCode;
        try {
            responseCode = connection.getResponseCode();
            if (responseCode != MashapeError.OK.getCode()) {
                throw new MashapeApiException(
                    MashapeError.getMashapeError(responseCode));
            }
        } catch (IOException e) {
            throw new MashapeApiException(MashapeError.UNKNOWN_ERROR, e);
        }

        String json;
        try {
            json = ConnectionUtils.getContent(connection);
        } catch (IOException e) {
            throw new MashapeApiException(MashapeError.UNKNOWN_ERROR, e);
        }

        InProgressGameInfo info;
        try {
            info = new Gson().fromJson(json, InProgressGameInfo.class);
        } catch (JsonSyntaxException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
            throw new MashapeApiException(MashapeError.PARSE_FAILURE);
        }

        return info;
    }

    private String getUrl(String version, String section, String function,
        String arg) {
        return BASE_URL + mRegion.name().toLowerCase() + "/" + version + "/"
            + section + "/" + function + "/" + arg;
    }

    // defines clone() but doesn't implement cloneable() and doesn't call
    // super.clone()
    public MashapeApi duplicate() {
        return new MashapeApi(mKey, mRegion);
    }
}
