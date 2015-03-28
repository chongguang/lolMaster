package ch.epfl.sweng.lolmaster.utils;

/**
 * A helper class containing static methods for trivial tasks in managing Api
 * responses.
 * 
 * @author fKunstner
 */
public final class StringUtils {

    private StringUtils() {

    }

    /**
     * Normalize the given name.
     * 
     * Normalize means casting every letter to lowercase and eliminating spaces,
     * such that a
     * {@code normalizeName(summonerName).equals(summonerInternalName)}
     * 
     * Note that
     * {@code normalizeName(summonerInternalName).equals(summonerInternalName)}
     * 
     * @param name
     *            A name to normalize.
     * @return the normalized version of the name.
     */
    public static String normalizeName(String name) {
        return name.toLowerCase().replaceAll("\\s+", "");
    }
}
