package com.example.teddydance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    View backgroundView;
    MediaPlayer mediaPlayer;
    int[] songList;
    int index_song, bg;

    public static final int EASY = 4;
    public static final int MEDIUM = 5;
    public static final int HARD = 6;
    private int numberOfLife = 3;

    private boolean isDancing = false, isPlayable = false, isFinishRound = false;
    private int currentDifficulty, round, score;
    private int countDown, delay;
    private int screenHeight;

    private Dancer dancer;
    private TextView topText, scoreText;
    private ImageView[] life;
    private ImageView arrow_left, arrow_right, arrow_up, arrow_down;
    private Vibrator v;
    private Runnable interval;
    private Handler handler;

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
        setContentView(R.layout.activity_play);
        this.setTitle("Play");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set background and songs
        this.setup();

        // -------------------------------------------Get screen height-------------------------------------------
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        // -------------------------------------------Initialize--------------------------------------------------
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        scoreText = (TextView) findViewById(R.id.scoreText);
        topText = (TextView) findViewById(R.id.topText);
        dancer = new Dancer(PlayActivity.this, this);
        currentDifficulty = PlayActivity.EASY;
        round = 1;
        score = 0;
        delay = 600;
        String scoreStr = "Score : " + score;
        scoreText.setText(scoreStr);
        // -------------------------------------------Interval Time to Play Runnable--------------------------------------------------
        handler = new Handler();
        interval = new Runnable() {
            @Override
            public void run() {
                if (!isFinishRound)
                    setPlayable(true);
            }
        };
        // -------------------------------------------Set Life Image--------------------------------------------------
        life = new ImageView[numberOfLife];
        for (int i = 0; i < numberOfLife; i++) {
            life[i] = (ImageView) findViewById(getResources().getIdentifier("life"+i, "id", this.getPackageName()));
            life[i].getLayoutParams().height = (int)(screenHeight*0.05);
            life[i].getLayoutParams().width = (int)(screenHeight*0.05);
            life[i].setImageDrawable(getDrawable(R.drawable.heart));
        }
        // -------------------------------------------Set Arrow Key--------------------------------------------------
        int arrow_height = (int) (screenHeight*0.1);
        arrow_up = (ImageView) findViewById(R.id.arrow_up);
        arrow_up.getLayoutParams().height = arrow_height;
        arrow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayable) {
                    checkMove(Dancer.UP);
                    setPlayable(false);
                    handler.postDelayed(interval, delay);
                }
            }
        });

        arrow_down = (ImageView) findViewById(R.id.arrow_down);
        arrow_down.getLayoutParams().height = arrow_height;
        arrow_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayable) {
                    checkMove(Dancer.DOWN);
                    setPlayable(false);
                    handler.postDelayed(interval, delay);
                }
            }
        });

        arrow_left = (ImageButton) findViewById(R.id.arrow_left);
        arrow_left.getLayoutParams().width = arrow_height;
        arrow_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayable) {
                    checkMove(Dancer.LEFT);
                    setPlayable(false);
                    handler.postDelayed(interval, delay);
                }
            }
        });

        arrow_right = (ImageButton) findViewById(R.id.arrow_right);
        arrow_right.getLayoutParams().width = arrow_height;
        arrow_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlayable) {
                    checkMove(Dancer.RIGHT);
                    setPlayable(false);
                    handler.postDelayed(interval, delay);
                }
            }
        });

        setPlayable(false);
    } // ----- end onCreate()

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


    //----------------Touch to start a round
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if( !isDancing && action == MotionEvent.ACTION_DOWN ) {
            isDancing = true;
            startRound();
            return true;
        }
        return super.onTouchEvent(event);
    }
    //----------------Touch to start a round

    //----------------Game Logics
    public void startRound() {
        countDown = 3;
        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                topText.setText("" + countDown);
                countDown--;
            }
            public void onFinish() {
                topText.setText("Round " + round);
                dancer.randomDance(currentDifficulty);
                // Next round
                round = round+1;
            }
        }.start();
    }

    public void checkMove(int direction) {
        // Right move
        if(dancer.getFirstMoveInList() == direction) {
            dancer.removeFirstMove();
            dancer.dance(direction);
            if(dancer.isEmptyMove()) {
                score = score + 100;
                resetRound();
            }
        }
        // Wrong move
        else {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
            life[numberOfLife-1].setImageDrawable(getDrawable(R.drawable.black_heart));
            numberOfLife--;
            if(numberOfLife == 0) {
                //Start GameOverActivity
                Intent intent = new Intent(PlayActivity.this, GameOverActivity.class);
                intent.putExtra(Values.score, score);
                intent.putExtra(Values.screenHeight, screenHeight);
                intent.putExtra(Values.index_song, index_song);
                intent.putExtra(Values.bg, bg);
                startActivity(intent);
            }
        }
    }

    public void setPlayable(boolean playable) {
        this.isPlayable = playable;

        arrow_left.setEnabled(playable);
        arrow_right.setEnabled(playable);
        arrow_up.setEnabled(playable);
        arrow_down.setEnabled(playable);
    }

    public void resetRound() {
        isFinishRound = true;
        setPlayable(false);
        dancer.resetMove();
        isDancing = false;
        String scoreStr = "Score : " + score;
        scoreText.setText(scoreStr);
        topText.setText(getString(R.string.tap_to_start));

        if(round == 3) {
            currentDifficulty = PlayActivity.MEDIUM;
            topText.setTextColor(getColor(R.color.orange));
            topText.setText(getString(R.string.harder));
        }
        if(round == 5) {
            currentDifficulty = PlayActivity.HARD;
            topText.setTextColor(getColor(R.color.red));
            topText.setText(getString(R.string.more_harder));
        }

    }

    class Dancer {
        Context context;
        Activity activity;
        ImageView img;
        Drawable dancerDrawable;
        AnimationDrawable ani;
        Random rand;
        final ArrayList<Integer> moveSet;
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int UP = 2;
        public static final int DOWN = 3;
        private static final int DIRECTIONS = 4;
        private int danceCount, direction;
        boolean isMove = false;

        public Dancer(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;

            dancerDrawable = activity.getDrawable(R.drawable.dancer);

            img = (ImageView) activity.findViewById(R.id.img);
            img.getLayoutParams().height = (int) (screenHeight*0.45);
            img.setBackgroundResource(R.drawable.dancer_still);
            ani = (AnimationDrawable) img.getBackground();

            moveSet = new ArrayList<Integer>();
            rand = new Random();
        }

        public void resetMove() {
            moveSet.clear();
        }

        private void randomDance(int difficulty) {
            danceCount = 0;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (!isMove) {
                        direction = rand.nextInt(DIRECTIONS);
                        dance(direction);
                        moveSet.add(direction);
                        danceCount++;
                        if (danceCount != difficulty) {
                            handler.post(this);
                        } else {
                            isFinishRound = false;
                            handler.postDelayed(interval, delay);
                        }
                    }
                    else {
                        handler.post(this);
                    }
                }
            };
            handler.post(r);
        }

        private void dance(int direction) {
            switch(direction) {
                case LEFT:
                    img.setBackgroundResource(R.drawable.animation_left);
                    break;
                case RIGHT:
                    img.setBackgroundResource(R.drawable.animation_right);
                    break;
                case UP:
                    img.setBackgroundResource(R.drawable.animation_up);
                    break;
                case DOWN:
                    img.setBackgroundResource(R.drawable.animation_down);
                    break;
            }
            img.invalidate();
            ani.stop();
            ani = (AnimationDrawable) img.getBackground();
            ani.start();
            isMove = true;
            img.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isMove = false;
                }
            }, 1200);
        }

        public int getFirstMoveInList() {
            if(moveSet.isEmpty()) {
                return -1;
            }
            return moveSet.get(0);
        }

        public boolean isEmptyMove() {
            return moveSet.isEmpty();
        }

        public void removeFirstMove() {
            moveSet.remove(0);
        }
    }
    //----------------Game Logics
}