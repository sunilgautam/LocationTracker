package com.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reminder implements Serializable
{
    private static final long serialVersionUID = -2562258756290965911L;
    private String id;
    private String name;
    private String locationName;
    private double latitude;
    private double longitude;
    private String message;
    private int priority;
    private boolean isSendSMS;
    private List<Contact> contactList;
    private String crDate;

    public Reminder()
    {
	setContactList(new ArrayList<Contact>());
    }

    public Reminder(int id, String name, String locationName, String latitude, String longitude, String message, String isSendSMS, String contactListCSV, int priority, String crDate)
    {
	this.id = String.valueOf(id);
	this.name = name;
	this.locationName = locationName;
	this.latitude = Double.parseDouble(latitude);
	this.longitude = Double.parseDouble(longitude);
	this.message = message;
	this.isSendSMS = Boolean.parseBoolean(isSendSMS);
	this.contactList = new ArrayList<Contact>();
	setContactListCSV(contactListCSV);
	this.priority = priority;
	this.crDate = crDate;
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

    public int getPriority()
    {
	return priority;
    }

    public void setPriority(int priority)
    {
	this.priority = priority;
    }

    public boolean isSendSMS()
    {
	return isSendSMS;
    }

    public void setSendSMS(boolean isSendSMS)
    {
	this.isSendSMS = isSendSMS;
    }

    public List<Contact> getContactList()
    {
	return contactList;
    }

    public String getContactListCSV()
    {
	if (contactList.size() > 0)
	{
	    String lists = "";
	    for (Contact contact : contactList)
	    {
		lists += contact.getPhone() + ",";
	    }
	    return lists.substring(0, lists.lastIndexOf(","));
	}
	else
	{
	    return "";
	}
    }

    public void setContactList(List<Contact> contactList)
    {
	this.contactList = contactList;
    }

    public void setContactListCSV(String strContactList)
    {
	if (strContactList != null && !strContactList.equals(""))
	{
	    String[] contLists = strContactList.split(",");

	    if (contLists.length > 0)
	    {
		for (String contact : contLists)
		{
		    this.contactList.add(new Contact("", contact));
		}
	    }
	}
    }

    public String getCrDate()
    {
	return crDate;
    }

    public void setCrDate(String crDate)
    {
	this.crDate = crDate;
    }
}