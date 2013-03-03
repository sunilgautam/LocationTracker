package com.app.locationtracker;

import com.app.util.Utility;
import com.app.widget.MessageDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity
{

    public final static String LOGTAG = LoginActivity.class.getName();
    MessageDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_login);

	Utility.PACKAGE_NAME = getApplicationContext().getPackageName();
	Utility.SHARED_PREF_KEY = Utility.PACKAGE_NAME + ".PREFERENCE_FILE_KEY";
    }

    public void btnLoginClick(View view)
    {
	try
	{
	    Resources resources = getResources();
	    EditText pWord = (EditText) findViewById(R.id.txtPassword);

	    if (pWord.getText().toString().trim().equals(""))
	    {
		dialog = new MessageDialog(resources.getString(R.string.msg_log_pword_req), resources.getString(R.string.msg_log_title), MessageDialog.MESSAGE_WARN, LoginActivity.this);
	    }
	    else
	    {
		if (Utility.validateUser(getBaseContext(), pWord.getText().toString()))
		{
		    Intent intent = new Intent(LoginActivity.this, DashBoard.class);
		    startActivity(intent);
		}
		else
		{
		    dialog = new MessageDialog(resources.getString(R.string.msg_log_invalid), resources.getString(R.string.msg_log_title), MessageDialog.MESSAGE_ERROR, LoginActivity.this);
		    pWord.setText("");
		}
	    }
	}
	catch (Exception ex)
	{
	    dialog = new MessageDialog(getString(R.string.msg_err_open_act), getString(R.string.msg_dialog_title_error), MessageDialog.MESSAGE_ERROR, LoginActivity.this);
	}
    }

}
