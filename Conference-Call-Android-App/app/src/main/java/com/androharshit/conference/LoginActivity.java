package com.androharshit.conference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailBox, passwordBox;
    Button loginBtn, signupBtn;

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        auth =FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Just a moment...");

        emailBox = findViewById(R.id.signup_email_edt);
        passwordBox = findViewById(R.id.signup_password_edt);

        loginBtn = findViewById(R.id.signup_back);
        signupBtn = findViewById(R.id.goto_signup_page_btn);

        loginBtn.setOnClickListener(v -> {
            loginUser();

        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void loginUser()
    {
        emailBox.setError(null);
        passwordBox.setError(null);
        String email=emailBox.getText().toString();
        String passwd=passwordBox.getText().toString();
        boolean cancel=false;
        View focusView=null;
        if (TextUtils.isEmpty(passwd) && !checkPassword(passwd)) {
            passwordBox.setError(getString(R.string.invalid_pass));
            focusView=passwordBox;
            cancel=true;
        }
        if (TextUtils.isEmpty(email) && !checkEmail(email)) {
            emailBox.setError(getString(R.string.invalid_Email));
            focusView=emailBox;
            cancel=true;
        }
        if(cancel)
        {
            focusView.requestFocus();
        }
        else {
            signinUser();
        }
    }

    private void signinUser() {
        progressDialog.show();

        String email,password;
        email=emailBox.getText().toString();
        password=passwordBox.getText().toString();

        auth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean checkEmail(String email)
    {
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }
    private boolean checkPassword(String passwd)
    {
        return passwd.length()>6;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

        }
    }
}