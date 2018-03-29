package app.ok.bimbomind;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by o.k on 15.03.2018.
 */

public class Game extends Activity {

    private LinearLayout field_game;
    private LinearLayout color_picker;
    private LinearLayout[] field_code_pins;
    private TextView[] field_color_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field);
        Context context = this;
        Button back = (Button) findViewById(R.id.game_field_back);
        LinearLayout layout = (LinearLayout) findViewById(R.id.game_field_layout);
        field_game = (LinearLayout) findViewById(R.id.game_field_code);
        color_picker = (LinearLayout) findViewById(R.id.game_field_color_picker);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        int field_code = 5;
        field_color_picker = new TextView[field_code];
        field_code_pins = new LinearLayout[field_code];
        for (int i = 0; i < field_color_picker.length; i ++) {
            field_color_picker[i] = new TextView(context);
            field_code_pins[i] = new LinearLayout(context);
        }
        for (int i = 0; i < field_color_picker.length; i ++) {
            field_color_picker[i].setOnTouchListener(new MyTouchListener());
            field_code_pins[i].setOnDragListener(new MyDragListener(i));
        }
        setFieldGame(field_code);
        setFieldColorPicker(field_code);
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
                    field_code_pins[i].setMinimumHeight((int) (field_game.getHeight() * 0.66));
                    field_code_pins[i].setBackgroundColor(Color.GRAY);
                    field_game.addView(field_code_pins[i]);
                }
            }
        });
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
                    field_color_picker[i].setMinimumHeight((int) (field_game.getHeight() * 0.66));
                    field_color_picker[i].setBackgroundColor(Color.RED);
                    color_picker.addView(field_color_picker[i]);
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
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
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
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    if (((LinearLayout) v).getChildCount() < 1)
                        container.addView(view);
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
