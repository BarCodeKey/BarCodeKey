package app.domain;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;

import app.barcodekey.R;
import ezvcard.Ezvcard;
import ezvcard.VCard;

public class ContactsHandler extends Activity {


    private static final String LOG_TAG = "Logitagi";
    private static final String KEY_FORMAT = "KEY;ENCODING=B:";
    private static final String INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED = "finishActivityOnSaveCompleted";
    private static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";

    private int REQUEST_CODE_INSERT_OR_EDIT;

    private String publicKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        REQUEST_CODE_INSERT_OR_EDIT = getResources().getInteger(R.integer.REQUEST_CODE_INSERT_OR_EDIT);

        if (getIntent().hasExtra("vcard")){
            String vcard = getIntent().getStringExtra("vcard");
            System.out.println("veecaardi: " + vcard);
            addOrEditContact(vcard);
        }else if (getIntent().hasExtra("addSami")){
            addSami();
        }
    }


    /**
     * This lets the user insert or edit the scanned contact
     * @param vCardString  vCard-formatted string
     */
    public void addOrEditContact(String vCardString) {
        System.out.println("saatiin: " + vCardString);

        String [] vCardAndPublicKey = cleanAndGetPublicKey(vCardString);
        vCardString = vCardAndPublicKey[0];
        String publicKey = vCardAndPublicKey[1];

        try{
            VCard vCard = Ezvcard.parse(vCardString).first();
            String name = vCard.getStructuredName().getGiven();
            name += " " + vCard.getStructuredName().getFamily();
            String phone = vCard.getTelephoneNumbers().get(0).getText();
            String email = vCard.getEmails().get(0).getValue();

            System.out.println("Name: " + name);
            System.out.println("Phone number: " + phone);
            System.out.println("Email: " + email);
            System.out.println("Public key: " + publicKey);

            Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
            intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            intent.putExtra(INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);

            // Lets save the public key to global variable so it can be used in onActivityResult
            this.publicKey = publicKey;

            startActivityForResult(intent, REQUEST_CODE_INSERT_OR_EDIT);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }

    }



    /**
     * This method cleans public key off from given vCard formatted String and returns clened vCard and the public key in an array
     * @param vCardString String in vCard to be cleaned
     * @return String array where 1st object is cleaned vCard and 2nd object is the public key
     */
    public String[] cleanAndGetPublicKey(String vCardString) {
        String[] lines = vCardString.split("\\r?\\n");
        String publicKey = "";
        String cleanVCard= "";

        // Add lines to cleanVCard (expect the key and the last line)
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].startsWith(KEY_FORMAT)) {
                publicKey = lines[i].replace(KEY_FORMAT, "");
            }  else {
                cleanVCard += lines[i] + "\n";
            }
        }
        // Add the last line (we don't want to add newline at the end of cleanVCard)
        cleanVCard += lines[lines.length - 1];
        return new String[]{cleanVCard, publicKey};
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("kutsuttu contactsHandlerin onActivityResulttia");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCide: " + resultCode);
        if(requestCode == REQUEST_CODE_INSERT_OR_EDIT  && resultCode == RESULT_OK) {
        //    System.out.println("päästiin läpi");

            //returns a lookup URI to the contact just selected
            Uri uri = data.getData();
            System.out.println("Saatu URI: " + uri);
            String id = "";
            int idx;
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            // Lets read the first row and only
            if (cursor.moveToFirst()) {
                idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                id = cursor.getString(idx);

                // Lets save the public key
                saveMimetypeData(id, MIMETYPE_PUBLIC_KEY, publicKey);
            /**
                String name = "", phone = "", hasPhone = "";
                idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                name = cursor.getString(idx);

                idx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                hasPhone = cursor.getString(idx);

                System.out.println("Tulostetaan Urin tiedot:");
                System.out.println(id);
                System.out.println(name);
                System.out.println(readMimetypeData(id, MIMETYPE_PUBLIC_KEY));
            **/
            }
        }
        finish();
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
            values.put(Data.DATA1, value);
            int mod = getContentResolver().update(
                    Data.CONTENT_URI,
                    values,
                    Data.RAW_CONTACT_ID + "=" + contactId + " AND "
                            + Data.MIMETYPE + "= '"
                            + mimetype + "'", null);

            if (mod == 0) {
                values.put(Data.RAW_CONTACT_ID, contactId);
                values.put(Data.MIMETYPE, mimetype);
                getContentResolver().insert(Data.CONTENT_URI, values);
                Log.v(LOG_TAG, "data inserted");
            } else {
                Log.v(LOG_TAG, "data updated");
            }
        } catch (Exception e) {
            Log.v(LOG_TAG, "failed");
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
        Cursor cursor = getContentResolver().query(
                Data.CONTENT_URI,
                new String[] {
                        Data.DATA1
                },
                Data.RAW_CONTACT_ID + "=" + contactId + " AND " + Data.MIMETYPE + "= '" + mimetype
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


    /**
     * Väliaikainen
     */

    public void tulostaKaikki() {
        Cursor contactsCursor = null;
        try {
            contactsCursor = getContentResolver().query(RawContacts.CONTENT_URI,
                    new String [] { RawContacts._ID },
                    null, null, null);
            if (contactsCursor != null && contactsCursor.moveToFirst()) {
                do {
                    String rawContactId = contactsCursor.getString(0);
                    Cursor noteCursor = null;
                    try {
                        noteCursor = getContentResolver().query(Data.CONTENT_URI,
                                new String[] {Data._ID, Note.NOTE},
                                Data.RAW_CONTACT_ID + "=?" + " AND "
                                        + Data.MIMETYPE + "='" + Note.CONTENT_ITEM_TYPE + "'",
                                new String[] {rawContactId}, null);

                        if (noteCursor != null && noteCursor.moveToFirst()) {
                            String note = noteCursor.getString(noteCursor.getColumnIndex(Note.NOTE));
                            System.out.println("Note: " + note);
                        }
                    } finally {
                        if (noteCursor != null) {
                            noteCursor.close();
                        }
                    }
                } while (contactsCursor.moveToNext());
            }
        } finally {
            if (contactsCursor != null) {
                contactsCursor.close();
            }
        }
    }



    /**
     * Väliaikanen Samin koklailua varten
     */
    public void addSami()  {
        /**
         VCard vcard = new VCard();

         StructuredName n = new StructuredName();
         n.setFamily("Parasmies");
         n.setGiven("Sami");
         vcard.setStructuredName(n);
         vcard.addTelephoneNumber("+358432398212433");
         vcard.addEmail("fsdfdsfsd.fds@sami.sami");

         addOrEditContact(vcard.write());

         **/

        String sami = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "EMAIL:sami@sami.fi\n" +
                "TEL:+3585695636363728\n" +
                "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
                "END:VCARD";

        addOrEditContact(sami);
    }

}
