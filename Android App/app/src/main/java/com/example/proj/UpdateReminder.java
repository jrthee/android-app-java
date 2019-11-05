package com.example.proj;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class UpdateReminder extends AppCompatActivity {
    private EditText mDescriptionEditText;
    private TextView mTimeText;
    private Button mSelectTimeBtn;
    private Button mUpdateBtn;
    private MyDBHelper dbHelper;
    private String filter = "";
    private TimePickerDialog timePickerDialog;
    Calendar selected=null;
    private long reminderId;
    private ThingsToDo c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        mDescriptionEditText = (EditText) findViewById(R.id.title_view);
        mTimeText = (TextView) findViewById(R.id.time_view);
        mSelectTimeBtn = (Button) findViewById(R.id.selectTime);
        mSelectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPickerDialog(false);
            }
        });
        mUpdateBtn = (Button) findViewById(R.id.updateNewReminder);
        dbHelper = new MyDBHelper(this);
        try {
            reminderId = getIntent().getLongExtra("REMINDER_ID",1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        c = dbHelper.getContact(reminderId);
        mDescriptionEditText.setText(c.getTitle());
        mTimeText.setText(c.getTime());
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateReminder();
            }
        });
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

    private void updateReminder() {
        String description = mDescriptionEditText.getText().toString().trim();

        if(selected==null){
            Toast.makeText(this,"Please select a time",Toast.LENGTH_SHORT).show();
        }
        ThingsToDo updatedContact = new ThingsToDo(description,selected.getTime().toString(),c.getRequest_code());
        dbHelper.updateContact(reminderId,this,updatedContact);
        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
        intent.putExtra("title",mDescriptionEditText.getText().toString());
        intent.putExtra("time",selected.getTime().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),c.getRequest_code(),intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, selected.getTimeInMillis(),pendingIntent);
        Toast.makeText(getApplicationContext(),"Reminder Updated!",Toast.LENGTH_SHORT).show();
        finish();
    }
}

