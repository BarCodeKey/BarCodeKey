package app.contacts;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import app.util.Constants;

/**
 * This class handles the communication between the app and android's contact list where
 * all the scanned user information is saved.
 */
public class ContactsHandler {

    private Context context;

    public ContactsHandler(Context context){
        this.context = context;
    }

    /** String type // default value = type
     * This saves data to contacts for given contact and mimetype
     * @param contactId Given contact
     * @param mimetype Mimetype of the data
     * @param value The data
     */
    public void saveMimetypeData(String contactId, String mimetype, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.Data.DATA1, value);
            int mod = this.context.getContentResolver().update(
                    ContactsContract.Data.CONTENT_URI,
                    values,
                    ContactsContract.Data.RAW_CONTACT_ID + "=" + contactId + " AND "
                            + ContactsContract.Data.MIMETYPE + "= '"
                            + mimetype + "'", null);

            if (mod == 0) {
                values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
                values.put(ContactsContract.Data.MIMETYPE, mimetype);
                this.context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                Constants.log("data inserted");
            } else {
                Constants.log("data updated");
            }
        } catch (Exception e) {
            Constants.log("failed");
        }
    }

/*
    public String readMimetypeData(String contactId, String mimetype){
        String value;
        Cursor cursor = this.context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[] {
                        ContactsContract.Data.DATA1
                },
                ContactsContract.Data.RAW_CONTACT_ID + "=" + contactId + " AND " + ContactsContract.Data.MIMETYPE + "= '" + mimetype
                        + "'", null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    value = cursor.getString(0);
                    cursor.close();
                    return value;
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public void saveMimetypeData2(String lookupKey, String mimetype, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.Data.DATA1, value);
            int mod = this.context.getContentResolver().update(
                    ContactsContract.Data.CONTENT_URI,
                    values,
                    ContactsContract.Data.LOOKUP_KEY + "=" + lookupKey + " AND "
                            + ContactsContract.Data.MIMETYPE + "= '"
                            + mimetype + "'", null);

            if (mod == 0) {
                values.put(ContactsContract.Data.LOOKUP_KEY, lookupKey);
                values.put(ContactsContract.Data.MIMETYPE, mimetype);
                this.context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                Log.v(Constants.LOG_TAG, "data inserted");
            } else {
                Log.v(Constants.LOG_TAG, "data updated");
            }
        } catch (Exception e) {
            Log.v(Constants.LOG_TAG, "failed");
        }
    }
    */

    /**
     * This reads mimetype data from contact
     * @param lookupKey Contact to read
     * @param mimetype Mimetype
     * @return Returns the value or null if data not found
     */
    public String readMimetypeData(String lookupKey, String mimetype){
        String value;
        Cursor cursor = this.context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.Data.DATA1},
                ContactsContract.Data.LOOKUP_KEY + "=? AND " + ContactsContract.Data.MIMETYPE + "=?",
                new String[]{lookupKey,mimetype},
                null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    value = cursor.getString(0);
                    cursor.close();
                    return value;
                }
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public Uri getLookupUri(Uri uri){
        String id = "";
        int idx;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            id = cursor.getString(idx);
            Constants.log("id: " + id);
        }

        try{
            Uri.Builder b = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id).buildUpon();
            b.appendPath(ContactsContract.Contacts.Entity.CONTENT_DIRECTORY);
            Uri lookupUri = b.build();
            return lookupUri;
        }catch (IllegalArgumentException e){
            Constants.log("Exception: " + e);
        }
        return null;
    }

}
