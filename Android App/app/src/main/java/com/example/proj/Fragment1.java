package com.example.proj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Fragment1 extends Fragment {
    private FragmentTracker ft;
    private View v;
    public static final String fragmentTitle="What's your name?";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ft.fragmentVisible(fragmentTitle);
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.first_fragment,container,false);
        Button b_next=v.findViewById(R.id.next_button);
        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft.goNext();
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
        EditText uname=v.findViewById(R.id.u_name);
        EditText lname=v.findViewById(R.id.u_lastname);
        EditText nname=v.findViewById(R.id.u_nickname);
        ft.saveNameAndLastName(uname.getText().toString(),lname.getText().toString());
        ft.saveNickname(nname.getText().toString());
        v=null;
    }
}

