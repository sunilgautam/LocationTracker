package com.app.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.app.locationtracker.R;

public class MessageDialog
{
    public static int MESSAGE_ERROR = 0;
    public static int MESSAGE_INFO = 1;
    public static int MESSAGE_WARN = 2;
    public static int MESSAGE_SUCCESS = 3;

    public MessageDialog(String message, String header, int MESSAGE_TYPE, Activity activity)
    {
	AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

	alertDialog.setTitle(header);
	alertDialog.setMessage(message);
	
	switch (MESSAGE_TYPE)
	{
	    case 0:
		alertDialog.setIcon(R.drawable.error_icon);
		break;
	    case 1:
		alertDialog.setIcon(R.drawable.info_icon);
		break;
	    case 2:
		alertDialog.setIcon(R.drawable.warning_icon);
		break;
	    case 3:
		alertDialog.setIcon(R.drawable.success_icon);
		break;

	    default:
		alertDialog.setIcon(R.drawable.info_icon);
		break;
	}

	alertDialog.setButton("OK", new DialogInterface.OnClickListener()
	{
	    public void onClick(DialogInterface dialog, int which)
	    {
		
	    }
	});

	alertDialog.show();
    }
}