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

public class ObjectFragment extends Fragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_POSTER = "poster";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_layout, container, false);
        Bundle args = getArguments();
        ImageView imageView=v.findViewById(R.id.large_image);
        imageView.setImageResource(args.getInt(ARG_POSTER));
        TextView titleText=v.findViewById(R.id.title_text);
        titleText.setText(args.getString(ARG_TITLE));
        return v;
    }
}
