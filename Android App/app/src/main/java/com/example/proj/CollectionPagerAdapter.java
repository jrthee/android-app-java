package com.example.proj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    List<Movie> list_movie;
    public CollectionPagerAdapter(FragmentManager fm, List<Movie> list_m) {
        super(fm);
        this.list_movie=list_m;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle args = new Bundle();
        args.putString(ObjectFragment.ARG_TITLE, list_movie.get(i).getName());
        args.putInt(ObjectFragment.ARG_POSTER, list_movie.get(i).getPoster_id());
        Fragment fragment = new ObjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() { return list_movie.size();}

    @Override
    public CharSequence getPageTitle(int position) {
        return list_movie.get(position).getName();
    }
}
