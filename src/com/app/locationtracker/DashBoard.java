package com.app.locationtracker;

import com.app.exception.BaseException;
import com.app.util.GPSTrackerService;
import com.app.util.TestService;
import com.app.widget.MessageDialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class DashBoard extends Activity
{
    MessageDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_dash_board);
	if(!isMyServiceRunning())
	{
	    startService(new Intent(this, GPSTrackerService.class));
	}
	else
	{
	    Toast.makeText(this, "My Service already Started", Toast.LENGTH_LONG).show();
	}
	checkGPSServiceStatus();
    }

    private boolean isMyServiceRunning()
    {
	ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
	{
	    if ("com.app.util.GPSTrackerService".equals(service.service.getClassName()))
	    {
		return true;
	    }
	}
	return false;
    }

    @Override
    protected void onResume()
    {
	super.onResume();
    }

    public void checkGPSServiceStatus()
    {
	LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	if (!enabled)
	{
	    Intent intent = new Intent(DashBoard.this, EnableGPSActivity.class);
	    startActivity(intent);
	}
    }

    public void btnAddLocationClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, AddLocationActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(new BaseException(ex, 101), DashBoard.this);
	}
    }

    public void btnReminderClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, ShowMapActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(new BaseException(ex, 101), DashBoard.this);
	}
    }

    public void btnHistoryClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, DragMarkerActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(new BaseException(ex, 101), DashBoard.this);
	}
    }

    public void btnAboutClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, TestActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(new BaseException(ex, 101), DashBoard.this);
	}
    }
}
