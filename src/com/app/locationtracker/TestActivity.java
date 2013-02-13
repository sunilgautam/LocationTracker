package com.app.locationtracker;

import com.app.util.GPSTrackerService;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class TestActivity extends Activity implements LocationListener
{
    GPSTrackerService gps;
    private TextView latituteField;
    private TextView longitudeField;
    private LocationManager locationManager;
    private String provider;

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_test);
	latituteField = (TextView) findViewById(R.id.TextView02);
	longitudeField = (TextView) findViewById(R.id.TextView04);

//	gps = new GPSTrackerService(TestActivity.this);
	gps = new GPSTrackerService();

	if (gps.canGetLocation())
	{

	    double latitude = gps.getLatitude();
	    double longitude = gps.getLongitude();

	    // \n is for new line
	    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
	}
	else
	{
	    Intent intent = new Intent(TestActivity.this, EnableGPSActivity.class);
	    startActivity(intent);
	}
    }

    @Override
    public void onLocationChanged(Location location)
    {
	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
	// TODO Auto-generated method stub

    }
}