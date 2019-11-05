package com.example.proj;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText email;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            if(currentUser.isEmailVerified()) {
                //Intent intent = new Intent(MainActivity.this, Signin.class);
                //Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Email must be verified",
                        Toast.LENGTH_SHORT).show();
            }
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if(user.isEmailVerified())
                    {
                        //Intent intent = new Intent(MainActivity.this, Signin.class);
                        //Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Email must be verified",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void SendVerificationEmail(View view)
    {
        SendVerification();
    }
    private void SendVerification(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Email has been sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void Register(View view){
        if(email.getText().toString().matches("") || password.getText().toString().matches(""))
        {
            Toast.makeText(MainActivity.this, "Both email and password required",
                    Toast.LENGTH_SHORT).show();
        }
        else
            createAccount(email.getText().toString(),password.getText().toString());
    }
    public void Login(View view){
        if(email.getText().toString().matches("") || password.getText().toString().matches(""))
        {
            Toast.makeText(MainActivity.this, "Both email and password required",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            mAuth.signInWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString()).addOnSuccessListener(this,
                    new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, "User authentication successful!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "User authentication failed",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void createAccount(String email, String password) {
        Log.d("MyDebugMsg", "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this,
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("MyDebugMsg", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "User authentication successful!",
                                Toast.LENGTH_SHORT).show();
                        SendVerification();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MyDebugMsg", "Failure:"+e.getMessage());
                FirebaseUser user = mAuth.getCurrentUser();
                Toast.makeText(MainActivity.this, "User authentication failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void SendPassReset(View view){
        ResetPass();
    }
    private void ResetPass(){
        mAuth.sendPasswordResetEmail(email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Email has been sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClickPic(View view) {
        //Toast.makeText(this,"toast",Toast.LENGTH_SHORT).show();
        ImageView imageView=findViewById(R.id.fb);
        AnimatorSet spinSet = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.custom_animator);
        spinSet.setTarget(imageView);
        spinSet.start();
    }
}

