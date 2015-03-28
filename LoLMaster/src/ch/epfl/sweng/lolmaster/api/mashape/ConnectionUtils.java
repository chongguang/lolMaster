package ch.epfl.sweng.lolmaster.api.mashape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An utility class to manage (HttpUrl)Connection
 * 
 * @author fKunstner
 */
public final class ConnectionUtils {

    private ConnectionUtils() {

    }

    /**
     * Reads the inputStream of the given connection to a String
     * 
     * @param connection
     *            The {@code HttpUrlConnection} to read
     * @return A String containing the full inputStream
     * @throws IOException
     *             if the data could not be retrieved.
     */
    public static String getContent(HttpURLConnection connection)
        throws IOException {
        InputStream is = null;
        BufferedReader rd = null;
        String line;
        StringBuilder response = new StringBuilder();
        try {
            is = connection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (rd != null) {
                rd.close();
            }
        }
        return response.toString();
    }

    public static HttpURLConnection openConnection(String url)
        throws IOException {
        return openConnection(url, null);
    }

    /**
     * Opens a {@code HttpURLConnection} to specified url with specified
     * headers.
     * 
     * @param url
     *            The URL to perform a GET request on.
     * @param headers
     *            A map of key -> value to set to the headers.
     * @return An open {@code HttpURLConnection}.
     * @throws IOException
     *             if an error occurs while opening the connection.
     */
    public static HttpURLConnection openConnection(String url,
        Map<String, String> headers) throws IOException {
        HttpURLConnection connection =
            (HttpURLConnection) new URL(url).openConnection();

        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(false);

        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return connection;
    }
}
