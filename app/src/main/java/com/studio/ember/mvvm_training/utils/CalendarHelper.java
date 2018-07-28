package com.studio.ember.mvvm_training.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarHelper {

    private Context context;
    private static final int REQUEST_CODE_EMAIL = 1;

    public CalendarHelper(Context ctx) {
        this.context = ctx;
    }

    /**
     * TODO: Get account when opening app
     */
    private void getAccounts(){
        try {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                    new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, false, null, null, null, null);
            //ToDoApplication.getSingleton().startActivityForResult(intent, REQUEST_CODE_EMAIL);
        } catch (ActivityNotFoundException e) {
            // TODO
        }
    }


    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    public long getCalendarInfo() {
        // Run query
        long calID = 0;
        String displayName = null;
        String accountName = null;
        String ownerName = null;

        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{"selleks93@gmail.com", "com.google",
                "selleks93@gmail.com"};
        // Submit the query and get a Cursor object back.

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
            // Use the cursor to step through the returned records
            if (cur != null) {
                while (cur.moveToNext()) {
                    calID = 0;
                    displayName = null;
                    accountName = null;
                    ownerName = null;

                    // Get the field values
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                    displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                    accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                    ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                    // Do something with the values...
                    Log.d("CALENDAR_INFO", "getCalendarInfo: " + calID + " - " + displayName + " - " + accountName + " - " + ownerName);
                }
                cur.close();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CALENDAR}, 1);
            }
        }
        return calID;

    }

    public void addReminder() {



        long calID = getCalendarInfo();
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2018, 7, 22, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2018, 6, 23, 20, 43);
        endMillis = endTime.getTimeInMillis();


        ContentResolver cr = context.getContentResolver();
        ContentValues eventValues = new ContentValues();
        //values.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTSTART, endMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        eventValues.put(CalendarContract.Events.TITLE, "Test");
        //eventValues.put(CalendarContract.Reminders.DESCRIPTION, "test description");
        eventValues.put(CalendarContract.Events.CALENDAR_ID, calID);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName(Locale.getDefault()));

        //eventValues.put(Events.RRULE, "FREQ=DAILY;COUNT=2;UNTIL="+endMillis);
        /*eventValues.put("eventStatus", 1);
        eventValues.put("visibility", 3);
        eventValues.put("transparency", 0);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);*/


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR}, 1);
        }else{
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, eventValues);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.d("event id", eventID+"");

            try {
                ContentResolver cr2 = context.getContentResolver();
                ContentValues values2 = new ContentValues();
                values2.put(CalendarContract.Reminders.MINUTES, 1);
                values2.put(CalendarContract.Reminders.EVENT_ID, eventID);
                values2.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALARM);


                Uri uriReminders = cr2.insert(CalendarContract.Reminders.CONTENT_URI, values2);
                Log.d("uriReminders id", uriReminders + "");
            }catch (Exception error){
                Log.d("Excetion",error.getMessage()+"");
            }
        }
    }


}
