package com.example.proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieData {

    List<Map<String,?>> moviesList;

    public List<Map<String, ?>> getMoviesList() {
        return moviesList;
    }

    public int getSize(){
        return moviesList.size();
    }

    public HashMap getItem(int i){
        if (i >=0 && i < moviesList.size()){
            return (HashMap) moviesList.get(i);
        } else return null;
    }

    public MovieData(){
        moviesList = new ArrayList<Map<String,?>>();
        moviesList.add(createMovie("Red", R.drawable.red));
        moviesList.add(createMovie("Orange", R.drawable.orange));
        moviesList.add(createMovie("Yellow", R.drawable.yellow));
        moviesList.add(createMovie("Green", R.drawable.green));
        moviesList.add(createMovie("Blue", R.drawable.blue));
        moviesList.add(createMovie("Purple", R.drawable.purple));
        moviesList.add(createMovie("Pink", R.drawable.pink));
        moviesList.add(createMovie("Black", R.drawable.black));
        moviesList.add(createMovie("Brown", R.drawable.brown));
        moviesList.add(createMovie("Grey", R.drawable.grey));
    }

    private HashMap createMovie(String name, int image) {
        HashMap movie = new HashMap();
        movie.put("image",image);
        movie.put("name", name);
        movie.put("selection",false);
        return movie;
    }
}


