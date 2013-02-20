package com.app.locationtracker;

import com.app.db.ReminderDBHelper;
import com.app.util.ReminderAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends Activity
{

    ReminderAdapter reminderAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_history);

	listView = (ListView) findViewById(R.id.historyList);
	View empty = findViewById(R.id.empty);
	listView.setEmptyView(empty);
	reminderAdapter = new ReminderAdapter();
	getHistory();
	listView.setAdapter(reminderAdapter);
    }

    public void getHistory()
    {
	// GET HISTORY
	ReminderDBHelper db = new ReminderDBHelper(this);
	this.reminderAdapter.setReminderList(db.getReminderHistory());
    }
    
    public void deleteHistory()
    {
	// DELETE HISTORY
	ReminderDBHelper db = new ReminderDBHelper(this);
	db.deleteReminderHistory();
	Log.d("HISTORY", "HISTORY CLEARED");
	Toast.makeText(getBaseContext(), R.string.msg_hist_clear_success, Toast.LENGTH_LONG).show();
    }

    public void btnClearClick(View view)
    {
	AlertDialog confirmDialog = new AlertDialog.Builder(HistoryActivity.this)
	.setTitle(R.string.msg_hist_confirm_title)
	.setMessage(R.string.msg_hist_confirm)
	.setIcon(R.drawable.warning_icon)
	.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
	{
	    public void onClick(DialogInterface dialog, int whichButton)
	    {
		// CLEAR HISTORY
		deleteHistory();
		getHistory();
		reminderAdapter.notifyDataSetChanged();
	    }
	})
	.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
	{
	    public void onClick(DialogInterface dialog, int whichButton)
	    {
		// DO NOTHING
	    }
	})
	.create();
	confirmDialog.show();
    }

}
