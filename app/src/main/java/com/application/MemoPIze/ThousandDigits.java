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

public class ThousandDigits extends AppCompatActivity {

    private int count = 0;
    private boolean flag = false;
    private boolean flagSound = false;

    final private Handler handler = new Handler(Looper.getMainLooper());
    final private Handler speechHandler = new Handler(Looper.getMainLooper());

    final private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count < 1001) {
                count++;
                if (count == 1)
                    placeValueText.setText("Decimal");
                else
                    placeValueText.setText("Digit Number: " + count);

                thousandDigit.setText(String.valueOf(thousandDigitsOfPi[count]));
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
            if (count < 1001)
            {
                textToSpeech.speak(String.valueOf(thousandDigitsOfPi[count]), TextToSpeech.QUEUE_FLUSH, null);
                speechHandler.postDelayed(this, 1000);
            }
            else if (count == 1001)
            {
                textToSpeech.speak(String.valueOf(thousandDigitsOfPi[count]), TextToSpeech.QUEUE_FLUSH, null);
                speechHandler.removeCallbacks(this);
            }
        }
    };

    TextView placeValueText;
    TextView thousandDigit;
    Button next;
    Button reset;
    Button prev;
    Button moveTenDigitsUp;
    Button moveTenDigitsDown;
    ImageButton playAndPause;
    ImageButton playSound;
    AudioManager audioManager;
    TextToSpeech textToSpeech;

    private final String thousandDigitsOfPiString = "3." +
            "14159265358979323846264338327950288419716939937510" +
            "58209749445923078164062862089986280348253421170679" +
            "82148086513282306647093844609550582231725359408128" +
            "48111745028410270193852110555964462294895493038196" +
            "44288109756659334461284756482337867831652712019091" +
            "45648566923460348610454326648213393607260249141273" +
            "72458700660631558817488152092096282925409171536436" +
            "78925903600113305305488204665213841469519415116094" +
            "33057270365759591953092186117381932611793105118548" +
            "07446237996274956735188575272489122793818301194912" +
            "98336733624406566430860213949463952247371907021798" +
            "60943702770539217176293176752384674818467669405132" +
            "00056812714526356082778577134275778960917363717872" +
            "14684409012249534301465495853710507922796892589235" +
            "42019956112129021960864034418159813629774771309960" +
            "51870721134999999837297804995105973173281609631859" +
            "50244594553469083026425223082533446850352619311881" +
            "71010003137838752886587533208381420617177669147303" +
            "59825349042875546873115956286388235378759375195778" +
            "18577805321712268066130019278766111959092164201989";

    private final char[] thousandDigitsOfPi = thousandDigitsOfPiString.toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thousand_digits);

        placeValueText = (TextView)findViewById(R.id.placeValue);
        thousandDigit = (TextView)findViewById(R.id.thousandDigitValue);
        next = (Button)findViewById(R.id.nextButton);
        reset = (Button)findViewById(R.id.resetButton);
        prev = (Button)findViewById(R.id.previousButton);
        moveTenDigitsUp = (Button)findViewById(R.id.tenDigitsUp);
        moveTenDigitsDown = (Button)findViewById(R.id.tenDigitsDown);
        playAndPause = (ImageButton)findViewById(R.id.playAndPauseButton);
        playSound = (ImageButton)findViewById(R.id.sound);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                }
                else if (status == TextToSpeech.ERROR)
                    System.out.println("Error");
            }
        });
    }

    protected void onResume()
    {
        super.onResume();

        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < 1001)
                {
                    flag = !flag;
                    if (flag)
                    {
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
                if(count < 1001)
                {
                    speechHandler.removeCallbacks(speechRunnable);
                    handler.removeCallbacks(runnable);

                    count++;
                    if(count == 1)
                        placeValueText.setText("Decimal");
                    else
                        placeValueText.setText("Digit Number: " + count);

                    thousandDigit.setText(String.valueOf(thousandDigitsOfPi[count]));
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
                thousandDigit.setText("3");
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

                    thousandDigit.setText(String.valueOf(thousandDigitsOfPi[count]));
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
                if (count <= 991)
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

                    thousandDigit.setText(String.valueOf(thousandDigitsOfPi[count]));
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

                    thousandDigit.setText(String.valueOf(thousandDigitsOfPi[count]));
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
