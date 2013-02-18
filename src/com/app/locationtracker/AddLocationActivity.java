package com.app.locationtracker;

import java.util.ArrayList;
import com.app.db.ReminderDBHelper;
import com.app.pojo.Contact;
import com.app.pojo.Reminder;
import com.app.widget.MessageDialog;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class AddLocationActivity extends Activity
{
    private Reminder reminder = null;
    static final private int REQUEST_SELECT_MAP = 1001;
    static final private int REQUEST_SELECT_RECIPIENT = 1002;
    private MessageDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_add_location);

	if (savedInstanceState != null)
	{
	    this.reminder = (Reminder) savedInstanceState.getSerializable("ser_reminder");
	    if (this.reminder == null)
	    {
		this.reminder = new Reminder();
	    }
	    else
	    {
		// ASSIGN VALUES TO FORM
		if (this.reminder.getLatitude() != 0.0 && this.reminder.getLongitude() != 0.0)
		{
		    TextView txtViewL = (TextView) findViewById(R.id.tvLocationStatus);
		    Drawable img = getResources().getDrawable(R.drawable.small_success_icon);
		    // img.setBounds( 0, 0, 60, 60 );
		    txtViewL.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		    txtViewL.setText("Location : " + this.reminder.getLocationName() + " (" + String.valueOf(this.reminder.getLatitude()) + ", " + String.valueOf(this.reminder.getLongitude()) + ")");
		}
		
		if (this.reminder.getContactList().size() > 0)
		{
		    TextView txtViewR = (TextView) findViewById(R.id.tvRecipientsStatus);
		    txtViewR.setText(this.reminder.getContactList().size() + " SMS recipients selected");
		}
	    }
	}
	else
	{
	    this.reminder = new Reminder();
	}
    }

    public void btnSelectMapClick(View view)
    {
	Intent intent = new Intent(AddLocationActivity.this, SelectLocationActivity.class);
	intent.putExtra("pre_selected_lat", this.reminder.getLatitude());
	intent.putExtra("pre_selected_lon", this.reminder.getLongitude());
	startActivityForResult(intent, REQUEST_SELECT_MAP);
    }

    public void btnAddRecipients(View view)
    {
	Intent intent = new Intent(AddLocationActivity.this, SelectRecipientActivity.class);
	startActivityForResult(intent, REQUEST_SELECT_RECIPIENT);
    }

    public void btnSaveClick(View view)
    {
	EditText eName = (EditText) findViewById(R.id.txtReminderName);
        EditText eMessage = (EditText) findViewById(R.id.txtReminderMessage);
        CheckBox cSendSMS = (CheckBox) findViewById(R.id.chkSendSMS);
        
        if (eName.getText().toString().trim().equals("") || eMessage.getText().toString().trim().equals(""))
        {
            dialog = new MessageDialog("Please fill required fields", "Alert", MessageDialog.MESSAGE_WARN, AddLocationActivity.this);
        }
        else if (this.reminder.getLatitude() == 0.0 || this.reminder.getLongitude() == 0.0)
        {
            dialog = new MessageDialog("Please select location", "Alert", MessageDialog.MESSAGE_WARN, AddLocationActivity.this);
        }
        else if (cSendSMS.isChecked() && this.reminder.getContactList().size() == 0)
        {
            dialog = new MessageDialog("Please select SMS recipients", "Alert", MessageDialog.MESSAGE_WARN, AddLocationActivity.this);
        }
        else
        {
            // SAVE REMINDER
            ReminderDBHelper db = new ReminderDBHelper(this);
            db.addReminder(this.reminder);
            
            Log.d("DONE ", "SAVE DONE");
        }
    }

    public void btnCancelClick(View view)
    {
	finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
	super.onSaveInstanceState(outState);
	outState.putSerializable("ser_reminder", this.reminder);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == REQUEST_SELECT_MAP)
	{
	    if (resultCode == RESULT_OK)
	    {
		String locationName = data.getStringExtra("selected_location");
		int lat = data.getIntExtra("selected_lat", 0);
		int lon = data.getIntExtra("selected_long", 0);
		if (lat != 0 && lon != 0)
		{
		    // LOCATION SELECTED
		    this.reminder.setLatitude((lat / 1E6));
		    this.reminder.setLongitude((lon / 1E6));
		    this.reminder.setLocationName(locationName);

		    TextView txtView = (TextView) findViewById(R.id.tvLocationStatus);
		    Drawable img = getResources().getDrawable(R.drawable.small_success_icon);
		    // img.setBounds( 0, 0, 60, 60 );
		    txtView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		    txtView.setText("Location : " + this.reminder.getLocationName() + " (" + String.valueOf(this.reminder.getLatitude()) + ", " + String.valueOf(this.reminder.getLongitude()) + ")");
		}
		else
		{
		    // UNABLE TO SELECT LOCATION
		}
		// Toast.makeText(getBaseContext(), lat / 1E6 + "," + lon / 1E6,
		// Toast.LENGTH_SHORT).show();
	    }
	}
	else if (requestCode == REQUEST_SELECT_RECIPIENT)
	{
	    if (resultCode == RESULT_OK)
	    {
		if (data != null)
		{
		    ArrayList<Contact> contactList = (ArrayList<Contact>) data.getSerializableExtra("selected_contacts");
		    this.reminder.setContactList(contactList);
		    TextView txtView = (TextView) findViewById(R.id.tvRecipientsStatus);
		    txtView.setText(this.reminder.getContactList().size() + " SMS recipients selected");

		    // Toast.makeText(getBaseContext(), "Selected => " +
		    // contactList.size(), Toast.LENGTH_SHORT).show();
		}
	    }
	}
    }
}
