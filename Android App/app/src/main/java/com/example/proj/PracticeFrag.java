package com.example.proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class PracticeFrag extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_layout);
    }

    public void onClick1(View view) {
        Intent intent = new Intent(this,MasterDetail.class);
        startActivity(intent);
    }
    public void onClick2(View view) {
        Intent intent = new Intent(this,MasterDetail.class);
        startActivity(intent);
    }
    public void onClick3(View view) {
        Intent intent = new Intent(this,MainActivity5.class);
        //Intent intent = new Intent(this,ViewPager.class);
        startActivity(intent);
    }
    public void onClick4(View view) {
        Intent intent = new Intent(this,ViewPager.class);
        startActivity(intent);
    }
}
