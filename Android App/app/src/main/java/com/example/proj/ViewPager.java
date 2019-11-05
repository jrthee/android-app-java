package com.example.proj;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewPager extends FragmentActivity {
    public class DepthPageTransformer implements android.support.v4.view.ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 0) {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleY(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        }
    }
    CollectionPagerAdapter mCollectionPageAdapter;
    android.support.v4.view.ViewPager mViewPager;
    private List<Movie> movie_list = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_layout);

        movie_list.add(new Movie("Apple", R.drawable.apple));
        movie_list.add(new Movie("Banana", R.drawable.banana));
        movie_list.add(new Movie("Broccoli", R.drawable.broccoli));
        movie_list.add(new Movie("Carrot", R.drawable.carrot));
        movie_list.add(new Movie("Corn", R.drawable.corn));
        movie_list.add(new Movie("Grapes", R.drawable.grapes));
        movie_list.add(new Movie("Lettuce", R.drawable.lettuce));
        movie_list.add(new Movie("Pepper", R.drawable.pepper));
        movie_list.add(new Movie("Strawberry", R.drawable.strawberry));
        movie_list.add(new Movie("Watermelon", R.drawable.watermelon));

        mCollectionPageAdapter = new CollectionPagerAdapter(getSupportFragmentManager(),movie_list);
        mViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPageAdapter);
        mViewPager.setPageTransformer(true,new DepthPageTransformer());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
}


