package app.ok.bimbomind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by o.k on 15.03.2018.
 */

public class SettingsBackground extends Activity {

    private Button back;
    private RadioButton single_color;
    private RadioButton image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background_settings);
        back = (Button) findViewById(R.id.background_settings_back);
        single_color = (RadioButton) findViewById(R.id.background_settings_single_color);
        image = (RadioButton) findViewById(R.id.background_settings_image);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsBackground.this, Settings.class);
                startActivity(intent);
                finish();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsBackground.this, Settings.class);
        startActivity(intent);
        finish();
    }
}
