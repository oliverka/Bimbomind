package app.ok.bimbomind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by o.k on 15.03.2018.
 */

public class Tutorial extends Activity {

    private Button back;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        back = (Button) findViewById(R.id.tutorial_back);
        text = (TextView) findViewById(R.id.tutorial_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tutorial.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Tutorial.this, Settings.class);
        startActivity(intent);
        finish();
    }
}
