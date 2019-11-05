package com.example.proj;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;


public class MyRecyclerAdapter
        extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<ThingsToDo> mToDoList;
    private Context mContext;
    private RecyclerView myRecyclerView;
    public MyRecyclerAdapter(List<ThingsToDo> myDataset, Context context, RecyclerView recyclerView)
    {
        mToDoList = myDataset;
        mContext = context;
        myRecyclerView = recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_v;
        public TextView time_v;
        public ImageView menu_img;
        public ViewHolder(View view) {
            super(view);
            title_v = (TextView) view.findViewById(R.id.title_view);
            time_v = (TextView) view.findViewById(R.id.time_view);
            menu_img = (ImageView) view.findViewById(R.id.pop_up_menu);
        }
    }
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        final ViewHolder view_holder = new ViewHolder(v);
        return view_holder;
    }
    public void updateList(List<ThingsToDo> myDataset) {
        mToDoList = myDataset;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ThingsToDo c = mToDoList.get(position);
        holder.title_v.setText("Title: " +c.getTitle());
        holder.time_v.setText("Time: " + c.getTime());
        holder.menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete_item:
                                MyDBHelper dbHelper = new MyDBHelper(mContext);
                                dbHelper.deleteContact(c.getId(), mContext);
                                mToDoList.remove(position);
                                myRecyclerView.removeViewAt(position);

                                //Cancel Alarm

                                Intent intent = new Intent(mContext, AlarmReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, c.getRequest_code(), intent, 0);
                                AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                alarmManager.cancel(pendingIntent);

                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, mToDoList.size());
                                notifyDataSetChanged();
                                return true;
                            case R.id.update_item:
                                intent = new Intent(mContext, UpdateReminder.class);
                                intent.putExtra("REMINDER_ID", c.getId());
                                mContext.startActivity(intent);
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    private void goToUpdateActivity(long personId) {
        Intent intent = new Intent(mContext, UpdateReminder.class);
        intent.putExtra("CONTACT_ID",personId);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() { return mToDoList.size(); }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

