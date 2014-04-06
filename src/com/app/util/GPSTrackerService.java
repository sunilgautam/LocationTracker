package com.app.util;

import java.util.List;
import com.app.db.ReminderDBHelper;
import com.app.pojo.Reminder;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTrackerService extends Service implements LocationListener
{
    public final static String LOGTAG = GPSTrackerService.class.getName();
    private ReminderDBHelper db = new ReminderDBHelper(this);
    private Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    // THE MINIMUM DISTANCE TO CHANGE UPDATES IN METERS
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // THE MINIMUM TIME BETWEEN UPDATES IN MILLISECONDS
    private static final long MIN_TIME_BW_UPDATES = 0; // 1000 * 60 * 1 => 1 minute

    // THE RANGE FOR NOTIFICATIONS
    private static final long NOTIFICATION_RANGE = 300; // 300 meters

    // MAXIMUM THRESHOLD TIME BETWEEN UPDATES
    public static final long LOCATION_UPDATE_MAX_DELTA_THRESHOLD = 1000 * 60 * 5;

    // DECLARING A LOCATION MANAGER
    protected LocationManager locationManager;

    private static List<Reminder> reminderList = null;
    
    public GPSTrackerService()
    {
	super();
    }

    public Location registerLocationListner()
    {
	try
	{
	    Location tempLocation = null;
	    locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

	    // getting GPS status
	    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    // getting network status
	    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	    if (!isGPSEnabled && !isNetworkEnabled)
	    {
		// no network provider is enabled
	    }
	    else
	    {
		this.canGetLocation = true;
		if (isNetworkEnabled)
		{
		    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		    Log.d(LOGTAG, "Network Enabled");
		    if (locationManager != null)
		    {
			tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    }
		}
		// if GPS Enabled get lat/long using GPS Services
		if (isGPSEnabled)
		{
		    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		    Log.d(LOGTAG, "GPS Enabled");
		    if (locationManager != null)
		    {
			tempLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    }
		}
	    }
	    updateLocation(tempLocation);
	}
	catch (Exception e)
	{
	    Log.e(LOGTAG, e.getStackTrace().toString());
	    return null;
	}
	if (location != null)
	{
	    Log.d(LOGTAG, "INIT Lat => " + location.getLatitude() + "Long => " + location.getLongitude());
	}
	
	return location;
    }

    public void stopUsingGPS()
    {
	if (locationManager != null)
	{
	    locationManager.removeUpdates(GPSTrackerService.this);
	}
    }

    public double getLatitude()
    {
	if (location != null)
	{
	    latitude = location.getLatitude();
	}
	return latitude;
    }

    public double getLongitude()
    {
	if (location != null)
	{
	    longitude = location.getLongitude();
	}
	return longitude;
    }

    public boolean isLocationInRange(double startLatE6, double startLongE6, double endLatE6, double endLongE6)
    {

	float[] result = new float[5];
	Location.distanceBetween(startLatE6, startLongE6, endLatE6, endLongE6, result);
	Log.d(LOGTAG, "Distance => " + result[0]);
	if (result[0] <= NOTIFICATION_RANGE)
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }

    public void getReminders()
    {
	// GET REMINDERS
	if (db == null)
	{
	    db = new ReminderDBHelper(this);
	}
	reminderList = db.getAllRemindersByPriority();
	
    }

    public void resetReminders()
    {
	// RESET REMINDERS
	reminderList = null;
    }
    
    public void moveToHistory(Reminder reminder)
    {
	// MOVE TO HISTORY
	
	if (db == null)
	{
	    db = new ReminderDBHelper(this);
	}
	db.setReminderDone(reminder);
	Log.d(LOGTAG, "Reminder moved to history");
    }
    
    public void changeSnoozeState(Reminder reminder)
    {
	// CHANGE SNOOZE STATE
	
	if (db == null)
	{
	    db = new ReminderDBHelper(this);
	}
	db.setReminderSnoozing(reminder, true);
	Log.d(LOGTAG, "Reminder state changed to snoozing");
    }

    public boolean canGetLocation()
    {
	return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location)
    {
	Log.d(LOGTAG, "Location changed, checking for accuracy ...");
	updateLocation(location);
    }
    
    public void updateLocation(Location newLocation)
    {
	Log.d(LOGTAG, "Old location => " + location);
	Log.d(LOGTAG, "New location => " + newLocation);
	// CASES WHERE WE ONLY HAVE ONE OR THE OTHER.
	if (newLocation != null && location == null)
	{
	    Log.d(LOGTAG, "Last location null");
	    this.location = newLocation;
	    return;
	}
	else if (newLocation == null)
	{
	    Log.d(LOGTAG, "updated location is null");
	    return;
	}
	
        doLocationChangedAction(newLocation);
   }
   
    synchronized public void doLocationChangedAction(Location location)
    {
	if (location != null)
	{
	    Log.d(LOGTAG, "here => 1 2");
	    this.location = location;
	    getReminders();
	    Log.d(LOGTAG, "Location changed => " + location.getLatitude() + " " + location.getLongitude());
	    if (reminderList != null)
	    {
		Log.d(LOGTAG, "Checking distances from reminder locations ...");
		Log.d(LOGTAG, "No of reminders (" + reminderList.size() + ")");
		for (Reminder reminder : reminderList)
		{
		    if (isLocationInRange(location.getLatitude(), location.getLongitude(), reminder.getLatitude(), reminder.getLongitude()))
		    {
			// REMINDER LOCATION IS IN RANGE => SHOW NOTIFICATION
			Log.d(LOGTAG, "Reminder location is in range => Moving reminder to snoozing state => Creating notification");
			//moveToHistory(reminder);
			changeSnoozeState(reminder);
		    
			Utility.createNotification(GPSTrackerService.this ,Integer.parseInt(reminder.getId()), reminder);
		    }
		}
	    
		resetReminders();
	    }
	    else
	    {
		Log.d(LOGTAG, "reminderList is null");
	    }
	}
    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
	this.mContext = getBaseContext();
	registerLocationListner();
	super.onStartCommand(intent, flags, startId);
	return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
	return null;
    }

}