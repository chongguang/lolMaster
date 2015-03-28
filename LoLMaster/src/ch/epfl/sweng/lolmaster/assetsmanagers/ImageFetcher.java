/**
 * 
 */
package ch.epfl.sweng.lolmaster.assetsmanagers;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.dto.LolImage;
import ch.epfl.sweng.lolmaster.gui.outofgamefeature.championlist.OutOfGameChampion;

/**
 * Utility class to get {@see Drawable} from image in the asset folder.
 * 
 * @author ajjngeor (alain george : 217451)
 */
public abstract class ImageFetcher {
    private static Drawable mEmptyItemSlot = null;
    private static final int IMAGE_SIZE = 100;
    private static final int CORNER_ROUNDING = 2;
    private static final int BORDER_SIZE = 2;

    private ImageFetcher() {

    }

    /**
     * Get a {@see Drawable} from an image group {@see LolImage#getGroup()} and
     * filename {@see LolImage#getFull()}
     * 
     * @param context
     *            A valid context to load assets from.
     * @param group
     *            The group of a {@see LolImage}
     * @param fileName
     *            The full name of a {@see LolImage}
     * @return
     */
    public static Drawable getImage(Context context, String group,
            String fileName) {
        String path = "img/" + group + "/" + fileName;
        return getDrawableByPath(context, path);
    }

    public static Drawable getChampionBanner(Context context,
            String championName) {

        String iconPath = "champion_banners/" + normalizedString(championName)
                + "_banner.png";
        return getDrawableByPath(context, iconPath);
    }

    public static Drawable getRankIcon(Context context, String rankName) {
        String iconPath = "ranked_icons/" + normalizedString(rankName) + ".png";
        return getDrawableByPath(context, iconPath);
    }

    private static String normalizedString(String s) {
        String normalizedString = s;

        normalizedString = normalizedString.replaceAll("\\s+", "");
        normalizedString = normalizedString.replaceAll("'", "");
        normalizedString = normalizedString.replaceAll("\\.", "");
        normalizedString = normalizedString.toLowerCase();

        return normalizedString;
    }

    private static Drawable getDrawableByPath(Context context, String path) {
        Drawable draw = null;
        try {
            InputStream ims;
            ims = context.getAssets().open(path);
            draw = Drawable.createFromStream(ims, null);
        } catch (IOException e) {
            String errorMessage = "Error when trying to fetch the image at location "
                    + path + ", replacing by default bitmap.";
            Log.w(OutOfGameChampion.class.getName(), errorMessage, e);
            draw = getNotFoundImage(context);
        }
        return draw;
    }

    /**
     * Gets the default image for not found items, representing a [?]
     * 
     * @param context
     * @return
     */
    public static Drawable getNotFoundImage(Context context) {
        try {
            InputStream ims;
            ims = context.getAssets().open("champion_icons/error_square_0.png");
            return Drawable.createFromStream(ims, null);
        } catch (IOException e) {
            String errorMessage = "Couldn't access the asset folder.";
            throw new ConfigurationException(errorMessage, e);
        }
    }

    /**
     * Gets the image for empty item slots.
     * 
     * @param context
     *            The application context
     * @return
     */
    public static synchronized Drawable getEmptyItemSlotImage(Context context) {
        if (mEmptyItemSlot == null) {
            int size = IMAGE_SIZE;
            int minCoord = BORDER_SIZE;
            int maxCoord = IMAGE_SIZE - BORDER_SIZE;

            Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);

            RectF rect = new RectF(minCoord, minCoord, maxCoord, maxCoord);

            Paint paint = new Paint();
            paint.setColor(Color.LTGRAY);

            c.drawRoundRect(rect, CORNER_ROUNDING, CORNER_ROUNDING, paint);

            mEmptyItemSlot = new BitmapDrawable(context.getResources(), b);
        }
        return mEmptyItemSlot;
    }

    /**
     * Get an image based on a {@see LolImage}.
     * 
     * @param context
     * @param imageData
     * @return
     */
    public static Drawable getImage(Context context, LolImage imageData) {
        return getImage(context, imageData.getGroup(), imageData.getFull());
    }
}
