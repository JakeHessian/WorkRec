package com.example.startuppage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DiscussionCommentSection extends AppCompatActivity {
    Boolean dualPanel;
    Cursor listCursor;
    ListView listview;
    EditText discussionComment;
    Button button;
    FirebaseAuth mAuth;
    ArrayList<QueryDocumentSnapshot> discussionComments = new ArrayList<QueryDocumentSnapshot>();
    public static final String ACTIVITY_NAME = "Chat Window";
    DiscussionCommentSection.DiscussionCommentSectionHandler discussionCommentSectionHandler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    String currentUid;
    String time,date,message,user_id,post_id,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_comment_section);
         username = getIntent().getExtras().getString("username");
         time = getIntent().getExtras().getString("time");
         date = getIntent().getExtras().getString("date");
         message = getIntent().getExtras().getString("message");
         user_id = getIntent().getExtras().getString("user_id");
         post_id = getIntent().getExtras().getString("post_id");
        TextView messageC = (TextView) findViewById(R.id.commentContainer);
        TextView authorC = (TextView) findViewById(R.id.commentAuthor);
        TextView dateC = (TextView) findViewById(R.id.commentDate);
        TextView timeC = (TextView) findViewById(R.id.commentTime);
        messageC.setText(post_id);
        authorC.setText(username);
        dateC.setText(date);
        timeC.setText(time);

        discussionComment= findViewById(R.id.commentEdit);
        listview = findViewById(R.id.discussionCommentList);
        discussionCommentSectionHandler = new DiscussionCommentSection.DiscussionCommentSectionHandler(this);
        listview.setAdapter(discussionCommentSectionHandler);
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        button = findViewById(R.id.newCommentBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String discussionCommentStr = discussionComment.getText().toString();
                Date dateObject = new Date();
                final String date = dateFormat.format(dateObject);
                final String time = timeFormat.format(dateObject);

                String uid = mAuth.getCurrentUser().getUid();
                db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                     @Override
                                                                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                         if (task.isSuccessful()) {
                                                                                             DocumentSnapshot result = task.getResult();
                                                                                             Log.i("xxxaa",result.get("fName").toString());
                                                                                             newComment(date,time,discussionCommentStr, result.get("fName").toString());
                                                                                             discussionComment.setText("");
                                                                                         } else {
                                                                                             Log.d("TAG", "Error getting documents: ", task.getException());
                                                                                         }
                                                                                     }
                                                                                 }
                );
            }});
        fetchDiscussion();
    }
    private void fetchDiscussion(){
        Task<QuerySnapshot> discussion = db.collection("discussionReply").whereEqualTo("user_id",currentUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                discussionComments.clear();
                Log.i("TsadAG", "Error getting documents: ");

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot data : task.getResult()) {
                        discussionComments.add(data);
                        Log.i("TsadAG", "Error getting documents: ");
                    }
                    discussionCommentSectionHandler.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private void newComment(String date, String time,String message, String username){
        Map<String, Object> discussionObject = new HashMap<>();
        discussionObject.put("post_id",post_id);
        discussionObject.put("date",date);
        discussionObject.put("message",message);
        discussionObject.put("time",time);
        discussionObject.put("username",username);
        discussionObject.put("user_id",currentUid);

        db.collection("discussionReply").add(discussionObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Discussion Comment", " Data written with ID: " + documentReference.getId());
                fetchDiscussion();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Discussion Comment", "Error adding document", e);
                    }
                });}

    private class DiscussionCommentSectionHandler extends ArrayAdapter<QueryDocumentSnapshot> {
        public DiscussionCommentSectionHandler(Context ctx){
            super(ctx,0);
        }

        public QueryDocumentSnapshot getitem(int position){
            return discussionComments.get(position);
        }
        public int getCount(){
            return discussionComments.size();
        }


        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = DiscussionCommentSection.this.getLayoutInflater();
            View data = null;
            data = inflater.inflate(R.layout.fragment_discussion_comment,null);
            QueryDocumentSnapshot disData = getitem(position);
            Log.i("ppxxxz",disData.getData().toString());
            TextView message = (TextView) data.findViewById(R.id.commentContainerEach);
            TextView author = (TextView) data.findViewById(R.id.commentAuthorEach);
            TextView date = (TextView) data.findViewById(R.id.commentDateEach);
            TextView time = (TextView) data.findViewById(R.id.commentTimeEach);
            message.setText(disData.get("message").toString());
            author.setText(disData.get("username").toString());
            date.setText(disData.get("date").toString());
            time.setText(disData.get("time").toString());
            return data;
        }

    }

}