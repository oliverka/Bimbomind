package app.ok.bimbomind;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by o.k on 15.03.2018.
 */

public class Game extends Activity {

    private Button back;
    private LinearLayout field_game;
    private LinearLayout color_picker;
    private LinearLayout layout;
    private ImageView[] field_code_pins;
    private TextView[] field_color_picker;
    private Context context;
    private int field_code = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field);
        context = this;
        back = (Button) findViewById(R.id.game_field_back);
        layout = (LinearLayout) findViewById(R.id.game_field_layout);
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
        field_color_picker = new TextView[field_code];
        field_code_pins = new ImageView[field_code];
        for (int i = 0; i < field_color_picker.length; i ++) {
            field_color_picker[i] = new TextView(context);
            field_code_pins[i] = new ImageView(context);
        }
        setFieldGame(field_code);
        setFieldColorPicker(field_code);
        for (int i = 0; i < field_color_picker.length; i ++) {
            final int finalI = i;
            field_color_picker[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipData.Item item = new ClipData.Item((Intent) v.getTag());
                    ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},item);
                    ClipData.Item data = dragData.getItemAt(0);
                    System.err.println("Data: " + item.getText() + " " + item.getHtmlText());
                    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(field_color_picker[finalI]);
                    v.startDrag(dragData,myShadow, null,0);
                }
            });
            myDragEventListener mDragListen = new myDragEventListener();
            field_code_pins[i].setOnDragListener(mDragListen);
        }
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
                    field_code_pins[i].setImageResource(R.drawable.code_border);
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

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {
            super(v);
            shadow = new ColorDrawable(Color.LTGRAY);
        }
        @Override
        public void onProvideShadowMetrics (Point size, Point touch) {
            int width, height;
            width = getView().getWidth() / 2;
            height = getView().getHeight() / 2;
            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }
        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }
    }
    protected class myDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            //System.err.println(action);
            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        //v.setColorFilter(Color.BLUE);
                        v.invalidate();
                        return true;

                    }
                    return false;

                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setColorFilter(Color.GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setColorFilter(Color.BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData = (String) item.getText();
                    System.err.println("Dragged Data: " + dragData);
                    //v.clearColorFilter();
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    //v.clearColorFilter();
                    v.invalidate();
                    if (event.getResult()) {
                        System.err.println(event.getLocalState() + " || " + v.getId());

                    } else {
                        Toast.makeText(context, "The drop didn't work.", Toast.LENGTH_LONG);

                    }

                    // returns true; the value is ignored.
                    return true;

                // An unknown action type was received.
                default:
                    //Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    };
}
