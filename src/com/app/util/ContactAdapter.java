package com.app.util;

import java.util.ArrayList;
import java.util.List;
import com.app.pojo.Contact;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class ContactAdapter extends BaseAdapter
{
    private List<Contact> contactList = new ArrayList<Contact>();

    public ContactAdapter()
    {

    }

    @Override
    public int getCount()
    {
	return getContactList().size();
    }

    @Override
    public Object getItem(int index)
    {
	return getContactList().get(index);
    }

    @Override
    public long getItemId(int index)
    {
	return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent)
    {
	if (view == null)
	{
	    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	    view = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
	}

	Contact contact = getContactList().get(index);
	CheckedTextView nameView = (CheckedTextView) view.findViewById(android.R.id.text1);
	nameView.setText(contact.getName() + "\n" + contact.getPhone());
	return view;
    }

    public List<Contact> getContactList()
    {
	return contactList;
    }

    public void setContactList(List<Contact> contactList)
    {
	this.contactList = contactList;
    }

}
