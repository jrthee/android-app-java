package com.example.proj;

import android.view.View;

public interface OnListItemClickListener {
    void onItemClick(View v, int position, String movie);
    void onItemLongClick(View v, int position, String movie);
}
