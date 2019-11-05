package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class PlayFrag extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_layout);
    }

    public void onClick1(View view) {
        //setContentView(R.layout.play_sample_activity);
        Intent intent = new Intent(this,PlayAnimals.class);
        startActivity(intent);
    }
    public void onClick2(View view) {
        //setContentView(R.layout.play_sample_activity);
        Intent intent = new Intent(this,PlayAnimals.class);
        startActivity(intent);
    }
    public void onClick3(View view) {
        //setContentView(R.layout.play_sample_activity);
        Intent intent = new Intent(this,PlayAnimals.class);
        startActivity(intent);
    }
    public void onClick4(View view) {
        //setContentView(R.layout.play_sample_activity);
        Intent intent = new Intent(this,PlayAnimals.class);
        startActivity(intent);
    }
}

