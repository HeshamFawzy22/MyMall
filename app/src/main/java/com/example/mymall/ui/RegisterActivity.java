package com.example.mymall.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.example.mymall.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class RegisterActivity extends AppCompatActivity {

    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUpFragment = false;

    protected FrameLayout registerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        if (setSignUpFragment) {
            setSignUpFragment = false;
            setDefaultFragment(new SignUpFragment());
        } else {
            setDefaultFragment(new SignInFragment());
        }
    }

    ///// Mobile back btn
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            SignUpFragment.disableCloseBtn = false;
            SignInFragment.disableCloseBtn = false;

            if (onResetPasswordFragment) {
                onResetPasswordFragment = false;
                setFragment(new SignInFragment());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(registerFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_out_from_right);
        fragmentTransaction.replace(R.id.register_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void initView() {
        registerFrameLayout = findViewById(R.id.register_frame_layout);
    }
}