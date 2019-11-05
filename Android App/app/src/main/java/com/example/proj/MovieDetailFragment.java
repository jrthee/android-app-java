package com.example.proj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {
    public static MovieDetailFragment newInstance(String u, String t, String y, String l, String s, String di, float r, String d)
    {
        MovieDetailFragment movieDetailFragment=new MovieDetailFragment();
        Bundle args=new Bundle();
        //args.putInt("id",i);
        args.putString("url",u);
        args.putString("title",t);
        args.putString("year",y);
        args.putString("length",l);
        args.putString("stars",s);
        args.putString("directors",di);
        args.putFloat("rating",r);
        args.putString("description",d);
        movieDetailFragment.setArguments(args);
        return movieDetailFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        final View v = inflater.inflate(R.layout.fragment_layout, container, false);
        ImageView imageView=v.findViewById(R.id.large_image);
        Picasso.get().load(args.getString("url")).into(imageView);
        //imageView.setImageResource(args.getInt("id"));
        EditText editText=v.findViewById(R.id.title_text);
        editText.setText(args.getString("title"));
        return v;
    }
}


