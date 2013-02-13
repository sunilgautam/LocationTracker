package com.app.locationtracker;

import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class EnableGPSActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_enable_gps);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        checkGPSServiceStatus();
    }

    public void btnOkClick(View view)
    {
	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	startActivity(intent);
    }
    
    public void checkGPSServiceStatus()
    {
	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	if (enabled)
	{
	    finish();
	}
    }
}
