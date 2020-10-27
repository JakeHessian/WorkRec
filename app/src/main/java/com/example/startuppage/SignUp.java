package com.example.startuppage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {

    public Button btnC;
    public Button btnSignIn;
    public ImageView imgIcon;
    public TextView headerS, pgHint;
    public TextInputLayout fullName, username, password;
    protected static final String ACTIVITY_NAME = "SignUp";
    public Pair[] pairs = new Pair[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        Log.i(ACTIVITY_NAME, "User clicked Sign Up");
        btnC = findViewById(R.id.createB);
        btnSignIn = findViewById(R.id.signInB);
        imgIcon = findViewById(R.id.imgIcon);
        headerS = findViewById(R.id.header);
        pgHint = findViewById(R.id.pgHint);
        fullName = findViewById(R.id.fullName);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Sign In");
                Intent intent = new Intent(SignUp.this, Login.class);

                pairs[0] = new Pair<View,String>(imgIcon, "icon");
                pairs[1] = new Pair<View,String>(headerS, "page_purpose");
                pairs[2] = new Pair<View,String>(pgHint, "page_hint");
                pairs[3] = new Pair<View,String>(fullName, "fullName_trans");
                pairs[4] = new Pair<View,String>(username, "user_trans");
                pairs[5] = new Pair<View,String>(password, "pass_trans");
                pairs[6] = new Pair<View,String>(btnC, "create_trans");
                pairs[7] = new Pair<View,String>(btnSignIn, "signIn_trans");

                ActivityOptions activityO3 = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
                startActivity(intent, activityO3.toBundle());
            }
        });

    }
}