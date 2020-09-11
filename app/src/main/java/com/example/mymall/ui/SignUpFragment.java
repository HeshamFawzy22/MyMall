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
import com.example.mymall.database.UserDao;
import com.example.mymall.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    //ui
    protected Button btnSignUp;
    protected TextInputLayout etEmail;
    protected TextInputLayout etFullName;
    protected TextInputLayout etPassword;
    protected TextInputLayout etConfirmPassword;
    protected TextView tvAlreadyHaveAnAccount;
    protected ProgressBar progressBar;
    protected ImageView closeBtn;
    FirebaseAuth auth;

    //Declare
    public static boolean disableCloseBtn = false;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
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

    private boolean validatedForm() {
        String userNameText = etFullName.getEditText().getText().toString().trim();
        String emailText = etEmail.getEditText().getText().toString().trim();
        String passwordText = etPassword.getEditText().getText().toString().trim();
        String confirmPasswordText = etConfirmPassword.getEditText().getText().toString().trim();

        if (emailText.isEmpty()) {
            etEmail.setError(getString(R.string.required));
            return false;
        } else if (!validEmail()) {
            etEmail.setError("Please Enter Valid Email");
            return false;
        } else {
            etEmail.setError(null);
        }
        if (userNameText.isEmpty()) {
            etFullName.setError(getString(R.string.required));
            return false;
        } else {
            etFullName.setError(null);
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
        if (confirmPasswordText.isEmpty()) {
            etConfirmPassword.setError(getString(R.string.required));
            return false;
        } else if (!confirmPasswordText.equals(passwordText)) {
            etPassword.setError(getString(R.string.notMatchingPasswords));
            etConfirmPassword.setError(getString(R.string.notMatchingPasswords));
            return false;
        } else {
            etPassword.setError(null);
            etConfirmPassword.setError(null);
        }
        return true;
    }

    private boolean validEmail() {
        String emailText = etEmail.getEditText().getText().toString().trim();
        return emailText.contains("@") && emailText.contains(".");
    }

    private boolean validPassword() {
        String passwordText = etPassword.getEditText().getText().toString().trim();
        return passwordText.length() >= 6;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_up_btn) {
            if (validatedForm()) {
                registerUser();
            }
        } else if (view.getId() == R.id.sign_up_tv_already_have_an_account) {
            setFragment(new SignInFragment());
        } else if (view.getId() == R.id.close_btn) {
            startMainActivity();
        }
    }

    private void registerUser() {
        auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setEnabled(false);
        btnSignUp.setTextColor(Color.argb(50,255,255,255));
        String emailText = etEmail.getEditText().getText().toString().trim();
        String passwordText = etPassword.getEditText().getText().toString().trim();
        auth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserToDatabase();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToDatabase() {
        final User user = new User();
        user.setName(etFullName.getEditText().getText().toString());
        user.setPassword(etPassword.getEditText().getText().toString());
        user.setEmail(etEmail.getEditText().getText().toString());
        user.setId(auth.getUid());

        UserDao.addUser(user, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Map<String,Object> listSize = new HashMap<>();
                listSize.put("list_size" , (long) 0);
                UserDao.addUserWishList(user.getId(), listSize, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            btnSignUp.setEnabled(false);
                            btnSignUp.setTextColor(Color.argb(50,255,255,255));
                            progressBar.setVisibility(View.INVISIBLE);
                            startMainActivity();
                        }else {
                            btnSignUp.setEnabled(true);
                            btnSignUp.setTextColor(Color.rgb(255,255,255));
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnSignUp.setEnabled(true);
                btnSignUp.setTextColor(Color.rgb(255,255,255));
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_out_from_right);
        fragmentTransaction.replace(R.id.register_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void initView(View rootView) {
        btnSignUp = rootView.findViewById(R.id.sign_up_btn);
        btnSignUp.setOnClickListener(SignUpFragment.this);
        etEmail = rootView.findViewById(R.id.sign_up_et_email);
        etFullName = rootView.findViewById(R.id.sign_up_et_full_name);
        etPassword = rootView.findViewById(R.id.sign_up_et_password);
        etConfirmPassword = rootView.findViewById(R.id.sign_up_et_confirm_password);
        progressBar = rootView.findViewById(R.id.progressBar);
        tvAlreadyHaveAnAccount = rootView.findViewById(R.id.sign_up_tv_already_have_an_account);
        tvAlreadyHaveAnAccount.setOnClickListener(SignUpFragment.this);
        closeBtn = rootView.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(SignUpFragment.this);
    }
}