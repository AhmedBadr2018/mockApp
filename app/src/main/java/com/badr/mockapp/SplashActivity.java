package com.badr.mockapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        final ImageView logoImage = (ImageView) findViewById(R.id.logoImage);

        assert logoImage != null;
        logoImage.animate().rotation(-360f).setDuration(1500);

        final Thread myThread = new Thread() {
            @Override
            public void run() {

                try {

                    sleep(2000);

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        myThread.start();
    }

}
