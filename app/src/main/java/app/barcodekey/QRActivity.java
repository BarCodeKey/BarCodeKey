package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.contacts.Contact;
import app.contacts.ContactsHandler;
import app.contacts.QRMaker;
import app.contacts.QRScanner;
import app.util.Constants;
import ezvcard.VCard;

/**
 * This class handles the viewing of an another user's information as a QR code.
 */
public class QRActivity extends Activity {

    private ContactsHandler contactsHandler;
    private Uri uri;
    private String id;
    private ImageView imageView;
    private String id2;
    private String lookupKey;

    /**
     * Sets everything up when the view is started. Checks the user's contact information with
     * his/hers public key, then creates and displays the QR code containing that information.
     *
     * @param savedInstanceState saved application state to be recreated if present
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        imageView = (ImageView) findViewById(R.id.QR_code);
        this.contactsHandler = new ContactsHandler(this);

        Contact contact = readData();

        this.imageView.setImageBitmap(QRMaker.createQRcodeBitmap(contact.toString(), Constants.QR_WIDTH, Constants.QR_HEIGHT));

        updateUserInfoTextViews(contact);
    }

    /**
     * Refreshes the user's information that is shown as text in the screen.
     *
     * @param contact the user whose information is to be displayed
     */
    public void updateUserInfoTextViews(Contact contact) {

        TextView textView = (TextView) findViewById(R.id.mainmenu_name_view);
        textView.setText(contact.getGiven());
        textView.append(" ");
        textView.append(contact.getFamily());

        textView = (TextView) findViewById(R.id.mainmenu_phone_number_view);
        if (contact.getNumber().equals("")) {
            textView.setText(R.string.has_no_number);
        } else {
            textView.setText(contact.getNumber());
        }

        textView = (TextView) findViewById(R.id.mainmenu_email_view);
        if (contact.getEmail().equals("")) {
            textView.setText(R.string.has_no_email);
        } else {
            textView.setText(contact.getEmail());
        }

        textView = (TextView) findViewById(R.id.mainmenu_pubkey_view);
        if (contact.getPublicKey().equals("")) {
            textView.setText(getText(R.string.has_no_public_key));
        } else {
            textView.setText(getText(R.string.has_public_key));
        }
    }

    /**
     * Reads the contact information of the user and transforms it into a Contact that
     * is easier to handle.
     *
     * @return user's information as an instance of the Contact class
     */
    public Contact readData(){
        Contact contact = new Contact();
        // Testailua
        int idx;
        id = "";
        id2 = "";
        uri = getIntent().getData();
        System.out.println("QRActivityssä saatu URI: " + uri);

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            id2 = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
            id = cursor.getString(idx);


            idx = cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY);
            lookupKey = cursor.getString(idx);
            /*
            uri = ContactsContract.Contacts.getLookupUri(Integer.parseInt(id2), lookupKey);
            System.out.println("QRActivityssä löydetty toinen URI: " + uri);
            */


            String phone = "", given = "", family = "", prefix = "", publicKey = "", email = "";
            /*
            idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            name = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.Data.DATA1);
            System.out.println("Data1: " + cursor.getString(idx));

            */

            idx = cursor.getColumnIndex(ContactsContract.Data.DATA2);
            given = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.Data.DATA3);
            family = cursor.getString(idx);

            /*
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA4);
            prefix = cursor.getString(idx);
            */

            /*
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA5);
            System.out.println("Data5: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA6);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA7);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA8);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA9);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA10);
            System.out.println("Data10: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA11);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA12);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA13);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA14);
            System.out.println("Data: " + cursor.getString(idx));
            idx = cursor.getColumnIndex(ContactsContract.Data.DATA15);
            System.out.println("Data15: " + cursor.getString(idx));

            Cursor c = getContentResolver().query(
                    Data.CONTENT_URI,
                    null,
                    Data.MIMETYPE + "=? OR " + Data.MIMETYPE + "=?",
                    new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},
                            Data.CONTACT_ID);
            System.out.println("phone ja email cursor");
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndex(Data.CONTACT_ID));
                name = c.getString(c.getColumnIndex(Data.DISPLAY_NAME));
                String data1 = c.getString(c.getColumnIndex(Data.DATA1));

                System.out.println(id + ", name=" + name + ", data1=" + data1);
            }

            */
            System.out.println("ReadMimetypeData");
            email = contactsHandler.readMimetypeData2(lookupKey, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            phone = contactsHandler.readMimetypeData2(lookupKey, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

            System.out.println("Tulostetaan Urin tiedot:");
            System.out.println("id: " + id);
            System.out.println("id2: " + id2);
            System.out.println("lookup key: " + lookupKey);
            System.out.println(given + " " + family);
            if (phone != null){
                System.out.println(phone);

            } if (email != null){
                System.out.println(email);
            }
            publicKey = contactsHandler.readMimetypeData2(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);
            if (publicKey == null){
                System.out.println("publicKey on nulli");
            } else {
                System.out.println("publicKey ei oo nulli vaan: " + publicKey);
                contact.setPublicKey(publicKey);
            }
            contact.setPublicKey(publicKey);
            contact.setGiven(given);
            contact.setFamily(family);
            contact.setEmail(email);
            contact.setNumber(phone);
     }
        return contact;
    }

    /**
     * Starts the scanning activity when the user presses the scan button.
     *
     * @param view
     */
    public void scan(View view){
        Intent intent = new Intent(this, QRScanner.class);
        intent.putExtra(Constants.EXTRA_STARTED_FROM_QCB, true);
        intent.putExtra(Constants.EXTRA_ID, id);
        startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
    }

    /**
     * Performs operations when the user returns from the QR code scanning activity.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    public void onActivityResultQRScanner(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Constants.RESULT_FINISH_MAIN) {
            System.out.println("lopetetaan");
            finish();
        }
    }

    /**
     * Performs operations when the user returns from an another activity.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("QRActivityn onActivityresult");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);

        switch(requestCode){
            case Constants.REQUEST_CODE_QRSCANNER:
                onActivityResultQRScanner(requestCode, resultCode, intent);
                break;
        }
    }

    /**
     * POISTETAAN?
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return true;
    }

    /**
     * POISTETAAN?
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * POISTETAAN NÄMÄ LOPULLISESTA KOODISTA?
     */
    @Override
    public void onPause(){
        System.out.println("QRActivityn onPause");
        super.onPause();
    }

    @Override
    public void onRestart(){
        System.out.println("QRActivityn onRestart");
        super.onRestart();
    }

    @Override
    public void onResume(){
        System.out.println("QRActivityn onResume");
        super.onResume();
    }

}