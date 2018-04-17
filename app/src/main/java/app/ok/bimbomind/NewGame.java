package app.ok.bimbomind;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by o.k on 15.03.2018.
 */

public class NewGame extends Activity {

    private Database database;
    private CheckBox allow_empty;
    private CheckBox allow_multiple;
    private EditText turns;
    private EditText colors;
    private EditText holes;
    private ToggleButton ai_human;
    private ToggleButton set_guess;
    private TextView error_text;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        //database = new Database(this,null,null, 1);
        database = Database.getInstance();
        Button new_game = (Button) findViewById(R.id.new_game_start_game);
        Button abort = (Button) findViewById(R.id.new_game_abort);
        allow_empty = (CheckBox) findViewById(R.id.new_game_allow_empty);
        allow_multiple = (CheckBox) findViewById(R.id.new_game_allow_multiple);
        turns = (EditText) findViewById(R.id.new_game_turns_input);
        colors = (EditText) findViewById(R.id.new_game_colors_input);
        holes = (EditText) findViewById(R.id.new_game_holes_input);
        ai_human = (ToggleButton) findViewById(R.id.new_game_ai_human);
        set_guess = (ToggleButton) findViewById(R.id.new_game_set_guess);
        error_text = (TextView) findViewById(R.id.new_game_error_text);
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveStats()) {
                    Intent intent;
                    if(set_guess.isChecked()){
                        intent = new Intent(NewGame.this, Game.class);
                    }
                    else{
                        intent = new Intent(NewGame.this, EnterCode.class);
                    }
                    startActivity(intent);
                    finish();
                }
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

    private boolean saveStats() {
        try {
            error_text.setText("");
            boolean turns1 = true, colors1 = true, holes1 = true;
            if (Integer.parseInt(String.valueOf(turns.getText())) > 12) {
                turns1 = false;
                error_text.setTextColor(Color.RED);
                error_text.append("Only less than 13 turns are allow\n");
            }
            if (Integer.parseInt(String.valueOf(colors.getText())) != 5 &&
                    Integer.parseInt(String.valueOf(colors.getText())) != 6 &&
                    Integer.parseInt(String.valueOf(colors.getText())) != 8) {
                colors1 = false;
                error_text.setTextColor(Color.RED);
                error_text.append("Only 5, 6 or 8 different colors are allow\n");
            }
            if (Integer.parseInt(String.valueOf(holes.getText())) != 5 &&
                    Integer.parseInt(String.valueOf(holes.getText())) != 6 &&
                    Integer.parseInt(String.valueOf(holes.getText())) != 8) {
                holes1 = false;
                error_text.setTextColor(Color.RED);
                error_text.append("Only 5, 6 or 8 holes are allow\n");
            }
            if (turns1 && colors1 && holes1) {
                database.setPreference(Database.PREFERENCE_SAVEGAME_ALLOWEMPTY, allow_empty.isChecked());
                database.setPreference(Database.PREFERENCE_SAVEGAME_ALLOWMULTIPLE, allow_multiple.isChecked());
                database.setPreference(Database.PREFERENCE_SAVEGAME_MAXTURNS, Integer.parseInt(String.valueOf(turns.getText())));
                database.setPreference(Database.PREFERENCE_SAVEGAME_COLORCOUNT, Integer.parseInt(String.valueOf(colors.getText())));
                database.setPreference(Database.PREFERENCE_SAVEGAME_HOLES, Integer.parseInt(String.valueOf(holes.getText())));
                database.setPreference(Database.PREFERENCE_SAVEGAME_AI, ai_human.isChecked());
                database.setPreference(Database.PREFERENCE_SAVEGAME_SET, set_guess.isChecked());
                return true;
            }
        }catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter some values",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
