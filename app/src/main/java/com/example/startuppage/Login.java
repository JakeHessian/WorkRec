
        package com.example.startuppage;

        import android.app.ActivityOptions;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.util.Pair;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.material.textfield.TextInputLayout;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public String fireBaseUserId;
    public Button btnF;
    public Button btnL;
    public Button btnCN;
    public ImageView img;
    public TextView headerS, pgHint;
    public TextInputLayout username, password;
    protected static final String ACTIVITY_NAME = "Login";
    public Pair[] pairs = new Pair[8];
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
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
                Log.i(ACTIVITY_NAME, "User clicked Create an account");
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

        btnL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Login");
                String email = username.getEditText().getText().toString().trim();
                String getPass = password.getEditText().getText().toString().trim();


                mAuth.signInWithEmailAndPassword("1379.arman@gmail.com", "Arm123!!").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this, MenuActivity.class);
                            i.putExtra("USERID", mAuth.getCurrentUser().getUid());
                            startActivity(i);
                        } else {
                            Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPass = new EditText(v.getContext());
                AlertDialog.Builder passResetDialog = new AlertDialog.Builder(v.getContext());
                passResetDialog.setTitle("Reset Password?");
                passResetDialog.setMessage("Enter your email to receive you password reset link");
                passResetDialog.setView(resetPass);

                passResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = resetPass.getText().toString();
                        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset link has been sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error: Reset link can't be sent - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passResetDialog.create().show();
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