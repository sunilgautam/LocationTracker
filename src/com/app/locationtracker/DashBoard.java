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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DashBoard extends Activity
{
    MessageDialog dialog = null;
    public static String PACKAGE_NAME;
    public static String SHARED_PREF_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_dash_board);

	PACKAGE_NAME = getApplicationContext().getPackageName();
	SHARED_PREF_KEY = PACKAGE_NAME + ".PREFERENCE_FILE_KEY";
	
	// CHECK IS TRACKER SERVICE IS RUNNING
	if (!isTrackerServiceRunning())
	{
	    startService(new Intent(this, GPSTrackerService.class));
	}
	else
	{
	    Toast.makeText(this, "My Service already Started", Toast.LENGTH_LONG).show();
	}

	if (isInternetConnected())
	{
	    checkGPSServiceStatus();
	}
	else
	{
	    // NO INTERNET CONNECTION
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

    private boolean isTrackerServiceRunning()
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
