package app.contacts;

import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

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

    public void savePublicKey(int contactId, String mimetype, String value){
        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, Constants.ACCOUNT_NAME);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        builder.withValue(ContactsContract.RawContacts.SYNC1, null);
        operationList.add(builder.build());

        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactId);
        builder.withValue(ContactsContract.Data.MIMETYPE, mimetype);
        builder.withValue(ContactsContract.Data.DATA1, value);
        builder.withValue(ContactsContract.Data.DATA2, "Julkinen avain");
        operationList.add(builder.build());

        try {
            this.context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e){
            System.out.println("Ei onnistunut julkisen avaimen lisääminen " + e);
        }
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

    public String readMimetypeData2(String lookupKey, String mimetype){
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

}
