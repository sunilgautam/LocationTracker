package com.app.locationtracker;

import com.app.db.ReminderDBHelper;
import com.app.util.ReminderAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;

public class RemindersActivity extends Activity
{

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
	reminderAdapter = new ReminderAdapter(getResources().getStringArray(R.array.loc_priority_label), getResources().getStringArray(R.array.loc_priority_value));
	getReminders();
	listView.setAdapter(reminderAdapter);
    }

    public void getReminders()
    {
	// GET REMINDERS
	ReminderDBHelper db = new ReminderDBHelper(this);
	this.reminderAdapter.setReminderList(db.getAllReminders());
    }

}
