package app.ok.bimbomind;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;

/**
 * Created by o.k on 15.03.2018.
 */

public class SettingsBackground extends Activity {

    private Button back;
    private Button slider_color;
    private Button choose_image;
    private SeekBar slider_red;
    private SeekBar slider_green;
    private SeekBar slider_blue;
    private int[] rgbs;
    private Database database;
    private Context context;
    private RadioButton single_color;
    private RadioButton image;
    private ImageView image_preview;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_settings);
        rgbs = new int[3];
        Button[] given_color = new Button[5];
        database = new Database(this, null, null, 1);
        context = this;
        slider_color = (Button) findViewById(R.id.background_settings_slider_color);
        choose_image = (Button) findViewById(R.id.background_settings_choose_image);
        image_preview = (ImageView) findViewById(R.id.background_settings_image_preview);
        slider_red = (SeekBar) findViewById(R.id.background_settings_color_picker_red);
        slider_green = (SeekBar) findViewById(R.id.background_settings_color_picker_green);
        slider_blue = (SeekBar) findViewById(R.id.background_settings_color_picker_blue);
        given_color[0] = (Button) findViewById(R.id.background_settings_color1);
        given_color[1] = (Button) findViewById(R.id.background_settings_color2);
        given_color[2] = (Button) findViewById(R.id.background_settings_color3);
        given_color[3] = (Button) findViewById(R.id.background_settings_color4);
        given_color[4] = (Button) findViewById(R.id.background_settings_color5);
        slider_red.setMax(255);
        slider_red.getProgressDrawable().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        slider_red.getThumb().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        slider_green.setMax(255);
        slider_green.getProgressDrawable().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        slider_green.getThumb().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        slider_blue.setMax(255);
        slider_blue.getProgressDrawable().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);
        slider_blue.getThumb().setColorFilter(0xFF0000FF, PorterDuff.Mode.MULTIPLY);
        back = (Button) findViewById(R.id.background_settings_back);
        single_color = (RadioButton) findViewById(R.id.background_settings_single_color);
        image = (RadioButton) findViewById(R.id.background_settings_image);

        String pathName = database.getPreferenceString(Database.PREFERENCE_SETTINGS_BACKGROUND_IMAGEPATH);
        System.err.println("Path: " + pathName);
        System.err.println("Red: " + database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_R));
        if (pathName != null && !pathName.equals("")) {
            File f = new File(pathName);
            if (f.exists()) {
                Drawable d = Drawable.createFromPath(pathName);
                image_preview.setImageDrawable(d);
            }
        }
        if (database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE) == 0)
            single_color.setChecked(true);
        else if (database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_USEIMAGE) == 1)
            image.setChecked(true);
        rgbs[0] = database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_R);
        rgbs[1] = database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_G);
        rgbs[2] = database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_B);
        slider_red.setProgress(database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_R));
        slider_green.setProgress(database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_G));
        slider_blue.setProgress(database.getPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_B));
        slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsBackground.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
        slider_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgbs[0] = progress;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
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
                rgbs[1] = progress;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
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
                rgbs[2] = progress;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        given_color[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[0] = 0;
                rgbs[1] = 255;
                rgbs[2] = 0;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
                slider_red.setProgress(0);
                slider_green.setProgress(255);
                slider_blue.setProgress(0);
            }
        });
        given_color[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[0] = 0;
                rgbs[1] = 0;
                rgbs[2] = 255;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
                slider_red.setProgress(0);
                slider_green.setProgress(0);
                slider_blue.setProgress(255);
            }
        });
        given_color[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[0] = 255;
                rgbs[1] = 255;
                rgbs[2] = 0;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
                slider_red.setProgress(255);
                slider_green.setProgress(255);
                slider_blue.setProgress(0);
            }
        });
        given_color[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[0] = 255;
                rgbs[1] = 0;
                rgbs[2] = 255;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
                slider_red.setProgress(255);
                slider_green.setProgress(0);
                slider_blue.setProgress(255);
            }
        });
        given_color[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgbs[0] = 255;
                rgbs[1] = 0;
                rgbs[2] = 0;
                slider_color.setBackgroundColor(Color.rgb(rgbs[0], rgbs[1], rgbs[2]));
                slider_red.setProgress(255);
                slider_green.setProgress(0);
                slider_blue.setProgress(0);
            }
        });
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 0);
                } catch (ActivityNotFoundException ex) {
                }
            }
        });
    }
    public void background_settings_radio_group(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.background_settings_single_color:
                if (checked)
                    image.setChecked(false);
                    break;
            case R.id.background_settings_image:
                if (checked)
                    single_color.setChecked(false);
                    break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                database.setPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_IMAGEPATH, selectedImage.getPath());
                image_preview.setImageBitmap(bitmapImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        database.setPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_R, rgbs[0]);
        database.setPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_G, rgbs[1]);
        database.setPreference(Database.PREFERENCE_SETTINGS_BACKGROUND_COLOR_B, rgbs[2]);
        Intent intent = new Intent(SettingsBackground.this, Settings.class);
        startActivity(intent);
        finish();
    }
}
