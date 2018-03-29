package app.ok.bimbomind;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

/**
 * Created by o.k on 15.03.2018.
 */

public class PinSettings extends Activity {

    private Button back;
    private Button slider_color;
    private SeekBar slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_settings);
        back = (Button) findViewById(R.id.pin_settings_back);
        slider_color = (Button) findViewById(R.id.pin_settings_slider_color);
        slider = (SeekBar) findViewById(R.id.pin_settings_color_picker);
        slider.setMax(0xFFFFFF);
        slider.getProgressDrawable().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        slider.getThumb().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.err.println(progress);
                //String color = "#" + Integer.toHexString(progress);
                //System.err.println(Color.parseColor(color));
                //slider_color.setBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinSettings.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PinSettings.this, Settings.class);
        startActivity(intent);
        finish();
    }

}
