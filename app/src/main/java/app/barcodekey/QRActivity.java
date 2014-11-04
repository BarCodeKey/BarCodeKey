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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import app.contacts.ContactsHandler;
import app.contacts.QRResultHandler;


public class QRActivity extends Activity {

    private ContactsHandler contactsHandler;
    private static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";
    private Uri uri;
    private String id;
    private String id2;


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

            String name = "", phone = "", hasPhone = "", publicKey = "";
            idx = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            name = cursor.getString(idx);




            System.out.println("Tulostetaan Urin tiedot:");
            System.out.println(id);
            System.out.println(name);
            publicKey = "koira";
            System.out.println(publicKey);
            publicKey = contactsHandler.readMimetypeData(id, MIMETYPE_PUBLIC_KEY);
            if (publicKey == null){
                System.out.println("publickey on nulli");
                scan();
            } else{
                System.out.println("publickey ei oo nulli");
                System.out.println(publicKey);
            }
            //jos on nulli niin kaatuu
          //  System.out.println(publicKey);

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

    public void scan(){
        /* KIRJASTON KAUTTA (EXTRAHIDAS BUILD)
        Intent captureIntent = new Intent(this, CaptureActivity.class);
        CaptureActivityIntents.setPromptMessage(captureIntent, "Scanning barcode...");
        startActivityForResult(captureIntent, getResources().getInteger(.integer.REQUEST_CODE_SCAN));
        */

        // INTENTINTEGRATORIN KAUTTA
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        System.out.println("QRActivityn onActivityresult");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCide: " + resultCode);
        if (resultCode == RESULT_OK){
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                /**
                 //laitetaan testimielessä luettu qr tekstinä main menuun
                 TextView textView = (TextView) findViewById(R.id.Testiteksti);
                 textView.setText("Luettu QR: " + scanResult.getContents());
                 **/

                Intent i = new Intent(this, QRResultHandler.class);
                i.putExtra("vcard", scanResult.getContents().toString());
                i.putExtra("id", id);
                System.out.println("laitetaan id: " + id);
                startActivity(i);
            }
            // else continue with any other code you need in the method
        }
    }
}
