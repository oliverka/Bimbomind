package app.ok.bimbomind;

/**
 * Created by Johann on 29.03.2018.
 */

import java.util.Random;

public class Code {

    //enthält den zu lösenden Code
    private Pin[] code;

    //Konstruktor zur verwendung als Code-Generator. setzt alle notwendigen parameter
    public Code(int numberOfHoles, int numberOfPins, boolean allowEmpty, boolean allowMultiple) {
        code = new Pin[numberOfHoles];
        //initialisiere Code
        for(int i=0;i<code.length;i++) {
            code[i] = new Pin(-1, 0, 0, 0);
        }
        generateCode(numberOfHoles, numberOfPins,  allowEmpty, allowMultiple);
    }

    public Code(Pin[] code){
        this.code = code;
    }

    public boolean generateCode(int numberOfHoles, int numberOfPins, boolean allowEmpty, boolean allowMultiple) {
        if(numberOfHoles > numberOfPins && !allowMultiple) { //Mehr Löcher angegeben als mit den gegebenen Parametern gefüllt werden können
            return false;
        }

        Random r = new Random();

        int range = numberOfPins;
        if(allowEmpty) range++; //Zusätzliche Möglichkeit, wenn leere zugelassen sind
        int[] occurred = new int[range]; //Speichert bereits aufgetretene Pins

        Database db = Database.getInstance();
        for(int i = 0; i<numberOfHoles; i++) {
            boolean success = false;
            while(!success) {
                int tmp;
                success = false;
                if(allowEmpty) {
                    tmp = r.nextInt(range)-1;
                    if(tmp < 0 ) {
                        if(!allowMultiple && occurred[tmp+1] > 0) {
                        }
                        else {
                            code[i] = new Pin(-1, -1, -1, -1);
                            occurred[tmp+1]++;
                            success = true;
                        }
                    }
                    else {
                        if(!allowMultiple && occurred[tmp+1] > 0) {
                        }
                        else {
                            code[i] = db.getPin(i);
                            occurred[tmp+1]++;
                            success = true;
                        }
                    }
                }
                else {
                    tmp = r.nextInt(range);
                    if(!allowMultiple && occurred[tmp] > 0) {
                    }
                    else {
                        code[i] = db.getPin(i);
                        occurred[tmp]++;
                        success = true;
                    }
                }
            }
        }

        if(containsEmpty() || containsMultiple()) {
            return false;
        }

        return true;
    }

    public Pin[] getCode(){
        return code;
    }

    //gibt den code an der konsole aus
    public void printCode() {
        System.err.print("Code: ");
        for(int i = 0; i<code.length; i++) {
            System.err.print(code[i].toString() + " ");
        }
        System.err.println();
    }

    //bestimmt, ob leere Stellen vorkommen
    public boolean containsEmpty() {
        for(int i = 0; i<code.length; i++) {
            if(code[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //bestimmt, ob ein Pin mehrfach vorkommt
    public boolean containsMultiple() {
        for(int i = 0; i<code.length-1; i++) {
            for(int j = i+1; j<code.length; j++) {
                if(code[i].equals(code[j])) {
                    return true;
                }
            }
        }
        return false;
    }
}
