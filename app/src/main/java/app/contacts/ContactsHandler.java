package app.contacts;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import app.util.Constants;

/**
 * Created by szetk on 10/28/14.
 */
public class ContactsHandler {

    private Context activity;

    public ContactsHandler(Context activity){
        this.activity = activity;
    }

    /**
     * This saves data to contacts for given contact and mimetype
     * @param contactId Given contact
     * @param mimetype Mimetype of the data
     * @param value The data
     */
    public void saveMimetypeData(String contactId, String mimetype, String value) {
        try {
            ContentValues values = new ContentValues();
            values.put(ContactsContract.Data.DATA1, value);
            int mod = this.activity.getContentResolver().update(
                    ContactsContract.Data.CONTENT_URI,
                    values,
                    ContactsContract.Data.RAW_CONTACT_ID + "=" + contactId + " AND "
                            + ContactsContract.Data.MIMETYPE + "= '"
                            + mimetype + "'", null);

            if (mod == 0) {
                values.put(ContactsContract.Data.RAW_CONTACT_ID, contactId);
                values.put(ContactsContract.Data.MIMETYPE, mimetype);
                this.activity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                Log.v(Constants.LOG_TAG, "data inserted");
            } else {
                Log.v(Constants.LOG_TAG, "data updated");
            }
        } catch (Exception e) {
            Log.v(Constants.LOG_TAG, "failed");
        }
    }

    /**
     * This reads mimetype data from contact
     * @param contactId Contact to read
     * @param mimetype Mimetype
     * @return Returns the value or null if data not found
     */
    public String readMimetypeData(String contactId, String mimetype){
        String value;
        Cursor cursor = this.activity.getContentResolver().query(
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
}
