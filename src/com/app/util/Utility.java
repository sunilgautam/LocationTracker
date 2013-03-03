package com.app.util;

import java.io.File;
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
    private static final String LOGTAG = Utility.class.getName();
    private static final DateFormat utilDateFormatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm a");
    private static final String APP_SET_SNOOZE_TIMEOUT = "app_set_snooze_timeout";
    private static final String APP_SET_ALARM_TONE = "app_set_alarm_tone";
    private static final String APP_SET_ALARM_URI = "app_set_alarm_uri";
    private static final String APP_SET_ALARM_PATH = "app_set_alarm_path";
    private static final String APP_SET_VIBRATE = "app_set_vibrate";
    private static final String APP_SET_PASSWORD = "app_set_password";
    public static String PACKAGE_NAME;
    public static String SHARED_PREF_KEY;

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
    
    public static int getPriorityIndex(int value)
    {
	if (value == 1)
	{
	    return 2;
	}
	else if (value == 2)
	{
	    return 1;
	}
	else
	{
	    return 0;
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
	    return "android.resource://" + PACKAGE_NAME + "/" + R.raw.tone_1;
	}
	else if (tone.equals("Tone 2"))
	{
	    return "android.resource://" + PACKAGE_NAME + "/" + R.raw.tone_2;
	}
	else if (tone.equals("Tone 3"))
	{
	    return "android.resource://" + PACKAGE_NAME + "/" + R.raw.tone_3;
	}
	else
	{
	    return "User Defined";
	}
    }
    
    public static boolean isToneUserDefined(String tone)
    {
	if (tone.equals("User defined"))
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }
    
    public static Setting getSettings(Context context)
    {
	Setting setting = new Setting();
	SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
	setting.setSnoozeTimeout(sharedPref.getInt(APP_SET_SNOOZE_TIMEOUT, setting.getDef_SnoozeTimeout()));
	setting.setAlarmTone(sharedPref.getString(APP_SET_ALARM_TONE, setting.getDef_AlarmTone()));
	setting.setAlarmUri(sharedPref.getString(APP_SET_ALARM_URI, null));
	setting.setAlarmPath(sharedPref.getString(APP_SET_ALARM_PATH, null));
	setting.setVibrate(sharedPref.getBoolean(APP_SET_VIBRATE, setting.isDef_IsVibrate()));
	setting.setPassWord(sharedPref.getString(APP_SET_PASSWORD, setting.getDef_PassWord()));
	
	Log.d(LOGTAG, setting.toString());
	return setting;
    }
    
    public static void setSettings(Context context, Setting setting)
    {
	Log.d(LOGTAG, "SAVING SETTINGS");
	Log.d(LOGTAG, setting.toString());
	SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = sharedPref.edit();
	editor.putInt(APP_SET_SNOOZE_TIMEOUT, setting.getSnoozeTimeout());
	editor.putString(APP_SET_ALARM_TONE, setting.getAlarmTone());
	editor.putString(APP_SET_ALARM_URI, setting.getAlarmUri());
	editor.putString(APP_SET_ALARM_PATH, setting.getAlarmPath());
	editor.putBoolean(APP_SET_VIBRATE, setting.isVibrate());
	editor.commit();
    }
    
    public static void setPassword(Context context, String pWord)
    {
	Log.d(LOGTAG, "SAVING PASSWORD");
	SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
	SharedPreferences.Editor editor = sharedPref.edit();
	editor.putString(APP_SET_PASSWORD, pWord);
	editor.commit();
    }
    
    public static boolean validateUser(Context context, String pWord)
    {
	Log.d(LOGTAG, "VALIDATING USER ...");
	
	try
	{
	    Setting setting = getSettings(context);
	    if (setting.getPassWord().equals(pWord))
	    {
		Log.d(LOGTAG, "VALID USER");
		return true;
	    }
	    else
	    {
		Log.d(LOGTAG, "INVALID USER");
		return false;
	    }
	}
	catch (Exception ex)
	{
	    Log.d(LOGTAG, "ERROR WHILE VALIDATING USER");
	    return false;
	}
    }
    
    public static void createNotification(Context context, int notfId, Reminder reminder)
    {
	Log.d(LOGTAG, "CREATING NOTIFICATION (" + notfId + ")");
	Log.d(LOGTAG, reminder.toString());
	
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
	else if (Utility.isToneUserDefined(setting.getAlarmTone()))
	{
	    Uri fileURI = Uri.parse(setting.getAlarmUri());
		
	    File toneFile = new File(setting.getAlarmPath());
	    if (toneFile.exists())
	    {
		notification.sound = fileURI;
	    }
	    else
	    {
		Log.d(LOGTAG, "USER DEFINED TONE NOT FOUND");
		notification.defaults |= Notification.DEFAULT_SOUND;
	    }
	}
	else
	{
	    notification.sound = Uri.parse(toneURI);
	}
	
	// VIBRATION
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
