package com.example.mymall;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mymall.ui.MainActivity;
import com.example.mymall.ui.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.mymall.ui.MainActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, 3000);
    }

    private void checkUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startMainActivity();
        } else {
            startRegisterActivity();
        }
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void startRegisterActivity() {
        startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
        finish();
    }
}