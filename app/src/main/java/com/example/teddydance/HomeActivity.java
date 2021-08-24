package com.example.teddydance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.setup();

        Button play_button = (Button) findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlayActivity.class);
                intent.putExtra(Values.index_song, index_song);
                intent.putExtra(Values.bg, bg);
                startActivity(intent);
            }
        });

        Button customize_button = (Button) findViewById(R.id.customize_button);
        customize_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CustomizeActivity.class);
                intent.putExtra(Values.index_song, index_song);
                intent.putExtra(Values.bg, bg);
                startActivity(intent);
            }
        });

        Button high_score_btn = (Button) findViewById(R.id.high_score_btn);
        high_score_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HighScoreActivity.class);
                intent.putExtra(Values.index_song, index_song);
                intent.putExtra(Values.bg, bg);
                startActivity(intent);
            }
        });

        Button exit_button = (Button) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageBox(0);
            }
        });

        final TextView creditText = (TextView) findViewById(R.id.creditText);
        creditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageBox(1);
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

    public void messageBox(int number) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(number == 0) {
            builder.setTitle("Do you want to leave the game ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finishAndRemoveTask();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle(getString(R.string.credit));
            builder.setMessage(
                    "\nBackground Images\n" +
                    "- noirlac\n" +
                    "- @catarina_lalanda\n" +
                    "- r/PixelArt\n" +
                    "- @16pxl\n" +
                        "\nSongs\n" +
                            "- Marshmello - Alone\n" +
                            "- Pinkfong - Baby Shark\n" +
                            "- Alan Walker - Faded\n" +
                            "- PSY - GANGNAM STYLE\n" +
                            "- iKON - LOVE SCENARIO\n" +
                            "- PIKOTARO - PPAP\n" +
                            "- N.Flying - Rooftop\n" +
                            "- Post Malone, Swae Lee - Sunflower\n" +
                            "\nTeddy and Heart made from Paint3D"
            );
            builder.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}