package app.ok.bimbomind;

/**
 * Created by Familie on 17.06.2016.
 */
public class Entry_Highscore {
    int usedturns, colorcount, score;
    String name;
    String datum;
    public Entry_Highscore(int score, int usedturns, int colorcount, String name, String datum) {
        this.score = score;
        this.usedturns = usedturns;
        this.colorcount = colorcount;
        this.name = name;
        this.datum = datum;
    }

    public int getScore() { return score;}

    public int getColorcount() { return colorcount;}

    public int getUsedturns() {
        return usedturns;
    }

    public String getDatum() {
        return datum;
    }

    public String getName() {
        return name;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsedturns(int usedturns) {
        this.usedturns = usedturns;
    }

    public void setColorcount(int colorcount) {this.colorcount = colorcount;}

    public void setScore(int score) {this.score = score;}
}
