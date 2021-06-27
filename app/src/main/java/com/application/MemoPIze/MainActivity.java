package com.application.MemoPIze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.MemoPIze.R;

public class MainActivity extends AppCompatActivity {

    Button show100DigitsButton;
    Button show1000DigitsButton;
    Button show10000DigitsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show100DigitsButton = (Button)findViewById(R.id.show100Digits);
        show1000DigitsButton = (Button)findViewById(R.id.show1000Digits);
        show10000DigitsButton = (Button)findViewById(R.id.show10000Digits);

        show100DigitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hundredDigitsActivity = new Intent(MainActivity.this, HundredDigits.class);
                startActivity(hundredDigitsActivity);
            }
        });

        show1000DigitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thousandDigitsActivity = new Intent(MainActivity.this, ThousandDigits.class);
                startActivity(thousandDigitsActivity);
            }
        });

        show10000DigitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tenThousandDigitsActivity = new Intent(MainActivity.this, TenThousandDigits.class);
                startActivity(tenThousandDigitsActivity);
            }
        });

    }
}