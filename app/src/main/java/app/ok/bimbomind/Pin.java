package app.ok.bimbomind;

/**
 * Created by Johann on 29.03.2018.
 */

public class Pin {

    private int r, g, b;
    private int id;

    public Pin(int id, int r, int g, int b) {
        this.id = id;
        this.r = r;
        this.g= g;
        this.b = b;
    }

    public boolean equals(Pin cmp) {
        if(cmp.r == r && cmp.g == g&& cmp.b == b) {
            return true;
        }
        else return false;
    }

    public boolean isEmpty() {
        if(getID() == -1) {
            return true;
        }
        else return false;
    }

    @Override
    public String toString() {
        int a = r+g+b;
        return String.valueOf(a);
    }

    public int getID(){return id;}
    public int[] getRGB(){return new int[] {r, g, b};}
}
