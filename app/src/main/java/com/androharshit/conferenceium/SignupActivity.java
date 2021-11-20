package com.androharshit.conferenceium;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

public class SignupActivity extends AppCompatActivity {
    Button backBtn,submitBtn;
    EditText emailBox,userName,mobileNumber,passwordBox,
            confirmPasswordBox;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide(); //hide the toolBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // hide statusBar
        init();
        actions();

    }
    private void init()
    {
        backBtn=findViewById(R.id.signup_back);
        submitBtn=findViewById(R.id.signup_submit_btn);
        emailBox=findViewById(R.id.signup_email_edt);
        userName=findViewById(R.id.signup_userName);
        mobileNumber=findViewById(R.id.signup_mobileno);
        passwordBox=findViewById(R.id.signup_password_edt);
        confirmPasswordBox=findViewById(R.id.signup_confirm_password_edt);
        auth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating Account...");
        progressDialog.setMessage("We are Creating Your Account. ");
        database=FirebaseFirestore.getInstance();
    }
    private void actions()
    {
        backBtn.setOnClickListener(v -> {
            Intent intent =new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        });
    submitBtn.setOnClickListener(v -> {
        progressDialog.show();
        String email,pass,name,mobile;
        name=userName.getText().toString();
        pass=passwordBox.getText().toString();
        email=emailBox.getText().toString();
        mobile=mobileNumber.getText().toString();
        User user=new User();
        user.setName(name);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPass(pass);

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    database.collection("Users").document().set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // You can write below also
                            startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                        }
                    });
                    Toast.makeText(SignupActivity.this, "Congrates! Account is Created.", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(SignupActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    });

    }


}