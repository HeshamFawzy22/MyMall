package com.example.mymall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymall.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.example.mymall.ui.RegisterActivity.onResetPasswordFragment;


public class SignInFragment extends Fragment implements View.OnClickListener {

    //ui
    protected Button btnSignIn;
    protected TextInputLayout etPassword;
    protected TextInputLayout etEmail;
    protected TextView tvForgetPassword;
    protected TextView tvDontHaveAnAccount;
    protected ProgressBar progressBar;
    protected ImageView closeBtn;

    //declare
    public static boolean disableCloseBtn = false;

    //Declare
    private FirebaseAuth auth;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initView(view);

        if (disableCloseBtn){
            closeBtn.setVisibility(View.GONE);
        }else {
            closeBtn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_btn) {
            if (validatedForm()) {
                btnSignIn.setEnabled(false);
                logIn();
            }
        } else if (view.getId() == R.id.sign_in_tv_forget_password) {
            onResetPasswordFragment = true;
            setFragment(new ResetPasswordFragment());
        } else if (view.getId() == R.id.sign_in_tv_dont_have_an_account) {
            setFragment(new SignUpFragment());
        } else if (view.getId() == R.id.close_btn) {
            startMainActivity();
        }
    }


    private void logIn() {
        auth = FirebaseAuth.getInstance();
        String emailText = etEmail.getEditText().getText().toString().trim();
        String passwordText = etPassword.getEditText().getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            btnSignIn.setEnabled(false);
                            btnSignIn.setTextColor(Color.argb(50,255,255,255));
                            progressBar.setVisibility(View.INVISIBLE);
                            startMainActivity();
                        } else {
                            btnSignIn.setEnabled(true);
                            btnSignIn.setTextColor(Color.rgb(255,255,255));
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void startMainActivity() {
        if (disableCloseBtn){
            disableCloseBtn = false;
        }else {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        getActivity().finish();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_out_from_left);
        fragmentTransaction.replace(R.id.register_frame_layout, fragment);
        fragmentTransaction.commit();
    }


    private boolean validEmail() {
        String emailText = etEmail.getEditText().getText().toString().trim();
        return emailText.contains("@") && emailText.contains(".");
    }

    private boolean validPassword() {
        String passwordText = etPassword.getEditText().getText().toString().trim();
        return passwordText.length() >= 6;
    }

    private boolean validatedForm() {
        String emailText = etEmail.getEditText().getText().toString().trim();
        String passwordText = etPassword.getEditText().getText().toString().trim();

        if (emailText.isEmpty()) {
            etEmail.setError(getString(R.string.required));
            return false;
        } else if (!validEmail()) {
            etEmail.setError("Please Enter Valid Email");
            return false;
        } else {
            etEmail.setError(null);
        }

        if (passwordText.isEmpty()) {
            etPassword.setError(getString(R.string.required));
            return false;
        } else if (!validPassword()) {
            etPassword.setError("Please Enter more than 6 Characters");
            return false;
        } else {
            etPassword.setError(null);
        }
        return true;
    }

    private void initView(View rootView) {
        btnSignIn = rootView.findViewById(R.id.sign_in_btn);
        btnSignIn.setOnClickListener(SignInFragment.this);
        etPassword = rootView.findViewById(R.id.sign_in_et_password);
        etEmail = rootView.findViewById(R.id.sign_in_et_email);
        tvForgetPassword = rootView.findViewById(R.id.sign_in_tv_forget_password);
        progressBar = rootView.findViewById(R.id.progressBar2);
        tvForgetPassword.setOnClickListener(SignInFragment.this);
        tvDontHaveAnAccount = rootView.findViewById(R.id.sign_in_tv_dont_have_an_account);
        tvDontHaveAnAccount.setOnClickListener(SignInFragment.this);
        closeBtn = rootView.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(SignInFragment.this);
    }
}