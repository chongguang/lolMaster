package ch.epfl.sweng.lolmaster.api.dto;

import dto.Static.Rune;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolRune extends LolDTO {
    private static final String PER_LEVEL = "per level";

    private Rune mRune;
    private Double mStatValue;
    private String mDescription;

    public LolRune(Rune rune) {
        mRune = rune;
        parse();

    }

    /**
     * Get rune's stat value
     * @return rune's stat value
     */
    public Double getStatValue() {
        return mStatValue;
    }

    /**
     * Get rune's description
     * @return rune's description
     */
    public String getDescription() {
        return mDescription;
    }

    private void parse() {
        String sanitizedDescription = mRune.getSanitizedDescription();
        int valueEndIndex;
        if (sanitizedDescription.contains("%")) {
            valueEndIndex = sanitizedDescription.indexOf('%');
        } else {
            valueEndIndex = sanitizedDescription.indexOf(' ');
        }
        mDescription = sanitizedDescription.substring(valueEndIndex);
        try {
            mStatValue =
                Double
                    .valueOf(sanitizedDescription.substring(0, valueEndIndex));
        } catch (NumberFormatException e) {
            mStatValue = Double.valueOf(0);
            mDescription = "Internal Error";
        }
        if (mDescription.contains(PER_LEVEL)) {
            int troncIndex =
                mDescription.indexOf(PER_LEVEL) + PER_LEVEL.length() + 1;
            mDescription = mDescription.substring(0, troncIndex);
        }

    }
}
