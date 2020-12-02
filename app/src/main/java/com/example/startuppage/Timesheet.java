package com.example.startuppage;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Timesheet extends AppCompatActivity {
    Button clockInButton;
    Button clockOutButton;
    ListView timeSheetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);
        clockInButton = findViewById(R.id.clockInButton);
        clockOutButton = findViewById(R.id.clockOutButton);



        clockOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getApplicationContext(),"Clocked Out",Toast.LENGTH_SHORT);
                t.show();
            }
        });
        clockInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getApplicationContext(),"Clocked In",Toast.LENGTH_SHORT);
                t.show();
            }
        });

    }
}