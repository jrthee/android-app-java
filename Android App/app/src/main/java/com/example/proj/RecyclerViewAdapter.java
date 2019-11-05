package com.example.proj;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    DatabaseReference mRootReference= FirebaseDatabase.getInstance().getReference();
    DatabaseReference postsRef = mRootReference.child("posts");
    List<Post> data=new ArrayList<Post>();
    Context contex;
    RecyclerView rv;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uname;
        public TextView utext;
        public  TextView ptime;
        public ViewHolder(View view) {
            super(view);
            uname=(TextView)view.findViewById(R.id.user_name);
            utext=(TextView)view.findViewById(R.id.user_text);
            ptime=(TextView)view.findViewById(R.id.post_time);
        }
    }
    public RecyclerViewAdapter(Context _context){
        contex=_context;
        rv=(RecyclerView)((AppCompatActivity)contex).findViewById(R.id.recycler_view);
        postsRef.addChildEventListener(new ChildEventListener() {
            //s: The key name of sibling location ordered before the new child.
            //This will be null for the first child node of a location.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p=new Post();

                p.id=dataSnapshot.getKey().toString();
                p.un=dataSnapshot.child("author").getValue().toString();
                p.text=dataSnapshot.child("text").getValue().toString();
                SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String time=dataSnapshot.child("timestamp").getValue().toString();
                Long t=Long.parseLong(time);
                p.time=localDateFormat.format(new Date(t));
                data.add(p);
                rv.scrollToPosition(data.size()-1);
                RecyclerViewAdapter.this.notifyItemInserted(data.size()-1);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String id=dataSnapshot.getKey().toString();
                int position=-1;
                for (int i=0; i<data.size(); i++)
                {
                    if(data.get(i).id.equals(id)){
                        position=i;
                        break;
                    }
                }
                if(position!=-1)
                {
                    data.remove(position);
                    Post p=new Post();
                    p.id=dataSnapshot.getKey().toString();
                    p.un=dataSnapshot.child("author").getValue().toString();
                    p.text=dataSnapshot.child("text").getValue().toString();
                    SimpleDateFormat localDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String time=dataSnapshot.child("timestamp").getValue().toString();
                    Long t=Long.parseLong(time);
                    p.time=localDateFormat.format(new Date(t));
                    data.add(position,p);
                    rv.scrollToPosition(position);
                    RecyclerViewAdapter.this.notifyItemChanged(position);
                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String id=dataSnapshot.getKey().toString();
                int position=-1;
                for (int i=0; i<data.size(); i++)
                {
                    if(data.get(i).id.equals(id)){
                        position=i;
                        break;
                    }
                }
                if(position!=-1)
                {
                    data.remove(position);
                    rv.scrollToPosition(position);
                    RecyclerViewAdapter.this.notifyItemRemoved(position);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uname.setText(data.get(position).un);
        holder.utext.setText(data.get(position).text);
        holder.ptime.setText(data.get(position).time);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

}

