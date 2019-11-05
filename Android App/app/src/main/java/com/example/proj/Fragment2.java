package com.example.proj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Fragment2 extends Fragment {
    private FragmentTracker ft;
    private View v;
    private static final String fragmentTitle="What's your favorite?";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Toast.makeText(getContext(),"Visible",Toast.LENGTH_SHORT).show();
        ft.fragmentVisible(fragmentTitle);
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.third_fragment,container,false);
        //Button b_next=v.findViewById(R.id.next_button);
        //b_next.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  ft.goNext();
            //}
        //});
        Button b_back=v.findViewById(R.id.back_button);
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft.goBack();
            }
        });
        Button b_finish=v.findViewById(R.id.finish_button);
        b_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ucolor=v.findViewById(R.id.u_color);
                EditText uanimal=v.findViewById(R.id.u_animal);
                EditText umovie=v.findViewById(R.id.u_movie);
                ft.saveColor(ucolor.getText().toString());
                ft.saveAnimal(uanimal.getText().toString());
                ft.saveMovie(umovie.getText().toString());
                ft.finished();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            ft=(FragmentTracker) context;
        }
        catch (ClassCastException ex) {
            throw new ClassCastException(context.toString()+"must implement FragmentTracker");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EditText ucolor=v.findViewById(R.id.u_color);
        EditText uanimal=v.findViewById(R.id.u_animal);
        EditText umovie=v.findViewById(R.id.u_movie);
        ft.saveColor(ucolor.getText().toString());
        ft.saveAnimal(uanimal.getText().toString());
        ft.saveMovie(umovie.getText().toString());
        v=null;
        //Toast.makeText(getContext(),uname.getText(),Toast.LENGTH_SHORT).show();
    }
}


