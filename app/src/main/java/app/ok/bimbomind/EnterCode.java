package app.ok.bimbomind;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Johann on 17.04.2018.
 */

public class EnterCode extends Activity {

    private LinearLayout layout;
    private LinearLayout color_picker;
    private LinearLayout[] field_code_pins;
    private TextView[] field_color_picker;
    private Context context;
    private Button set;
    private Button[][] firstrow;
    private Button[][] result;
    private TextView errorText;
    private int[][] rgbs;
    private Drawable[] backgrounds;
    private Database database;
    private Code code;

    private static int CodeLength;
    private static int ColorCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_code);
        database = Database.getInstance();
        CodeLength = database.getPreference(Database.PREFERENCE_SAVEGAME_HOLES);
        ColorCount = database.getPreference(Database.PREFERENCE_SAVEGAME_COLORCOUNT);
        rgbs = database.getColorSettings();
        context = this;
        Button back = (Button) findViewById(R.id.setcode_back);
        layout = (LinearLayout) findViewById(R.id.set_code);
        errorText = (TextView) findViewById(R.id.set_code_error);
        color_picker = (LinearLayout) findViewById(R.id.set_code_color_picker);
        set = (Button) findViewById(R.id.setcode_submit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterCode.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCode();
                if(isValid()){
                    database.saveGame(generateSavegame());
                    SaveGame test = database.loadGame();
                    test.getCode().printCode();
                    Intent intent = new Intent(EnterCode.this, Game.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    errorText.setText("Please enter a valid Code.");
                    errorText.setTextColor(Color.RED);
                }
            }
        });
        field_color_picker = new TextView[CodeLength];
        field_code_pins = new LinearLayout[CodeLength];
        for (int i = 0; i < field_color_picker.length; i ++) {
            field_color_picker[i] = new TextView(context);
            field_code_pins[i] = new LinearLayout(context);
        }
        for (int i = 0; i < field_color_picker.length; i ++) {
            field_color_picker[i].setOnTouchListener(new MyTouchListener());
            field_code_pins[i].setOnDragListener(new MyDragListener(i));
        }
        setFieldGame(CodeLength);
        setFieldColorPicker(CodeLength);
    }

    //    public SaveGame(Entry_Turn[] turnsMade, Code c, int maxTurns, int colorCount, int holes, boolean allowEmpty, boolean allowMultiple){
    private SaveGame generateSavegame(){
        return new SaveGame(new Code[] {new Code(), new Code(), new Code(), new Code(), new Code(), new Code(), new Code(), new Code()}, code, database.getPreference(Database.PREFERENCE_SAVEGAME_MAXTURNS),
                ColorCount, CodeLength, database.getPreferenceBoolean(Database.PREFERENCE_SAVEGAME_ALLOWEMPTY), database.getPreferenceBoolean(Database.PREFERENCE_SAVEGAME_ALLOWMULTIPLE));
    }

    private void setFieldGame(final int CodeLength) {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                double field_width = (layout.getWidth() / (CodeLength * 2 - 1));
                for (int i = 0; i < CodeLength; i ++) {
                    field_code_pins[i].setX((float) (field_width * i));
                    field_code_pins[i].setMinimumWidth((int) field_width);
                    field_code_pins[i].setMinimumHeight((int) (layout.getHeight() * 0.66));
                    field_code_pins[i].setBackgroundColor(Color.GRAY);
                    layout.addView(field_code_pins[i]);
                }
            }
        });
    }
    private void setFieldColorPicker(final int CodeLength) {
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                color_picker.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                double field_width = (layout.getWidth() / (CodeLength * 2 - 1));
                for (int i = 0; i < CodeLength; i ++) {
                    field_color_picker[i].setX((float) (field_width * i));
                    field_color_picker[i].setMinimumWidth((int) field_width);
                    field_color_picker[i].setMinimumHeight((int) (layout.getHeight() * 0.66));
                    color_picker.addView(field_color_picker[i]);
                }
                backgrounds = new Drawable[CodeLength];
                int double1 = -1;
                for (int i = 0; i < CodeLength; i++) {
                    for (int j = 0; j < i; j++) {
                        if (rgbs[i][0] == rgbs[j][0] && rgbs[i][1] == rgbs[j][1] && rgbs[i][2] == rgbs[j][2] && i != j)
                            double1 = j;
                    }
                    if (double1 == -1) {
                        field_color_picker[i].setBackgroundColor(Color.rgb(rgbs[i][0], rgbs[i][1], rgbs[i][2]));
                        backgrounds[i] = field_color_picker[i].getBackground();
                    }
                    else {
                        field_color_picker[i].setBackground(backgrounds[double1]);
                        backgrounds[i] = backgrounds[double1];
                    }
                    double1 = -1;
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    public void updateCode(){
        Pin[] Colors = new Pin[CodeLength];
        for(int i = 0; i<CodeLength; i++){
            Colors[i] = new Pin(-1, -1, -1, -1);
        }
        for(int i = 0; i<CodeLength; i++){
            for(int j = 0; j<ColorCount; j++){
                if(field_code_pins[i].getBackground() == backgrounds[j]) {
                    Colors[i] = database.getPin(j+1);
                    break;
                }
            }
        }
        code = new Code(Colors);
    }

    public boolean isValid(){
        if(code.containsEmpty() && !database.getPreferenceBoolean(database.PREFERENCE_SAVEGAME_ALLOWEMPTY)
                || code.containsMultiple() && !database.getPreferenceBoolean(database.PREFERENCE_SAVEGAME_ALLOWMULTIPLE)){
            return false;
        }
        return true;
    }


    class MyDragListener implements View.OnDragListener {

        int i = 9;

        public MyDragListener(int i) {
            this.i = i;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (field_code_pins[0] == v) {

            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    if (((LinearLayout) v).getChildCount() < 1) {
                        v.setBackground(view.getBackground());
                    }
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
}
