package com.example.teddydance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GameOverActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    int bg, index_song, score;

    HighScore highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        this.bg = intent.getIntExtra(Values.bg, 1);
        this.index_song = intent.getIntExtra(Values.index_song, 0);
        this.score = intent.getIntExtra(Values.score, -1);

        highScore = new HighScore();
        ArrayList<HashMap<String, String>> arrayList;
        arrayList = new ArrayList<>();
        arrayList = highScore.read_sortedScore(getApplicationContext(), arrayList);
        if ( arrayList.size() == 0 || score > Integer.valueOf(arrayList.get(0).get(Values.score))) {
            TextView newHighScore = (TextView) findViewById(R.id.newHighScore);
            newHighScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.large_text));
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.alone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        TextView scoreText = (TextView) findViewById(R.id.scoreText);
        scoreText.setText(String.valueOf(score));
        
        ImageView save = (ImageView) findViewById(R.id.save);
        int screenHeight = intent.getIntExtra(Values.screenHeight, 1);
        save.getLayoutParams().height = (int) (screenHeight * 0.08);
        save.setImageDrawable(getDrawable(R.drawable.custom_button_save));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) findViewById(R.id.editText);
                String playerName = editText.getText().toString();
                if (playerName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_LONG)
                            .show();
                } else {
                    messageBox(playerName);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mediaPlayer.pause();
    }

    @Override
    public void onBackPressed() { }

    public void messageBox(String playerName) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you \"" + playerName + "\" ?");
        builder.setMessage("Save your score ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                highScore.saveScore(getApplicationContext(), playerName, score);
                Intent intent = new Intent(GameOverActivity.this, HighScoreActivity.class);
                intent.putExtra(Values.bg, bg);
                intent.putExtra(Values.index_song, index_song);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}