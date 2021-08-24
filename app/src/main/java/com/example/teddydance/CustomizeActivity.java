package com.example.teddydance;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomizeActivity extends AppCompatActivity {

    View backgroundView;
    MediaPlayer mediaPlayer;
    int[] songList;
    int index_song, bg;


    private void setup() {
        Intent intent = getIntent();
        if(intent != null) {
            index_song = intent.getIntExtra(Values.index_song, 0);
            bg = intent.getIntExtra(Values.bg, 1);
        } else {
            // Default
            index_song = 0;
            bg = 1;
        }

        int bg_resID = getResources().getIdentifier("bg"+bg, "drawable", this.getPackageName());
        backgroundView = findViewById(R.id.bg);
        backgroundView.setBackgroundResource(bg_resID);

        songList = Values.songList;
        mediaPlayer = MediaPlayer.create(this, songList[index_song]);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void setBG(int bg) {
        int bg_resID = getResources().getIdentifier("bg"+bg, "drawable", this.getPackageName());
        backgroundView = findViewById(R.id.bg);
        backgroundView.setBackgroundResource(bg_resID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // bg and index_song
        Button[] buttons = new Button[6];
        for(int i=0; i<6; i++) {
            int resID = getResources().getIdentifier("button_bg" + String.valueOf(i+1), "id", getPackageName());
            buttons[i] = (Button) findViewById(resID);
        }

        setup();

        final TextView text = findViewById(R.id.name_music);
        final String[] nameList = {"Alone", "Baby Shark", "Faded", "GANGNAM STYLE",
                "LOVE SCENARIO", "PPAP", "Rooftop", "Sunflower"};
        text.setText(String.valueOf(index_song+1) + ". " + nameList[index_song]);

        buttons[bg-1].setText("Selected");

        buttons[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bg = 1;
                setBG(bg);
                buttons[0].setText("Selected");
                buttons[1].setText("");
                buttons[2].setText("");
                buttons[3].setText("");
                buttons[4].setText("");
                buttons[5].setText("");
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bg = 2;
                setBG(bg);
                buttons[0].setText("");
                buttons[1].setText("Selected");
                buttons[2].setText("");
                buttons[3].setText("");
                buttons[4].setText("");
                buttons[5].setText("");
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bg = 3;
                setBG(bg);
                buttons[0].setText("");
                buttons[1].setText("");
                buttons[2].setText("Selected");
                buttons[3].setText("");
                buttons[4].setText("");
                buttons[5].setText("");
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bg = 4;
                setBG(bg);
                buttons[0].setText("");
                buttons[1].setText("");
                buttons[2].setText("");
                buttons[3].setText("Selected");
                buttons[4].setText("");
                buttons[5].setText("");
            }
        });

        buttons[4].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bg = 5;
                setBG(bg);
                buttons[0].setText("");
                buttons[1].setText("");
                buttons[2].setText("");
                buttons[3].setText("");
                buttons[4].setText("Selected");
                buttons[5].setText("");
            }
        });

        buttons[5].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bg = 6;
                setBG(bg);
                buttons[0].setText("");
                buttons[1].setText("");
                buttons[2].setText("");
                buttons[3].setText("");
                buttons[4].setText("");
                buttons[5].setText("Selected");
            }
        });

        // change song
        Button btn_next = (Button) findViewById(R.id.next_music);
        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                index_song++;
                if (index_song > 7) {
                    index_song = 0;
                }
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(CustomizeActivity.this, songList[index_song]);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                text.setText(String.valueOf(index_song+1) + ". " + nameList[index_song]);
            }
        });

        Button btn_before = (Button) findViewById(R.id.before_music);
        btn_before.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                index_song--;
                if (index_song < 0) {
                    index_song = 7;
                }
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(CustomizeActivity.this, songList[index_song]);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                text.setText(String.valueOf(index_song+1) + ". " + nameList[index_song]);
            }
        });

        ImageButton back_btn = (ImageButton) findViewById(R.id.back_button);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(CustomizeActivity.this, HomeActivity.class);
                backIntent.putExtra(Values.bg, bg);
                backIntent.putExtra(Values.index_song, index_song);
                startActivity(backIntent);
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
}