package com.app.locationtracker;

import java.util.ArrayList;
import com.app.db.ReminderDBHelper;
import com.app.pojo.Contact;
import com.app.pojo.Reminder;
import com.app.util.ReminderAdapter;
import com.app.util.Utility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RemindersActivity extends Activity
{

    public final static String LOGTAG = RemindersActivity.class.getName();
    public final static int EDIT_REMINDER = 1002;
    ReminderAdapter reminderAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_reminders);

	listView = (ListView) findViewById(R.id.reminderList);
	View empty = findViewById(R.id.empty);
	listView.setEmptyView(empty);
	reminderAdapter = new ReminderAdapter();
	getReminders();
	listView.setAdapter(reminderAdapter);
	listView.setOnItemClickListener(new OnItemClickListener()
	{
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    {
		final Reminder reminder = (Reminder) listView.getAdapter().getItem(position);
	        
		AlertDialog confirmDialog = new AlertDialog.Builder(RemindersActivity.this)
		.setTitle(reminder.getName())
		.setMessage(Html.fromHtml(String.format(getResources().getString(R.string.msg_rem_details), reminder.getLocationName(), reminder.getLatitude(), reminder.getLongitude(), reminder.getMessage(), reminder.isSendSMS() ? "Yes" : "No", reminder.getContactListCSV(), Utility.getPriorityName(reminder.getPriority()))))
		.setIcon(R.drawable.info_icon)
		.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener()
		{
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                	// EDIT REMINDER
                	editReminder(reminder);
                    }
                })
		.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener()
		{
		    public void onClick(DialogInterface dialog, int whichButton)
		    {
			// DELETE REMINDER
			deleteReminder(reminder);
			getReminders();
			reminderAdapter.notifyDataSetChanged();
		    }
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
		    public void onClick(DialogInterface dialog, int whichButton)
		    {
			// DO NOTHING
		    }
		})
		.create();
		confirmDialog.show();
	    }
	});
    }

    public void getReminders()
    {
	// GET REMINDERS
	ReminderDBHelper db = new ReminderDBHelper(this);
	this.reminderAdapter.setReminderList(db.getAllReminders());
    }
    
    public void deleteReminder(Reminder reminder)
    {
	// DELETE 
	ReminderDBHelper db = new ReminderDBHelper(this);
	db.deleteReminder(reminder);
	Log.d(LOGTAG, "REMINDER DELETED");
	Toast.makeText(getBaseContext(), R.string.msg_rem_del_success, Toast.LENGTH_SHORT).show();
    }
    
    public void editReminder(Reminder reminder)
    {
	// EDIT 
	Log.d(LOGTAG, "EDITING REMINDER ...");
	Intent intent = new Intent(RemindersActivity.this, EditLocationActivity.class);
	intent.putExtra("selected_reminder", reminder);
	startActivityForResult(intent, EDIT_REMINDER);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	super.onActivityResult(requestCode, resultCode, data);

	if (requestCode == EDIT_REMINDER)
	{
	    if (resultCode == RESULT_OK)
	    {
		Log.d(LOGTAG, "REMINDER UPDATED");
		getReminders();
		reminderAdapter.notifyDataSetChanged();
	    }
	}
    }

}
