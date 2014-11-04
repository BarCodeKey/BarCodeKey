package app.barcodekey;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import app.contacts.ContactsHandler;


public class QRActivity extends Activity {

    private ContactsHandler contactsHandler;
    private static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        this.contactsHandler = new ContactsHandler(this);

        // Testailua
        int idx;
        String id = "";
        Cursor cursor = getContentResolver().query(getIntent().getData(), null, null, null, null);
        if (cursor.moveToFirst()) {
            //idx = cursor.getColumnIndex(ContactsContract.Contacts._ID);
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
}
