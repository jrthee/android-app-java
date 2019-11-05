package com.example.proj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostMessage extends AppCompatActivity {
    public static class Node {

        public String value1;
        public String value2;

        public Node(String v1, String v2) {
            value1=v1;
            value2=v2;
        }
    }
    public static class Post {
        public String author;
        public String text;
        Object timestamp;
        public String link;
        public Post() {}
        public Post(String author, String text,String link) {
            this.author=author;
            this.text=text;
            timestamp=ServerValue.TIMESTAMP;
            this.link=link;
        }
        public Object gettimestamp(){
            return timestamp;
        }
    }
    EditText set_text;
    DatabaseReference mRootReference= FirebaseDatabase.getInstance().getReference();
    FirebaseUser currentUser;
    RecyclerView rv;
    RecyclerViewAdapter rva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message);
        set_text=(EditText)findViewById(R.id.set_text);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        rv=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rv.setLayoutManager(layoutManager);
        rva=new RecyclerViewAdapter(this);
        rv.setAdapter(rva);
    }
    public void SetMessage(View view){
        //This will only overwrite the value of the node "new"
        //it will create a node named "new" if it does not exist
        mRootReference.child("new").setValue(set_text.getText().toString(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(PostMessage.this,"Data could not be saved",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostMessage.this,"Data saved successfully",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //using a map
        Map<String, Node> nodes = new HashMap<String, Node>();
        nodes.put("new", new Node("new value1", "new value2"));
        nodes.put("new1", new Node("new val1", "new val2"));
        nodes.put("new2", new Node("new val1", "new val2"));
        mRootReference.child("new_node").setValue(nodes);
    }
    public void UpdateValue(View view){
        Map<String, Object> nodes = new HashMap<String, Object>();
        nodes.put("new/value1", "updated value1");
        nodes.put("new1/value2", "updated val2");
        nodes.put("new2/value3", "new val3");
        mRootReference.child("new_node").updateChildren(nodes);
    }

    public void PostMessage(View view){
        DatabaseReference postsRef = mRootReference.child("posts");
        DatabaseReference newPostRef = postsRef.push();
        newPostRef.setValue(new Post(currentUser.getDisplayName(), set_text.getText().toString(),null));
        String postId = newPostRef.getKey();
        Map<String, Object> nodes = new HashMap<String, Object>();
        nodes.put(postId+"/extra", "extra info");
        postsRef.updateChildren(nodes);
    }
    public void UpdateConcurrValue(View view){
        DatabaseReference upvotesRef =  mRootReference.child("likes");
        upvotesRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentValue = mutableData.getValue(Integer.class);
                if (currentValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                System.out.println("Transaction completed");
            }
        });
    }
}

