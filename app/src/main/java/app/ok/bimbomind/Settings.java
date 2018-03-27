package app.ok.bimbomind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by o.k on 15.03.2018.
 * sssss
 */

public class Settings extends Activity {

    private Button back;
    private Button background;
    private Button tutorial;
    private Button pin_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        back = (Button) findViewById(R.id.settings_back);
        background = (Button) findViewById(R.id.settings_background);
        tutorial = (Button) findViewById(R.id.settings_tutorial);
        pin_settings = (Button) findViewById(R.id.settings_pin_settings);
        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, SettingsBackground.class);
                startActivity(intent);
                finish();
            }
        });
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Tutorial.class);
                startActivity(intent);
                finish();
            }
        });
        pin_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, PinSettings.class);
                startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainMenu.class);
                startActivity(intent);
                finish();
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
}
