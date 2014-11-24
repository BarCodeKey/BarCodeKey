package app.contacts;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import app.util.Constants;

public class QRScanResultHandler extends Activity {

    private String publicKey = "";
    private ContactsHandler contactsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contactsHandler = new ContactsHandler(this);

        if (getIntent().hasExtra(Constants.EXTRA_ID)){ //We have scanned a QR for a contact
            String vcard = getIntent().getStringExtra(Constants.EXTRA_VCARD);
            String idString = getIntent().getStringExtra(Constants.EXTRA_ID);
            System.out.println("idString: " + idString);
            int id = Integer.parseInt(idString);
            System.out.println("löyty id: " + id);
            editContact(id, vcard);
        } else if (getIntent().hasExtra(Constants.EXTRA_VCARD)){
            String vcard = getIntent().getStringExtra(Constants.EXTRA_VCARD);
        //    System.out.println("veecaardi: " + vcard);
            insertOrEditContact(vcard);
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
            intent.putExtra(Constants.INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);

            contactDataHandling(vCardString, intent);

            startActivityForResult(intent, Constants.REQUEST_CODE_INSERT_OR_EDIT);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void editContact(int id, String vCardString) {
        try{
            Intent intent = new Intent(Intent.ACTION_EDIT);
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
            intent.setData(contactUri);
            intent.putExtra(Constants.INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);
  //          intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            contactDataHandling(vCardString, intent);

            startActivityForResult(intent, Constants.REQUEST_CODE_EDIT);
        } catch(Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void contactDataHandling(String vCardString, Intent intent){
        Contact contact = new Contact(vCardString);

        System.out.println("contactDataHandlingissa saatiin: " + vCardString);

        publicKey = contact.getPublicKey();
        String name = contact.getGiven() + " " +  contact.getFamily();
        String phone = contact.getNumber();
        String email = contact.getEmail();

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
        System.out.println("kutsuttu QRScanResultHandlerin onActivityResulttia");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);
        if(requestCode == Constants.REQUEST_CODE_INSERT_OR_EDIT) {
            onActivityResultInsertOrEdit(requestCode, resultCode, data);
        } else if (requestCode == Constants.REQUEST_CODE_EDIT){
            onActivityResultEdit(requestCode, resultCode, data);
        }
        finish();
    }

    private void onActivityResultInsertOrEdit(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            //    System.out.println("päästiin läpi");

            //returns a lookup URI to the contact just selected
            Uri uri = data.getData();
            System.out.println("onActivityResultInsertOrEditissä saatu URI: " + uri);
            String id = "";
            int idx;
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            // Lets read the first row and only
            if (cursor.moveToFirst()) {
                idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                id = cursor.getString(idx);

                idx = cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY);
                String lookupKey = cursor.getString(idx);
                System.out.println("lookupkey: " + lookupKey);
                System.out.println("publickey: " + publicKey);
                // Lets save the public key
                contactsHandler.saveMimetypeData(id, Constants.MIMETYPE_PUBLIC_KEY, publicKey);
                //contactsHandler.savePublicKey(idx, Constants.MIMETYPE_PUBLIC_KEY, publicKey);

                String name = "", phone = "", hasPhone = "";
                idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                name = cursor.getString(idx);

                System.out.println("Tulostetaan Urin tiedot:");
                System.out.println(id);
                System.out.println(name);
                System.out.println("avain: " + this.contactsHandler.readMimetypeData(id, Constants.MIMETYPE_PUBLIC_KEY));
            }
        }
    }

    private void onActivityResultEdit(int requestCode, int resultCode, Intent data) {
        // Toistaiseksi tämä on vaikka ihan sama. Lisätään public key
        onActivityResultInsertOrEdit(requestCode, resultCode, data);
    }

}
