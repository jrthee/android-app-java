package com.example.proj;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapterWithImage extends
        RecyclerView.Adapter<RecyclerViewAdapterWithImage.ViewHolder> {
    DatabaseReference mRootReference= FirebaseDatabase.getInstance().getReference();
    DatabaseReference postsRef = mRootReference.child("posts");
    List<Post> data=new ArrayList<Post>();
    Context contex;
    RecyclerView rv;
    final static class WorkerDownloadImage extends AsyncTask<String, String, Bitmap > {
        private final WeakReference<Context> parentRef;
        private final WeakReference<ImageView> imageViewRef;
        public  WorkerDownloadImage(final Context parent, final ImageView imageview)
        {
            parentRef=new WeakReference<Context>(parent);
            imageViewRef=new WeakReference<ImageView>(imageview);
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap result= HTTP_METHODS.downloadImageUsingHTTPGetRequest(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(final Bitmap result){
            final ImageView iv=imageViewRef.get();
            if(iv!=null)
            {
                iv.setImageBitmap(result);
            }
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uname;
        public TextView utext;
        public  TextView ptime;
        public ImageView imv;
        public ViewHolder(View view) {
            super(view);
            uname=(TextView)view.findViewById(R.id.user_name);
            utext=(TextView)view.findViewById(R.id.user_text);
            ptime=(TextView)view.findViewById(R.id.post_time);
            imv=(ImageView)view.findViewById(R.id.img_view);
        }
    }
    public RecyclerViewAdapterWithImage(Context _context){
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
                if(dataSnapshot.hasChild("link"))
                    p.link=dataSnapshot.child("link").getValue().toString();
                else
                    p.link=null;
                data.add(p);
                rv.scrollToPosition(data.size()-1);
                RecyclerViewAdapterWithImage.this.notifyItemInserted(data.size()-1);
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
                    if(dataSnapshot.hasChild("link"))
                        p.link=dataSnapshot.child("link").getValue().toString();
                    else
                        p.link=null;
                    data.add(position,p);
                    rv.scrollToPosition(position);
                    RecyclerViewAdapterWithImage.this.notifyItemChanged(position);
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
                    RecyclerViewAdapterWithImage.this.notifyItemRemoved(position);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_with_image, parent, false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uname.setText(data.get(position).un);
        holder.utext.setText(data.get(position).text);
        holder.ptime.setText(data.get(position).time);
        if(data.get(position).link!=null)
            new WorkerDownloadImage(contex,holder.imv).execute(data.get(position).link);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

}

