package com.app.locationtracker;

import java.util.ArrayList;
import com.app.db.ReminderDBHelper;
import com.app.pojo.Contact;
import com.app.pojo.Reminder;
import com.app.util.Utility;
import com.app.widget.MessageDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class EditLocationActivity extends Activity
{

    public final static String LOGTAG = EditLocationActivity.class.getName();
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
		    txtViewL.setText(String.format(getResources().getString(R.string.loc_rem_sel_location), this.reminder.getLocationName(), String.valueOf(this.reminder.getLatitude()), String.valueOf(this.reminder.getLongitude())));
		}

		if (this.reminder.getContactList().size() > 0)
		{
		    TextView txtViewR = (TextView) findViewById(R.id.tvRecipientsStatus);
		    txtViewR.setText(String.format(getResources().getString(R.string.loc_rem_sel_recipients), this.reminder.getContactList().size()));
		}
	    }
	}
	else
	{
	    Bundle extras = getIntent().getExtras();
	    Reminder reminder = (Reminder) extras.getSerializable("selected_reminder");
	    if (reminder != null)
	    {
		this.reminder = reminder;
		
		EditText txtReminderName = (EditText) findViewById(R.id.txtReminderName);
		txtReminderName.setText(this.reminder.getName());
		
		TextView txtViewL = (TextView) findViewById(R.id.tvLocationStatus);
		Drawable img = getResources().getDrawable(R.drawable.small_success_icon);
		txtViewL.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		txtViewL.setText(String.format(getResources().getString(R.string.loc_rem_sel_location), this.reminder.getLocationName(), String.valueOf(this.reminder.getLatitude()), String.valueOf(this.reminder.getLongitude())));
		
		EditText txtReminderMessage = (EditText) findViewById(R.id.txtReminderMessage);
		txtReminderMessage.setText(this.reminder.getMessage());
		
		CheckBox chkSendSMS = (CheckBox) findViewById(R.id.chkSendSMS);
		chkSendSMS.setChecked(this.reminder.isSendSMS());

		if (this.reminder.getContactList().size() > 0)
		{
		    TextView txtViewR = (TextView) findViewById(R.id.tvRecipientsStatus);
		    txtViewR.setText(String.format(getResources().getString(R.string.loc_rem_sel_recipients), this.reminder.getContactList().size()));
		}
		else
		{
		    TextView txtViewR = (TextView) findViewById(R.id.tvRecipientsStatus);
		    txtViewR.setText(getResources().getString(R.string.loc_rem_no_recipients));
		}
		
		Spinner spinner = (Spinner) findViewById(R.id.spnPriority);
		spinner.setSelection(Utility.getPriorityIndex((this.reminder.getPriority())));
	    }
	    else
	    {
		
	    }
	}
    }

    public void btnSelectMapClick(View view)
    {
	Log.d(LOGTAG, "SELECTING MAP ...");
	Intent intent = new Intent(EditLocationActivity.this, SelectLocationActivity.class);
	intent.putExtra("pre_selected_lat", this.reminder.getLatitude());
	intent.putExtra("pre_selected_lon", this.reminder.getLongitude());
	startActivityForResult(intent, REQUEST_SELECT_MAP);
    }

    public void btnAddRecipients(View view)
    {
	Log.d(LOGTAG, "SELECTING RECIPIENTS ...");
	Intent intent = new Intent(EditLocationActivity.this, SelectRecipientActivity.class);
	intent.putExtra("selected_contacts", (ArrayList<Contact>)this.reminder.getContactList());
	startActivityForResult(intent, REQUEST_SELECT_RECIPIENT);
    }

    public void btnSaveClick(View view)
    {
	Resources resources = getResources();
	EditText eName = (EditText) findViewById(R.id.txtReminderName);
	EditText eMessage = (EditText) findViewById(R.id.txtReminderMessage);
	CheckBox cSendSMS = (CheckBox) findViewById(R.id.chkSendSMS);
	Spinner spinner = (Spinner) findViewById(R.id.spnPriority);

	if (eName.getText().toString().trim().equals("") || eMessage.getText().toString().trim().equals(""))
	{
	    dialog = new MessageDialog(resources.getString(R.string.msg_rem_req), resources.getString(R.string.msg_dialog_title_alert), MessageDialog.MESSAGE_WARN, EditLocationActivity.this);
	}
	else if (this.reminder.getLatitude() == 0.0 || this.reminder.getLongitude() == 0.0)
	{
	    dialog = new MessageDialog(resources.getString(R.string.msg_rem_loc_req), resources.getString(R.string.msg_dialog_title_alert), MessageDialog.MESSAGE_WARN, EditLocationActivity.this);
	}
	else if (cSendSMS.isChecked() && this.reminder.getContactList().size() == 0)
	{
	    dialog = new MessageDialog(resources.getString(R.string.msg_rem_rec_req), resources.getString(R.string.msg_dialog_title_alert), MessageDialog.MESSAGE_WARN, EditLocationActivity.this);
	}
	else
	{
	    this.reminder.setName(eName.getText().toString());
	    this.reminder.setMessage(eMessage.getText().toString());
	    this.reminder.setSendSMS(cSendSMS.isChecked());
	    this.reminder.setPriority(Utility.getPriorityValue(String.valueOf(spinner.getSelectedItem())));
	    // UPDATE REMINDER
	    ReminderDBHelper db = new ReminderDBHelper(this);
	    db.updateReminder(this.reminder);

	    Log.d(LOGTAG, "REMINDER UPDATED");
	    Log.d(LOGTAG, this.reminder.toString());
	    Toast.makeText(getBaseContext(), R.string.msg_rem_update_success, Toast.LENGTH_LONG).show();
	    setResult(Activity.RESULT_OK);
	    finish();
	}
    }

    public void btnCancelClick(View view)
    {
	setResult(Activity.RESULT_CANCELED);
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
		Log.d(LOGTAG, "MAP SELECTED");
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
		    txtView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
		    txtView.setText(String.format(getResources().getString(R.string.loc_rem_sel_location), this.reminder.getLocationName(), String.valueOf(this.reminder.getLatitude()), String.valueOf(this.reminder.getLongitude())));
		}
		else
		{
		    // UNABLE TO SELECT LOCATION
		}
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
		    if (contactList.size() > 0)
		    {
			Log.d(LOGTAG, "RECIPIENTS SELECTED");
			txtView.setText(String.format(getResources().getString(R.string.loc_rem_sel_recipients), this.reminder.getContactList().size()));
		    }
		    else
		    {
			Log.d(LOGTAG, "NO RECIPIENTS SELECTED");
			txtView.setText(R.string.loc_rem_no_recipients);
		    }
		}
	    }
	}
    }

}