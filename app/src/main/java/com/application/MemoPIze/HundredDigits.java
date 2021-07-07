package com.application.MemoPIze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class HundredDigits extends AppCompatActivity {

    private int count = 0;
    private boolean flag = false;
    private boolean flagSound = false;

    final private Handler handler = new Handler(Looper.getMainLooper());
    final private Handler speechHandler = new Handler(Looper.getMainLooper());

    final private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count < 101) {
                count++;
                if (count == 1)
                    placeValueText.setText("Decimal");
                else
                    placeValueText.setText("Digit Number: " + count);

                hundredDigit.setText(String.valueOf(hundredDigitsOfPi[count]));
                handler.postDelayed(this, 1000);
            }
            else
            {
                flag = !flag;
                playAndPause.setImageResource(android.R.drawable.ic_media_play);
                handler.removeCallbacks(this);
            }
        }
    };
    final private Runnable speechRunnable = new Runnable() {
        @Override
        public void run() {
            if (count < 101)
            {
                textToSpeech.speak(String.valueOf(hundredDigitsOfPi[count]), TextToSpeech.QUEUE_FLUSH, null);
                speechHandler.postDelayed(this, 1000);
            }
            else if (count == 101)
            {
                textToSpeech.speak(String.valueOf(hundredDigitsOfPi[count]), TextToSpeech.QUEUE_FLUSH, null);
                speechHandler.removeCallbacks(this);
            }
        }
    };

    TextView placeValueText;
    TextView hundredDigit;
    Button next;
    Button reset;
    Button prev;
    Button moveTenDigitsUp;
    Button moveTenDigitsDown;
    ImageButton playAndPause;
    ImageButton playSound;
    AudioManager audioManager;
    TextToSpeech textToSpeech;

    private final String hundredDigitsOfPiString = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679";
    private final char[] hundredDigitsOfPi = hundredDigitsOfPiString.toCharArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hundred_digits);

        placeValueText = (TextView) findViewById(R.id.placeValue);
        hundredDigit = (TextView) findViewById(R.id.hundredDigitValue);
        next = (Button) findViewById(R.id.nextButton);
        reset = (Button) findViewById(R.id.resetButton);
        prev = (Button) findViewById(R.id.previousButton);
        moveTenDigitsUp = (Button) findViewById(R.id.tenDigitsUp);
        moveTenDigitsDown = (Button) findViewById(R.id.tenDigitsDown);
        playAndPause = (ImageButton) findViewById(R.id.playAndPauseButton);
        playSound = (ImageButton) findViewById(R.id.sound);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                } else if (status == TextToSpeech.ERROR)
                    System.out.println("Error");
            }
        });

    }

    protected void onResume() {
        super.onResume();

        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 101)
                {
                    flag = !flag;
                    if (flag) {
                        playAndPause.setImageResource(android.R.drawable.ic_media_pause);
                        speechHandler.post(speechRunnable);
                        handler.postDelayed(runnable, 1000);
                    }

                    else
                    {
                        playAndPause.setImageResource(android.R.drawable.ic_media_play);
                        speechHandler.removeCallbacks(speechRunnable);
                        handler.removeCallbacks(runnable);
                    }
                }
            }

        });

        playSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagSound = !flagSound;
                if (flagSound) {
                    playSound.setImageResource(android.R.drawable.ic_lock_silent_mode);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    else
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                }

                else
                {
                    playSound.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                    else
                        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 101)
                {
                    speechHandler.removeCallbacks(speechRunnable);
                    handler.removeCallbacks(runnable);

                    count++;
                    if(count == 1)
                        placeValueText.setText("Decimal");
                    else
                        placeValueText.setText("Digit Number: " + count);

                    hundredDigit.setText(String.valueOf(hundredDigitsOfPi[count]));
                    if(flag)
                    {
                        speechHandler.post(speechRunnable);
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechHandler.removeCallbacks(speechRunnable);
                handler.removeCallbacks(runnable);

                placeValueText.setText("Digit Number: 1");
                hundredDigit.setText("3");
                count = 0;

                if(flag)
                {
                    speechHandler.post(speechRunnable);
                    handler.postDelayed(runnable, 1000);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    speechHandler.removeCallbacks(speechRunnable);
                    handler.removeCallbacks(runnable);

                    count--;
                    if (count == 0)
                        placeValueText.setText("Digit Number: 1");
                    else if (count == 1)
                        placeValueText.setText("Decimal");
                    else
                        placeValueText.setText("Digit Number: " + count);

                    hundredDigit.setText(String.valueOf(hundredDigitsOfPi[count]));
                    if(flag)
                    {
                        speechHandler.post(speechRunnable);
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        });

        moveTenDigitsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= 91)
                {
                    speechHandler.removeCallbacks(speechRunnable);
                    handler.removeCallbacks(runnable);

                    if (count == 0)
                        count += 11;
                    else
                        count += 10;

                    if (count == 0)
                        placeValueText.setText("Digit Number: 1");
                    else if (count == 1)
                        placeValueText.setText("Decimal");
                    else
                        placeValueText.setText("Digit Number: " + count);

                    hundredDigit.setText(String.valueOf(hundredDigitsOfPi[count]));
                    if(flag)
                    {
                        speechHandler.post(speechRunnable);
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        });

        moveTenDigitsDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >= 11)
                {
                    speechHandler.removeCallbacks(speechRunnable);
                    handler.removeCallbacks(runnable);

                    if (count == 11)
                        count -= 11;
                    else
                        count -= 10;

                    if (count == 0)
                        placeValueText.setText("Digit Number: 1");
                    else if (count == 1)
                        placeValueText.setText("Decimal");
                    else
                        placeValueText.setText("Digit Number: " + count);

                    hundredDigit.setText(String.valueOf(hundredDigitsOfPi[count]));
                    if(flag)
                    {
                        speechHandler.post(speechRunnable);
                        handler.postDelayed(runnable, 1000);
                    }
                }
            }
        });
    }

    protected void onPause()
    {
        super.onPause();
        if(textToSpeech != null)
        {
            playAndPause.setImageResource(android.R.drawable.ic_media_play);
            flag = false;
            speechHandler.removeCallbacks(speechRunnable);
            handler.removeCallbacks(runnable);

            int musicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (musicVolume == 0)
            {
                playSound.setImageResource(android.R.drawable.ic_lock_silent_mode);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                else
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
            else
            {
                playSound.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
                else
                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);

            }
        }
    }

    protected void onDestroy()
    {
        playSound.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
        else
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);

        super.onDestroy();
    }
}