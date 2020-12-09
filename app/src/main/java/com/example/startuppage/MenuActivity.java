package com.example.startuppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MenuActivity extends AppCompatActivity {

    CardView card1, card2, card3;
    Button logoutBtn;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        userId = getIntent().getStringExtra("USERID");

        logoutBtn = findViewById(R.id.logoutBtn);

        card1 = findViewById(R.id.cardView);
        card2 = findViewById(R.id.cardView2);
        card3 = findViewById(R.id.cardView3);

        card1.setBackgroundResource(R.drawable.my_button_bg);
        card2.setBackgroundResource(R.drawable.my_button_bg);
        card3.setBackgroundResource(R.drawable.my_button_bg);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MenuActivity.this, Login.class);
                startActivity(i);
            }
        });


        card1.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(MenuActivity.this, Discussion.class);
                startActivity(i);
            }
        }));

        card2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();


                Intent i = new Intent(MenuActivity.this, Timesheet.class);
                i.putExtra("USERID", userId);
                startActivity(i);
            }
        }));

        card3.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MenuActivity.this, Reporting.class);
                startActivity(i);
            }
        }));
    }
}
