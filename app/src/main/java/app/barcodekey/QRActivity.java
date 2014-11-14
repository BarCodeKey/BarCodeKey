package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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


public class QRActivity extends Activity {

    private ContactsHandler contactsHandler;
    private Uri uri;
    private String id;
    private ImageView imageView;
    private String id2;
    private String lookupKey;


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

    // kopio tavallaan
    public void updateUserInfoTextViews(Contact contact) {

        TextView textView = (TextView) findViewById(R.id.mainmenu_name_view);
        textView.setText(contact.getGiven());
        textView.append(" ");
        textView.append(contact.getFamily());

        textView = (TextView) findViewById(R.id.mainmenu_phone_number_view);
        textView.setText(contact.getNumber());

        textView = (TextView) findViewById(R.id.mainmenu_email_view);
        textView.setText(contact.getEmail());

        textView = (TextView) findViewById(R.id.mainmenu_pubkey_view);
        if (contact.getPublicKey() == null) {
            textView.setText(getText(R.string.has_no_public_key));
        } else {
            textView.setText(getText(R.string.has_public_key));
        }
    }

    public Contact readData(){
        Contact contact = new Contact(this);
        // Testailua
        int idx;
        id = "";
        id2 = "";
        uri = getIntent().getData();
        VCard vCard = new VCard();

        /* Tää kusee jossain kohtaan kivasti */
        Cursor cursor = getContentResolver().query(getIntent().getData(), null, null, null, null);
        if (cursor.moveToFirst()) {
            idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            id2 = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID);
            id = cursor.getString(idx);


            idx = cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY);
            lookupKey = cursor.getString(idx);

            String name = "", phone = "", hasPhone = "", publicKey = "", email = "";
            idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            name = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
            email = cursor.getString(idx);

            idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phone = cursor.getString(idx);


            System.out.println("Tulostetaan Urin tiedot:");
            System.out.println("id: " + id);
            System.out.println("id2: " + id2);
            System.out.println("lookup key: " + lookupKey);
            System.out.println(name);
            System.out.println(phone);
            System.out.println(email);
            publicKey = contactsHandler.readMimetypeData(id, Constants.MIMETYPE_PUBLIC_KEY);
            if (publicKey == null){
                System.out.println("publicKey on nulli");
            } else {
                System.out.println("publicKey ei oo nulli vaan: " + publicKey);
                contact.setPublicKey(publicKey);
            }
            // kaipaa vielä parantelua, selkeesti
            contact.setFamily("Sukunimi");
            contact.setGiven(name);
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