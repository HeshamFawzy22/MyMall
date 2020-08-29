package com.example.mymall.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ResetPasswordFragment extends Fragment implements View.OnClickListener {

    //ui
    protected EditText etEmail;
    protected Button btnResetPassword;
    protected TextView tvBack;
    protected ViewGroup emailIconContainer;
    protected ImageView icEmail;
    protected TextView icEmailTxt;
    protected ProgressBar progressBar;

    //Declare
    FirebaseAuth auth;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkChangedText();

    }

    private void checkChangedText() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(etEmail.getText())) {
            btnResetPassword.setEnabled(false);
            btnResetPassword.setTextColor(Color.argb(50, 255, 255, 255));
        } else {
            btnResetPassword.setEnabled(true);
            btnResetPassword.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reset_password_btn_reset) {
            resetPassword();
        } else if (view.getId() == R.id.reset_password_tv_back) {
            setFragment(new SignInFragment());
        }
    }

    private void resetPassword() {

        TransitionManager.beginDelayedTransition(emailIconContainer);
        icEmail.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        btnResetPassword.setEnabled(false);
        btnResetPassword.setTextColor(Color.argb(50, 255, 255, 255));

        auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(etEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            successResetPassword();
                        } else {
                            failedResetPassword(task);
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private void failedResetPassword(Task<Void> task) {
        String errorMsg = task.getException().getMessage();
        progressBar.setVisibility(View.GONE);

        btnResetPassword.setEnabled(true);
        btnResetPassword.setTextColor(Color.rgb(255, 255, 255));

        icEmailTxt.setText(errorMsg);
        icEmailTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

        TransitionManager.beginDelayedTransition(emailIconContainer);
        icEmailTxt.setVisibility(View.VISIBLE);
    }

    private void successResetPassword() {

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0, icEmail.getWidth() / 2, icEmail.getHeight() / 2);
        scaleAnimation.setDuration(100);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(1);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                icEmailTxt.setText("Recovery email sent successfully ! check your inbox");
                icEmailTxt.setTextColor(getResources().getColor(R.color.successGreen));

                TransitionManager.beginDelayedTransition(emailIconContainer);
                icEmailTxt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                icEmail.setImageResource(R.drawable.ic_mail_green);

            }

        });

        icEmail.startAnimation(scaleAnimation);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_out_from_right);
        fragmentTransaction.replace(R.id.register_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void initView(View rootView) {
        etEmail = rootView.findViewById(R.id.reset_password_et_email);
        btnResetPassword = rootView.findViewById(R.id.reset_password_btn_reset);
        btnResetPassword.setOnClickListener(ResetPasswordFragment.this);
        tvBack = rootView.findViewById(R.id.reset_password_tv_back);
        tvBack.setOnClickListener(ResetPasswordFragment.this);
        icEmail = rootView.findViewById(R.id.reset_password_ic_email);
        icEmailTxt = rootView.findViewById(R.id.reset_password_ic_email_txt);
        progressBar = rootView.findViewById(R.id.reset_password_progress_bar);
        emailIconContainer = rootView.findViewById(R.id.reset_password_ic_email_container);
    }
}