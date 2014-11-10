package app.barcodekey;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import app.contacts.ContactsHandler;
import app.contacts.QRHandler;
import app.contacts.QRResultHandler;
import app.contacts.VCardHandler;
import app.security.KeyHandler;
import ezvcard.VCard;
import ezvcard.property.StructuredName;


public class QRActivity extends Activity {

    private ContactsHandler contactsHandler;
    private static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";
    private static final String KEY_FORMAT = "KEY;ENCODING=B:";
    private Uri uri;
    private String id;
    private VCardHandler vCardHandler;
    private QRHandler qrHandler;
    private ImageView imageView;
    private boolean initialized = false;
    private String id2;
    private String lookupKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        this.contactsHandler = new ContactsHandler(this);

        // Testailua
        int idx;
        id = "";
        id2 = "";
        uri = getIntent().getData();
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
            publicKey = "koira";
            System.out.println(publicKey);
            publicKey = contactsHandler.readMimetypeData(id, MIMETYPE_PUBLIC_KEY);
            if (publicKey == null) {
                System.out.println("publickey on nulli");
                System.out.println("skannataan");
                scan();
            } else {
                System.out.println("publickey ei oo nulli");
                System.out.println(publicKey);
                initialize();
                updateQRCode(name, phone, email, publicKey);
            }

        }

    }

    public void initialize() {
        if (!initialized) {
            vCardHandler = new VCardHandler(this);
            qrHandler = new QRHandler();
            imageView = (ImageView) findViewById(R.id.QR_code);
            initialized = true;
        }
}

    public void updateQRCode(String name, String phone, String email, String publicKey) {

        VCard vcard = new VCard();

        StructuredName n = new StructuredName();
        n.setFamily(name);
        n.setGiven("");
        vcard.setStructuredName(n);
        vcard.addTelephoneNumber(phone);
        vcard.addEmail(email);
        //cleanPublicKeyFromString(vcard.write(), publicKey);

        qrHandler.createQRcodeBitmap(cleanPublicKeyFromString(vcard.write(), publicKey));
        qrHandler.displayQRbitmapInImageView(imageView);
    }

    public String cleanPublicKeyFromString(String string, String publicKey) {
        String[] lines = string.split("\\r?\\n");
        String cleanString = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("PRODID")) {
                cleanString += KEY_FORMAT + publicKey + "\n";
            } else {
                cleanString += lines[i] + "\n";
            }
        }
        return cleanString;
    }

    public void scan() {
        /* KIRJASTON KAUTTA (EXTRAHIDAS BUILD)
        Intent captureIntent = new Intent(this, CaptureActivity.class);
        CaptureActivityIntents.setPromptMessage(captureIntent, "Scanning barcode...");
        startActivityForResult(captureIntent, getResources().getInteger(.integer.REQUEST_CODE_SCAN));
        */

        // INTENTINTEGRATORIN KAUTTA
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
        System.out.println("skannattu?");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("QRActivityn onActivityresult");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);
        if (resultCode == RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                System.out.println("scanResult ei ollu nulli");
                /**
                 //laitetaan testimielessä luettu qr tekstinä main menuun
                 TextView textView = (TextView) findViewById(R.id.Testiteksti);
                 textView.setText("Luettu QR: " + scanResult.getContents());
                 **/
                Intent i = new Intent(this, QRResultHandler.class);
                i.putExtra("id", id);
                i.putExtra("vcard", scanResult.getContents().toString());
         //       i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
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