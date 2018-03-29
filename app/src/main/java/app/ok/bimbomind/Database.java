package app.ok.bimbomind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Familie on 17.06.2016.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "bimbomind.db";
    static final String DATABASE_NAME_FRONT = "bimbomind_front.db";
    private static final String TABLE_SCORE = "score";
    private static final String TABLE_PINS = "pins";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_USEDTURNS = "usedturns";
    private static final String COLUMN_COLORCOUNT = "colorcount";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "datum";


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORE_TABLE = " CREATE TABLE " + TABLE_SCORE + " (" +COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_SCORE + " INT, "  + COLUMN_USEDTURNS + " INT, " + COLUMN_COLORCOUNT + " INT," + COLUMN_NAME +
                " TEXT, " + COLUMN_DATE + " TEXT " + ")";
        System.err.println(CREATE_HIGHSCORE_TABLE);
        db.execSQL(CREATE_HIGHSCORE_TABLE);
        //TODO: Shared preferences for saving settings, game settings
        //String CREATE_PIN_TABLE = "CREATE TABLE " + TABLE_PINS + "(r INT, g INT, b INT)";
        //TODO: Table for saved Game

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);

    }

    protected void addScore (Entry_Highscore eintrag) {

        if(numberOfEntrys(eintrag.getColorcount()) > 10){
            Entry_Highscore worst = getWorstScore(eintrag.getColorcount());
            if(worst.getScore() > eintrag.getScore()){
                return; //Score ist zu schlecht
            }
            else {
                deleteEntry(worst.getScore(), worst.getUsedturns(), worst.getColorcount(), worst.getName(), worst.getDatum());
                //schlechtesten score löschen und neuen einfügen
            }
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, eintrag.getScore());
        values.put(COLUMN_USEDTURNS, eintrag.getUsedturns());
        values.put(COLUMN_COLORCOUNT, eintrag.getColorcount());
        values.put(COLUMN_NAME, eintrag.getName());
        values.put(COLUMN_DATE, eintrag.getDatum());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SCORE,null,values);
//        System.err.println("Highscore: "+getAllScores()[0].getScore());
        db.close();
    }

    protected Entry_Highscore getWorstScore(int colorcount) {
        Entry_Highscore[] eintraege = getAllScores(colorcount);
        if (eintraege.length>0 && eintraege.length<10)
            return eintraege[eintraege.length-1];
         else if (eintraege.length >=10)
            return eintraege[9];
        else
            return null;
    }

    protected Entry_Highscore getBestScore(int colorcount) {
        Entry_Highscore[] eintraege = getAllScores(colorcount);
        if (eintraege.length>0)
            return eintraege[0];
        else
            return null;
    }

    protected Entry_Highscore[] getAllScores(int colorcount){
        Entry_Highscore[] eintraege;
        String query = "SELECT * from " + TABLE_SCORE + " WHERE " + COLUMN_COLORCOUNT + "=" + colorcount + " order by " + COLUMN_SCORE + " desc";

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        eintraege = new Entry_Highscore[cursor.getCount()];
        for(int i = 0; i<cursor.getCount(); i++){
            eintraege[i] = new Entry_Highscore(Integer.parseInt(cursor.getString(1)), cursor.getInt(2), cursor.getInt(3), cursor.getString(2),cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        return eintraege;
    }

    protected Entry_Highscore[] getAllScores() {
        Entry_Highscore[] eintraege = new Entry_Highscore[numberOfData()];
        String query = "SELECT * from "+ TABLE_SCORE + " order by " + COLUMN_SCORE + " desc";

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()&&numberOfData()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                eintraege[i] = new Entry_Highscore(Integer.parseInt(cursor.getString(1)), cursor.getInt(2), cursor.getInt(3), cursor.getString(2),cursor.getString(3));
                cursor.moveToNext();
            }
            //sortiert nach der Groesse
            for (int j = 0; j < eintraege.length; j++) {
                for (int i = 0; i < eintraege.length - 1; i++) {
                    if (eintraege[i + 1].getScore() > eintraege[i].getScore()) {
                        Entry_Highscore eintrag = eintraege[i];
                        eintraege[i] = eintraege[i + 1];
                        eintraege[i + 1] = eintrag;
                    }
                }
            }
        }
        cursor.close();
        return eintraege;
    }

    public void printAll(){
        Entry_Highscore[] e = getAllScores();
        for (int i = 0; i< e.length; i++) {
            System.err.println(e[i].getName() + " " + e[i].getScore());
        }
    }

    protected int numberOfData() {
        String query = "Select count(*) FROM " + TABLE_SCORE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int i=cursor.getInt(0);
        return i;
    }

    protected int numberOfEntrys(int colorcount){
        String query = "Select count(*) FROM " + TABLE_SCORE + " WHERE " + COLUMN_COLORCOUNT +"="+colorcount;
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

    protected void deleteEntry(int score, int usedturns, int colorcount, String name, String datum){
        String query = "DELETE FROM " + TABLE_SCORE + " WHERE " + COLUMN_COLORCOUNT + "=" + colorcount + " AND " + COLUMN_SCORE + "=" + score + " AND " + COLUMN_DATE + "=" + datum + " AND " +
                COLUMN_NAME + "=" + name + " AND " + COLUMN_USEDTURNS + "=" + usedturns;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    protected void resetHighscores(int colorcount){
        String query = "DELETE FROM " + TABLE_SCORE + " WHERE " + COLUMN_COLORCOUNT + "=" + colorcount;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
}
