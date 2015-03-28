package ch.epfl.sweng.lolmaster.database;

import android.content.ContentValues;
import android.database.Cursor;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary.LolParticipant.LolParticipantStats;
import ch.epfl.sweng.lolmaster.database.MatchHistoryDBHelper.TableField;
import dto.MatchHistory.ParticipantIdentity;

/**
 * The model representing entries of the MatchHistory database.
 * 
 * {@see MatchHistoryDBHelper}
 * 
 * @author lcg31439
 */
public class MatchSummaryModel {
    private long mMatchId;
    private long mSummonerId;
    private long mMatchCreation;
    private String mRegion;
    private boolean mIsWinner;
    private int mChampionId;
    private long mKills;
    private long mDeaths;
    private long mAssists;
    private long mGoldEarned;
    private int[] mSpells = new int[TableField.SPELLS.length];
    private long[] mItems = new long[TableField.ITEMS.length];

    /**
     * Creates a {@see MatchSummaryDataModel} from a database {@see Cursor}
     * 
     * @param cursor
     */
    public MatchSummaryModel(Cursor cursor) {
        mMatchId =
            cursor.getLong(cursor.getColumnIndexOrThrow(TableField.MATCH_ID
                .getName()));
        mSummonerId =
            cursor.getInt(cursor.getColumnIndexOrThrow(TableField.SUMMONER_ID
                .getName()));
        mMatchCreation =
            cursor.getLong(cursor
                .getColumnIndexOrThrow(TableField.MATCH_CREATION.getName()));
        mRegion =
            cursor.getString(cursor.getColumnIndexOrThrow(TableField.REGION
                .getName()));
        mIsWinner =
            cursor.getInt(cursor.getColumnIndexOrThrow(TableField.IS_WINNER
                .getName())) == 1;
        mChampionId =
            cursor.getInt(cursor.getColumnIndexOrThrow(TableField.CHAMPION_ID
                .getName()));
        mKills =
            cursor.getLong(cursor.getColumnIndexOrThrow(TableField.KILLS
                .getName()));
        mDeaths =
            cursor.getLong(cursor.getColumnIndexOrThrow(TableField.DEATHS
                .getName()));
        mAssists =
            cursor.getLong(cursor.getColumnIndexOrThrow(TableField.ASSISTS
                .getName()));
        mGoldEarned =
            cursor.getLong(cursor.getColumnIndexOrThrow(TableField.GOLD_EARNED
                .getName()));
        for (int i = 0; i < TableField.SPELLS.length; i++) {
            mSpells[i] =
                cursor.getInt(cursor.getColumnIndexOrThrow(TableField.SPELLS[i]
                    .getName()));
        }
        for (int i = 0; i < TableField.ITEMS.length; i++) {
            mItems[i] =
                cursor.getLong(cursor.getColumnIndexOrThrow(TableField.ITEMS[i]
                    .getName()));
        }
    }

    /**
     * Creates a {@see MatchSummaryDataModel} from Riot api based informations.
     * 
     * @param summonerId
     * @param matchSummary
     */
    public MatchSummaryModel(LolId summonerId, LolMatchSummary matchSummary) {
        ParticipantIdentity playerIdentity =
            matchSummary.getParticipantIdentity(summonerId);

        mSummonerId = summonerId.getValue();

        mMatchId = matchSummary.getMatchId();
        mMatchCreation = matchSummary.getMatchCreation();
        mRegion = matchSummary.getRegion();

        LolParticipant participant =
            matchSummary.getParticipant(playerIdentity.getParticipantId());

        mChampionId = participant.getChampionId();
        mSpells = participant.getSpells();

        LolParticipantStats stats = participant.getStats();

        mIsWinner = stats.isWinner();
        mKills = stats.getKills();
        mDeaths = stats.getDeaths();
        mAssists = stats.getAssists();
        mGoldEarned = stats.getGoldEarned();
        mItems = stats.getItems();
    }

    public long getMatchId() {
        return mMatchId;
    }

    public long getSummonerId() {
        return mSummonerId;
    }

    public long getMatchCreation() {
        return mMatchCreation;
    }

    public String getRegion() {
        return mRegion;
    }

    public boolean isWinner() {
        return mIsWinner;
    }

    public int getChampionId() {
        return mChampionId;
    }

    public long getKills() {
        return mKills;
    }

    public long getDeaths() {
        return mDeaths;
    }

    public long getAssists() {
        return mAssists;
    }

    public long getGoldEarned() {
        return mGoldEarned;
    }

    public int[] getSpells() {
        return mSpells;
    }

    public long[] getItems() {
        return mItems;
    }

    /**
     * Creates the {@see ContentValues} to write in DB.
     * 
     * @return
     */
    public ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(TableField.MATCH_ID.getName(), mMatchId);
        values.put(TableField.SUMMONER_ID.getName(), mSummonerId);
        values.put(TableField.MATCH_CREATION.getName(), mMatchCreation);
        values.put(TableField.REGION.getName(), mRegion);
        values.put(TableField.IS_WINNER.getName(), mIsWinner);
        values.put(TableField.CHAMPION_ID.getName(), mChampionId);
        values.put(TableField.KILLS.getName(), mKills);
        values.put(TableField.DEATHS.getName(), mDeaths);
        values.put(TableField.ASSISTS.getName(), mAssists);
        values.put(TableField.GOLD_EARNED.getName(), mGoldEarned);
        for (int i = 0; i < TableField.SPELLS.length; i++) {
            values.put(TableField.SPELLS[i].getName(), mSpells[i]);
        }
        for (int i = 0; i < TableField.ITEMS.length; i++) {
            values.put(TableField.ITEMS[i].getName(), mItems[i]);
        }
        return values;
    }

}
