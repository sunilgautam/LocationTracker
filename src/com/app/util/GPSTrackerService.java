package com.app.util;

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

    private Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

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
	if(location != null)
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

    public boolean canGetLocation()
    {
	return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location)
    {
	Toast.makeText(this, "Lat => " + location.getLatitude() + "Long => " + location.getLongitude(), Toast.LENGTH_LONG).show();
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