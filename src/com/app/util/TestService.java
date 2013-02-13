package com.app.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class TestService extends Service
{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
	Toast.makeText(this, "My Service Started onStartCommand()", Toast.LENGTH_LONG).show();
	return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
	Toast.makeText(this, "My Service Started onStart()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate()
    {
	//Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
	return null;
    }

}
