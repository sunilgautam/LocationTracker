package com.app.locationtracker;

import java.util.ArrayList;
import com.app.pojo.Contact;
import com.app.util.ContactAdapter;
import com.app.widget.MessageDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

public class SelectRecipientActivity extends Activity
{
    public final static String LOGTAG = SelectRecipientActivity.class.getName();
    ContactAdapter contactAdapter;
    ListView listView;
    ArrayList<Contact> contactList = new ArrayList<Contact>();
    MessageDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_select_recipient);

	listView = (ListView) findViewById(R.id.contactList);
	contactAdapter = new ContactAdapter();
	getContacts();
	listView.setAdapter(contactAdapter);
	listView.setItemsCanFocus(false);
	listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void getContacts()
    {
	Log.d(LOGTAG, "GETTING CONTACT LIST");
	ContentResolver cr = getContentResolver();
	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

	if (cur.getCount() > 0)
	{
	    String id, name, phone;
	    while (cur.moveToNext())
	    {
		id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
		{
		    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
		    if (pCur.moveToNext())
		    {
			phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			Contact contact = new Contact(name, phone);
			contactAdapter.getContactList().add(contact);
		    }
		    pCur.close();
		}
	    }
	}
    }

    public void btnDoneClick(View view)
    {
	contactList.clear();
	int cntChoice = listView.getCount();
	SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

	for (int i = 0; i < cntChoice; i++)
	{
	    if (sparseBooleanArray.get(i))
	    {
		Contact contact = (Contact) listView.getItemAtPosition(i);
		contactList.add(contact);
	    }
	}
	
	Intent resultData = new Intent();
	resultData.putExtra("selected_contacts", contactList);
	setResult(Activity.RESULT_OK, resultData);
	finish();

        //if (contactList.size() > 0)
        //{
        //    Intent resultData = new Intent();
        //    resultData.putExtra("selected_contacts", contactList);
        //    setResult(Activity.RESULT_OK, resultData);
        //    finish();
        //}
        //else
        //{
        //    dialog = new MessageDialog("Please select a recipient", "Alert", MessageDialog.MESSAGE_WARN, SelectRecipientActivity.this);
        //}

    }

}
