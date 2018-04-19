package app.ok.bimbomind;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by o.k on 15.03.2018.
 */

public class Game extends Activity {

    private LinearLayout big_layout;
    private LinearLayout field_game;
    private LinearLayout color_picker;
    private LinearLayout[] field_code_pins;
    private TextView[] field_color_picker;
    private LinearLayout layout;
    private Context context;
    private Button[][] firstrow;
    private Button[][] result;
    private int round;
    private int[][] rgbs;
    private Drawable[] backgrounds;
    private Drawable[] code_drawable;
    private Drawable[][] backgrounds_firstrow;
    private Database database;
    private int rightPlace;
    private int shape;
    private SaveGame saveGame;
    private Pin[] code;
    private Code[] firstrow_code;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field);
        database = Database.getInstance();
        saveGame = database.loadGame();
        code = saveGame.getCode().getCode();
        firstrow_code = saveGame.getTurns();
        round = 0;
        final int field_code = saveGame.getColorCount();
        final int rounds = saveGame.getMaxTurns();
        final int holes = saveGame.getHoles();
        shape = database.getPreference(Database.PREFERENCE_PINS_SHAPE);
        rgbs = database.getColorSettings();

        context = this;
        Button back = (Button) findViewById(R.id.game_field_back);
        layout = (LinearLayout) findViewById(R.id.game_field_field);
        field_game = (LinearLayout) findViewById(R.id.game_field_code);
        color_picker = (LinearLayout) findViewById(R.id.game_field_color_picker);
        big_layout = (LinearLayout) findViewById(R.id.game_field_layout);
        Button set = (Button) findViewById(R.id.game_field_set);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round < rounds) {
                    Drawable[] backgrounds1 = new Drawable[holes];
                    int[] already_right_place = new int[holes];
                    int k = 0;
                    for (int i = 0; i < field_code_pins.length; i++) {
                        backgrounds1[i] = field_code_pins[i].getBackground();
                        backgrounds_firstrow[round][i] = backgrounds1[i];
                        firstrow[round][i].setBackground(backgrounds1[i]);
                        if (code_drawable[i] == backgrounds1[i]) {
                            rightPlace++;
                            result[round][k].setBackgroundColor(Color.RED);
                            k ++;
                            already_right_place[i] = 1;
                        }
                    }
                    for (int i = 0; i < field_code_pins.length; i++) {
                        if (already_right_place[i] == 0) {
                            for (int j = 0; j < field_code_pins.length; j++) {
                                if (code_drawable[i] == backgrounds1[j]) {
                                    result[round][k].setBackgroundColor(Color.BLACK);
                                    k ++;
                                    break;
                                }
                            }
                        }
                    }
                    //System.err.println("Right place: " + rightPlace + " Right color: " + rightColor);
                    round ++;
                    //System.err.println("Round: " + round + " Rounds: " + rounds + " Fieldcode: " + field_code);
                    if (rightPlace == holes) {
                        WonDialog cdd = new WonDialog(Game.this, context, round, holes);
                        cdd.show();
                    }
                    else if (round == rounds) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setTitle(R.string.message)
                                .setMessage(R.string.result_in_highscore)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Game.this, MainMenu.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    rightPlace = 0;
                }
            }
        });
        field_color_picker = new TextView[field_code];
        field_code_pins = new LinearLayout[holes];
        for (int i = 0; i < field_color_picker.length; i ++) {
            field_color_picker[i] = new TextView(context);
            field_color_picker[i].setOnTouchListener(new MyTouchListener());
        }for (int i = 0; i < field_code_pins.length; i ++) {
            field_code_pins[i] = new LinearLayout(context);
            field_code_pins[i].setOnDragListener(new MyDragListener(i));
        }
        setFieldGame(holes);
        setFieldColorPicker(field_code);
        init(holes, rounds);
    }

    private void init(final int field_code, final int rounds) {
        final LinearLayout[] rows = new LinearLayout[rounds];
        firstrow = new Button[rounds][field_code];
        result = new Button[rounds][field_code];
        backgrounds_firstrow = new Drawable[rounds][field_code];

        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                double field_width_1 = (layout.getWidth() / (field_code * 2 - 1)) * (0.6);
                double field_width_2 = (layout.getWidth() / (field_code * 2 - 1)) * (0.3);
                double field_height = (color_picker.getHeight());
                for (int i = 0; i < rows.length; i ++) {
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(layout.getWidth(), (int) field_height);
                    rows[i] = new LinearLayout(context);
                    rows[i].setOrientation(LinearLayout.HORIZONTAL);
                    for (int j = 0; j < field_code; j++) {
                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) field_width_1, (int) (field_game.getHeight() * 0.66));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) field_width_1, (int) field_width_1);
                        firstrow[i][j] = new Button(context);
                        firstrow[i][j].setX((float) (field_width_1 * j));
                        if (i < backgrounds_firstrow.length)
                            firstrow[i][j].setBackground(backgrounds_firstrow[i][j]);
                        switch (shape) {
                            case 0:
                                firstrow[i][j].setBackgroundResource(R.drawable.triangle);
                                break;
                            case 1:
                                firstrow[i][j].setBackgroundResource(R.drawable.circle);
                                break;
                            case 2:
                                firstrow[i][j].setBackgroundResource(R.drawable.rhombus);
                                break;
                            default:
                                firstrow[i][j].setBackgroundResource(R.drawable.square);
                                break;
                        }
                        rows[i].addView(firstrow[i][j], params);
                    }
                    layout.addView(rows[i],params1);
                }
                for (int i = 0; i < rows.length; i ++) {
                    for (int j = 0; j < field_code; j++) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) field_width_2, (int) (field_game.getHeight() * 0.66));
                        result[i][j] = new Button(context);
                        result[i][j].setX((float) (field_width_2 * j) + (float) (field_width_1 * field_code));
                        rows[i].addView(result[i][j], params);
                    }
                }
            }
        });
    }

    private void setFieldGame(final int field_code) {
        ViewTreeObserver vto = field_game.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                field_game.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                double field_width = (field_game.getWidth() / (field_code * 2 - 1));
                for (int i = 0; i < field_code; i ++) {
                    field_code_pins[i].setX((float) (field_width * i));
                    field_code_pins[i].setMinimumWidth((int) field_width);
                    //field_code_pins[i].setMinimumHeight((int) (field_game.getHeight() * 0.66));
                    field_code_pins[i].setMinimumHeight((int) field_width);
                    switch (shape) {
                        case 0:
                            field_code_pins[i].setBackgroundResource(R.drawable.triangle);
                            break;
                        case 1:
                            field_code_pins[i].setBackgroundResource(R.drawable.circle);
                            break;
                        case 2:
                            field_code_pins[i].setBackgroundResource(R.drawable.rhombus);
                            break;
                        default:
                            field_code_pins[i].setBackgroundResource(R.drawable.square);
                            break;
                    }
                    field_game.addView(field_code_pins[i]);
                }
            }
        });
        if (database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE) == 0) {
            String hex = String.format("#%02x%02x%02x", database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_R), database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_G), database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_B));
            big_layout.setBackgroundColor(Color.parseColor(hex));
        }
        else {
            //TODO set Image
        }
    }
    private void setFieldColorPicker(final int field_code) {
        ViewTreeObserver vto = field_game.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                color_picker.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                double field_width = (field_game.getWidth() / (field_code * 2 - 1));
                for (int i = 0; i < field_code; i ++) {
                    field_color_picker[i].setX((float) (field_width * i));
                    field_color_picker[i].setMinimumWidth((int) field_width);
                    //field_color_picker[i].setMinimumHeight((int) (field_game.getHeight() * 0.66));
                    field_color_picker[i].setMinimumHeight((int) field_width);
                    color_picker.addView(field_color_picker[i]);
                }
                backgrounds = new Drawable[field_code];
                code_drawable = new Drawable[field_code];
                int double1 = -1;
                for (int i = 0; i < field_code; i++) {
                    for (int j = 0; j < i; j++) {
                        if (rgbs[i][0] == rgbs[j][0] && rgbs[i][1] == rgbs[j][1] && rgbs[i][2] == rgbs[j][2] && i != j)
                            double1 = j;
                    }
                    if (double1 == -1) {
                        Drawable mDrawable;
                        switch (shape) {
                            case 0:
                                mDrawable = context.getResources().getDrawable(R.drawable.triangle);
                                break;
                            case 1:
                                mDrawable = context.getResources().getDrawable(R.drawable.circle);
                                break;
                            case 2:
                                mDrawable = context.getResources().getDrawable(R.drawable.rhombus);
                                break;
                            default:
                                mDrawable = context.getResources().getDrawable(R.drawable.square);
                                break;
                        }
                        String hex = String.format("#%02x%02x%02x", rgbs[i][0], rgbs[i][1], rgbs[i][2]);
                        mDrawable.setColorFilter(Color.parseColor(hex) , PorterDuff.Mode.MULTIPLY);
                        field_color_picker[i].setBackground(mDrawable);
                        backgrounds[i] = field_color_picker[i].getBackground();
                    }
                    else {
                        field_color_picker[i].setBackground(backgrounds[double1]);
                        backgrounds[i] = backgrounds[double1];
                    }
                    double1 = -1;
                }
                for (int i = 0; i < backgrounds.length; i ++) {
                    code_drawable[i] = backgrounds[code[i].getID() - 1];
                    System.err.println("ID: "+code[i].getID());
                }
                for (int i = 0; i < round; i ++) {
                    for (int j = 0; j < field_code; j++) {
                        if (firstrow_code[i] != null)
                            backgrounds_firstrow[i][j] = backgrounds[firstrow_code[i].getCode()[j].getID()];
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        SaveGame saveGame1 = new SaveGame(saveTurns(),saveGame.getCode(), saveGame.getMaxTurns(), saveGame.getColorCount(), saveGame.getHoles(), saveGame.allowEmpty(), saveGame.allowMultiple());
        database.saveGame(saveGame1);
        finish();
    }
    private Code[] saveTurns() {
        Code[] code = new Code[round];
        for (int l = 0; l < code.length; l++) {
            Pin[] colors = new Pin[saveGame.getHoles()];
            for (int i = 0; i < saveGame.getHoles(); i++) {
                for (int j = 0; j < saveGame.getColorCount(); j++) {
                    if (backgrounds[j] == backgrounds_firstrow[l][i]) {
                        colors[i] = database.getPin(j + 1);
                        break;
                    }
                }
            }
            code[l] = new Code(colors);
        }
        return code;
    }

    private final class MyTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
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


    class MyDragListener implements View.OnDragListener {

        int i;

        MyDragListener(int i) {
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
