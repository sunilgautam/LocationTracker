package com.app.pojo;

public class Reminder
{
    private String name;
    private String locationName;
    private double latitude;
    private double longitude;
    private String message;
    private boolean isSendSMS;
    private String alarmTone;

    public Reminder()
    {

    }

    public Reminder(String name, String locationName, double latitude, double longitude, String message, boolean isSendSMS, String alarmTone)
    {
	this.name = name;
	this.locationName = locationName;
	this.latitude = latitude;
	this.longitude = longitude;
	this.message = message;
	this.isSendSMS = isSendSMS;
	this.alarmTone = alarmTone;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getLocationName()
    {
	return locationName;
    }

    public void setLocationName(String locationName)
    {
	this.locationName = locationName;
    }

    public double getLatitude()
    {
	return latitude;
    }

    public void setLatitude(double latitude)
    {
	this.latitude = latitude;
    }

    public double getLongitude()
    {
	return longitude;
    }

    public void setLongitude(double longitude)
    {
	this.longitude = longitude;
    }

    public String getMessage()
    {
	return message;
    }

    public void setMessage(String message)
    {
	this.message = message;
    }

    public boolean isSendSMS()
    {
	return isSendSMS;
    }

    public void setSendSMS(boolean isSendSMS)
    {
	this.isSendSMS = isSendSMS;
    }

    public String getAlarmTone()
    {
	return alarmTone;
    }

    public void setAlarmTone(String alarmTone)
    {
	this.alarmTone = alarmTone;
    }
}