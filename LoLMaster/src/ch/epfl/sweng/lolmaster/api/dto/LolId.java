package ch.epfl.sweng.lolmaster.api.dto;

/**
 * A LolId encapsulating an Id value returned by the Api, because it randomly
 * returns int, long or String.
 * 
 * It's only usage is checking equality {@see LolId.equals}.
 * 
 * @author fKunstner
 * 
 */
public final class LolId extends LolDTO {
    private final int mValue;

    public LolId(long value) {
        if (value < 0 || value > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(value
                + " is not a valid representationg of an Id");
        }
        mValue = (int) value;
    }

    public LolId(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(value
                + " is negative : not a valid representation of an Id");
        }
        mValue = value;
    }

    public LolId(String value) {
        // Will throw a NumberFormatException if invalid representation of ID.
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < 0) {
                throw new IllegalArgumentException(value
                    + " is negative : not a valid representationg of an Id");
            }
            mValue = intValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(value
                + " is not a valid reprensation of an Id");
        }
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof LolId) {
            return this.mValue == ((LolId) that).mValue;
        } else {
            return false;
        }
    }

    /**
     * This method should only be used in the Api to make calls. Use {@see
     * LolId.equals} for equality checks.
     * 
     * @return
     */
    public int getValue() {
        return mValue;
    }

    @Override
    public int hashCode() {
        return mValue;
    }
}
