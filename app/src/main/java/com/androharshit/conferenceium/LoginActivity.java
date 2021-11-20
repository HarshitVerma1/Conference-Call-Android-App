package com.androharshit.conferenceium;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide(); //hide the toolBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // hide statusBar
        init();
        actions();
    }
    private void init(){
        signup=findViewById(R.id.signup_btn);
    }
    private void actions(){
        signup.setOnClickListener(v->{
            Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
            startActivity(intent);
        });
    }
}