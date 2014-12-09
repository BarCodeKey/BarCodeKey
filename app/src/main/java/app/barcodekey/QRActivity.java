package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import android.telephony.PhoneNumberUtils;
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


public class QRActivity extends Activity {

    private ContactsHandler contactsHandler;
    private Uri uri;
    private String id;
    private ImageView imageView;
    private String lookupKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        imageView = (ImageView) findViewById(R.id.QR_code);
        this.contactsHandler = new ContactsHandler(this);

        Contact contact = readData();
        System.out.println("kontakti: ");
        System.out.println(contact);

        this.imageView.setImageBitmap(QRMaker.createQRcodeBitmap(contact.toString(), Constants.QR_WIDTH, Constants.QR_HEIGHT));

        updateUserInfoTextViews(contact);
    }

    // kopio tavallaan
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

    public Contact readData(){
        System.out.println("QRActivityssä saatu URI: " + uri);
        uri = getIntent().getData();
        if (getIntent().hasExtra("entity")){
            uri = contactsHandler.getLookupUri(uri);
            System.out.println("QRActivityssä tehtiin URI: " + uri);
            return readDataFromEntity();
        } else{
            return readDataFromContactUri();
        }
    }

    public Contact readDataFromContactUri(){
        Contact contact = new Contact();
        String phone, given, family, email, publicKey;

        int idx;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {

            idx = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
            id = cursor.getString(idx);
            System.out.println("id: " + id);

            idx = cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY);
            lookupKey = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.Data.DATA2);
            given = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.Data.DATA3);
            family = cursor.getString(idx);

            System.out.println("ReadMimetypeData");
            email = contactsHandler.readMimetypeData2(lookupKey, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            phone = contactsHandler.readMimetypeData2(lookupKey, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            publicKey = contactsHandler.readMimetypeData2(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);


        /* TODO: selvitä tarviiko tätä?
            if (publicKey == null){
                System.out.println("publicKey on nulli");
            } else {
                System.out.println("publicKey ei oo nulli vaan: " + publicKey);
                contact.setPublicKey(publicKey);
            }
        */

            contact.setPublicKey(publicKey);
            contact.setGiven(given);
            contact.setFamily(family);
            contact.setEmail(email);
            contact.setNumber(phone);
        }
        return contact;
    }

    public Contact readDataFromEntity(){
        Contact contact = new Contact();
        String phone = "", given = "", family = "", email = "", publicKey, mime;
        int idx;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {

            idx = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
            id = cursor.getString(idx);
            System.out.println("id: " + id);

            idx = cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY);
            lookupKey = cursor.getString(idx);

            int mimeIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.MIMETYPE);
            System.out.println("verrokki: " + ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            do{
                mime = cursor.getString(mimeIdx);
                System.out.println("mime-stringi: " + mime);

                if (mime.equalsIgnoreCase(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                    int dataIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA2);
                    given = cursor.getString(dataIdx);
                    System.out.println("given on:" + given);
                    dataIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA3);
                    family = cursor.getString(dataIdx);
                    System.out.println("family on:" + family);
                } else if (ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                    int dataIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA1);
                    email = cursor.getString(dataIdx);
                } else if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equalsIgnoreCase(mime)) {
                    int dataIdx = cursor.getColumnIndex(ContactsContract.Contacts.Entity.DATA1);
                    phone = cursor.getString(dataIdx);
                    phone = PhoneNumberUtils.formatNumber(phone);
                }
            } while (cursor.moveToNext());

            publicKey = contactsHandler.readMimetypeData2(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);


        /* TODO: selvitä tarviiko tätä?
            if (publicKey == null){
                System.out.println("publicKey on nulli");
            } else {
                System.out.println("publicKey ei oo nulli vaan: " + publicKey);
                contact.setPublicKey(publicKey);
            }
        */

            contact.setPublicKey(publicKey);
            contact.setGiven(given);
            contact.setFamily(family);
            contact.setEmail(email);
            contact.setNumber(phone);
        }
        return contact;
    }


    public void scan(View view){
        Intent intent = new Intent(this, QRScanner.class);
        intent.putExtra(Constants.EXTRA_STARTED_FROM_QCB, true);
        intent.putExtra(Constants.EXTRA_ID, id);
        startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
    }

    public void onActivityResultQRScanner(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Constants.RESULT_FINISH_MAIN) {
            System.out.println("lopetetaan");
            finish();
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qr, menu);
        return true;
    }

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