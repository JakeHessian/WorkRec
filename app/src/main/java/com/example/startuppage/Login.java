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

public class Login extends AppCompatActivity {

    public Button btnF;
    public Button btnL;
    public Button btnCN;
    public ImageView img;
    public TextView headerS, pgHint;
    public TextInputLayout username , password;
    protected static final String ACTIVITY_NAME = "Login";
    public Pair[] pairs = new Pair[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "User entered login page");
        btnF = findViewById(R.id.forgotP);
        btnL = findViewById(R.id.loginB);
        btnCN = findViewById(R.id.newAcc);
        img = findViewById(R.id.imgIcon);
        headerS = findViewById(R.id.header);
        pgHint = findViewById(R.id.pgHint);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        btnCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Login");
                Intent intent = new Intent(Login.this, SignUp.class);

                pairs[0] = new Pair<View,String>(img, "icon");
                pairs[1] = new Pair<View,String>(headerS, "page_purpose");
                pairs[2] = new Pair<View,String>(pgHint, "page_hint");
                pairs[3] = new Pair<View,String>(username, "user_trans");
                pairs[4] = new Pair<View,String>(password, "pass_trans");
                pairs[5] = new Pair<View,String>(btnF, "forgot_trans");
                pairs[6] = new Pair<View,String>(btnL, "login_trans");
                pairs[7] = new Pair<View,String>(btnCN, "new_trans");

                ActivityOptions activityO2 = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
                startActivity(intent, activityO2.toBundle());
            }
        });

    }

    public void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    public void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    public void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    public void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}