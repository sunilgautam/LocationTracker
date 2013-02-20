package com.app.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.app.pojo.Reminder;
import com.app.util.Utility;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDBHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "reminder_manager";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE " + ReminderEntry.TABLE_NAME + " (" + 
	    ReminderEntry._ID + " INTEGER PRIMARY KEY," + 
	    ReminderEntry.COLUMN_NAME_R_NAME + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_LOCATION_NAME + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_IS_SEND_SMS + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_CONTACT_LISTS + TEXT_TYPE + COMMA_SEP +
	    ReminderEntry.COLUMN_NAME_PRIORITY + " INTEGER" + COMMA_SEP +
	    ReminderEntry.COLUMN_NAME_IS_DONE + TEXT_TYPE + COMMA_SEP + 
	    ReminderEntry.COLUMN_NAME_CR_DATE + TEXT_TYPE + " )";
    
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ReminderEntry.TABLE_NAME;

    public ReminderDBHelper(Context context)
    {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
	db.execSQL(SQL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
	db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    public void addReminder(Reminder reminder)
    {
	SQLiteDatabase db = null;
	try
	{
            db = this.getWritableDatabase();
     
            ContentValues values = new ContentValues();
            values.put(ReminderEntry.COLUMN_NAME_R_NAME, reminder.getName()); 
            values.put(ReminderEntry.COLUMN_NAME_LOCATION_NAME, reminder.getLocationName());
            values.put(ReminderEntry.COLUMN_NAME_LATITUDE, reminder.getLatitude());
            values.put(ReminderEntry.COLUMN_NAME_LONGITUDE, reminder.getLongitude());
            values.put(ReminderEntry.COLUMN_NAME_MESSAGE, reminder.getMessage());
            values.put(ReminderEntry.COLUMN_NAME_IS_SEND_SMS, reminder.isSendSMS());
            values.put(ReminderEntry.COLUMN_NAME_CONTACT_LISTS, reminder.getContactListCSV());
            values.put(ReminderEntry.COLUMN_NAME_PRIORITY, reminder.getPriority());
            values.put(ReminderEntry.COLUMN_NAME_IS_DONE, "0");
            values.put(ReminderEntry.COLUMN_NAME_CR_DATE, Utility.getReminderDate(new Date()));
     
            db.insert(ReminderEntry.TABLE_NAME, null, values);
	}
	catch(Exception ex)
	{
	    
	}
	finally
	{
	    if (db != null)
	    {
		db.close();
	    }
	}
        
    }
 
    public Reminder getReminder(int id)
    {
	SQLiteDatabase db = null;
	Reminder reminder = null;
	Cursor cursor = null;
        
        try
	{
            db = this.getReadableDatabase();
            
            cursor = db.query(ReminderEntry.TABLE_NAME, 
            		new String[] {
            				ReminderEntry._ID, 
                                    	ReminderEntry.COLUMN_NAME_R_NAME, 
                                    	ReminderEntry.COLUMN_NAME_LOCATION_NAME, 
                                    	ReminderEntry.COLUMN_NAME_LATITUDE, 
                                    	ReminderEntry.COLUMN_NAME_LONGITUDE, 
                                    	ReminderEntry.COLUMN_NAME_MESSAGE, 
                                    	ReminderEntry.COLUMN_NAME_IS_SEND_SMS, 
                                    	ReminderEntry.COLUMN_NAME_CONTACT_LISTS,
                                    	ReminderEntry.COLUMN_NAME_PRIORITY, 
                                    	ReminderEntry.COLUMN_NAME_CR_DATE,
                                    	ReminderEntry.COLUMN_NAME_IS_DONE
            				}, ReminderEntry._ID + "=?",
            		new String[] {
            				String.valueOf(id)
            			      }, 
            			      null, 
            			      null, 
            			      null, 
            			      null);
            if (cursor != null)
                cursor.moveToFirst();
     
            reminder = new Reminder(
        	    Integer.parseInt(cursor.getString(0)), 
                        			cursor.getString(1),
                        			cursor.getString(2),
                        			cursor.getString(3),
                        			cursor.getString(4),
                        			cursor.getString(5),
                        			cursor.getString(6),
                        			cursor.getString(7),
                        			cursor.getInt(8),
                        			cursor.getString(10)
            				);
	}
	catch (Exception ex)
	{
	    
	}
	finally
	{
	    if (cursor != null)
	    {
		cursor.close();
	    }
	    if (db != null)
            {
        	db.close();
            }
	}
        
        return reminder;
    }
 
    public List<Reminder> getAllReminders()
    {
        List<Reminder> reminderList = new ArrayList<Reminder>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
        try
	{
            db = this.getReadableDatabase();            
            cursor = db.query(ReminderEntry.TABLE_NAME, 
        		new String[] {
        				ReminderEntry._ID, 
                                	ReminderEntry.COLUMN_NAME_R_NAME, 
                                	ReminderEntry.COLUMN_NAME_LOCATION_NAME, 
                                	ReminderEntry.COLUMN_NAME_LATITUDE, 
                                	ReminderEntry.COLUMN_NAME_LONGITUDE, 
                                	ReminderEntry.COLUMN_NAME_MESSAGE, 
                                	ReminderEntry.COLUMN_NAME_IS_SEND_SMS, 
                                	ReminderEntry.COLUMN_NAME_CONTACT_LISTS,
                                	ReminderEntry.COLUMN_NAME_PRIORITY,
                                	ReminderEntry.COLUMN_NAME_IS_DONE,
                                	ReminderEntry.COLUMN_NAME_CR_DATE
        			     }, ReminderEntry.COLUMN_NAME_IS_DONE + "=?",
        		new String[] {
        				"0"
        			     }, 
        			      	null, 
        			      	null, 
        			      	ReminderEntry._ID + " DESC", 
        			      	null);
            if (cursor != null)
            {
        	if (cursor.moveToFirst())
                {
                    do
                    {
                	Reminder reminder = new Reminder(
        			Integer.parseInt(cursor.getString(0)), 
        			cursor.getString(1),
        			cursor.getString(2),
        			cursor.getString(3),
        			cursor.getString(4),
        			cursor.getString(5),
        			cursor.getString(6),
        			cursor.getString(7),
        			cursor.getInt(8),
        			cursor.getString(10)
        		);
                        
                	reminderList.add(reminder);
                    } while (cursor.moveToNext());
                }
            }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	finally
	{
	    if (cursor != null)
	    {
		cursor.close();
	    }
	    if (db != null)
            {
        	db.close();
            }
	}
 
        return reminderList;
    }
    
    public List<Reminder> getReminderHistory()
    {
        List<Reminder> reminderList = new ArrayList<Reminder>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
        try
	{
            db = this.getReadableDatabase();
            cursor = db.query(ReminderEntry.TABLE_NAME, 
        		new String[] {
        				ReminderEntry._ID, 
                                	ReminderEntry.COLUMN_NAME_R_NAME, 
                                	ReminderEntry.COLUMN_NAME_LOCATION_NAME, 
                                	ReminderEntry.COLUMN_NAME_LATITUDE, 
                                	ReminderEntry.COLUMN_NAME_LONGITUDE, 
                                	ReminderEntry.COLUMN_NAME_MESSAGE, 
                                	ReminderEntry.COLUMN_NAME_IS_SEND_SMS, 
                                	ReminderEntry.COLUMN_NAME_CONTACT_LISTS,
                                	ReminderEntry.COLUMN_NAME_PRIORITY,
                                	ReminderEntry.COLUMN_NAME_IS_DONE,
                                	ReminderEntry.COLUMN_NAME_CR_DATE
        			     }, ReminderEntry.COLUMN_NAME_IS_DONE + "=?",
        		new String[] {
        				"1"
        			     }, 
        			      	null, 
        			      	null, 
        			      	ReminderEntry._ID + " DESC", 
        			      	null);
            if (cursor != null)
            {
        	if (cursor.moveToFirst())
                {
                    do
                    {
                	Reminder reminder = new Reminder(
        			Integer.parseInt(cursor.getString(0)), 
        			cursor.getString(1),
        			cursor.getString(2),
        			cursor.getString(3),
        			cursor.getString(4),
        			cursor.getString(5),
        			cursor.getString(6),
        			cursor.getString(7),
        			cursor.getInt(8),
        			cursor.getString(10)
        		);
                        
                	reminderList.add(reminder);
                    } while (cursor.moveToNext());
                }
            }
	}
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	finally
	{
	    if (cursor != null)
	    {
		cursor.close();
	    }
	    if (db != null)
            {
        	db.close();
            }
	}
 
        return reminderList;
    }
 
    public void deleteReminder(Reminder reminder)
    {
        SQLiteDatabase db = null;
        try
        {
            db = this.getWritableDatabase();
            
            db.delete(ReminderEntry.TABLE_NAME, ReminderEntry._ID + " = ?", new String[] { String.valueOf(reminder.getId()) });
        }
        catch(Exception ex)
        {
            
        }
        finally
        {
            if (db != null)
            {
        	db.close();
            }
        }
    }
    
    public void deleteReminderHistory() {
        SQLiteDatabase db = null;
        try
        {
            db = this.getWritableDatabase();
            
            db.delete(ReminderEntry.TABLE_NAME, ReminderEntry.COLUMN_NAME_IS_DONE + " = ?", new String[] { "1" });
        }
        catch(Exception ex)
        {
            
        }
        finally
        {
            if (db != null)
            {
        	db.close();
            }
        }
        
    }
 
    public int getReminderCount()
    {
        String countQuery = "SELECT  * FROM " + ReminderEntry.TABLE_NAME;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        
	try
	{
	    db = this.getReadableDatabase();
	    cursor = db.rawQuery(countQuery, null);
	}
	catch (Exception ex)
	{
	    
	}
	finally
	{
	    if (db != null)
            {
        	db.close();
            }
	}
 
        return (cursor == null) ? 0 : cursor.getCount();
    }

}
