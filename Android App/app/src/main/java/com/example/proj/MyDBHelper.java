package com.example.proj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "Things";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_REQUEST_CODE = "request_code";

    public MyDBHelper(Context context) {super(context,DATABASE_NAME,null,DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_TIME + " TEXT NOT NULL, " +
                COLUMN_REQUEST_CODE + " INTEGER NOT NULL);"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    public void saveNewContact(ThingsToDo thingsToDo, Context context){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,thingsToDo.getTitle());
        values.put(COLUMN_TIME,thingsToDo.getTime());
        values.put(COLUMN_REQUEST_CODE,thingsToDo.getRequest_code());
        db.insert(TABLE_NAME,null,values);
        db.close();
        Toast.makeText(context,"Added successfully.",Toast.LENGTH_SHORT).show();
    }
    public List<ThingsToDo> contactList(String filter) {
        String query;
        if(filter.equals("")){
            query="SELECT * FROM " + TABLE_NAME;
        } else {
            query="SELECT * FROM " + TABLE_NAME + " ORDER BY " + filter;
        }
        List<ThingsToDo> contactLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        ThingsToDo c;

        if (cursor.moveToFirst()) {
            do {
                c = new ThingsToDo();
                c.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                c.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                c.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
                c.setRequest_code(cursor.getInt(cursor.getColumnIndex(COLUMN_REQUEST_CODE)));
                contactLinkedList.add(c);
            } while (cursor.moveToNext());
        }
        return contactLinkedList;
    }

    public ThingsToDo getContact(long id) {
        SQLiteDatabase db=this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id="+ id;
        Cursor cursor = db.rawQuery(query,null);
        ThingsToDo c = new ThingsToDo();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            c.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            c.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
            c.setRequest_code(cursor.getInt(cursor.getColumnIndex(COLUMN_REQUEST_CODE)));
        }
        return c;
    }

    public void deleteContact(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id="+id);
        Toast.makeText(context,"Deleted successfully.",Toast.LENGTH_SHORT).show();
    }
    public void updateContact(long contactId, Context context, ThingsToDo thingsToDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET title ='"+ thingsToDo.getTitle() +
                "', time='" +thingsToDo.getTime() + "', request_code = '"+thingsToDo.getRequest_code() +
                "' WHERE _id='"+ contactId + "'");
        Toast.makeText(context,"Updated successfully.",Toast.LENGTH_SHORT).show();
    }
}

