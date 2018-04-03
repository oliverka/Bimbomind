package app.ok.bimbomind;

import java.util.Map;

/**
 * Created by Johann on 29.03.2018.
 */

public class Entry_Turn {
    private Code c;
    private boolean isCode;

    public Entry_Turn(Code _c, boolean _isCode){
        c = _c;
        isCode = _isCode;
    }

    public Code getCode(){return c;}
    public boolean isCode(){return isCode;}
}
