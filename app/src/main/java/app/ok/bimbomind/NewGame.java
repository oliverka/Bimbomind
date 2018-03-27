package app.ok.bimbomind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by o.k on 15.03.2018.
 */

public class NewGame extends Activity {

    private Button new_game;
    private Button abort;
    private CheckBox allow_empty, allow_multiple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        new_game = (Button) findViewById(R.id.new_game_start_game);
        abort = (Button) findViewById(R.id.new_game_abort);
        allow_empty = (CheckBox) findViewById(R.id.new_game_allow_empty);
        allow_multiple = (CheckBox) findViewById(R.id.new_game_allow_multiple);
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGame.this, Game.class);
                startActivity(intent);
                finish();
            }
        });
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abort();
                Intent intent = new Intent(NewGame.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void abort() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }
}
