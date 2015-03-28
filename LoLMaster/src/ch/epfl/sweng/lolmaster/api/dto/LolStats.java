/**
 * 
 */
package ch.epfl.sweng.lolmaster.api.dto;

import dto.Stats.PlayerStatsSummary;
import dto.Stats.PlayerStatsSummaryList;

/**
 * @author ajjngeor (alain george : 217451)
 * 
 */
public class LolStats extends LolDTO {

    private PlayerStatsSummaryList mAllSummaryStats = null;
    private PlayerStatsSummary mNormalSummaryStat = null;
    private PlayerStatsSummary mRankedSummaryStat = null;

    public LolStats(PlayerStatsSummaryList allStats) {
        this.mAllSummaryStats = allStats;

        for (PlayerStatsSummary stats : mAllSummaryStats
            .getPlayerStatSummaries()) {
            if ("Unranked".equals(stats.getPlayerStatSummaryType())) {
                this.mNormalSummaryStat = stats;
            }
            if ("RankedSolo5x5".equals(stats.getPlayerStatSummaryType())) {
                this.mRankedSummaryStat = stats;
            }
        }
    }

    /**
     * Get the number of ranked wins
     * @return the number of ranked wins
     */
    public int getNumberOfRankedWins() {
        if (!(mRankedSummaryStat == null)) {
            return mRankedSummaryStat.getWins();
        }
        return 0;
    }

    /**
     * Get the number of normal wins
     * @return the number of normal wins
     */
    public int getNumberOfNormalWins() {
        if (!(mNormalSummaryStat == null)) {
            return mNormalSummaryStat.getWins();
        }
        return 0;
    }

}
