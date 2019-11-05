package com.example.proj;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

public class AddNewThing extends AppCompatActivity {
    public static final String PREFS_NAME = "com.example.labassignment8.MY_PREFERENCE_FILE_KEY";
    private EditText mDescriptionEditText;
    private TextView mTimeText;
    private Button mSelectTimeBtn;
    private Button mAddBtn;
    private MyDBHelper dbHelper;
    private String filter = "";
    private TimePickerDialog timePickerDialog;
    Calendar selected=null;
    private int request_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thing);
        mDescriptionEditText = (EditText) findViewById(R.id.title_view);
        mTimeText = (TextView) findViewById(R.id.time_view);
        mAddBtn = (Button) findViewById(R.id.addNewReminder);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });
        mSelectTimeBtn = (Button) findViewById(R.id.selectTime);
        mSelectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPickerDialog(false);
            }
        });
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.request_code=0;
        int rc = settings.getInt("request_code",-1);
        if(rc!=-1){
            this.request_code=rc;
        }
    }

    private void openPickerDialog(boolean is24hour) {
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(
                this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                is24hour);
        timePickerDialog.setTitle("Create Reminder");
        timePickerDialog.show();
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener
            = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE,minute);
            calSet.set(Calendar.SECOND,0);
            calSet.set(Calendar.MILLISECOND,0);
            if(calSet.compareTo(calNow) <= 0){
                calSet.add(Calendar.DATE,1);
            }
            selected=calSet;
            mTimeText.setText(selected.getTime().toString());
        }};

    private void setAlarm(){
        dbHelper = new MyDBHelper(this);
        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
        intent.putExtra("title",mDescriptionEditText.getText().toString());
        intent.putExtra("time",selected.getTime().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), request_code,intent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,selected.getTimeInMillis(),pendingIntent);
        Toast.makeText(getApplicationContext(),"Reminder Added!",Toast.LENGTH_SHORT).show();
        ThingsToDo c = new ThingsToDo(mDescriptionEditText.getText().toString(),selected.getTime().toString(),request_code);
        dbHelper.saveNewContact(c,this);
        finish();
    }
    @Override
    protected void onStop(){
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("request_code",request_code+1);
        editor.commit();
    }
}

