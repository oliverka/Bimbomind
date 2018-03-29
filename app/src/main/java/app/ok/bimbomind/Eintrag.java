package dtts.dtts.dtts.dtts.app;

/**
 * Created by Familie on 17.06.2016.
 */
public class Eintrag {
    int score;
    String name;
    String datum;
    public Eintrag(int score, String name, String datum) {
        this.score = score;
        this.name = name;
        this.datum = datum;
    }

    public int getScore() {
        return score;
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

    public void setScore(int score) {
        this.score = score;
    }
}
