package com.example.marwatalaat.smartparking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                finish();
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);


            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
