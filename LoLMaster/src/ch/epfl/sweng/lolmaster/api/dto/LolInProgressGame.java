package ch.epfl.sweng.lolmaster.api.dto;

import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame;
import ch.epfl.sweng.lolmaster.api.mashape.dto.InProgressGameInfo.InProgressGameData.InProgressGame.ChampionSelection;
import ch.epfl.sweng.lolmaster.utils.StringUtils;

/**
 * @author fKunstner
 */
final public class LolInProgressGame extends LolDTO {
    private final InProgressGame mGame;

    public LolInProgressGame(InProgressGameInfo inProgessGameInfo) {
        mGame = inProgessGameInfo.getGame();
    }

    /**
     * Get the team the specified player is in if he is in this game,
     * {@code null} otherwise.
     * 
     * @param playerName
     *            the name of the player, normalized of not.
     * @return the team the specified player is in if he is in this game,
     *         {@code null} otherwise.
     */
    public LolTeam getTeamOfPlayer(String playerName) {
        String player = StringUtils.normalizeName(playerName);

        LolTeam playerTeam = null;

        playerTeam =
            getTeam1().hasPlayer(player) ? getTeam1() : getTeam2().hasPlayer(
                player) ? getTeam2() : null;

        return playerTeam;
    }

    /**
     * Get the team the specified player is not in if he is in this game,
     * {@code null} otherwise.
     * 
     * @param playerName
     *            the name of the player, normalized or not
     * @return The {@LolTeam} the player is not in if he is in this
     *         game, {@code null} otherwise.
     */
    public LolTeam getEnemyTeamOfPlayer(String playerName) {
        String player = StringUtils.normalizeName(playerName);

        LolTeam playerTeam = null;

        playerTeam =
            getTeam1().hasPlayer(player) ? getTeam2() : getTeam2().hasPlayer(
                player) ? getTeam1() : null;

        return playerTeam;
    }

    /**
     * Returns the {@see LolId} of the champion played by {@code playerName}, or
     * {@code null} if this player is not found in this game.
     * 
     * @param playerName
     *            The name of the player (normalized or not)
     * @return the {@see LolChampionSelection} of the player, or null if this
     *         player is not found in this game.
     */
    public LolId getChampionByPlayer(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return null;
        }

        String normalizedPlayerName = StringUtils.normalizeName(playerName);

        for (ChampionSelection selection : mGame.getChampionSelections()) {
            if (selection.getSummonerInternalName()
                .equals(normalizedPlayerName)) {
                return new LolId(selection.getChampionId());
            }
        }
        return null;
    }

    /**
     * Get player's champion of current gamme
     * @param playerName The name of the player
     * @return player's champion of current game
     */
    public LolChampionSelection getChampionSelectionByPlayer(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return null;
        }
        String normalizedPlayerName = StringUtils.normalizeName(playerName);

        for (ChampionSelection selection : mGame.getChampionSelections()) {
            if (selection.getSummonerInternalName()
                .equals(normalizedPlayerName)) {
                return new LolChampionSelection(selection);
            }
        }
        return null;
    }

   
    /**
     * Get first team of current game
     * @return first team of current game
     */
    public LolTeam getTeam1() {
        return new LolTeam(mGame.getTeamOne());
    }

    /**
     * Get second team of current game
     * @return second team of current game
     */
    public LolTeam getTeam2() {
        return new LolTeam(mGame.getTeamTwo());
    }

}
