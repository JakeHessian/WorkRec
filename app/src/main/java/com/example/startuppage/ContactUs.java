package com.example.startuppage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.google.android.material.snackbar.Snackbar;

public class ContactUs extends AppCompatActivity {
    Button facebook, twitter, needH;
    RelativeLayout getL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getL = findViewById(R.id.getLayout);
        needH = findViewById(R.id.needHelp);
        facebook = findViewById(R.id.fb);
        twitter =  findViewById(R.id.twitter);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookShow("https://www.facebook.com/getWorkRecToday");
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterShow("https://twitter.com/RecWork");
            }
        });

        needH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar();
            }
        });
    }

    private void showSnackBar() {
        final Snackbar snackbar = Snackbar.make(getL,"Author: Sujean\nVersion:1.0\nVisit our social media platforms by clicking one of the buttons to be redirected to webpage", Snackbar.LENGTH_LONG).setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar closeBar = Snackbar.make(getL, "Choose social media platform", Snackbar.LENGTH_SHORT);
                closeBar.show();
            }
        });
        snackbar.show();

    }

    public void facebookShow (String s){
        Uri webpage = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

    public void twitterShow (String s){
        Uri webpage = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(intent);
    }

}