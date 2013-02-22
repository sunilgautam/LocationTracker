package com.app.pojo;

import com.app.util.Utility;

public class Setting
{
    private int snoozeTimeout;
    private String alarmTone;
    private String alarmUri;
    private String alarmPath;
    private boolean isVibrate;
    
    private int def_SnoozeTimeout = 5;
    private String def_AlarmTone = "Default";
    private boolean def_IsVibrate = true;

    public Setting()
    {

    }

    public Setting(int snoozeTimeout, String alarmTone, boolean isVibrate, String alarmUri, String alarmPath)
    {
	this.snoozeTimeout = snoozeTimeout;
	this.alarmTone = alarmTone;
	this.isVibrate = isVibrate;
	this.alarmUri = alarmUri;
	this.alarmPath = alarmPath;
    }

    public int getSnoozeTimeout()
    {
	return snoozeTimeout;
    }

    public void setSnoozeTimeout(int snoozeTimeout)
    {
	this.snoozeTimeout = snoozeTimeout;
    }

    public String getAlarmTone()
    {
	return alarmTone;
    }

    public void setAlarmTone(String alarmTone)
    {
	this.alarmTone = alarmTone;
	if (!Utility.isToneUserDefined(this.alarmTone))
	{
	    this.alarmUri = null;
	    this.alarmPath = null;
	}
    }
    
    public String getAlarmUri()
    {
        return alarmUri;
    }

    public void setAlarmUri(String alarmUri)
    {
        this.alarmUri = alarmUri;
    }

    public String getAlarmPath()
    {
        return alarmPath;
    }

    public void setAlarmPath(String alarmPath)
    {
        this.alarmPath = alarmPath;
    }

    public boolean isVibrate()
    {
	return isVibrate;
    }

    public void setVibrate(boolean isVibrate)
    {
	this.isVibrate = isVibrate;
    }

    public int getDef_SnoozeTimeout()
    {
        return def_SnoozeTimeout;
    }

    public void setDef_SnoozeTimeout(int def_SnoozeTimeout)
    {
        this.def_SnoozeTimeout = def_SnoozeTimeout;
    }

    public String getDef_AlarmTone()
    {
        return def_AlarmTone;
    }

    public void setDef_AlarmTone(String def_AlarmTone)
    {
        this.def_AlarmTone = def_AlarmTone;
    }

    public boolean isDef_IsVibrate()
    {
        return def_IsVibrate;
    }

    public void setDef_IsVibrate(boolean def_IsVibrate)
    {
        this.def_IsVibrate = def_IsVibrate;
    }
    
    @Override
    public String toString()
    {
        return "SETTING => TONE => " + this.alarmTone + " | TONE_URI => " + this.alarmUri + " | TONE_PATH => " + this.alarmPath + " | IS_VIBRATE => " + this.isVibrate + " | SNOOZE_TIMEOUT => " + this.snoozeTimeout;
    }
}
