package com.example.proj;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class FirebaseDBActivity extends AppCompatActivity {
    TextView db_value;
    EditText custom_value;
    Button bTrue;
    Button bFalse;
    Button bCustom;
    //reference to the root of the Firebase JSON tree
    DatabaseReference mRootReference= FirebaseDatabase.getInstance().getReference();
    DatabaseReference mValueRef=mRootReference.child("value");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_db);
        db_value=(TextView)findViewById(R.id.result);
        custom_value=(EditText)findViewById(R.id.custom_value);
        bTrue=(Button)findViewById(R.id.true_button);
        bFalse=(Button)findViewById(R.id.false_button);
        bCustom=(Button)findViewById(R.id.custom_button);
    }
    @Override
    protected void onStart(){
        super.onStart();
        mValueRef.addValueEventListener(new ValueEventListener() {
            //This will be called by the firebase db whenever the "value" changes in the db
            //This is the push mechanism we need
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text=dataSnapshot.getValue().toString();
                db_value.setText(text);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void PostMessageActivity(View view){
        startActivity(new Intent(this,PostMessage.class));
    }
    public void PostMessageWithImageActivity(View view){
        startActivity(new Intent(this,ChantwithImageUpload.class));
    }
    public void makeTrue(View view){
        mValueRef.setValue("True");
    }
    public void makeFalse(View view){
        mValueRef.setValue("False");
    }
    public void makeCustom(View view){
        mValueRef.setValue(custom_value.getText().toString());
    }
}

