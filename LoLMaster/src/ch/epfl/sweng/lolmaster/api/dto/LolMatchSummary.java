package ch.epfl.sweng.lolmaster.api.dto;

import java.util.ArrayList;
import java.util.List;

import dto.MatchHistory.MatchSummary;
import dto.MatchHistory.Participant;
import dto.MatchHistory.ParticipantIdentity;
import dto.MatchHistory.ParticipantStats;

/**
 * @author lcg31439
 * 
 */
public class LolMatchSummary extends LolDTO {

    private final MatchSummary mMatchSummary;

    public LolMatchSummary(MatchSummary matchSummary) {
        mMatchSummary = matchSummary;
    }

    /**
     * Get the match ID
     * @return match ID in long
     */
    public long getMatchId() {
        return mMatchSummary.getMatchId();
    }

    /**
     * Get participants of the match
     * @return participants of the match
     */
    public List<LolParticipant> getParticipants() {
        List<LolParticipant> participants = new ArrayList<LolParticipant>();
        for (Participant p : mMatchSummary.getParticipants()) {
            participants.add(new LolParticipant(p));
        }
        return participants;
    }

    /**
     * Get identities of the participants
     * @return itdentities of the participants
     */
    public List<ParticipantIdentity> getParticipantIdentities() {
        return mMatchSummary.getParticipantIdentities();
    }

    /**
     * Get the match creation date in long
     * @return match creation date in long
     */
    public long getMatchCreation() {
        return mMatchSummary.getMatchCreation();
    }

    /**
     * Get the match duration
     * @return the match duration
     */
    public long getMatchDuration() {
        return mMatchSummary.getMatchDuration();
    }

    /**
     * Get the match mode
     * @return the match mode
     */
    public String getMatchMode() {
        return mMatchSummary.getMatchMode();
    }

    /**
     * Get the match type
     * @return the match type
     */
    public String getMatchType() {
        return mMatchSummary.getMatchType();
    }

    /**
     * Get the region of the match
     * @return the region of the match
     */
    public String getRegion() {
        return mMatchSummary.getRegion();
    }

    /**
     * Get the season when the match was played
     * @return the season when the match was player
     */
    public String getSeason() {
        return mMatchSummary.getSeason();
    }

    /**
     * Get the participant identity of a summoner
     * @param summonerId The summoner LolId
     * @return the participant identity of a summoner
     */
    public ParticipantIdentity getParticipantIdentity(LolId summonerId) {
        for (ParticipantIdentity identity : getParticipantIdentities()) {
            if (new LolId(identity.getPlayer().getSummonerId())
                .equals(summonerId)) {
                return identity;
            }
        }
        return null;
    }

    /**
     * Get LolParticipant from participant Id
     * @param participantId The Participant Id
     * @return LolParticipant
     */
    public LolParticipant getParticipant(int participantId) {
        for (Participant p : mMatchSummary.getParticipants()) {
            if (p.getParticipantId() == participantId) {
                return new LolParticipant(p);
            }
        }
        return null;
    }

    /**
     * DTO for a participant of a match {@see LolMatchSummary}
     * 
     * @author fKunstner
     */
    public static class LolParticipant {
        private Participant mParticipant;

        public LolParticipant(Participant participant) {
            mParticipant = participant;
        }

        /**
         * Get participant's champion ID
         * @return participant's championID
         */
        public int getChampionId() {
            return mParticipant.getChampionId();
        }

        /**
         * Get participant's spells ID
         * @return participant's spells ID
         */
        public int[] getSpells() {
            return new int[] {mParticipant.getSpell1Id(),
                mParticipant.getSpell2Id() };
        }

        /**
         * Get participant's stats
         * @return participant's stats
         */
        public LolParticipantStats getStats() {
            return new LolParticipantStats(mParticipant.getStats());
        }

        /**
         * DTO for a participant's statistics {@see LolParticipant}
         * 
         * @author fKunstner
         */
        public static class LolParticipantStats {
            private ParticipantStats mStats;

            public LolParticipantStats(ParticipantStats stats) {
                mStats = stats;
            }

            /**
             * Get if the participant won
             * @return true if win , false is loss
             */
            public boolean isWinner() {
                return mStats.isWinner();
            }

            /**
             * Get participant's number of kills
             * @return participant's number of kills
             */
            public long getKills() {
                return mStats.getKills();
            }

            /**
             * Get participant's number of death
             * @return participant's number of death
             */
            public long getDeaths() {
                return mStats.getDeaths();
            }

            /**
             * Get participant number of assists
             * @return participant's number of assists 
             */
            public long getAssists() {
                return mStats.getAssists();
            }

            /**
             * Get how much gold the participant earned
             * @return how much gold the particpant earned
             */
            public long getGoldEarned() {
                return mStats.getGoldEarned();
            }

            /**
             * Get participant's items
             * @return participant's items
             */
            public long[] getItems() {
                return new long[] {mStats.getItem0(), mStats.getItem1(),
                    mStats.getItem2(), mStats.getItem3(), mStats.getItem4(),
                    mStats.getItem5(), mStats.getItem6() };
            }
        }
    }
}
