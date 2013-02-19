package com.app.db;

import android.provider.BaseColumns;

public class ReminderEntry implements BaseColumns
{
    public static final String TABLE_NAME = "reminders";
    public static final String COLUMN_NAME_R_NAME = "r_name";
    public static final String COLUMN_NAME_LOCATION_NAME = "location_name";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";
    public static final String COLUMN_NAME_MESSAGE = "message";
    public static final String COLUMN_NAME_IS_SEND_SMS = "is_send_sms";
    public static final String COLUMN_NAME_CONTACT_LISTS = "contact_lists";
    public static final String COLUMN_NAME_PRIORITY = "priority";
    public static final String COLUMN_NAME_IS_DONE = "is_done";
    public static final String COLUMN_NAME_CR_DATE = "cr_date";
}
