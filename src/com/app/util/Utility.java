package com.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
