package app.ok.bimbomind;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Johann on 29.03.2018.
 */

public class StoredPreferences{
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    private static final String FILENAME = "BimboMindPreferences";

    public StoredPreferences(Context c){
        sp = c.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        ed = sp.edit();
    }

    protected void storePreference(String key, String value){
        ed.putString(key, value);
    }

    protected void storePreference(String key, int value){
        ed.putInt(key, value);
    }

    protected String getString(String key){
        return sp.getString(key, "void");
    }

    protected int getInteger(String key){
        return sp.getInt(key, 0);
    }
}
