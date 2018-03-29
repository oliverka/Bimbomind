package dtts.dtts.dtts.dtts.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Familie on 17.06.2016.
 */
public class HighscoreTable extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "scoreDB.db";
    static final String DATABASE_NAME_FRONT = "scoreDB_front.db";
    private static final String TABLE_SCORE = "score";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "datum";

    public HighscoreTable(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = " CREATE TABLE " + TABLE_SCORE + "(" +COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_SCORE + " TEXT, " + COLUMN_NAME +"" +
                " TEXT, " + COLUMN_DATE + " TEXT " + ")";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);

    }

    protected void addScore (Eintrag eintrag) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, eintrag.getScore());
        values.put(COLUMN_NAME, eintrag.getName());
        values.put(COLUMN_DATE, eintrag.getDatum());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SCORE,null,values);
//        System.err.println("Highscore: "+getAllScores()[0].getScore());
        db.close();
    }

    protected int getWorstScore() {
        Eintrag[] eintraege = getAllScores();
        if (eintraege.length>0 && eintraege.length<10)
            return eintraege[eintraege.length-1].getScore();
         else if (eintraege.length >=10)
            return eintraege[9].getScore();
        else
            return 0;
    }

    protected int getBestScore() {
        Eintrag[] eintraege = getAllScores();
        if (eintraege.length>0)
            return eintraege[0].getScore();
        else
            return 0;
    }

    protected Eintrag[] getAllScores() {
        Eintrag[] eintraege = new Eintrag[numberOfData()];
        String query = "SELECT * from "+ TABLE_SCORE + " order by " + COLUMN_SCORE + " desc";

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()&&numberOfData()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                eintraege[i] = new Eintrag(Integer.parseInt(cursor.getString(1)),cursor.getString(2),cursor.getString(3));
                cursor.moveToNext();
            }
            //sortiert nach der Groesse
            for (int j = 0; j < eintraege.length; j++) {
                for (int i = 0; i < eintraege.length - 1; i++) {
                    if (eintraege[i + 1].getScore() > eintraege[i].getScore()) {
                        Eintrag eintrag = eintraege[i];
                        eintraege[i] = eintraege[i + 1];
                        eintraege[i + 1] = eintrag;
                    }
                }
            }
        }
        cursor.close();
        return eintraege;
    }
    protected int numberOfData() {
        String query = "Select count(*) FROM " + TABLE_SCORE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int i=cursor.getInt(0);
        return i;
    }

    protected void deleteHighscore() {
        SQLiteDatabase db = this.getWritableDatabase();
        int length = numberOfData();
        System.err.println(length);

        String query = "SELECT * FROM " + TABLE_SCORE;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        System.err.println(cursor.getCount()+ " "+ cursor.getString(0));

        for (int i = 0 ; i <= length ; i++) {
            String delete = "DELETE FROM " + TABLE_SCORE + " WHERE id = " +String.valueOf(i);
            db.execSQL(delete);
        }
    }
}
