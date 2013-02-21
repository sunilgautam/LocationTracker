package com.app.pojo;

public class Setting
{
    private int snoozeTimeout;
    private String alarmTone;
    private boolean isVibrate;
    
    private int def_SnoozeTimeout = 5;
    private String def_AlarmTone = "Default";
    private boolean def_IsVibrate = true;

    public Setting()
    {

    }

    public Setting(int snoozeTimeout, String alarmTone, boolean isVibrate)
    {
	this.snoozeTimeout = snoozeTimeout;
	this.alarmTone = alarmTone;
	this.isVibrate = isVibrate;
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
        return "SETTING => T => " + this.alarmTone + " | V => " + this.isVibrate + " | T => " + this.snoozeTimeout;
    }
}
