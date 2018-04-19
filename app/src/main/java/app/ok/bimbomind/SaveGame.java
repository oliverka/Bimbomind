package app.ok.bimbomind;

/**
 * Created by Johann on 03.04.2018.
 */

public class SaveGame {
    private Code[] turns;
    private int colors, holes, maxTurns;
    private Code c;
    private boolean allowMultiple, allowEmpty;

    public SaveGame(Code[] turns, Code c, int maxTurns, int colorCount, int holes, boolean allowEmpty, boolean allowMultiple){
        this.turns = turns;
        this.maxTurns = maxTurns;
        colors = colorCount;
        this.c = c;
        this.holes = holes;
        this.allowEmpty = allowEmpty;
        this.allowMultiple = allowMultiple;
    }

    public Code[] getTurns(){return turns;}
    public Code getCode(){return c;}
    public int getColorCount(){return colors;}
    public int getMaxTurns(){return maxTurns;}
    public int getHoles(){return holes;}
    public boolean allowEmpty(){return allowEmpty;}
    public boolean allowMultiple(){return allowMultiple;}
}
