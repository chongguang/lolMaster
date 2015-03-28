package ch.epfl.sweng.lolmaster.api.dto;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.Player;

/**
 * @author fKunstner
 */
final public class LolTeam extends LolDTO {
    private final List<Player> mPlayers;

    public LolTeam(List<Player> team) {
        mPlayers = team;
    }

    /**
     * Get the list of players in the team.
     * 
     * @return a {@code List<LolPlayer>} containing the players in the team.
     */
    public List<LolPlayer> getPlayers() {
        List<LolPlayer> playerList = new ArrayList<LolPlayer>();
        for (Player player : mPlayers) {
            playerList.add(new LolPlayer(player));
        }
        return playerList;
    }

    /**
     * Returns true if the player is in the team, false otherwise.
     * 
     * @param playerName
     *            The name of the player to check.
     * @return
     */
    public boolean hasPlayer(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return false;
        }

        String normalizedPlayerName =
            playerName.toLowerCase().replaceAll("\\s+", "");

        for (Player player : mPlayers) {
            if (player.getSummonerInternalName().equals(normalizedPlayerName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof LolTeam) {
            LolTeam that = (LolTeam) object;
            return this.getPlayers().equals(that.getPlayers());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getPlayers().hashCode();
    }

    /**
     * The possible team ids.
     * 
     * @author fKunstner
     */
    public enum TeamId {
        TEAM1(100),
        TEAM2(200);

        private final int mId;

        private TeamId(int id) {
            mId = id;
        }

        /**
         * Get the TeamId object from the enum by the {@code int id}.
         * 
         * @param id
         *            the numerical id of the team
         * @return a TeamId if the id is valid, null otherwise.
         * 
         */
        public static TeamId getTeamId(int id) {
            for (TeamId teamId : TeamId.values()) {
                if (teamId.getId() == id) {
                    return teamId;
                }
            }
            return null;
        }

        public int getId() {
            return mId;
        }

    }
}
