package com.example.proj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MasterDetail extends AppCompatActivity implements ListFragment.OnItemSelectedListener{
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_md);
        if (savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ListFragment()).commit();
        }
        twoPane=false;
        if (findViewById(R.id.detail_container)!=null) {
            twoPane=true;
        }
    }

    @Override
    public void onListItemSelected(String name, int poster_id) {
        Bundle args = new Bundle();
        args.putString(ObjectFragment.ARG_TITLE, name);
        args.putInt(ObjectFragment.ARG_POSTER, poster_id);
        Fragment fragment = new ObjectFragment();
        fragment.setArguments(args);
        if (twoPane)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,fragment).addToBackStack(null).commit();
        }
        else
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        }
    }
}


