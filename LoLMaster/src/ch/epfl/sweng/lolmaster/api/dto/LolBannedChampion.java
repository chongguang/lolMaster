package ch.epfl.sweng.lolmaster.api.dto;

import ch.epfl.sweng.lolmaster.api.dto.LolTeam.TeamId;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.BannedChampion;

/**
 * @author fKunstner
 */
final public class LolBannedChampion extends LolDTO {

    private final BannedChampion mBannedChampion;

    public LolBannedChampion(BannedChampion bannedChampion) {
        this.mBannedChampion = bannedChampion;
    }

    /**
     * Get the champion ID
     * 
     * @return the {@see LolId} of the champion.
     */
    public LolId getChampionId() {
        return new LolId(mBannedChampion.getChampionId());
    }

    /**
     * Get the ID of the team that banned that champion.
     * 
     * @return the {@see LolTeam.TeamId} of the team that banned that champion.
     */
    public TeamId getTeamId() {
        return LolTeam.TeamId.getTeamId(mBannedChampion.getTeamId());
    }
}
