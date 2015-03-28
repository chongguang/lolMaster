package ch.epfl.sweng.lolmaster.api;

import android.content.Context;

/**
 * A manager for keys stored in the file system
 * 
 * @author fKunstner
 */
public final class ApiKeysManager {
    private static ApiKeys mApiKeysSingleton = null;

    private ApiKeysManager() {

    }

    public synchronized static void onCreate(Context context) {
        if (mApiKeysSingleton == null) {
            mApiKeysSingleton = new ApiKeys(context);
        }
    }

    /**
     * Get API keys.
     * 
     * @return An {@see ApiKeys} object containing the api keys needed.
     * @throws ApiKeyException
     *             If the api keys could not be retrieved
     */
    public synchronized static ApiKeys getApiKeys() {
        if (mApiKeysSingleton == null) {
            throw new IllegalStateException(
                "Tried to access api keys before initialisation.");
        }
        return mApiKeysSingleton;
    }
}
