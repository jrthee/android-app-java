package com.example.proj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListFragment extends Fragment {
    public interface OnItemSelectedListener {
        public void onListItemSelected(String name, int poster_id);
    }
    OnItemSelectedListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_list_fragment, container, false);
        final String [] movieItems = {"Red","Orange","Yellow","Green","Blue","Purple","Pink","Black","Brown","Grey"};
        ListView listView = (ListView) v.findViewById(R.id.my_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_expandable_list_item_1,movieItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (movieItems[position] == "Red") {
                    clickListener.onListItemSelected("Red", R.drawable.red);
                }
                if (movieItems[position] == "Orange") {
                    clickListener.onListItemSelected("Orange", R.drawable.orange);
                }
                if (movieItems[position] == "Yellow") {
                    clickListener.onListItemSelected("Yellow", R.drawable.yellow);
                }
                if (movieItems[position] == "Green") {
                    clickListener.onListItemSelected("Green", R.drawable.green);
                }
                if (movieItems[position] == "Blue") {
                    clickListener.onListItemSelected("Blue", R.drawable.blue);
                }
                if (movieItems[position] == "Purple") {
                    clickListener.onListItemSelected("Purple", R.drawable.purple);
                }
                if (movieItems[position] == "Pink") {
                    clickListener.onListItemSelected("Pink", R.drawable.pink);
                }
                if (movieItems[position] == "Black") {
                    clickListener.onListItemSelected("Black", R.drawable.black);
                }
                if (movieItems[position] == "Brown") {
                    clickListener.onListItemSelected("Brown", R.drawable.brown);
                }
                if (movieItems[position] == "Grey") {
                    clickListener.onListItemSelected("Grey", R.drawable.grey);
                }
            }
        });

        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickListener=(OnItemSelectedListener) context;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()+"must implement EventTrack");
        }
    }
}


