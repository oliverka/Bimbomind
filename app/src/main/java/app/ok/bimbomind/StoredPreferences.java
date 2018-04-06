package app.ok.bimbomind;

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
        System.err.println(key + " to store: " + value);
        ed.commit();
    }

    protected void storePreference(String key, int value){
        ed.putInt(key, value);
        System.err.println(key + " to store: " + value);
        ed.commit();
    }

    protected String getString(String key){
        System.err.println(key + " got: " + sp.getString(key,"void"));
        return sp.getString(key, "void");
    }

    protected int getInteger(String key){
        System.err.println(key + " got: " + sp.getInt(key,0));
        return sp.getInt(key, 0);
    }
}
