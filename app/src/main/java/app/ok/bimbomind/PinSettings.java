package app.ok.bimbomind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by o.k on 15.03.2018.
 */

public class PinSettings extends Activity {

    private Button slider_color;
    private SeekBar slider_red;
    private SeekBar slider_green;
    private SeekBar slider_blue;
    private int[][] rgbs;
    private Spinner pins;
    private int colornum;
    private int shape;
    private Database database;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_settings);
        rgbs = new int[8][3];
        Button[] given_color = new Button[5];
        database = Database.getInstance();
        rgbs = database.getColorSettings();
        context = this;
        Button back = (Button) findViewById(R.id.pin_settings_back);
        Button save = (Button) findViewById(R.id.pin_settings_save);
        slider_color = (Button) findViewById(R.id.pin_settings_slider_color);
        slider_red = (SeekBar) findViewById(R.id.pin_settings_color_picker_red);
        slider_green = (SeekBar) findViewById(R.id.pin_settings_color_picker_green);
        slider_blue = (SeekBar) findViewById(R.id.pin_settings_color_picker_blue);
        given_color[0] = (Button) findViewById(R.id.pin_settings_color1);
        given_color[1] = (Button) findViewById(R.id.pin_settings_color2);
        given_color[2] = (Button) findViewById(R.id.pin_settings_color3);
        given_color[3] = (Button) findViewById(R.id.pin_settings_color4);
        given_color[4] = (Button) findViewById(R.id.pin_settings_color5);
        Button triangle = (Button) findViewById(R.id.pin_settings_dreieck);
        Button circle = (Button) findViewById(R.id.pin_settings_kreis);
        Button rhombus = (Button) findViewById(R.id.pin_settings_rhombus);
        Button square = (Button) findViewById(R.id.pin_settings_quadrat);
        slider_red.setMax(255);
        slider_red.getProgressDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        slider_red.getThumb().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        slider_green.setMax(255);
        slider_green.getProgressDrawable().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        slider_green.getThumb().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        slider_blue.setMax(255);
        slider_blue.getProgressDrawable().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);
        slider_blue.getThumb().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);
        String[] arraySpinner = new String[] {"Color 1", "Color 2", "Color 3", "Color 4", "Color 5", "Color 6", "Color 7", "Color 8"};
        pins = (Spinner) findViewById(R.id.pin_settings_pins);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        pins.setAdapter(adapter);
        slider_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colornum = pins.getSelectedItemPosition();
                rgbs[colornum][0] = progress;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colornum = pins.getSelectedItemPosition();
                rgbs[colornum][1] = progress;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        slider_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colornum = pins.getSelectedItemPosition();
                rgbs[colornum][2] = progress;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colornum = position;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
                slider_red.setProgress(rgbs[colornum][0]);
                slider_green.setProgress(rgbs[colornum][1]);
                slider_blue.setProgress(rgbs[colornum][2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStats();
                Intent intent = new Intent(PinSettings.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStats();
                Toast.makeText(context, R.string.saved_succesfully, Toast.LENGTH_SHORT).show();
            }
        });
        given_color[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[colornum][0] = 0;
                rgbs[colornum][1] = 255;
                rgbs[colornum][2] = 0;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
                slider_red.setProgress(0);
                slider_green.setProgress(255);
                slider_blue.setProgress(0);
            }
        });
        given_color[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[colornum][0] = 0;
                rgbs[colornum][1] = 0;
                rgbs[colornum][2] = 255;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
                slider_red.setProgress(0);
                slider_green.setProgress(0);
                slider_blue.setProgress(255);
            }
        });
        given_color[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[colornum][0] = 255;
                rgbs[colornum][1] = 255;
                rgbs[colornum][2] = 0;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
                slider_red.setProgress(255);
                slider_green.setProgress(255);
                slider_blue.setProgress(0);
            }
        });
        given_color[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[colornum][0] = 255;
                rgbs[colornum][1] = 0;
                rgbs[colornum][2] = 255;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
                slider_red.setProgress(255);
                slider_green.setProgress(0);
                slider_blue.setProgress(255);
            }
        });
        given_color[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[colornum][0] = 255;
                rgbs[colornum][1] = 0;
                rgbs[colornum][2] = 0;
                slider_color.setBackgroundColor(Color.rgb(rgbs[colornum][0],rgbs[colornum][1],rgbs[colornum][2]));
                slider_red.setProgress(255);
                slider_green.setProgress(0);
                slider_blue.setProgress(0);
            }
        });

        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape = 0;
                Toast.makeText(context, "Triangle selected", Toast.LENGTH_SHORT).show();
            }
        });
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape = 1;
                Toast.makeText(context, "Circle selected", Toast.LENGTH_SHORT).show();
            }
        });
        rhombus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape = 2;
                Toast.makeText(context, "Rhombus selected", Toast.LENGTH_SHORT).show();
            }
        });
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shape = 3;
                Toast.makeText(context, "Square selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveStats();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveStats();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveStats();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveStats();
        Intent intent = new Intent(PinSettings.this, Settings.class);
        startActivity(intent);
        finish();
    }
    private void saveStats() {
        database.updateColorSettings(rgbs);
        database.setPreference(Database.PREFERENCE_PINS_SHAPE, shape);
    }
}
