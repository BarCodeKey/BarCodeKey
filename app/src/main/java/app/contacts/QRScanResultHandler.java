package app.contacts;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import app.util.Constants;

/**
 * Handles the data that is extracted from a scanned QR code.
 */
public class QRScanResultHandler extends Activity {

    private String publicKey = "";
    private ContactsHandler contactsHandler;

    /**
     * Executed when the activity is created, eg. after something is scanned.
     * Parses the contact information and inserts it into the device's contacts
     * list with the help of the ContactsHandler class.
     *
     * @param savedInstanceState saved application state to be recreated if present
     */
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

    /**
     * This lets the user edit a contact that is already present
     * @param vCardString  vCard-formatted string
     */
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

    /**
     * Parses the fields from a vCard-formatted strings and inserts them into the
     * device's contacts list.
     *
     * @param vCardString vCard-formatted string
     * @param intent android's abstract description of an operation to be performed
     */
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

    /**
     * The methods that start the creating or editing of a contact create an action to
     * do so, this method listens for them and acts accordingly.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param data android's abstract description of an operation to be performed
     */
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

    /**
     * Handles the contacts list editing action when creating a new contact.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param data android's abstract description of an operation to be performed
     */
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

    /**
     * Handles the contacts list editing action when editing an existing contact.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param data android's abstract description of an operation to be performed
     */
    private void onActivityResultEdit(int requestCode, int resultCode, Intent data) {
        // Toistaiseksi tämä on vaikka ihan sama. Lisätään public key
        onActivityResultInsertOrEdit(requestCode, resultCode, data);
    }

}
