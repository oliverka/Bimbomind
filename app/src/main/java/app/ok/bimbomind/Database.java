package app.ok.bimbomind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ok on 17.06.2016.
 * Modified by Johann on 12.04.2018.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "bimbomind.db";
    static final String DATABASE_NAME_FRONT = "bimbomind_front.db";
    private static final String TABLE_SCORE = "score";
    private static final String TABLE_PINS = "pins";
    private static final String TABLE_SAVE = "Savegame";

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
    public static final String PREFERENCE_SAVEGAME_AI = "PREFERENCE_SAVEGAME_AI";
    public static final String PREFERENCE_SAVEGAME_SET = "PREFERENCE_SAVEGAME_SET";


    public static final String PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE = "PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_COLOR_R = "PREFERENCE_SETTINGS_BACKGROUND_COLOR_R";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_COLOR_G = "PREFERENCE_SETTINGS_BACKGROUND_COLOR_G";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_COLOR_B = "PREFERENCE_SETTINGS_BACKGROUND_COLOR_B";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_IMAGEPATH = "PREFERENCE_SETTINGS_BACKGROUND_IMAGEPATH";
    public static final String PREFERENCE_SETTINGS_BACKGROUND_PINSHAPE = "PREFERENCE_SETTINGS_BACKGROUND_PINSHAPE";
    public static final String PREFERENCE_PINS_SHAPE = "PREFERENCE_PINS_COLUMN_SHAPE";

    private static final int[][] DEFAULT_COLORS = {{0, 0, 0}, {0, 0, 255}, {0, 255, 0}, {0, 255, 255}, {255, 0, 0}, {255, 0, 255}, {255, 255, 0}, {255, 255, 255}};

    private StoredPreferences sp;

    private Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        sp = new StoredPreferences(context);
    }

    private static Database database = null;

    public static Database getInstance(Context c){
        if(database != null){
            return database;
        }
        else{
            database =  new Database(c, null, null, 1);
            database.onCreate(database.getWritableDatabase());
            return database;
        }
    }

    public static Database getInstance(){
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = " CREATE TABLE " + TABLE_SCORE + " (" + SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, " + SCORE_COLUMN_SCORE + " INT, " + SCORE_COLUMN_USEDTURNS + " INT, " + SCORE_COLUMN_COLORCOUNT + " INT," + SCORE_COLUMN_NAME +
                " TEXT, " + SCORE_COLUMN_DATE + " TEXT " + ")";
        try{
            db.execSQL(CREATE_TABLE);
        }
        catch(SQLiteException e){ }
        CREATE_TABLE = " CREATE TABLE " + TABLE_PINS + " (" +SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, " + PINS_COLUMN_R + " INT, " + PINS_COLUMN_G + " INT, " + PINS_COLUMN_B + " INT)";
        try{
            db.execSQL(CREATE_TABLE);
        }
        catch(SQLiteException e){ }
        CREATE_TABLE = " CREATE TABLE " + TABLE_SAVE + " (" +SCORE_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + SAVE_PIN1 + " INT, " + SAVE_PIN2 + " INT, " + SAVE_PIN3 + " INT, " + SAVE_PIN4 + " INT, "
                + SAVE_PIN5 + " INT, " + SAVE_PIN6 + " INT, " + SAVE_PIN7 + " INT, " + SAVE_PIN8 + " INT)";
        try{
            db.execSQL(CREATE_TABLE);
        }
        catch(SQLiteException e){ }
    }

    public void saveGame(SaveGame sg) {
        setPreference(PREFERENCE_SAVEGAME_COLORCOUNT, sg.getColorCount());
        setPreference(PREFERENCE_SAVEGAME_ALLOWEMPTY, sg.allowEmpty());
        setPreference(PREFERENCE_SAVEGAME_ALLOWMULTIPLE, sg.allowMultiple());
        setPreference(PREFERENCE_SAVEGAME_MAXTURNS, sg.getMaxTurns());
        setPreference(PREFERENCE_SAVEGAME_HOLES, sg.getHoles());
        resetDatabase(TABLE_SAVE);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (int i = 1; i < sg.getHoles()+1; i++) {
            values.put("PIN" + i, sg.getCode().getCode()[i - 1].getID());
        }
        db.insert(TABLE_SAVE, null, values);
        int k;
        if (sg.getTurns() == null)
            k = 0;
        else
            k = sg.getTurns().length;
        for (int i = 0; i < k; i++) {
            values = new ContentValues();
            for (int j = 1; j < sg.getHoles()+1; j++) {
                values.put("PIN" + j, sg.getTurns()[i].getCode()[j-1].getID());
            }
            db.insert(TABLE_SAVE, null, values);
        }
        db.close();
    }

    public void resetSaveGame(){
        saveGame(new SaveGame(new Code[] {new Code(), new Code(), new Code(), new Code(), new Code(), new Code(), new Code(), new Code()}, new Code(), 10, 5, 5, true, true));
    }

    public SaveGame loadGame() {
        Code[] turns;
        int colors, holes, maxTurns;
        Code c = null;
        boolean allowMultiple, allowEmpty;

        colors = getPreference(PREFERENCE_SAVEGAME_COLORCOUNT);
        maxTurns = getPreference(PREFERENCE_SAVEGAME_MAXTURNS);
        holes = getPreference(PREFERENCE_SAVEGAME_HOLES);
        allowMultiple = getPreferenceBoolean(PREFERENCE_SAVEGAME_ALLOWMULTIPLE);
        allowEmpty = getPreferenceBoolean(PREFERENCE_SAVEGAME_ALLOWEMPTY);

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SAVE + " ORDER BY " + SCORE_COLUMN_ID + " ASC";
        Cursor cu = db.rawQuery(query, null);
        cu.moveToFirst();
        turns = new Code[cu.getCount()-1];
        for (int i = 0; i < cu.getCount() && i<maxTurns; i++) {
            Pin[] turnPins = new Pin[holes];
            for(int j = 0; j<holes; j++){
                turnPins[j] = database.getPin(cu.getInt(j+1));
            }
            if(i == 0){
                c = new Code(turnPins);
            }
            else{
                turns[i-1] = new Code(turnPins);
            }
            cu.moveToNext();
        }
        cu.close();
        db.close();
        SaveGame res = new SaveGame(turns, c, maxTurns, colors, holes, allowEmpty, allowMultiple);
        return res;
    }

    public Pin getPin(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PINS + " WHERE " + SCORE_COLUMN_ID + "=" + id;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getCount() < 1) return new Pin(-1, -1, -1, -1);
        Pin res = new Pin(id, c.getInt(1), c.getInt(2), c.getInt(3));
        c.close();
        db.close();
        return res;
    }

    public int[][] getColorSettings(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PINS + " ORDER BY " + SCORE_COLUMN_ID;
        int[][] colors = new int[8][3];
        try{
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            for(int i =  0; i<c.getCount(); i++){
                int r = c.getColumnIndex(PINS_COLUMN_R);
                int g = c.getColumnIndex(PINS_COLUMN_G);
                int b = c.getColumnIndex(PINS_COLUMN_B);
                colors[i][0] = c.getInt(r);
                colors[i][1] = c.getInt(g);
                colors[i][2] = c.getInt(b);
                c.moveToNext();
            }
            c.close();
        }
        catch (SQLiteException e){
            e.printStackTrace();
        }
        db.close();
        return colors;
    }

    public void updateColorSettings(int[][] colors) {
        resetDatabase(TABLE_PINS);
        SQLiteDatabase db = this.getWritableDatabase();
        updateColorSettings(colors, db);
        db.close();
    }

    public void updateColorSettings(int[][] colors, SQLiteDatabase db) {
        for (int i = 0; i < 8; i++) {
            ContentValues values = new ContentValues();
            values.put(PINS_COLUMN_R, colors[i][0]);
            values.put(PINS_COLUMN_G, colors[i][1]);
            values.put(PINS_COLUMN_B, colors[i][2]);
            try{
                db.insert(TABLE_PINS,  null, values);
            }
            catch(SQLiteException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }

    protected void addScore(Entry_Highscore eintrag) {

        if (numberOfEntrys(eintrag.getColorcount()) > 10) {
            Entry_Highscore worst = getWorstScore(eintrag.getColorcount());
            if (worst.getScore() > eintrag.getScore()) {
                return; //Score ist zu schlecht
            }
            else {
                deleteScore(worst.getScore(), worst.getUsedturns(), worst.getColorcount(), worst.getName(), worst.getDatum());
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

        db.insert(TABLE_SCORE, null, values);
        db.close();
    }

    protected Entry_Highscore getWorstScore(int colorcount) {
        Entry_Highscore[] eintraege = getAllScores(colorcount);
        if (eintraege.length > 0 && eintraege.length < 10)
            return eintraege[eintraege.length - 1];
        else if (eintraege.length >= 10)
            return eintraege[9];
        else
            return null;
    }

    protected Entry_Highscore getBestScore(int colorcount) {
        Entry_Highscore[] eintraege = getAllScores(colorcount);
        if (eintraege.length > 0)
            return eintraege[0];
        else
            return null;
    }

    protected Entry_Highscore[] getAllScores(int colorcount) {
        Entry_Highscore[] eintraege;
        String query = "SELECT * from " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount + " order by " + SCORE_COLUMN_SCORE + " desc";

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        eintraege = new Entry_Highscore[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            eintraege[i] = new Entry_Highscore(Integer.parseInt(cursor.getString(1)), cursor.getInt(2), cursor.getInt(3), cursor.getString(2), cursor.getString(3));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return eintraege;
    }

    protected Entry_Highscore[] getAllScores() {
        Entry_Highscore[] eintraege = new Entry_Highscore[numberOfData()];
        String query = "SELECT * from " + TABLE_SCORE + " order by " + SCORE_COLUMN_SCORE + " desc";

        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && numberOfData() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                eintraege[i] = new Entry_Highscore(Integer.parseInt(cursor.getString(1)), cursor.getInt(2), cursor.getInt(3), cursor.getString(2), cursor.getString(3));
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
        db.close();
        return eintraege;
    }

    protected int numberOfData() {
        String query = "Select count(*) FROM " + TABLE_SCORE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int i=cursor.getInt(0);
        cursor.close();
        db.close();
        return i;
    }

    protected int numberOfEntrys(int colorcount) {
        String query = "Select count(*) FROM " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int i=cursor.getInt(0);
        cursor.close();
        db.close();
        return i;
    }

    protected void deleteScore(int score, int usedturns, int colorcount, String name, String datum){
        String query = "DELETE FROM " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount + " AND " + SCORE_COLUMN_SCORE + "=" + score + " AND " + SCORE_COLUMN_DATE + "=" + datum + " AND " +
                SCORE_COLUMN_NAME + "=" + name + " AND " + SCORE_COLUMN_USEDTURNS + "=" + usedturns;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    protected void resetHighscores(int colorcount) {
        String query = "DELETE FROM " + TABLE_SCORE + " WHERE " + SCORE_COLUMN_COLORCOUNT + "=" + colorcount;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    protected void resetDatabase(String dbName) {
        String query = "DELETE FROM " + dbName + " WHERE 0=0";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    protected int getPreference(String key) {
        return sp.getInteger(key);
    }

    protected String getPreferenceString(String key) {
        return sp.getString(key);
    }

    protected boolean getPreferenceBoolean(String key) {
        return sp.getBoolean(key);
    }

    protected void setPreference(String key, int value) {
        sp.storePreference(key, value);
    }

    protected void setPreference(String key, String value) {
        sp.storePreference(key, value);
    }

    protected void setPreference(String key, boolean value) {
        sp.storePreference(key, value);
    }
}
