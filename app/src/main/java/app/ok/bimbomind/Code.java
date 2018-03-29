package app.ok.bimbomind;

/**
 * Created by Johann on 29.03.2018.
 */

import java.util.Random;

public class Code {

    private Pin[] code, pins;

    public Code(int numberOfHoles, int numberOfPins, Pin[] pins, boolean allowEmpty, boolean allowMultiple) {
        code = new Pin[numberOfHoles];
        for(int i=0;i<code.length;i++) {
            code[i] = new Pin(0, 0, 0);
        }
        this.pins = pins;
        generateCode(numberOfHoles, numberOfPins,  allowEmpty, allowMultiple);
    }

    public void setPins(Pin[] newPins) {
        pins = newPins;
    }

    public boolean generateCode(int numberOfHoles, int numberOfPins, boolean allowEmpty, boolean allowMultiple) {
        if(numberOfHoles > numberOfPins && !allowMultiple) { //More Holes than can be filled
            return false;
        }

        Random r = new Random();

        int range = numberOfPins;
        if(allowEmpty) range++;
        int[] occurred = new int[range];

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
                            code[i] = new Pin(-1, -1, -1);
                            occurred[tmp+1]++;
                            success = true;
                        }
                    }
                    else {
                        if(!allowMultiple && occurred[tmp+1] > 0) {
                        }
                        else {
                            code[i] = pins[tmp];
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
                        code[i] = pins[tmp];
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


    public void printCode() {
        System.out.print("Code: ");
        for(int i = 0; i<code.length; i++) {
            System.out.print(code[i].toString() + " ");
        }
        System.out.println();
    }

    private boolean containsEmpty() {
        for(int i = 0; i<code.length; i++) {
            if(code[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMultiple() {
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
