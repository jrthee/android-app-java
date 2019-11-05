package com.example.proj;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyRecyclerAdapter2 extends RecyclerView.Adapter<MyRecyclerAdapter2.ViewHolder> implements Filterable {
    List<Map<String, ?>> md;
    List<Map<String, ?>> md_filtered;
    Context context;
    private OnListItemClickListener onListItemClickListener=null;


    final class WorkerDownloadRatings extends AsyncTask<Void,String,String> {
        private final WeakReference<Context> parentRef;
        private final WeakReference<String> ratingRef;
        List<Map<String, ?>> filteredList = new ArrayList<>();
        public WorkerDownloadRatings(final Context parent, final String _rating)
        {
            parentRef=new WeakReference<Context>(parent);
            ratingRef=new WeakReference<String>(_rating);
        }
        @Override
        protected String doInBackground(Void... voids) {
            final String rating=ratingRef.get();
            String result = MyUtility.downloadJSONusingHTTPGetRequest("http://cis651.com/movies/rating/"+rating);
            return result;
        }
        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i=0; i<jsonArray.length(); i++) {
                    //moviesList.add(Map<jsonArray.getString(i),?>);
                    HashMap movie = new HashMap();
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    Iterator<?> keys = jsonObject2.keys();
                    while(keys.hasNext() ){
                        Log.d("ThisDebug","length3");
                        String key = (String)keys.next();
                        String value = jsonObject2.getString(key);
                        movie.put(key, value);
                    }
                    filteredList.add(movie);
                }
                md_filtered = filteredList;
            }
            catch (JSONException ex) {
                Log.d("MyDebugMsg",ex.getMessage());
            }
        }
    }


    final class WorkerDuplicateMovie extends AsyncTask<JSONObject,Void,Void> {
        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            MyUtility.sendHttPostRequest("http://cis651.com/add",jsonObjects[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(context,"Movie Duplicated!",Toast.LENGTH_SHORT).show();
        }
    }



    public MyRecyclerAdapter2(Context _context, List<Map<String, ?>> list) {
        md=md_filtered=list;
        context = _context;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    md_filtered = md;
                } else {
                    new WorkerDownloadRatings(context,charString).execute();
                    //List<Map<String, ?>> filteredList = new ArrayList<>();
                    //for (Map movie : md) {
                    //  if (movie.get("name").toString().toLowerCase().contains(charString.toLowerCase())) {
                    //    filteredList.add(movie);
                    //}
                    //}
                    //md_filtered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = md_filtered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                md_filtered = (ArrayList<Map<String,?>>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movie_name;
        public TextView movie_year;
        public ImageView poster_img;
        public ViewHolder(View view) {
            super(view);
            movie_name = (TextView)view.findViewById(R.id.movie_name);
            poster_img = (ImageView)view.findViewById(R.id.poster_photo);
        }
    }

    public Map getItem(int i) { return md_filtered.get(i);}
    public void setOnItemClickListener(OnListItemClickListener listener) {
        onListItemClickListener=listener;
    }
    public MyRecyclerAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout2,parent,false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.movie_name.setText(md_filtered.get(position).get("name").toString());
        //holder.movie_year.setText(md_filtered.get(position).get("year").toString());
        //holder.poster_img.setImageResource(Integer.parseInt(md_filtered.get(position).get("image").toString()));

        Picasso.get().load(md_filtered.get(position).get("url").toString()).into(holder.poster_img);
        //holder.name.setText(rogerModelArrayList.get(position).getName());

        ViewCompat.setTransitionName(holder.poster_img,md_filtered.get(position).get("name").toString()+"-"+position);
        holder.poster_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onListItemClickListener!=null)
                    onListItemClickListener.onItemLongClick(view,position,md_filtered.get(position).get("id").toString());
                PopupMenu popupMenu = new PopupMenu(context, view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu2, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.duplicate_item:
                                Toast.makeText(context,"toast dup",Toast.LENGTH_SHORT).show();

                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("primaryID",md_filtered.get(position).get("primaryID").toString()+"_new");
                                    jsonObject.put("id",md_filtered.get(position).get("id").toString()+"_new");
                                    jsonObject.put("name",md_filtered.get(position).get("name").toString());
                                    jsonObject.put("description",md_filtered.get(position).get("description").toString());
                                    jsonObject.put("stars",md_filtered.get(position).get("stars").toString());
                                    jsonObject.put("length",md_filtered.get(position).get("length").toString());
                                    jsonObject.put("image",md_filtered.get(position).get("image").toString());
                                    jsonObject.put("year",md_filtered.get(position).get("year").toString());
                                    jsonObject.put("rating",md_filtered.get(position).get("rating").toString());
                                    jsonObject.put("director",md_filtered.get(position).get("director").toString());
                                    jsonObject.put("url",md_filtered.get(position).get("url").toString());
                                }
                                catch (JSONException ex) {
                                    Log.d("MyDebugMsg",ex.getMessage());
                                }
                                //new WorkerDuplicateMovie().execute(jsonObject);
                                //MyUtility.sendHttPostRequest("https://cis651.com/add",jsonObject);

                                return true;
                            case R.id.delete_item:
                                Toast.makeText(context,"toast del",Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
        holder.poster_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onListItemClickListener!=null)
                    onListItemClickListener.onItemClick(view,position,md_filtered.get(position).get("id").toString());
            }
        });
    }
    @Override
    public int getItemCount() { return md_filtered.size(); }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}


