package com.example.carehomemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.manager.MHomeActivity;
import com.example.carehomemanagement.staff.SHomeActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sessionManager.isLoggedin()) {
                    Intent i;
                    if (sessionManager.getUserName().equals("manager")) {
                        i = new Intent(SplashActivity.this,
                                SHomeActivity.class);
                    } else {
                        i = new Intent(SplashActivity.this,
                                MHomeActivity.class);
                    }
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashActivity.this,
                            LoginActivity.class);

                    startActivity(i);
                }

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}