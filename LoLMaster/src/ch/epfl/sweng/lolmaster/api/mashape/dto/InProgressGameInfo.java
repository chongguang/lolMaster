package ch.epfl.sweng.lolmaster.api.mashape.dto;

import java.util.List;

/**
 * Data Transfer Object to receive Json from the Mashape Api.
 * 
 * @author fKunstner
 */
public class InProgressGameInfo {

    private InProgressGameData data;

    public InProgressGameData.InProgressGame getGame() {
        return data.getGame();
    }

    /**
     * Data Transfer Object to receive Json from the Mashape Api.
     * 
     * @author fKunstner
     */
    public static class InProgressGameData {
        private InProgressGame game;

        public InProgressGame getGame() {
            return game;
        }

        /**
         * Data Transfer Object to receive Json from the Mashape Api.
         * 
         * @author fKunstner
         */
        public static class InProgressGame {
            private List<ChampionSelection> playerChampionSelections;
            private List<BannedChampion> bannedChampions;
            private List<Player> teamOne;
            private List<Player> teamTwo;
            private int maxNumPlayers;
            private String queueTypeName;
            private String gameType;
            private String gameMode;

            public int getMaxNumPlayer() {
                return maxNumPlayers;
            }

            public String getQueueTypeName() {
                return queueTypeName;
            }

            public String getGameType() {
                return gameType;
            }

            public String getGameMode() {
                return gameMode;
            }

            public List<Player> getTeamOne() {
                return teamOne;
            }

            public List<Player> getTeamTwo() {
                return teamTwo;
            }

            public List<BannedChampion> getBannedChampions() {
                return bannedChampions;
            }

            public List<ChampionSelection> getChampionSelections() {
                return playerChampionSelections;
            }

            /**
             * Data Transfer Object to receive Json from the Mashape Api.
             * 
             * @author fKunstner
             */
            public static class Player {
                private long summonerId;
                private String summonerInternalName;
                private String summonerName;

                public long getSummonerId() {
                    return summonerId;
                }

                public String getSummonerInternalName() {
                    return summonerInternalName;
                }

                public String getSummonerName() {
                    return summonerName;
                }
            }

            /**
             * Data Transfer Object to receive Json from the Mashape Api.
             * 
             * @author fKunstner
             */
            public static class ChampionSelection {
                private String summonerInternalName;
                private long championId;
                private int spell1Id;
                private int spell2Id;

                public String getSummonerInternalName() {
                    return summonerInternalName;
                }

                public long getChampionId() {
                    return championId;
                }

                public int getSpell1Id() {
                    return spell1Id;
                }

                public int getSpell2Id() {
                    return spell2Id;
                }

            }

            /**
             * Data Transfer Object to receive Json from the Mashape Api.
             * 
             * @author fKunstner
             */
            public static class BannedChampion {
                private int teamId;
                private int championId;

                public int getTeamId() {
                    return teamId;
                }

                public int getChampionId() {
                    return championId;
                }
            }
        }
    }
}
