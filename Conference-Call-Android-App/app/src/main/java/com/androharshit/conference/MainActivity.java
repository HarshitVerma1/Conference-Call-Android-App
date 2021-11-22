package com.androharshit.conference;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button  demoBtn,shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        demoBtn= (Button) findViewById(R.id.demoBtn);
        demoBtn.setOnClickListener(v1 -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this , LoginActivity.class));
            finish();
        });
        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .setFeatureFlag("invite.enabled",false)
                    .setFeatureFlag("help.enabled",false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        shareBtn=findViewById(R.id.share);
        shareBtn.setOnClickListener(view -> {
            boolean cancel=false;
            View focusView=null;
            EditText editText= findViewById(R.id.codeBox);
            editText.setError(null);
            if (editText.getText().toString().length() <1) {
                editText.setError(getString(R.string.invalid_share_text));
                focusView=editText;
                cancel=true;
            }
            if(cancel)
            {
                focusView.requestFocus();
            }
            else
            {
                String strCode="Hi dear! let's catch up meet, to join the meeting on Conferenceium Android App by my room code : \n" +"'"+editText.getText().toString()+"'\n"+ "and download the app by attached link"+ "\n"+"https://play.google.com/store/apps/details?id=com.androharshit.conference" ;
                Intent intent=new Intent();
                intent.setAction(intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,strCode);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }


    public void onButtonClick(View v) {
        EditText editText = findViewById(R.id.codeBox);
        String text = editText.getText().toString();
        if (text.length() > 0) {
            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(text)
                    .setFeatureFlag("invite.enabled",false)
                    .setFeatureFlag("help.enabled",false)
                    .build();
            JitsiMeetActivity.launch(this, options);
        }

        // This is a demo app  You can add share button Functionality



    }

}