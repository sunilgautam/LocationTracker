package com.app.locationtracker;

import com.app.util.GPSTrackerService;
import com.app.widget.MessageDialog;
import android.location.LocationManager;
import android.os.Bundle;
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
	if (!isGPSServiceRunning())
	{
	    startService(new Intent(this, GPSTrackerService.class));
	}
	else
	{
	    Toast.makeText(this, "My Service already Started", Toast.LENGTH_LONG).show();
	}
	checkGPSServiceStatus();
    }

    private boolean isGPSServiceRunning()
    {
	try
	{
	    System.out.println(GPSTrackerService.class.getName());
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
	    {
		if (GPSTrackerService.class.getName().equals(service.service.getClassName()))
		{
		    return true;
		}
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
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
	try
	{
	    LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
	    boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    if (!enabled)
	    {
		Intent intent = new Intent(DashBoard.this, EnableGPSActivity.class);
		startActivity(intent);
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
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
	    dialog = new MessageDialog(getResources().getString(R.string.msg_err_open_act), getResources().getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
	}
    }

    public void btnReminderClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, RemindersActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(getResources().getString(R.string.msg_err_open_act), getResources().getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
	}
    }

    public void btnHistoryClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, HistoryActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(getResources().getString(R.string.msg_err_open_act), getResources().getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
	}
    }

    public void btnAboutClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, AboutActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(getResources().getString(R.string.msg_err_open_act), getResources().getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
	}
    }
}
