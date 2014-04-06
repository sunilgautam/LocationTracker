package com.app.locationtracker;

import com.app.util.GPSTrackerService;
import com.app.widget.MessageDialog;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DashBoard extends Activity
{
    public final static String LOGTAG = DashBoard.class.getName();
    MessageDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_dash_board);

	if (isInternetConnected())
	{
	    checkGPSServiceStatus();
	}
	else
	{
	    // NO INTERNET CONNECTION
	    Log.d(LOGTAG, "NO INTERNET CONNECTION");
	    AlertDialog alertDialog = new AlertDialog.Builder(DashBoard.this)
	    				.setTitle(getString(R.string.msg_dialog_internet_err_title))
	    				.setMessage(getString(R.string.msg_dialog_internet_err))
	    				.setIcon(R.drawable.error_icon)
	    				.create();

	    alertDialog.setButton("OK", new DialogInterface.OnClickListener()
	    {
		public void onClick(DialogInterface dialog, int which)
		{
		    // CLOSE APPLICATION
		    finish();
		}
	    });

	    alertDialog.show();
	}
	
	// CHECK IS TRACKER SERVICE IS RUNNING
	if (!isTrackerServiceRunning())
	{
	    Log.d(LOGTAG, "SERVICE NOT RUNNING => STARTING SERVICE ...");
	    startService(new Intent(this, GPSTrackerService.class));
	}
	else
	{
	    Log.d(LOGTAG, "SERVICE RUNNING");
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.activity_dashboard_menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

	switch (item.getItemId())
	{
	    case R.id.menu_setting:
		Intent intent = new Intent(DashBoard.this, SettingsActivity.class);
		startActivity(intent);
		return true;
	    default:
		return super.onOptionsItemSelected(item);
	}
    }

    public boolean isInternetConnected()
    {
	ConnectivityManager connectivity = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	if (connectivity != null)
	{
	    NetworkInfo[] info = connectivity.getAllNetworkInfo();
	    if (info != null)
	    {
		for (int i = 0; i < info.length; i++)
		{
		    if (info[i].getState() == NetworkInfo.State.CONNECTED)
		    {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    public void checkGPSServiceStatus()
    {
	try
	{
	    LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//	    boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
//	    if (!enabled)
//	    {
//		Log.d(LOGTAG, "GPS NOT ENABLED. PLEASE ENABLE GPS");
//		Intent intent = new Intent(DashBoard.this, EnableGPSActivity.class);
//		startActivity(intent);
//	    }

	    boolean isGPSEnabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    boolean isNetworkEnabled = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	    if (!isGPSEnabled || !isNetworkEnabled)
	    {
		Log.d(LOGTAG, "GPS AND NETWORK PROVIDER NOT ENABLED. PLEASE ENABLE GPS AND NETWORK PROVIDER");
		Intent intent = new Intent(DashBoard.this, EnableGPSActivity.class);
		startActivity(intent);
	    }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
    }

    private boolean isTrackerServiceRunning()
    {
	try
	{
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

    public void btnAddLocationClick(View view)
    {
	try
	{
	    Intent intent = new Intent(DashBoard.this, AddLocationActivity.class);
	    startActivity(intent);
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(getString(R.string.msg_err_open_act), getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
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
	    dialog = new MessageDialog(getString(R.string.msg_err_open_act), getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
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
	    dialog = new MessageDialog(getString(R.string.msg_err_open_act), getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
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
	    dialog = new MessageDialog(getString(R.string.msg_err_open_act), getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, DashBoard.this);
	}
    }
}
