package com.app.util;

import java.util.List;
import com.app.db.ReminderDBHelper;
import com.app.locationtracker.NotificationInfoActivity;
import com.app.locationtracker.NotificationReceiver;
import com.app.locationtracker.R;
import com.app.pojo.Reminder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GPSTrackerService extends Service implements LocationListener
{
    private NotificationManager mNotificationManager;
    public final static String LOGTAG = GPSTrackerService.class.getName();
    private Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // The range for notifications
    private static final long NOTIFICATION_RANGE = 100; // 100 meters

    // Declaring a Location Manager
    protected LocationManager locationManager;

    private static List<Reminder> reminderList = null;

    public Location getLocation()
    {
	try
	{
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
		    Log.d("Network", "Network");
		    if (locationManager != null)
		    {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null)
			{
			    latitude = location.getLatitude();
			    longitude = location.getLongitude();
			}
		    }
		}
		// if GPS Enabled get lat/long using GPS Services
		if (isGPSEnabled)
		{
		    if (location == null)
		    {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
			Log.d("GPS Enabled", "GPS Enabled");
			if (locationManager != null)
			{
			    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			    if (location != null)
			    {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			    }
			}
		    }
		}
	    }

	}
	catch (Exception e)
	{
	    System.out.println("<==###### ERROR ######==>" + e.getStackTrace());
	    return null;
	}
	if (location != null)
	{
	    Toast.makeText(this, "Lat => " + location.getLatitude() + "Long => " + location.getLongitude(), Toast.LENGTH_LONG).show();
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
	ReminderDBHelper db = new ReminderDBHelper(this);
	reminderList = db.getAllReminders();
    }

    public void resetReminders()
    {
	// RESET REMINDERS
	reminderList = null;
    }

    public boolean canGetLocation()
    {
	return this.canGetLocation;
    }

    @Override
    public synchronized void onLocationChanged(Location location)
    {
	Toast.makeText(this, "Lat => " + location.getLatitude() + "Long => " + location.getLongitude(), Toast.LENGTH_LONG).show();
	getReminders();
	if (reminderList != null)
	{
	    for (Reminder reminder : reminderList)
	    {
		if (isLocationInRange(location.getLatitude(), location.getLongitude(), reminder.getLatitude(), reminder.getLongitude()))
		{
		    // REMINDER LOCATION IS IN RANGE => SHOW NOTIFICATION
		    Log.d(LOGTAG, "Creating notification");
		    Utility.createNotification(GPSTrackerService.this ,Integer.parseInt(reminder.getId()), reminder);
		}
	    }
	}
	else
	{
	    Log.d(LOGTAG, "No reminderList");
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
	getLocation();
	super.onStartCommand(intent, flags, startId);
	return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
	return null;
    }

}