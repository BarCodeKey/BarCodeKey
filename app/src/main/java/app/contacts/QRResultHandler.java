package app.contacts;

import android.app.Activity;
import android.content.ContentUris;
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

public class QRResultHandler extends Activity {


    private static final String LOG_TAG = "Logitagi";
    private static final String KEY_FORMAT = "KEY;ENCODING=B:";
    private static final String INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED = "finishActivityOnSaveCompleted";
    private static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";

    private int REQUEST_CODE_INSERT_OR_EDIT;
    private int REQUEST_CODE_EDIT;

    private String publicKey = "";
    private ContactsHandler contactsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        REQUEST_CODE_INSERT_OR_EDIT = getResources().getInteger(R.integer.REQUEST_CODE_INSERT_OR_EDIT);
        REQUEST_CODE_EDIT = getResources().getInteger(R.integer.REQUEST_CODE_EDIT);
        this.contactsHandler = new ContactsHandler(this);

        if (getIntent().hasExtra("id")){ //We have scanned a QR for a contact
            String vcard = getIntent().getStringExtra("vcard");
            String idString = getIntent().getStringExtra("id");
            System.out.println("idString: " + idString);
            int id = Integer.parseInt(idString);
            System.out.println("löyty id: " + id);
            editContact(id, vcard);
        } else if (getIntent().hasExtra("vcard")){
            String vcard = getIntent().getStringExtra("vcard");
        //    System.out.println("veecaardi: " + vcard);
            insertOrEditContact(vcard);
        }else if (getIntent().hasExtra("addSami")){
            addSami();
        }
    }

    /**
     * This lets the user insert or edit the scanned contact
     * @param vCardString  vCard-formatted string
     */
    public void insertOrEditContact(String vCardString) {
        try{
            Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
            intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
            intent.putExtra(INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);

            contactDataHandling(vCardString, intent);

            startActivityForResult(intent, REQUEST_CODE_INSERT_OR_EDIT);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void editContact(int id, String vCardString) {
        try{
            Intent intent = new Intent(Intent.ACTION_EDIT);
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
            intent.setData(contactUri);
            intent.putExtra(INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);
  //          intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            contactDataHandling(vCardString, intent);

            startActivityForResult(intent, REQUEST_CODE_EDIT);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void contactDataHandling(String vCardString, Intent intent){
        System.out.println("contactDataHandlingissa saatiin: " + vCardString);

        String [] vCardAndPublicKey = VCardHandler.cleanPublicKeyFromStringAndGetPublicKey(vCardString);
        vCardString = vCardAndPublicKey[0];
        publicKey = vCardAndPublicKey[1];

        VCard vCard = Ezvcard.parse(vCardString).first();
        String name = vCard.getStructuredName().getGiven();
        name += " " + vCard.getStructuredName().getFamily();
        String phone = vCard.getTelephoneNumbers().get(0).getText();
        String email = vCard.getEmails().get(0).getValue();

        System.out.println("Name: " + name);
        System.out.println("Phone number: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Public key: " + publicKey);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("kutsuttu QRResultHandlerin onActivityResulttia");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);
        if(requestCode == REQUEST_CODE_INSERT_OR_EDIT) {
            onActivityResultInsertOrEdit(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_CODE_EDIT){
            onActivityResultEdit(requestCode, resultCode, data);
        }
        finish();
    }

    private void onActivityResultInsertOrEdit(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
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
                contactsHandler.saveMimetypeData(id, MIMETYPE_PUBLIC_KEY, publicKey);

                String name = "", phone = "", hasPhone = "";
                idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                name = cursor.getString(idx);

                idx = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                hasPhone = cursor.getString(idx);

                System.out.println("Tulostetaan Urin tiedot:");
                System.out.println(id);
                System.out.println(name);
                System.out.println("avain: " + this.contactsHandler.readMimetypeData(id, MIMETYPE_PUBLIC_KEY));
            }
        }
    }

    private void onActivityResultEdit(int requestCode, int resultCode, Intent data) {
        // Toistaiseksi tämä on vaikka ihan sama. Lisätään public key
        onActivityResultInsertOrEdit(requestCode, resultCode, data);
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

         insertOrEditContact(vcard.write());

         **/

        String sami = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "EMAIL:sami@sami.fi\n" +
                "TEL:+3585695636363728\n" +
                "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
                "END:VCARD";

        insertOrEditContact(sami);
    }

}
