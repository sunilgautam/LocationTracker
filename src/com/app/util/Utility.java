package com.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import com.app.locationtracker.DashBoard;
import com.app.locationtracker.NotificationInfoActivity;
import com.app.locationtracker.R;
import com.app.pojo.Reminder;
import com.app.pojo.Setting;

public class Utility
{
    private static final DateFormat utilDateFormatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm a");
    private static final String APP_SET_SNOOZE_TIMEOUT = "app_set_snooze_timeout";
    private static final String APP_SET_ALARM_TONE = "app_set_alarm_tone";
    private static final String APP_SET_VIBRATE = "app_set_vibrate";

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
    
    public static String getSnoozeTimeoutText(int value)
    {
	if (value == 1)
	{
	    return "1 minute";
	}
	else if (value == 5)
	{
	    return "5 minute";
	}
	else if (value == 10)
	{
	    return "10 minute";
	}
	else if (value == 15)
	{
	    return "15 minute";
	}
	else if (value == 20)
	{
	    return "20 minute";
	}
	else if (value == 25)
	{
	    return "25 minute";
	}
	else
	{
	    return "30 minute";
	}
    }
    
    public static String getToneURI(String tone)
    {
	if (tone.equals("Default"))
	{
	    return "DEFAULT";
	}
	else if (tone.equals("Tone 1"))
	{
	    return "android.resource://" + DashBoard.PACKAGE_NAME + "/" + R.raw.tone_1;
	}
	else if (tone.equals("Tone 2"))
	{
	    return "android.resource://" + DashBoard.PACKAGE_NAME + "/" + R.raw.tone_2;
	}
	else if (tone.equals("Tone 3"))
	{
	    return "android.resource://" + DashBoard.PACKAGE_NAME + "/" + R.raw.tone_3;
	}
	else
	{
	    return "DEFAULT";
	}
    }

    public static int getSnoozeTimeoutValue(String value)
    {
	if (value.equals("1 minute"))
	{
	    return 1;
	}
	else if (value.equals("5 minute"))
	{
	    return 5;
	}
	if (value.equals("10 minute"))
	{
	    return 10;
	}
	else if (value.equals("15 minute"))
	{
	    return 15;
	}
	if (value.equals("20 minute"))
	{
	    return 20;
	}
	else if (value.equals("25 minute"))
	{
	    return 25;
	}
	else
	{
	    return 30;
	}
    }
    
    public static Setting getSettings(Context context)
    {
	Setting setting = new Setting();
	SharedPreferences sharedPref = context.getSharedPreferences(DashBoard.SHARED_PREF_KEY, Context.MODE_PRIVATE);
	setting.setSnoozeTimeout(sharedPref.getInt(APP_SET_SNOOZE_TIMEOUT, setting.getDef_SnoozeTimeout()));
	setting.setAlarmTone(sharedPref.getString(APP_SET_ALARM_TONE, setting.getDef_AlarmTone()));
	setting.setVibrate(sharedPref.getBoolean(APP_SET_VIBRATE, setting.isDef_IsVibrate()));
	System.out.println(setting);
	return setting;
    }
    
    public static void setSettings(Context context, Setting setting)
    {
	SharedPreferences sharedPref = context.getSharedPreferences(DashBoard.SHARED_PREF_KEY, Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = sharedPref.edit();
	editor.putInt(APP_SET_SNOOZE_TIMEOUT, setting.getSnoozeTimeout());
	editor.putString(APP_SET_ALARM_TONE, setting.getAlarmTone());
	editor.putBoolean(APP_SET_VIBRATE, setting.isVibrate());
	editor.commit();
    }
    
    public static void createNotification(Context context, int notfId, Reminder reminder)
    {
	Log.d("LOGTAG (CREATE)", "notfId => " + notfId + " reminder => " + reminder.getId());
	Setting setting = getSettings(context);
	
	PendingIntent contentIntent = PendingIntent
		.getActivity(context, 0, new Intent(context, NotificationInfoActivity.class)
		.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		.putExtra("notf_reminder", reminder)
		.putExtra("notfId", notfId)
		.setAction(String.valueOf(notfId)),
		PendingIntent.FLAG_ONE_SHOT);
	Notification notification = new Notification(R.drawable.icon, reminder.getName(), System.currentTimeMillis());
	notification.setLatestEventInfo(context, reminder.getName(), "(" + Utility.getPriorityName(reminder.getPriority()) + ") " + reminder.getMessage(), contentIntent);
	
	// TONE
	String toneURI = getToneURI(setting.getAlarmTone());
	if (toneURI.equals("DEFAULT"))
	{
	    notification.defaults |= Notification.DEFAULT_SOUND;
	}
	else
	{
	    notification.sound = Uri.parse(toneURI);
	}
	
	// TONE VIBRATION
	if (setting.isVibrate())
	{
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
	    //long[] vibrate = {0,200,100,200};
	    //notification.vibrate = vibrate;
	}

	// FLASH LIGHT
	notification.defaults |= Notification.DEFAULT_LIGHTS;
	//notification.ledARGB = 0xffff0000;//red color
	//notification.ledOnMS = 400;
	//notification.ledOffMS = 500;
	//notification.flags |= Notification.FLAG_SHOW_LIGHTS;


	NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	mNotificationManager.notify(notfId, notification);
    }
}
