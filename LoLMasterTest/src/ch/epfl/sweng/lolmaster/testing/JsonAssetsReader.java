package ch.epfl.sweng.lolmaster.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * Helper class to get json files as string stored in
 * assets/defaultJsonResponses/
 * 
 * @author fKunstner
 */
public abstract class JsonAssetsReader {
    private final static String PATH = "defaultJsonResponses/";

    public static InputStream getFileInputStream(Context context,
        String fileName) throws IOException {
        AssetManager manager = context.getResources().getAssets();

        return manager.open(PATH + fileName);
    }

    public static String getFileContent(Context context, String fileName)
        throws IOException {
        InputStream inStream = null;
        BufferedReader reader = null;
        try {
            inStream = getFileInputStream(context, fileName);

            reader = new BufferedReader(
                new InputStreamReader(inStream, "UTF-8"));

            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            return response.toString();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }
}
