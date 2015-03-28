package ch.epfl.sweng.lolmaster.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import ch.epfl.sweng.lolmaster.api.dto.LolId;
import ch.epfl.sweng.lolmaster.api.dto.LolMatchSummary;

/**
 * A class to make DB queries.
 * 
 * @author fKunstner
 */
public class DBManager {

    private MatchHistoryDBHelper mMatchHistoryDB;

    /**
     * Creates a new {@see DBManager}
     * 
     * @param context
     */
    public DBManager(Context context) {
        mMatchHistoryDB = new MatchHistoryDBHelper(context);
    }

    /**
     * Saves the {@code matchSummaries} into database.
     * 
     * @param matchSummaries
     * @param summonerId
     */
    public void saveMatchHistory(List<LolMatchSummary> matchSummaries,
        int summonerId, String region) {
        // Sort the list by match creation time in the increasing order
        Collections.sort(matchSummaries, new Comparator<LolMatchSummary>() {
            public int compare(LolMatchSummary lhs, LolMatchSummary rhs) {
                return new Date(lhs.getMatchCreation()).compareTo(new Date(rhs
                    .getMatchCreation()));
            }
        });

        SQLiteDatabase database = mMatchHistoryDB.getWritableDatabase();
        for (LolMatchSummary match : matchSummaries) {
            LolId id = new LolId(summonerId);
            MatchSummaryModel dbModel = new MatchSummaryModel(id, match);
            ContentValues values = dbModel.contentValues();
            try {
                database.insert(MatchHistoryDBHelper.TABLE_NAME, null, values);
            } catch (SQLiteConstraintException e) {
                Log.wtf(this.getClass().getName(), e.getMessage(), e);
            }
        }
        database.close();
    }

    /**
     * Get the complete match history of the given summoner.
     * 
     * @param summonerId
     * @return
     */
    public List<MatchSummaryModel> getMatchHistory(int summonerId, String region) {
        SQLiteDatabase database = mMatchHistoryDB.getReadableDatabase();

        StringBuilder q = new StringBuilder();
        q.append("SELECT * FROM " + MatchHistoryDBHelper.TABLE_NAME);
        q.append(" WHERE ");
        q.append(MatchHistoryDBHelper.TableField.SUMMONER_ID.getName() + "=?");
        q.append(" AND ");
        q.append(MatchHistoryDBHelper.TableField.REGION.getName() + "=?");

        String[] args = new String[] {String.valueOf(summonerId), region };

        Cursor cursor = database.rawQuery(q.toString(), args);

        cursor.moveToFirst();

        List<MatchSummaryModel> history = new ArrayList<MatchSummaryModel>();
        while (!cursor.isAfterLast()) {
            history.add(new MatchSummaryModel(cursor));
            cursor.moveToNext();
        }

        database.close();
        return history;
    }

    /**
     * Get the match creation time (Timestamp in second) of the most recent
     * match for a given summoner.
     * 
     * @param summonerId
     * @return
     */
    public long getLastMatchCreation(int summonerId, String region) {
        SQLiteDatabase database = mMatchHistoryDB.getReadableDatabase();

        String matchCreationField =
            MatchHistoryDBHelper.TableField.MATCH_CREATION.getName();

        String maxMatchCreation = "MAX(" + matchCreationField + ")";
        String whereClause =
            MatchHistoryDBHelper.TableField.SUMMONER_ID.getName() + "=\""
                + summonerId + "\" AND "
                + MatchHistoryDBHelper.TableField.REGION + "=\"" + region
                + "\"";
        Cursor cursor =
            database.rawQuery("SELECT " + maxMatchCreation + " FROM "
                + MatchHistoryDBHelper.TABLE_NAME + " WHERE " + whereClause,
                null);

        cursor.moveToFirst();

        int index = cursor.getColumnIndexOrThrow(maxMatchCreation);
        String max = cursor.getString(index);

        long result = -1;
        if (max != null) {
            result = Long.parseLong(max);
        }
        database.close();

        return result;
    }

    /**
     * Deletes everything.
     */
    public void deleteAllRecords() {
        SQLiteDatabase database = mMatchHistoryDB.getWritableDatabase();
        database.execSQL("delete from " + MatchHistoryDBHelper.TABLE_NAME);
        database.close();
    }

}
