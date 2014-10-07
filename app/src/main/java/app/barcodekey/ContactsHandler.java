package app.barcodekey;

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

import app.barcodekey.R;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;


/**
 * Created by szetk on 10/4/14.
 */
public class ContactsHandler extends Activity {

    private static final int PICK_CONTACT = 0;
    private static final String LOG_TAG = "Logitagi";

    /**
    private Context context;

    public ContactsHandler(Context context){
        this.context = context;
    }
**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSami();
    }



    public void addOrEditContact(String string) {
        VCard vCard = Ezvcard.parse(string).first();
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, vCard.getStructuredName().getGiven() + " " + vCard.getStructuredName().getFamily());
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, vCard.getTelephoneNumbers().get(0).getText());
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, vCard.getEmails().get(0).getValue());
        intent.putExtra(ContactsContract.Intents.Insert.NOTES, "hellurihei");
        intent.putExtra("public_key","kissaJaKoira");
        startActivityForResult(intent, PICK_CONTACT);
        //context.startActivity(intent);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("kutsuttu on onActivityResulttia");
        System.out.println("resultCode: " + resultCode);
        System.out.println("result_ok: " + RESULT_OK);
        if(requestCode == PICK_CONTACT  && resultCode == RESULT_OK) {
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
        finish();

        tulostaKaikki();
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


    public void addSami()  {
        VCard vcard = new VCard();

        StructuredName n = new StructuredName();
        n.setFamily("Parasmies");
        n.setGiven("Sami");
        vcard.setStructuredName(n);
        vcard.addTelephoneNumber("+358432398212433");
        vcard.addEmail("fsdfdsfsd.fds@sami.sami");

        addOrEditContact(vcard.write());
    }

    /**
    public String[] getFields(String vCard){
        String[] fields = new String[4];
        String[] lines = vCard.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++){
            String line = lines[i];
            if (line.startsWith("N:")){
                fields[0] = parseName(line);
            } else if (line.startsWith("TEL")){
                fields[1] = parseNumber(line);
            } else if (line.startsWith("EMAIL")){
                fields[2] = parseEmail(line);
            } else if (line.startsWith("KEY")){
                saveKey(line);
            }
        }
        return fields;
    }

     **/


}
