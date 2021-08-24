package com.example.teddydance;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HighScoreActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.setup();

        // Get screen height --------------------------------------------------------------------------------------
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        // End Get screen height ----------------------------------------------------------------------------------

        ImageView homeButton = (ImageView) findViewById(R.id.home_button);
        homeButton.getLayoutParams().height = (int) (screenHeight*0.11);
        homeButton.setImageDrawable(getDrawable(R.drawable.custom_button_home));
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HighScoreActivity.this, HomeActivity.class);
                intent.putExtra(Values.bg, bg);
                intent.putExtra(Values.index_song, index_song);
                startActivity(intent);
            }
        });

        HighScore highScore = new HighScore();
        ArrayList<HashMap<String, String>> arrayList;
        arrayList = new ArrayList<>();
        arrayList = highScore.read_sortedScore(getApplicationContext(), arrayList);

        final ListView listView = (ListView) findViewById(R.id.listView);
        SimpleAdapter simpleAdapter;
        simpleAdapter = new SimpleAdapter(HighScoreActivity.this, arrayList, R.layout.column,
                new String[] {"player", "score"},
                new int[] {R.id.playerView, R.id.scoreView});
        listView.setAdapter(simpleAdapter);
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
}