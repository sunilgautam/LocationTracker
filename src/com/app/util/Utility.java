package com.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.app.locationtracker.NotificationInfoActivity;
import com.app.locationtracker.R;
import com.app.pojo.Reminder;

public class Utility
{
    private static final DateFormat utilDateFormatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm a");

    public static String getReminderDate(Date crDate)
    {
	try
	{
	    return utilDateFormatter.format(crDate);
	}
	catch (Exception ex)
	{
	    return "";
	}
    }

    public static String getPriorityName(int value)
    {
	if (value == 1)
	{
	    return "Low";
	}
	else if (value == 2)
	{
	    return "Normal";
	}
	else
	{
	    return "High";
	}
    }

    public static int getPriorityValue(String value)
    {
	if (value.equals("Low"))
	{
	    return 1;
	}
	else if (value.equals("Normal"))
	{
	    return 2;
	}
	else
	{
	    return 3;
	}
    }
    
    public static void createNotification(Context context, int notfId, Reminder reminder)
    {
	PendingIntent contentIntent = PendingIntent
		.getActivity(context, 0, new Intent(context, NotificationInfoActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		.putExtra("notf_reminder", reminder)
		.putExtra("notfId", notfId),
		PendingIntent.FLAG_UPDATE_CURRENT);
	Notification notification = new Notification(R.drawable.icon, reminder.getName(), System.currentTimeMillis());
	notification.setLatestEventInfo(context, reminder.getName(), "(" + Utility.getPriorityName(reminder.getPriority()) + ")" + reminder.getMessage(), contentIntent);

	NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	mNotificationManager.notify(notfId, notification);
    }
}
