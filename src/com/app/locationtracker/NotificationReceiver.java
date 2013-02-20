package com.app.locationtracker;

import com.app.pojo.Reminder;
import com.app.util.Utility;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver
{
    public final static String LOGTAG = NotificationReceiver.class.getName();
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
	Bundle bundle = intent.getExtras();
	int notfId = bundle.getInt("notfId", 0);
	Reminder reminder = (Reminder) bundle.getSerializable("notf_reminder");
	if (reminder != null)
	{
	    Utility.createNotification(context, notfId, reminder);
	}
	else
	{
	    Log.e(LOGTAG, "No reminder object");
	}
    }
    
}
