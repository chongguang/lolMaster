package ch.epfl.sweng.lolmaster.api.dto;

import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.Player;

/**
 * @author fKunstner
 */
public class LolPlayer extends LolDTO {
    private final Player mPlayer;

    public LolPlayer(Player player) {
        mPlayer = player;
    }

    /**
     * Get the normalized name of the summoner.
     * 
     * This name is good for equality checks, but might not be equal to the
     * displayable version of this summoner name. {@see
     * LolPlayer.getSummonerName()}
     * 
     * @return The name of the summoner.
     */
    public String getSummonerInternalName() {
        return mPlayer.getSummonerInternalName();
    }

    /**
     * Get the (not normalized) name of the summoner.
     * 
     * This name is not normalized and thus should not be used for equality
     * checks but for display purposes. {@see
     * LolPlayer.getinternalSummonerName()}
     * 
     * @return The name of the summoner.
     */
    public String getSummonerName() {
        return mPlayer.getSummonerName();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof LolPlayer) {
            LolPlayer that = (LolPlayer) object;
            boolean equals = true;
            equals &=
                this.mPlayer.getSummonerId() == that.mPlayer.getSummonerId();
            equals &=
                this.mPlayer.getSummonerInternalName().equals(
                    that.mPlayer.getSummonerInternalName());
            equals &=
                this.mPlayer.getSummonerName().equals(
                    that.mPlayer.getSummonerName());
            return equals;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getSummonerName().hashCode();
    }

    /**
     * Get summoner ID
     * @return summoner's {@see LolId}
     */
    public LolId getSummonerId() {
        return new LolId(mPlayer.getSummonerId());
    }
}
