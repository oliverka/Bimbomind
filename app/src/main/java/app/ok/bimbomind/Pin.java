package app.ok.bimbomind;

/**
 * Created by Johann on 29.03.2018.
 */

public class Pin {

    private int r, g, b;

    public Pin(int r, int g, int b) {
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
        if(r == -1 && g == -1 && b == -1) {
            return true;
        }
        else return false;
    }

    @Override
    public String toString() {
        int a = r+g+b;
        return String.valueOf(a);
    }
}
