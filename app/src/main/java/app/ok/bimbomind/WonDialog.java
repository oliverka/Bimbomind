package app.ok.bimbomind;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WonDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity c;
    private Database database;
    private Button save_in_highscore, go_to_menu;
    private EditText input;
    private TextView text;
    private int rounds, number_of_holes;
    private Context context;


    public WonDialog (Activity a, Context context, int rounds, int number_of_holes) {
        super(a);
        database = Database.getInstance();
        this.context = context;
        this.c = a;
        this.rounds = rounds;
        this.number_of_holes = number_of_holes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.won_dialog);
        setCancelable(false);
        save_in_highscore = (Button) findViewById(R.id.won_dialog_save_in_highscore);
        go_to_menu = (Button) findViewById(R.id.won_dialog_go_to_menu);
        input = (EditText) findViewById(R.id.won_dialog_input);
        text = (TextView)findViewById(R.id.won_dialog_text);
        save_in_highscore.setOnClickListener(this);
        go_to_menu.setOnClickListener(this);
        text.setText("You won in " + rounds + " with " + number_of_holes + " holes. Please enter your name and save your result");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.won_dialog_save_in_highscore:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle(R.string.message)
                        .setMessage(R.string.result_in_highscore)
                        .setPositiveButton(android.R.string.yes, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Entry_Highscore entry = new Entry_Highscore(rounds, rounds, number_of_holes, String.valueOf(input.getText()), String.valueOf(System.currentTimeMillis()));
                                database.addScore(entry);
                                Toast.makeText(context, context.getResources().getString(R.string.save_score), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(c, MainMenu.class);
                                c.startActivity(intent);
                                c.finish();
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.no, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            case R.id.won_dialog_go_to_menu:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}