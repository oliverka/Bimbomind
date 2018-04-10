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
    private static final String TABLE_SAVE = "turns";

    private static final String SCORE_COLUMN_ID = "id";
    private static final String SCORE_COLUMN_SCORE = "score";
    private static final String SCORE_COLUMN_USEDTURNS = "usedturns";
    private static final String SCORE_COLUMN_COLORCOUNT = "colorcount";
    private static final String SCORE_COLUMN_NAME = "name";
    private static final String SCORE_COLUMN_DATE = "datum";

    private static final String PINS_COLUMN_R = "PINS_COLUMN_R";
    private static final String PINS_COLUMN_G = "PINS_COLUMN_G";
    private static final String PINS_COLUMN_B = "PINS_COLUMN_B";

    private static final String SAVE_PIN1 = "PIN1";
    private static final String SAVE_PIN2 = "PIN2";
    private static final String SAVE_PIN3 = "PIN3";
    private static final String SAVE_PIN4 = "PIN4";
    private static final String SAVE_PIN5 = "PIN5";
    private static final String SAVE_PIN6 = "PIN6";
    private static final String SAVE_PIN7 = "PIN7";
    private static final String SAVE_PIN8 = "PIN8";

    public static final String PREFERENCE_SAVEGAME_COLORCOUNT = "PREFERENCE_SAVEGAME_COLORCOUNT";
    public static final String PREFERENCE_SAVEGAME_MAXTURNS = "PREFERENCE_SAVEGAME_MAXTURNS";
    public static final String PREFERENCE_SAVEGAME_PINCOUNT = "PREFERENCE_SAVEGAME_PINCOUNT";
    public static final String PREFERENCE_SAVEGAME_HOLES = "PREFERENCE_SAVEGAME_HOLES";
    public static final String PREFERENCE_SAVEGAME_ALLOWEMPTY = "PREFERENCE_SAVEGAME_ALLOWEMPTY";
    public static final String PREFERENCE_SAVEGAME_ALLOWMULTIPLE = "PREFERENCE_SAVEGAME_ALLOWMULTIPLE";

    public static final String PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE = "PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_COLOR_R = "PREFERENCE_SETTINGS_BACKGROUND_COLOR_R";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_COLOR_G = "PREFERENCE_SETTINGS_BACKGROUND_COLOR_G";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_COLOR_B = "PREFERENCE_SETTINGS_BACKGROUND_COLOR_B";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_IMAGEPATH = "PREFERENCE_SETTINGS_BACKGROUND_IMAGEPATH";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_PINSHAPE = "PREFERENCE_SETTINGS_BACKGROUND_PINSHAPE";
    public static final String PREFERENCE_PINS_SHAPE = "PREFERENCE_PINS_COLUMN_SHAPE";

    private static final int[][] DEFAULT_COLORS = {{0, 0, 0}, {0, 0, 255}, {0, 255, 0}, {0, 255, 255}, {255, 0, 0}, {255, 0, 255}, {255, 255, 0}, {255, 255, 255}};

    private StoredPreferences sp;


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        sp = new StoredPreferences(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = " CREATE TABLE " + TABLE_SCORE + " (" +SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, " + SCORE_COLUMN_SCORE + " INT, "  + SCORE_COLUMN_USEDTURNS + " INT, " + SCORE_COLUMN_COLORCOUNT + " INT," + SCORE_COLUMN_NAME +
                " TEXT, " + SCORE_COLUMN_DATE + " TEXT " + ")";
        db.execSQL(CREATE_TABLE);
        CREATE_TABLE = " CREATE TABLE " + TABLE_PINS + " (" +SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, " + PINS_COLUMN_R + " INT, " + PINS_COLUMN_G + " INT, " + PINS_COLUMN_B + " INT)";
        db.execSQL(CREATE_TABLE);
        CREATE_TABLE = " CREATE TABLE " + TABLE_SAVE + " (" +SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + SAVE_PIN1 + " INT, " + SAVE_PIN2 + " INT, " + SAVE_PIN3 + " INT, " + SAVE_PIN4 + " INT, "
                + SAVE_PIN5 + " INT, " + SAVE_PIN6 + " INT, " + SAVE_PIN7 + " INT, " + SAVE_PIN8 + " INT)";
        db.execSQL(CREATE_TABLE);

        Cursor c = db.rawQuery("SELECT count(*) FROM " + TABLE_PINS, null);
        c.moveToFirst();
        if (c.getInt(0) < 8) updateColorSettings(DEFAULT_COLORS, db);
        db.close();
    }

    public void saveGame(SaveGame sg){
        setPreference(PREFERENCE_SAVEGAME_COLORCOUNT, sg.getColorCount());
        setPreference(PREFERENCE_SAVEGAME_ALLOWEMPTY, sg.allowEmpty());
        setPreference(PREFERENCE_SAVEGAME_ALLOWMULTIPLE, sg.allowMultiple());
        setPreference(PREFERENCE_SAVEGAME_MAXTURNS, sg.getMaxTurns());
        setPreference(PREFERENCE_SAVEGAME_HOLES, sg.getHoles());
        resetDatabase(TABLE_SAVE);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i = 1; i<9; i++){
            values.put("PIN"+i, sg.getCode().getCode()[i-1].getID());
        }
        db.insert(TABLE_SAVE, null, values);
        for(int i = 0; i<9; i++){
            values = new ContentValues();
            for(int j = 1; j<9; j++){
                values.put("PIN"+i, sg.getCode().getCode()[i-1].getID());
            }
            db.insert(TABLE_SAVE, null, values);
        }
        db.close();
    }

    public SaveGame loadGame(){
        Entry_Turn[] turns;
        int colors, holes, maxTurns;
        Code c;
        boolean allowMultiple, allowEmpty;

        colors = getPreference(PREFERENCE_SAVEGAME_COLORCOUNT);
        maxTurns = getPreference(PREFERENCE_SAVEGAME_MAXTURNS);
        holes = getPreference(PREFERENCE_SAVEGAME_HOLES);
        allowMultiple = getPreferenceBoolean(PREFERENCE_SAVEGAME_ALLOWMULTIPLE);
        allowEmpty = getPreferenceBoolean(PREFERENCE_SAVEGAME_ALLOWEMPTY);


        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SAVE + " ORDER BY " + SCORE_COLUMN_ID + " ASC";
        Cursor cu = db.rawQuery(query, null);
        cu.moveToFirst();
        for(int i =  0; i<cu.getCount(); i++){

        }
        db.close();

        return null;
        //return new SaveGame(turns, c, maxTurns, colors, holes, allowEmpty, allowMultiple);
    }

    public Pin getPin(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return null;
    }

    public int[][] getColorSettings(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PINS + " ORDER BY " + SCORE_COLUMN_ID + " ASC";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int[][] colors = new int[8][3];
        for(int i =  0; i<c.getCount(); i++){
            int r = c.getColumnIndex(PINS_COLUMN_R);
            int g = c.getColumnIndex(PINS_COLUMN_G);
            int b = c.getColumnIndex(PINS_COLUMN_B);
            colors[i][0] = c.getInt(r);
            colors[i][1] = c.getInt(g);
            colors[i][2] = c.getInt(b);
        }
        db.close();
        for(int i = 0; i< 8; i++){
            System.err.println("Color " +i+ " " + colors[i][0]+ " " + colors[i][1]+ " " + colors[i][2]);

        }
        return colors;
    }

    public void updateColorSettings(int[][] colors) {
        SQLiteDatabase db = this.getWritableDatabase();
        updateColorSettings(colors, db);
        db.close();
    }

    public void updateColorSettings(int[][] colors, SQLiteDatabase db) {
        for(int i = 0; i<8; i++){
            ContentValues values = new ContentValues();
            values.put(PINS_COLUMN_R, colors[i][0]);
            values.put(PINS_COLUMN_G, colors[i][1]);
            values.put(PINS_COLUMN_B, colors[i][2]);
            db.update(TABLE_PINS, values, "id=?", new String[] {Integer.toString(i)});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }

    protected void addScore(Entry_Highscore eintrag) {

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
        values.put(SCORE_COLUMN_SCORE, eintrag.getScore());
        values.put(SCORE_COLUMN_USEDTURNS, eintrag.getUsedturns());
        values.put(SCORE_COLUMN_COLORCOUNT, eintrag.getColorcount());
        values.put(SCORE_COLUMN_NAME, eintrag.getName());
        values.put(SCORE_COLUMN_DATE, eintrag.getDatum());

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
        String query = "SELECT * from " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount + " order by " + SCORE_COLUMN_SCORE + " desc";

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        eintraege = new Entry_Highscore[cursor.getCount()];
        for(int i = 0; i<cursor.getCount(); i++){
            eintraege[i] = new Entry_Highscore(Integer.parseInt(cursor.getString(1)), cursor.getInt(2), cursor.getInt(3), cursor.getString(2),cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return eintraege;
    }

    protected Entry_Highscore[] getAllScores() {
        Entry_Highscore[] eintraege = new Entry_Highscore[numberOfData()];
        String query = "SELECT * from "+ TABLE_SCORE + " order by " + SCORE_COLUMN_SCORE + " desc";

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
        db.close();
        cursor.close();
        return eintraege;
    }

    protected int numberOfData() {
        String query = "Select count(*) FROM " + TABLE_SCORE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int i=cursor.getInt(0);
        db.close();
        return i;
    }

    protected int numberOfEntrys(int colorcount){
        String query = "Select count(*) FROM " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT +"="+colorcount;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int i=cursor.getInt(0);
        db.close();
        return i;
    }

    protected void deleteEntry(int score, int usedturns, int colorcount, String name, String datum){
        String query = "DELETE FROM " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount + " AND " + SCORE_COLUMN_SCORE + "=" + score + " AND " + SCORE_COLUMN_DATE + "=" + datum + " AND " +
                SCORE_COLUMN_NAME + "=" + name + " AND " + SCORE_COLUMN_USEDTURNS + "=" + usedturns;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    protected void resetHighscores(int colorcount){
        String query = "DELETE FROM " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    protected void resetDatabase(String dbName){
        String query = "DELETE FROM " + dbName + " WHERE 0=0";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    protected int getPreference(String key){return sp.getInteger(key); }
    protected String getPreferenceString(String key) { return sp.getString(key); }
    protected boolean getPreferenceBoolean(String key) { return sp.getBoolean(key); }
    protected void setPreference(String key, int value){ sp.storePreference(key, value);}
    protected void setPreference(String key, String value){ sp.storePreference(key, value); }
    protected void setPreference(String key, boolean value) {sp.storePreference(key, value);}
}
