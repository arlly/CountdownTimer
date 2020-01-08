package com.example.countdowntimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static long START_TIME_IN_MILLIS = 30000;
    private TextView mTextViewCountDown;  //アクセス修飾子
    private Button mButtonStartPause;
    private Button mButtonReset;
    private final static String TAG_START = "0";
    private final static String TAG_PAUSE = "1";
    private final static String TAG_RESET = "2";

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private long mTimerLeftInMills = START_TIME_IN_MILLIS;

    /**
     * Sound
     **/
    private final static String TAG_SOUND1 = "decision12";
    private final static String TAG_SOUND2 = "decision16";
    private final static String TAG_SOUND3 = "decision21";
    private final static String TAG_SOUND4 = "decision24";

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int soundId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.buttonreset);

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                    playSound(TAG_SOUND2);
                } else {
                    playSound(TAG_SOUND1);
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                playSound(TAG_SOUND3);
            }
        });

        updateCountDownText();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimerLeftInMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerLeftInMills = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("スタート");
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }.start();

        mTimerRunning = true;
        mButtonStartPause.setText("一時停止");
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("スタート");
        mButtonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimerLeftInMills = START_TIME_IN_MILLIS;
        updateCountDownText();
        mButtonStartPause.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimerLeftInMills / 1000) / 60;
        int seconds = (int) (mTimerLeftInMills / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);

        if (seconds == 0) {
            playSound(TAG_SOUND4);
            resetTimer();
        }
    }

    private void playSound(String tag) {
        try {
            if (tag.equals(TAG_SOUND1)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.cat1a);
            } else if (tag.equals(TAG_SOUND2)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.cat1b);
            } else if (tag.equals(TAG_SOUND3)) {
                mediaPlayer = MediaPlayer.create(this, R.raw.decision21);
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.purring);
            }

            mediaPlayer.setLooping(false);
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } catch (Exception e) {
        }
    }
}
