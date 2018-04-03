package app.ok.bimbomind;

import java.util.Map;

/**
 * Created by Johann on 03.04.2018.
 */

public class SaveGame {
    private Entry_Turn[] turns;
    private int colors, maxTurns;
    private Code c;
    private boolean allowMultiple;

    public SaveGame(Entry_Turn[] turnsMade, Code c, int maxTurns, int colorCount){
        turns = turnsMade;
        this.maxTurns = maxTurns;
        colors = colorCount;
        this.c = c;
    }

    public Entry_Turn[] getTurns(){return turns;}
    public Code getCode(){return c;}
    public int getColorCount(){return colors;}
    public int getMaxTurns(){return maxTurns;}
}
