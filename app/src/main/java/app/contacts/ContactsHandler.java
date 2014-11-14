package app.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import app.util.Constants;

public class ContactsHandler {

    private Context context;

    public ContactsHandler(Context context){
        this.context = context;
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
}
