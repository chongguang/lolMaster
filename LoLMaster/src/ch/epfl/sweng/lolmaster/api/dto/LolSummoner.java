package ch.epfl.sweng.lolmaster.api.dto;

import dto.Summoner.Summoner;

/**
 * @author fKunstner
 */
final public class LolSummoner extends LolDTO {
    private final Summoner mSummoner;

    public LolSummoner(Summoner summoner) {
        if (summoner == null) {
            throw new IllegalArgumentException("Provided summoner is null.");
        }
        if (summoner.getId() < 0 || summoner.getId() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                "The provided summoner has invalid Id : " + summoner.getId());
        }
        mSummoner = summoner;
    }

    /**
     * Get the Id of the summoner
     * 
     * @return the {@see LolId} of the summoner.
     */
    public LolId getId() {
        return new LolId(mSummoner.getId());
    }

    /**
     * Get the (not normalized) name of the summoner.
     * 
     * This name is not normalized and thus should not be used for equality
     * checks but for display purposes.
     * 
     * @return The name of the summoner.
     */
    public String getName() {
        return mSummoner.getName();
    }

}
