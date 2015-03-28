/**
 * 
 */
package ch.epfl.sweng.lolmaster.api.dto;

import java.util.List;

import dto.League.League;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolLeagues extends LolDTO {

    private List<League> mLeagues;

    public LolLeagues(List<League> leagues) {

        this.mLeagues = leagues;
    }

    /**
     * Get Ranked solo queue league
     * @return ranked solo queue league
     */
    public LolLeague getRankedSoloQueueLeague() {
        return getLeagueFromQueueType("RANKED_SOLO_5x5");
    }

    /**
     * Get team 5v5 league
     * @return team 5v5 league
     */
    public LolLeague getRankedTeam5() {
        return getLeagueFromQueueType("RANKED_TEAM_5x5");
    }

    /**
     * Get team 3v3 league
     * @return team 3v3 league
     */
    public LolLeague getRankedTeam3() {
        return getLeagueFromQueueType("RANKED_TEAM_3x3");
    }

    private LolLeague getLeagueFromQueueType(String queueType) {
        for (League league : mLeagues) {
            if (league.getQueue().equals(queueType)) {
                return new LolLeague(league);
            }
        }
        return null;
    }
}
