package com.androharshit.conference;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText emailBox, passwordBox , namebox,mobileNumber,confirmPasswd;
    Button loginBtn, signupBtn;

    FirebaseFirestore database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        namebox=findViewById(R.id.signup_userName);
        emailBox = findViewById(R.id.signup_email_edt);
        passwordBox = findViewById(R.id.signup_password_edt);
        mobileNumber=findViewById(R.id.signup_mobileno);
        loginBtn = findViewById(R.id.signup_back);
        signupBtn = findViewById(R.id.goto_signup_page_btn);
        confirmPasswd=findViewById(R.id.signup_confirm_password_edt);
        signupBtn.setOnClickListener(v -> {
            registerUser();
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUser()
    {
        emailBox.setError(null);
        passwordBox.setError(null);
        mobileNumber.setError(null);
        namebox.setError(null);
//        signupEtRollNo.setError(null);

//        String userCheck=namebox.getText().toString().trim();
        String email=emailBox.getText().toString();
        String passwd=passwordBox.getText().toString();
        String userName=namebox.getText().toString();
        String confirmPass=confirmPasswd.getText().toString();
//        String roll=mobileNumber.getText().toString();
        String mobile=mobileNumber.getText().toString();
        boolean cancel=false;
        View focusView=null;

//        if (!userCheck.matches("^(?=.{7,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")) {
//            namebox.setError(getString(R.string.invalid_user_name_length));
//            focusView=namebox;
//            cancel=true;
//        }
        if (userName.length()<6) {
            namebox.setError(getString(R.string.invalid_user_name_length));
            focusView=namebox;
            cancel=true;
        }
//        String rollnoCheck=signupEtRollNo.getText().toString().trim();
//        if(!(rollnoCheck.length()==10 || rollnoCheck.length()==13) )
//        {
//            signupEtRollNo.setError(getString(R.string.invalid_rollno));
//            focusView=signupEtRollNo;
//            cancel=true;
//        }
//        if(!rollnoCheck.contains("841"))
//        {
//            signupEtRollNo.setError(getString(R.string.invalid_rollno));
//            focusView=signupEtRollNo;
//            cancel=true;
//        }

        if (TextUtils.isEmpty(passwd) && !checkPassword(passwd)) {
            passwordBox.setError(getString(R.string.invalid_password));
            focusView=passwordBox;
            cancel=true;
        }
        if (!(mobileNumber.getText().toString().trim().length()==10)) {
            mobileNumber.setError(getString(R.string.invalid_mobile));
            focusView=mobileNumber;
            cancel=true;
        }
        if (TextUtils.isEmpty(email) && !checkEmail(email)) {
            emailBox.setError(getString(R.string.invalid_Email));
            focusView=emailBox;
            cancel=true;
        }

        if(!passwd.equals(confirmPasswd.getText().toString()))
        {
            confirmPasswd.setError(getString(R.string.invalid_password));
            passwordBox.setError(getString(R.string.invalid_password));
            focusView=passwordBox;
            focusView=confirmPasswd;
            cancel=true;
        }
        if(cancel)
        {
            focusView.requestFocus();
        }
        else {
            createUser();

        }
    }

    private void createUser() {
        progressDialog.show();
        String email,pass,name,mobile;
        email=emailBox.getText().toString();
        pass=passwordBox.getText().toString();
        name=namebox.getText().toString();
        mobile=mobileNumber.getText().toString();
        User user = new User();
        user.setEmail(email);
        user.setPass(pass);
        user.setName(name);
        user.setMobile(mobile);

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    database.collection("Users")
                            .document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    Toast.makeText(SignupActivity.this, "Account is Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Validation for existing Email and password length and
     * another Validation methods which is really very useful for proper registration.
     */
    private boolean checkEmail(String email)
    {
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }
    private boolean checkPassword(String passwd)
    {
        String confirmedPassword=confirmPasswd.getText().toString();
        return passwd.equals(confirmedPassword) && passwd.length()>6;
    }

    public void showErrorBox(String message)
    {
//*Create a ErrorBox for error
        new AlertDialog.Builder(this)
                .setTitle("Heyyy")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}