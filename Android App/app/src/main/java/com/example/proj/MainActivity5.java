package com.example.proj;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.makeText;

public class MainActivity5 extends AppCompatActivity {
    RecyclerView recycler_view;
    MyRecyclerAdapter2 MyRecyclerAdapter2;

    final class WorkerDownloadMovieInfo extends AsyncTask<Void,String,String> {
        private final WeakReference<Activity> parentRef;
        private final WeakReference<String> movieRef;
        List<Map<String, ?>> movieItem = new ArrayList<>();
        public WorkerDownloadMovieInfo(final Activity parent, final String _movieID)
        {
            parentRef=new WeakReference<Activity>(parent);
            movieRef=new WeakReference<String>(_movieID);
        }
        @Override
        protected String doInBackground(Void... voids) {
            final String movieID=movieRef.get();
            String result = MyUtility.downloadJSONusingHTTPGetRequest("http://cis651.com/movies/id/'"+movieID+"'");
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
                    movieItem.add(movie);
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MovieDetailFragment movieDetailFragment=MovieDetailFragment.newInstance(movieItem.get(0).get("url").toString(),
                        movieItem.get(0).get("name").toString(),movieItem.get(0).get("year").toString(), movieItem.get(0).get("length").toString(),
                        movieItem.get(0).get("stars").toString(), movieItem.get(0).get("director").toString(),
                        Float.parseFloat(movieItem.get(0).get("rating").toString()),movieItem.get(0).get("description").toString());
                fragmentTransaction.replace(R.id.detailFragment,movieDetailFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                fragmentTransaction.commit();
            }
            catch (JSONException ex) {
                Log.d("MyDebugMsg",ex.getMessage());
            }
        }
    }

    final class WorkerDownloadJSON extends AsyncTask<Void,String,String> {
        private final WeakReference<Activity> parentRef;
        private final WeakReference<RecyclerView> recyclerViewWeakReference;
        List<Map<String,?>> md = new ArrayList<Map<String,?>>();
        public WorkerDownloadJSON(final Activity parent, final RecyclerView recyclerView)
        {
            parentRef=new WeakReference<Activity>(parent);
            recyclerViewWeakReference=new WeakReference<RecyclerView>(recyclerView);
        }
        @Override
        protected String doInBackground(Void... voids) {
            String result = MyUtility.downloadJSONusingHTTPGetRequest("http://cis651.com/movies");
            return result;
        }
        @Override
        protected void onPostExecute(final String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i=0; i<jsonArray.length(); i++) {
                    HashMap movie = new HashMap();
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    Iterator<?> keys = jsonObject2.keys();
                    while(keys.hasNext() ){
                        String key = (String)keys.next();
                        String value = jsonObject2.getString(key);
                        movie.put(key, value);
                    }
                    md.add(movie);
                }

                MyRecyclerAdapter2 = new MyRecyclerAdapter2(MainActivity5.this,md);
                MyRecyclerAdapter2.setOnItemClickListener(new OnListItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position, String movie) {
                        new WorkerDownloadMovieInfo(MainActivity5.this,movie).execute();
                    }

                    @Override
                    public void onItemLongClick(View v, int position, String movie) {
                        new WorkerDownloadMovieInfo(MainActivity5.this,movie).execute();
                    }
                });
                recycler_view.setAdapter(MyRecyclerAdapter2);
                recycler_view.setItemAnimator(new DefaultItemAnimator());
            }
            catch (JSONException ex) {
                Log.d("MyDebugMsg",ex.getMessage());
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        recycler_view = (RecyclerView)findViewById(R.id.mainRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recycler_view.setLayoutManager(layoutManager);
        new WorkerDownloadJSON(this,recycler_view).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu2, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast toast = makeText(getApplicationContext(), "Query Text=" + query, Toast.LENGTH_SHORT);
                //toast.show();
                MyRecyclerAdapter2.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //myRecyclerAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}


