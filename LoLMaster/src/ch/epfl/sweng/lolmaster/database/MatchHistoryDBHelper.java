package ch.epfl.sweng.lolmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author lcg31439
 */
public class MatchHistoryDBHelper extends SQLiteOpenHelper {

    // DB parameters
    private static final String DATABASE_NAME = "lolmaster.db";
    private static final int DATABASE_VERSION = 6;
    public static final String TABLE_NAME = "MatchHistory";

    private static final String TEXT_FIELD = "TEXT";
    private static final String INTEGER_FIELD = "INTEGER";

    /**
     * The fields of the MatchHistory table
     * 
     * @author fKunstner
     */
    public static enum TableField {
        MATCH_ID("matchId", TEXT_FIELD),
        SUMMONER_ID("summonerId", INTEGER_FIELD),
        MATCH_CREATION("matchCreation", INTEGER_FIELD),
        REGION("region", TEXT_FIELD),
        IS_WINNER("isWinner", INTEGER_FIELD),
        CHAMPION_ID("championId", INTEGER_FIELD),
        KILLS("kills", INTEGER_FIELD),
        DEATHS("deaths", INTEGER_FIELD),
        ASSISTS("assists", INTEGER_FIELD),
        GOLD_EARNED("goldEarned", INTEGER_FIELD),
        SPELL1("spell1", INTEGER_FIELD),
        SPELL2("spell2", INTEGER_FIELD),
        ITEM0("item0", INTEGER_FIELD),
        ITEM1("item1", INTEGER_FIELD),
        ITEM2("item2", INTEGER_FIELD),
        ITEM3("item3", INTEGER_FIELD),
        ITEM4("item4", INTEGER_FIELD),
        ITEM5("item5", INTEGER_FIELD),
        ITEM6("item6", INTEGER_FIELD);

        /**
         * The fields of the spells
         */
        public static final TableField[] SPELLS = {SPELL1, SPELL2 };
        /**
         * The fields of the spells
         */
        public static final TableField[] ITEMS = {ITEM0, ITEM1, ITEM2, ITEM3,
            ITEM4, ITEM5, ITEM6 };
        /**
         * The primary key of the tables (can be a coumpound key)
         */
        public static final TableField[] PRIMARY = {MATCH_ID, REGION };

        private String mName;
        private String mType;

        private TableField(String fieldName, String type) {
            mName = fieldName;
            mType = type;
        }

        /**
         * Get the name of the field
         * 
         * @return
         */
        public String getName() {
            return mName;
        }

        /**
         * Get the type of the field as represented in the database. Correspond
         * to the textual name of the types returned by {@see Cursor#getType}
         * 
         * @return
         */
        public String getType() {
            return mType;
        }
    }

    /**
     * Generate the query to create the database
     * 
     * CREATE TABLE TABLENAME(FIELD TYPE NOT NULL(, FIELD TYPE NOT NULL)*,
     * PRIMARY KEY (FIELD(, FIELD)*)));
     * 
     * @return
     */
    private static String getCreateDatabaseQuery() {
        StringBuilder query = new StringBuilder();
        query.append("create table " + TABLE_NAME);

        query.append("(");
        // FIELD TYPE NOT NULL(, FIELD TYPE NOT NULL)*
        for (TableField field : TableField.values()) {
            query.append(field.getName() + " " + field.getType());
            query.append(" NOT NULL, ");
        }

        // PRIMARY KEY(FIELD(, FIELD)*)
        query.append("PRIMARY KEY (");
        for (int i = 0; i < TableField.PRIMARY.length; i++) {
            TableField field = TableField.PRIMARY[i];
            query.append(field.getName());
            if (i != TableField.PRIMARY.length - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        query.append(");");
        return query.toString();
    }

    /**
     * Creates a MatchHistoryDBHelper
     * 
     * a SQLiteOpenHelper for the MatchHistory table.
     * 
     * @param context
     */
    public MatchHistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateDatabaseQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MatchHistoryDBHelper.class.getName(),
            "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
