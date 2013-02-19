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
    private String[] priorities_label;
    private String[] priorities_value;

    public ReminderAdapter(String[] priorities_label, String[] priorities_value)
    {
	this.priorities_label = priorities_label;
	this.priorities_value = priorities_value;
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
	holder.date_text.setText(reminder.getCrDate() + " (" + priorities_label[getIndexOf(String.valueOf(reminder.getPriority()), priorities_value)] + ")");
	return view;
    }

    private int getIndexOf(String toSearch, String[] array)
    {
	for (int i = 0; i < array.length; i++)
	{
	    if (array[i].equals(toSearch))
	    {
		return i;
	    }
	}
	return 0;
    }

    public List<Reminder> getReminderList()
    {
	return reminderList;
    }

    public void setReminderList(List<Reminder> reminderList)
    {
	this.reminderList = reminderList;
    }

    public String[] getPriorities_label()
    {
	return priorities_label;
    }

    public void setPriorities_label(String[] priorities_label)
    {
	this.priorities_label = priorities_label;
    }

    public String[] getPriorities_value()
    {
	return priorities_value;
    }

    public void setPriorities_value(String[] priorities_value)
    {
	this.priorities_value = priorities_value;
    }

    static class ViewHolder
    {
	TextView name_text;
	TextView date_text;
    }

}
