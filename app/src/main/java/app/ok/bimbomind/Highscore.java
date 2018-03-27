package app.ok.bimbomind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by o.k on 15.03.2018.
 */

public class Highscore extends Activity {

    private Button back;
    private Button reset;
    private LinearLayout table_name;
    private LinearLayout table_score;
    private Context context;
    private Spinner choice;
    private TextView[] names = new TextView[10];
    private TextView[] scores = new TextView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);
        context = this;
        back = (Button) findViewById(R.id.highscore_back);
        reset = (Button) findViewById(R.id.highscore_reset);
        table_name = (LinearLayout) findViewById(R.id.highscore_table_name);
        table_score = (LinearLayout) findViewById(R.id.highscore_table_score);
        choice = (Spinner) findViewById(R.id.highscore_highscore_choice);
        ArrayAdapter<String> adapter;
        List<String> list = new ArrayList<String>();
        list.add("5 Lochspiel");
        list.add("6 Lochspiel");
        list.add("8 Lochspiel");
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Highscore.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_highscore();
            }
        });
        choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.err.println(position + " " + id);
                setTable(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setTable();
    }

    private void reset_highscore() {
    }
//s
    private void setTable() {
        ViewTreeObserver vto = table_name.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                table_name.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                for (int i = 0; i < 10; i ++) {
                    names[i] = new TextView(context);
                    names[i].setText("Schalke: " + i);
                    names[i].setHeight(table_name.getHeight()/10);
                    table_name.addView(names[i]);
                    scores[i] = new TextView(context);
                    scores[i].setText(String.valueOf(i));
                    scores[i].setHeight(table_name.getHeight()/10);
                    table_score.addView(scores[i]);
                }
            }
        });
    }

    private void setTable(int game) {
        for (int i = 0; i < 10; i ++) {
            names[i].setText("Schalke: " + game);
            scores[i].setText(String.valueOf(i));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }
}
