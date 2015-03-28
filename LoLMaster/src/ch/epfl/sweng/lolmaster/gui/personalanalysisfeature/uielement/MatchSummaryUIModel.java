package ch.epfl.sweng.lolmaster.gui.personalanalysisfeature.uielement;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ch.epfl.sweng.lolmaster.api.dto.LolChampionData;
import ch.epfl.sweng.lolmaster.api.dto.LolItem;
import ch.epfl.sweng.lolmaster.api.dto.LolSummonerSpell;
import ch.epfl.sweng.lolmaster.database.MatchSummaryModel;

/**
 * @author lcg31439
 * 
 */
public class MatchSummaryUIModel {

    private MatchSummaryModel mMatch;
    private LolChampionData mChampion;
    private LolItem[] mItems;
    private LolSummonerSpell[] mSpells;

    public MatchSummaryUIModel(MatchSummaryModel match,
        LolChampionData champion, LolItem[] items, LolSummonerSpell[] spells) {
        mMatch = match;
        mChampion = champion;
        mItems = items;
        mSpells = spells;
    }

    /**
     * Calculate the KDA ratio of this match summary
     * 
     * @return
     */
    private double calculateKDA() {
        double d =
            mMatch.getDeaths() == 0 ? mMatch.getAssists() + mMatch.getKills()
                : (double) (mMatch.getAssists() + mMatch.getKills())
                    / (double) mMatch.getDeaths();

        BigDecimal b = new BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Return Victory if this match is win, otherwise Defeat
     * 
     * @return
     */
    public String matchResult() {
        return mMatch.isWinner() ? "Victory" : "Defeat";
    }

    public String creationTimeToString() {
        Date msTime = new Date(mMatch.getMatchCreation());
        SimpleDateFormat df =
            new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        return df.format(msTime) + "\n" + tf.format(msTime);
    }

    public String kdaInfo() {
        StringBuilder kdaInfo = new StringBuilder();

        kdaInfo.append("K/D/A: ");
        kdaInfo.append(mMatch.getKills());
        kdaInfo.append("/");
        kdaInfo.append(mMatch.getDeaths());
        kdaInfo.append("/");
        kdaInfo.append(mMatch.getAssists());
        kdaInfo.append("\n");

        kdaInfo.append("Ratio: " + calculateKDA());
        kdaInfo.append("\n");

        kdaInfo.append("Gold earned: " + mMatch.getGoldEarned());

        return kdaInfo.toString();
    }

    public MatchSummaryModel getMatch() {
        return mMatch;
    }

    public LolChampionData getChampion() {
        return mChampion;
    }

    public LolItem[] getItems() {
        return mItems;
    }

    public LolSummonerSpell[] getSpells() {
        return mSpells;
    }
}
