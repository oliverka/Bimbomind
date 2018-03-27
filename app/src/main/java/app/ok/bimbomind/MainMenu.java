package app.ok.bimbomind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private Button new_game;
    private Button load_game;
    private Button highscore;
    private Button settings;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        new_game = (Button) findViewById(R.id.main_menu_new_game);
        load_game = (Button) findViewById(R.id.main_menu_load_game);
        highscore = (Button) findViewById(R.id.main_menu_highscore);
        settings = (Button) findViewById(R.id.main_menu_settings);
        context = this;
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, NewGame.class);
                startActivity(intent);
                finish();
            }
        });
        load_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Game.class);
                startActivity(intent);
                finish();
            }
        });
        highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Highscore.class);
                startActivity(intent);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
