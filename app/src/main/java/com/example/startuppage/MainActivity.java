package com.example.startuppage;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public Button btn;
    protected static final String ACTIVITY_NAME = "MainActivity";
    public Pair[] pair = new Pair[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnStart);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Get Started");
                Intent mIntent = new Intent(MainActivity.this, Login.class);
                pair[0] = new Pair<View, String>(btn, "btn_trans");
                ActivityOptions activityO1 = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pair);
                startActivity(mIntent, activityO1.toBundle());
            }
        });
    }

}