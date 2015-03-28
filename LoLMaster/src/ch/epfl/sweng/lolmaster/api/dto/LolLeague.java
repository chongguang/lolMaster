/**
 * 
 */
package ch.epfl.sweng.lolmaster.api.dto;

import dto.League.League;
import dto.League.LeagueEntry;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolLeague extends LolDTO {

    private League mLeague;

    public LolLeague(League league) {
        this.mLeague = league;
    }

    /**
     * Get league's Tier of player or team
     * @return league's tier of player or team
     */
    public String getTier() {
        return mLeague.getTier();
    }

    /**
     * Get number of wins of player or team in this league
     * @return number of wins of player of team in this league
     */
    public int getNumberOfWin() {
        LeagueEntry participantEntry = getParticipantLeagueEntry();
        if (participantEntry != null) {
            return participantEntry.getWins();
        }
        return 0;
    }

    /**
     * Get league's division of player or team
     * @return league's division of player or team
     */
    public String getDivision() {
        LeagueEntry participantEntry = getParticipantLeagueEntry();
        if (participantEntry != null) {
            return participantEntry.getDivision();
        }
        return "?";
    }

    private LeagueEntry getParticipantLeagueEntry() {
        for (LeagueEntry leagueEntry : mLeague.getEntries()) {
            if (leagueEntry.getPlayerOrTeamId().equals(
                mLeague.getParticipantId())) {
                return leagueEntry;
            }
        }
        return null;
    }

}
