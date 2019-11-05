package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class PlayAnimals extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_sample_activity);
    }

    public void onClick1(View view) {
        Toast.makeText(PlayAnimals.this,"Incorrect",Toast.LENGTH_SHORT).show();
        //setContentView(R.layout.play_sample_activity);
        //Intent intent = new Intent(this,ViewPager.class);
        //startActivity(intent);
    }
    public void onClick2(View view) {
        Toast.makeText(PlayAnimals.this,"Correct!",Toast.LENGTH_SHORT).show();
        //setContentView(R.layout.play_sample_activity);
        //Intent intent = new Intent(this,ViewPager.class);
        //startActivity(intent);
    }
    public void onClick3(View view) {
        Toast.makeText(PlayAnimals.this,"Incorrect",Toast.LENGTH_SHORT).show();
        //setContentView(R.layout.play_sample_activity);
        //Intent intent = new Intent(this,ViewPager.class);
        //startActivity(intent);
    }
    public void onClick4(View view) {
        Toast.makeText(PlayAnimals.this,"Incorrect",Toast.LENGTH_SHORT).show();
        //setContentView(R.layout.play_sample_activity);
        //Intent intent = new Intent(this,ViewPager.class);
        //startActivity(intent);
    }

    public void Next(View view) {
        Intent intent = new Intent(PlayAnimals.this,PlayAnimals2.class);
        startActivity(intent);
    }
}
