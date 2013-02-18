package com.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Reminder implements Serializable
{
    private static final long serialVersionUID = -2562258756290965911L;
    private String id;
    private String name;
    private String locationName;
    private double latitude;
    private double longitude;
    private String message;
    private boolean isSendSMS;
    private String alarmTone;
    private ArrayList<Contact> contactList;

    public Reminder()
    {
	setContactList(new ArrayList<Contact>());
    }

    public Reminder(int id, String name, String locationName, String latitude, String longitude, String message, String isSendSMS, String alarmTone, ArrayList<Contact> contactList)
    {
	this.id = String.valueOf(id);
	this.name = name;
	this.locationName = locationName;
	this.latitude = Double.parseDouble(latitude);
	this.longitude = Double.parseDouble(longitude);
	this.message = message;
	this.isSendSMS = Boolean.parseBoolean(isSendSMS);
	this.alarmTone = alarmTone;
	this.contactList = contactList;
    }

    public String getId()
    {
	return id;
    }

    public void setId(String id)
    {
	this.id = id;
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

    public ArrayList<Contact> getContactList()
    {
        return contactList;
    }
    
    public String getContactListCSV()
    {
	if(contactList.size() > 0)
	{
	    String lists = "";
	    for(Contact contact : contactList)
	    {
		lists += contact.getPhone() + ",";
	    }
	    return lists.substring(0, lists.lastIndexOf(","));
	}
	else
	{
	    return null;
	}
    }

    public void setContactList(ArrayList<Contact> contactList)
    {
        this.contactList = contactList;
    }
}