package com.example.startuppage;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Discussion extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    public Button btn;
    protected static final String ACTIVITY_NAME = "MainActivity";
    public Pair[] pair = new Pair[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discussion_content);
        //initialize recyclerview and FIrebase objects
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("DiscussionBoard");
        mAuth = FirebaseAuth.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnStart);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME, "User clicked Get Started");
                Intent mIntent = new Intent(Discussion.this, Login.class);
                pair[0] = new Pair<View, String>(btn, "btn_trans");
                ActivityOptions activityO1 = ActivityOptions.makeSceneTransitionAnimation(Discussion.this, pair);
                startActivity(mIntent, activityO1.toBundle());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<discussion_dblink, discussionViewHolder> FBRA = new FirebaseRecyclerAdapter<discussion_dblink, discussionViewHolder>(
                discussion_dblink.class,
                R.layout.discussion_home,
                discussionViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(discussionViewHolder viewHolder, discussion_dblink model, int position) {
                final String post_key = getRef(position).getKey();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setUserName(model.getUsername());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent singleActivity = new Intent(Discussion.this, SinglePostActivity.class);
//                        singleActivity.putExtra("PostID", post_key);
//                        startActivity(singleActivity);
                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }

    public static class discussionViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public discussionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = mView.findViewById(R.id.post_title_txtview);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = mView.findViewById(R.id.post_desc_txtview);
            post_desc.setText(desc);
        }

        public void setUserName(String userName) {
            TextView postUserName = mView.findViewById(R.id.post_user);
            postUserName.setText(userName);
        }

    }
}