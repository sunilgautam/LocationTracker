package com.app.locationtracker;

import java.util.Calendar;
import com.app.pojo.Reminder;
import com.app.util.Utility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationInfoActivity extends Activity
{
    private int notfId;
    private Reminder reminder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_notification_info);
	Bundle bundle = getIntent().getExtras();
	notfId = bundle.getInt("notfId", 0);
	reminder = (Reminder) bundle.getSerializable("notf_reminder");

	TextView tv = (TextView) findViewById(R.id.tvReminderDetails);
	if (reminder != null)
	{
	    tv.setText(String.format(getResources().getString(R.string.msg_rem_details), reminder.getLocationName(), reminder.getLatitude(), reminder.getLongitude(), reminder.getMessage(), reminder.isSendSMS() ? "Yes" : "No", reminder.getContactListCSV(), Utility.getPriorityName(reminder.getPriority())));
	}

	Button btnSnooze = (Button) findViewById(R.id.btnSnooze);
	Button btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
	Button btnCancel = (Button) findViewById(R.id.btnCancel);

	btnSnooze.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// CLOSE NOTIFICATION, SNOOZE NOTIFICATION AND FINISH
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(notfId);
		snoozeNotification();
		Toast.makeText(getApplicationContext(), String.format(getString(R.string.msg_rem_snooz).toString(), 1), Toast.LENGTH_SHORT).show();
		finish();
	    }
	});

	btnSendSMS.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// SEND SMS ,CLOSE NOTIFICATION AND FINISH

		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(notfId);
		finish();
	    }
	});

	btnCancel.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// CLOSE NOTIFICATION AND FINISH
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(notfId);
		finish();
	    }
	});
    }

    public void snoozeNotification()
    {

	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.SECOND, 5);

	Intent intent = new Intent(this, NotificationReceiver.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	      .putExtra("notf_reminder", reminder)
	      .putExtra("notfId", notfId);

	PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notfId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	
	alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

}