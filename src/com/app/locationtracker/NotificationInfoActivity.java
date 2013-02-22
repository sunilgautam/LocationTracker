package com.app.locationtracker;

import java.util.Calendar;
import java.util.List;
import com.app.pojo.Contact;
import com.app.pojo.Reminder;
import com.app.pojo.Setting;
import com.app.util.Utility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationInfoActivity extends Activity
{
    private int notfId;
    private Reminder reminder = null;
    private Setting setting = null;
    private static final String LOG_TAG = NotificationInfoActivity.class.getName();

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
	    tv.setText(Html.fromHtml(String.format(getResources().getString(R.string.msg_rem_noti_details), reminder.getName(), reminder.getLocationName(), reminder.getLatitude(), reminder.getLongitude(), reminder.getMessage(), reminder.isSendSMS() ? "Yes" : "No", reminder.getContactListCSV(), Utility.getPriorityName(reminder.getPriority()))));
	}
	
	setting = Utility.getSettings(getBaseContext());

	Button btnSnooze = (Button) findViewById(R.id.btnSnooze);
	Button btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
	if (reminder != null && !reminder.isSendSMS())
	{
	    btnSendSMS.setEnabled(false);
	}
	Button btnCancel = (Button) findViewById(R.id.btnCancel);

	btnSnooze.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// CLOSE NOTIFICATION, SNOOZE NOTIFICATION AND FINISH
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(notfId);
		snoozeNotification();
		Toast.makeText(getApplicationContext(), String.format(getString(R.string.msg_rem_snooz).toString(), setting.getSnoozeTimeout()), Toast.LENGTH_SHORT).show();
		finish();
	    }
	});

	btnSendSMS.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// SEND SMS ,CLOSE NOTIFICATION AND FINISH
		sendSMS();
		Toast.makeText(getApplicationContext(), getString(R.string.msg_rem_sms_success).toString(), Toast.LENGTH_SHORT).show();
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
		Log.d(LOG_TAG, "NOTIFICATION CANCELED (" + notfId + ")");
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(notfId);
		finish();
	    }
	});
    }

    public void snoozeNotification()
    {
	Log.d(LOG_TAG, "SNOOZING NOTIFICATION ...");
	Log.d(LOG_TAG, reminder.toString());
	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MINUTE, setting.getSnoozeTimeout());

	Intent intent = new Intent(this, NotificationReceiver.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	      .putExtra("notf_reminder", reminder)
	      .putExtra("notfId", notfId)
	      .setAction(String.valueOf(notfId));

	PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notfId, intent, PendingIntent.FLAG_ONE_SHOT);
	
	alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
    
    public void sendSMS()
    {
	try
	{
	    Log.d(LOG_TAG, "SENDING MESSAGE");
	    SmsManager sms = SmsManager.getDefault();

    	    List<String> messages = sms.divideMessage(this.reminder.getMessage());
    	        
    	    for (Contact contact : this.reminder.getContactList())
    	    {
    		for (String message : messages)
    	        {
    		    Log.d(LOG_TAG, contact.getPhone());
    		    sms.sendTextMessage(contact.getPhone(), null, message, null, null);
    	        }
    	    }
    	Log.d(LOG_TAG, "MESSAGE SENT");
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

}