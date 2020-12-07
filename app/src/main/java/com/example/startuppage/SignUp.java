package com.example.startuppage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String userID;
    public Button btnC;
    public Button btnSignIn;
    public ImageView imgIcon;
    public TextView headerS, pgHint;
    public TextInputLayout fullName, username, password, address;
    protected static final String ACTIVITY_NAME = "SignUp";
    public Pair[] pairs = new Pair[8];
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        Log.i(ACTIVITY_NAME, "User clicked Sign Up");
        btnC = findViewById(R.id.createB);
        btnSignIn = findViewById(R.id.signInB);
        imgIcon = findViewById(R.id.imgIcon);
        headerS = findViewById(R.id.header);
        pgHint = findViewById(R.id.pgHint);
        fullName = findViewById(R.id.fullName);
        username = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked create an account");
                final String email = username.getEditText().getText().toString().trim();
                String getPass = password.getEditText().getText().toString().trim();
                final String getName = fullName.getEditText().getText().toString();
                final String getAddress = address.getEditText().getText().toString();

                if (TextUtils.isEmpty(email)){
                    username.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(getPass)){
                    password.setError("Password is required");
                    return;
                }

                if (getPass.length() < 6){
                    password.setError("Password must contain 6 or more characters");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference docR = db.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", getName);
                            user.put("email", email);
                            user.put("address", getAddress);
                            docR.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Store to Database - ", "User Profile: " + userID + " has been created");
                                    Intent mIntent = new Intent(SignUp.this, Login.class);
                                    startActivity(mIntent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Store to Database - ", "Error: " + e.toString());
                                }
                            });
                        } else {
                            Toast.makeText(SignUp.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Already have an account");
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