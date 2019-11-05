package com.example.proj;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity implements FragmentTracker {

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private GestureDetectorCompat mDetector;
    private final PersonInfo pi=new PersonInfo();
    private int next=1;
    private void loadTheFragment(Fragment f)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,f);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        fragmentTransaction.commit();
    }
    private void nextFragment(int lr)
    {
        if(lr==-1)
        {
            switch (next) {
                case 1:
                    next=3;
                    loadTheFragment(fragment3);
                    break;
                case 2:
                    next--;
                    loadTheFragment(fragment1);
                    break;
                case 3:
                    next--;
                    loadTheFragment(fragment2);
                    break;
            }
        }
        else
        {
            switch (next)
            {
                case 1:
                    next++;
                    loadTheFragment(fragment2);
                    break;
                case 2:
                    next++;
                    loadTheFragment(fragment3);
                    break;
                case 3:
                    next=1;
                    loadTheFragment(fragment1);
                    break;
            }
        }
    }

    @Override
    public void fragmentVisible(String s) {
        TextView tv=(TextView)findViewById(R.id.title);
        tv.setText(s);
    }

    @Override
    public void goNext() { nextFragment(1);}

    @Override
    public void goBack() { nextFragment(-1);}

    @Override
    public void saveNameAndLastName(String name, String lname) {
        pi.setName(name);
        pi.setLastname(lname);
    }

    @Override
    public void saveNickname(String nickname) {
        pi.setNickname(nickname);
    }

    @Override
    public void saveColor(String color) {
        pi.setColor(color);
    }

    @Override
    public void saveAnimal(String animal) {
        pi.setAnimal(animal);
    }

    @Override
    public void saveMovie(String movie) {
        pi.setMovie(movie);
    }

    @Override
    public void finished() {
        Intent i=new Intent(this, MainActivity2.class);
        i.putExtra("pi",pi);
        startActivity(i);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            if (event1.getX() < event2.getX()) {
                Toast toast = Toast.makeText(MainActivity3.this,"Fling right",Toast.LENGTH_SHORT);
                //toast.show();
                nextFragment(1);
            }
            else
            {
                Toast toast = Toast.makeText(MainActivity3.this,"Fling left",Toast.LENGTH_SHORT);
                //toast.show();
                nextFragment(-1);
            }

            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frags_activity_main);
        fragment3=new Fragment3();
        fragment2=new Fragment2();
        fragment1=new Fragment1();
        mDetector = new GestureDetectorCompat(this,new MyGestureListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTheFragment(fragment1);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}

