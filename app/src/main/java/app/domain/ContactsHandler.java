package app.domain;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;

import java.util.List;

import app.barcodekey.R;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Key;
import ezvcard.property.StructuredName;


public class ContactsHandler extends Activity {

    private static final int INSERT_OR_EDIT = 0;
    private static final String LOG_TAG = "Logitagi";
    private static final String KEY_FORMAT = "KEY;ENCODING=B:";
    private static final String INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED = "finishActivityOnSaveCompleted";

    private String publicKey; // Variable for reading publicKey from QR-code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
     * @param string Contact in vCard-formatted string
     */
    public void addOrEditContact(String string) {
        System.out.println("saatiin: " + string);
        string = cleanAndGetPublicKey(string); // this stores the public key to global variable
        System.out.println("tehtiin: " + string);
        try{
            VCard vCard = Ezvcard.parse(string).first();
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
            intent.putExtra(ContactsContract.Intents.Insert.NOTES, "DO NOT EDIT: " + publicKey);
            intent.putExtra(INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);
            startActivityForResult(intent, INSERT_OR_EDIT);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }

    }



    /**
     * This method cleans public key off from given string and stores it to global variable publicKey.
     * @param string String to be cleaned
     * @return Cleaned string
     */
    private String cleanAndGetPublicKey(String string) {
        String[] lines = string.split("\\r?\\n");
        String cleanString = "";

        for (int i = 0; i < lines.length; i++){
            if (lines[i].startsWith(KEY_FORMAT)) {
                this.publicKey = lines[i].replace(KEY_FORMAT, "");
            } else{
                cleanString += lines[i] + "\n";
            }
        }
        return cleanString;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    System.out.println("kutsuttu on onActivityResulttia");
    //    System.out.println("resultCode: " + resultCode);
    //    System.out.println("result_ok: " + RESULT_OK);
        if(requestCode == INSERT_OR_EDIT  && resultCode == RESULT_OK) {
            System.out.println("päästiin läpi");

            //returns a lookup URI to the contact just selected
            Uri uri = data.getData();
            System.out.println("Saatu URI: " + uri);
            String id = "", name = "", phone = "", hasPhone = "";
            int idx;
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                id = cursor.getString(idx);

                idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                name = cursor.getString(idx);

                idx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                hasPhone = cursor.getString(idx);
            }
            System.out.println("Tulostetaan Urin tiedot:");
            System.out.println(id);
            System.out.println(name);
            System.out.println(phone);
        }
        System.out.println("poistutaan täältä");
        // alert tallennettu onnistuneesti
        finish();

        //  tulostaKaikki();
    }

    private void tulostaKaikki() {
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
