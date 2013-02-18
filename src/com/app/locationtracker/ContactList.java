package com.app.locationtracker;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.provider.Contacts.Phones;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TwoLineListItem;

public class ContactList extends ListActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);

	Cursor c = getContentResolver().query(Phones.CONTENT_URI, null, null, null, null);
	startManagingCursor(c);

	ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.details, c, new String[] { Phones.NAME, Phones.NUMBER }, new int[] { android.R.id.text1, android.R.id.text2 });
	setListAdapter(adapter);
    }

}