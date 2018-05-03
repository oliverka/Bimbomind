package app.ok.bimbomind;

/**
 * Created by Johann on 29.03.2018.
 */

import java.util.Random;

public class Code {

    //enthält den zu lösenden Code, wird auch zur Speicherung von geratenen Kombinationen verwendet
    private Pin[] code;

    //Konstruktor zur verwendung als Code-Generator. setzt alle notwendigen parameter und generiert einen neuen Code
    public Code(int numberOfHoles, int numberOfPins, boolean allowEmpty, boolean allowMultiple) {
        code = new Pin[numberOfHoles];
        //initialisiere Code mit leeren Pins
        for(int i=0;i<code.length;i++) {
            code[i] = new Pin(-1, 0, 0, 0);
        }
        //generiert Code entsprechend der übergebenen Parameter
        generateCode(numberOfHoles, numberOfPins,  allowEmpty, allowMultiple);
    }

    //do not use unless you know what you are doing!
    public Code(){
        //initialisiert mit invaliden Werten
        this(new Pin[]{new Pin(-1, -1, -1, -1), new Pin(-1, -1, -1, -1), new Pin(-1, -1, -1, -1), new Pin(-1, -1, -1, -1),
        new Pin(-1, -1, -1, -1), new Pin(-1, -1, -1, -1), new Pin(-1, -1, -1, -1), new Pin(-1, -1, -1, -1)});
    }

    //Konstruktor anhand eines Pin-Arrays. nicht zur verwendung als Code-generator nutzen.
    public Code(Pin[] code){
        this.code = code;
    }

    public boolean generateCode(int numberOfHoles, int numberOfPins, boolean allowEmpty, boolean allowMultiple) {
        //Längerer Code als mit den gegebenen Parametern gefüllt werden kann
        if(!isPossible(numberOfHoles, numberOfPins, allowMultiple, allowEmpty)){
            return false;
        }
        Random r = new Random();

        int range = numberOfPins;
        if(allowEmpty) range++; //Zusätzliche Möglichkeit, wenn leere zugelassen sind
        int[] occurred = new int[range]; //Speichert bereits aufgetretene Pins

        Database db = Database.getInstance();
        //iterative Code generierung
        for(int i = 0; i<numberOfHoles; i++) {
            boolean success = false;
            //generiere so lange zufällig Pins, bis ein valider gefunden wurde.
            int iterations = 0;
            while(!success && iterations < 1000) { //Maximal 1000 Iterationen erlaubt -> verhindert endlosschleife (sollte sowieso nicht auftreten, aber mit geringer wahrscheinlichkeit möglich)
                int tmp;
                success = false;
                if(allowEmpty) {
                //Generiere auch leere Pins
                    tmp = r.nextInt(range)-1;
                    if(tmp < 0 ) {
                        if(!allowMultiple && occurred[tmp+1] > 0) {
                            //Tu nichts wenn mehrere nicht erlaubt sind und pin schon vorgekommen ist
                        }
                        else {
                            code[i] = new Pin(-1, -1, -1, -1);
                            occurred[tmp+1]++;
                            success = true;
                        }
                    }
                    else {
                        if(!allowMultiple && occurred[tmp+1] > 0) {
                            //Tu nichts wenn mehrere nicht erlaubt sind und pin schon vorgekommen ist
                        }
                        else {
                            code[i] = db.getPin(tmp);
                            occurred[tmp+1]++;
                            success = true;
                        }
                    }
                }
                else {
                    tmp = r.nextInt(range);
                    if(!allowMultiple && occurred[tmp] > 0) {
                        //Tu nichts wenn mehrere nicht erlaubt sind und pin schon vorgekommen ist
                    }
                    else {
                        code[i] = db.getPin(tmp);
                        occurred[tmp]++;
                        success = true;
                    }
                }
                iterations ++;
            }
        }

        //Überprüfe ob Code wirklich korrekt ist
        if(containsEmpty() || containsMultiple()) {
            return false;
        }
        //Code konnte generiert werden
        return true;
    }

    public boolean isPossible(int numberOfHoles, int numberOfPins, boolean allowMultiple, boolean allowEmpty){
        if(numberOfHoles > numberOfPins) { //Code kann unmöglich sein wenn mehr löche als farben
            if(allowMultiple) return true; //mehrere sind erlaubt, code ist auf jeden fall möglich
            else if(allowEmpty && numberOfPins+1 < numberOfHoles) return false; // Code ist auch mit leerem Pin unmöglich
            else if(!allowEmpty) return false; // mehrere sind nicht erlaubt, leere auch nicht -> unmöglich
        }
        return true; //keiner der oberen fälle, code kann korrekt generiert werden
    }

    public Pin[] getCode(){
        return code;
    }

    //gibt den code an der konsole aus, für debugging
    public void printCode() {
        System.err.print("Code: ");
        for(int i = 0; i<code.length; i++) {
            System.err.print(code[i].toString() + " ");
        }
        System.err.println(); //newline
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
