package com.app.util;

import java.util.ArrayList;
import java.util.List;
import com.app.locationtracker.R;
import com.app.pojo.Reminder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReminderAdapter extends BaseAdapter
{
    private List<Reminder> reminderList = new ArrayList<Reminder>();

    public ReminderAdapter()
    {
	
    }

    @Override
    public int getCount()
    {
	return getReminderList().size();
    }

    @Override
    public Object getItem(int index)
    {
	return getReminderList().get(index);
    }

    @Override
    public long getItemId(int index)
    {
	return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent)
    {
	ViewHolder holder;
	if (view == null)
	{
	    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	    view = inflater.inflate(R.layout.reminder_list_item, parent, false);

	    holder = new ViewHolder();
	    holder.name_text = (TextView) view.findViewById(R.id.name_view);
	    holder.date_text = (TextView) view.findViewById(R.id.date_view);

	    view.setTag(holder);
	}
	else
	{
	    holder = (ViewHolder) view.getTag();
	}

	Reminder reminder = getReminderList().get(index);
	holder.name_text.setText(reminder.getName());
	holder.date_text.setText(reminder.getCrDate() + " (" + Utility.getPriorityName(reminder.getPriority()) + ")");
	return view;
    }

    public List<Reminder> getReminderList()
    {
	return reminderList;
    }

    public void setReminderList(List<Reminder> reminderList)
    {
	this.reminderList = reminderList;
    }

    static class ViewHolder
    {
	TextView name_text;
	TextView date_text;
    }

}
