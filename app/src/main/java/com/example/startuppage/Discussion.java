package com.example.startuppage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Discussion extends AppCompatActivity {
    Boolean dualPanel;
    Cursor listCursor;
    ListView listview;
    EditText discussionMessage;
    Button button;
    FirebaseAuth mAuth;
    ArrayList<QueryDocumentSnapshot> discussionPosts = new ArrayList<QueryDocumentSnapshot>();
    public static final String ACTIVITY_NAME = "Chat Window";
    DiscussionHandler discussionHandler;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    String currentUid;
    Toolbar toolbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.discussion_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.discussionHelpIcon:
                Log.i(ACTIVITY_NAME, "Launching help DialogFragment");
                FragmentManager fm = getSupportFragmentManager();
                String helpMessage = getResources().getString(R.string.discussionHelp);
                discussion_help dialogFragment = discussion_help.newInstance(helpMessage);
                dialogFragment.show(fm, "Dialog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        toolbar = findViewById(R.id.discussionToolbar);
        toolbar.inflateMenu(R.menu.discussion_toolbar);
        toolbar.setTitle("Discussion");
        setSupportActionBar(toolbar);
        listview = findViewById(R.id.discussionList);
        discussionHandler = new DiscussionHandler(this);
        listview.setAdapter(discussionHandler);
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        discussionMessage = findViewById(R.id.discussionMessage);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Discussion.this, DiscussionCommentSection.class);
                QueryDocumentSnapshot getitem = discussionHandler.getitem(position);
                Bundle bundle = new Bundle();
                bundle.putString("username",getitem.get("username").toString());
                bundle.putString("post_id",getitem.getId().toString());
                bundle.putString("date",getitem.get("date").toString());
                bundle.putString("time",getitem.get("time").toString());
                bundle.putString("message",getitem.get("message").toString());
                bundle.putString("user_id",getitem.get("user_id").toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 10);
            }});

        fetchDiscussion();
        button = findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String discussionMessageStr = discussionMessage.getText().toString();
                Date dateObject = new Date();
                final String date = dateFormat.format(dateObject);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                final String time = timeFormat.format(dateObject);


                String uid = mAuth.getCurrentUser().getUid();
                db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();
                            Log.i("xxxaa",result.get("fName").toString());
                            newDiscussion(date,time,discussionMessageStr, result.get("fName").toString());
                            discussionMessage.setText("");

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                }
                );
            }});

    }
    private void newDiscussion(String date, String time,String message, String username){
        Map<String, Object> discussionObject = new HashMap<>();
        discussionObject.put("message",message);
        discussionObject.put("date",date);
        discussionObject.put("time",time);
        discussionObject.put("username",username);
        discussionObject.put("user_id",currentUid);

        db.collection("discussion").add(discussionObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Discussion", "Discussion Data written with ID: " + documentReference.getId());
                fetchDiscussion();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Discussion", "Error adding document", e);
                    }
                });}
    private void fetchDiscussion(){
        Task<QuerySnapshot> discussion = db.collection("discussion").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                discussionPosts.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot data : task.getResult()) {
                        discussionPosts.add(data);
                    }
                    discussionHandler.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    protected class DiscussionHandler extends ArrayAdapter<QueryDocumentSnapshot> {
        public DiscussionHandler(Context ctx){
            super(ctx,0);
        }

        public QueryDocumentSnapshot getitem(int position){
            return discussionPosts.get(position);
        }
        public int getCount(){
            return discussionPosts.size();
        }


        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = Discussion.this.getLayoutInflater();
            View data = null;
            data = inflater.inflate(R.layout.fragment_discussion_post,null);
            QueryDocumentSnapshot disData = getitem(position);
            TextView message = (TextView) data.findViewById(R.id.postContainer);
            TextView author = (TextView) data.findViewById(R.id.postAuthor);
            TextView date = (TextView) data.findViewById(R.id.postDate);
            TextView time = (TextView) data.findViewById(R.id.postTime);
            message.setText(disData.get("message").toString());
            author.setText(disData.get("username").toString());
            date.setText(disData.get("date").toString());
            time.setText(disData.get("time").toString());
            return data;
        }

    }

}