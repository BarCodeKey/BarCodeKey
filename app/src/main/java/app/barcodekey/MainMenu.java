package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.contacts.ContactsHandler;
import app.contacts.QRResultHandler;
import app.contacts.QRScanner;
import app.security.KeyHandler;
import app.contacts.VCardHandler;
import app.contacts.QRHandler;
import app.preferences.Settings;
import app.util.Constants;

public class MainMenu extends Activity {

    private static final String INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED = "finishActivityOnSaveCompleted";
    private static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";
    private static final String KEY_FORMAT = "KEY;ENCODING=B:";

    private QRHandler qrHandler;
    private KeyHandler kh;
    private VCardHandler VCardHandler = null;
    private ImageView imageView;
    private ContactsHandler contactsHandler;
    private boolean initialized = false;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialize();

        if (getIntent().getData() != null){
            // näytä kontaktin QR
            this.contactsHandler = new ContactsHandler(this);
            startFromQCB();
        } else {
            if (qrHandler.readQRfromInternalStorage(this)) {
                qrHandler.displayQRbitmapInImageView(imageView);
            }
            updateUserInfoTextViews();
        }
    }

    public void updateUserInfoTextViews() {


        TextView textView = (TextView) findViewById(R.id.mainmenu_name_view);
        textView.setText(getStringForUserInfoTextView("first_name", "No first name set"));
        textView.append(" ");
        textView.append(getStringForUserInfoTextView("last_name", ", no last name set"));

        textView = (TextView) findViewById(R.id.mainmenu_phone_number_view);
        textView.setText(getStringForUserInfoTextView("number", "No phone number set"));

        textView = (TextView) findViewById(R.id.mainmenu_email_view);
        textView.setText(getStringForUserInfoTextView("email", "No e-mail address set"));
    }

    public String getStringForUserInfoTextView(String fieldName, String emptyValueLabel) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString(fieldName, "error");
        if (value.equals("")) {
            return emptyValueLabel;
        }
        return value;
    }


    public void initialize(){
        if (!initialized){
            VCardHandler = new VCardHandler(this);
            qrHandler = new QRHandler();
            kh = new KeyHandler(this);
            imageView = (ImageView) findViewById(R.id.QR_code);

            VCardHandler.readFromSharedPreferences();
            VCardHandler.setPublicKey(kh.createKeys());
            updateQRCode();

            initialized = true;
        }
    }

    public void updateQRCode() {
        String vCard = VCardHandler.toString();
        qrHandler.createQRcodeBitmap(vCard);
        qrHandler.displayQRbitmapInImageView(imageView);
        qrHandler.storeQRtoInternalStorage(this);
    }


    public void startFromQCB(){
        String id2, lookupKey;
        Uri uri;

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
                Intent intent = new Intent(this, QRScanner.class);
                intent.putExtra("startedFromQCB", true);
                intent.putExtra("id", id);
                startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
            } else {
                System.out.println("publickey ei oo nulli");
                System.out.println(publicKey);
                // näytä QR-koodi
                //    initialize();
                //    updateQRCode(name, phone, email, publicKey);
            }

        }
    }

    /**
     * Väliaikanen metodi kokeilua varten
     */
    public void lisaaSami(View view) {
        Intent intent = new Intent(this, QRResultHandler.class);
        intent.putExtra("addSami", true);
        startActivity(intent);
    }

    public void scan(View view){
        Intent intent = new Intent(this, QRScanner.class);
        intent.putExtra("startedFromQCB", false);
        startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("tultu mainin onActivityResultiin");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);

        if(requestCode == Constants.REQUEST_CODE_SETTINGS){
            onActivityResultSettings(requestCode, resultCode, intent);
        } else if (requestCode == Constants.REQUEST_CODE_QRSCANNER){
            onActivityResultQRScanner(requestCode, resultCode, intent);
        }
    }

    public void onActivityResultQRScanner(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Constants.RESULT_FINISH_MAIN) {
            System.out.println("lopetetaan");
            finish();
        }
    }

    public void onActivityResultSettings(int requestCode, int resultCode, Intent intent){
        if (resultCode == Constants.RESULT_CHANGED) {
            VCardHandler.readFromSharedPreferences();
        } else if (resultCode == Constants.RESULT_RESET_KEYS) {
            VCardHandler.setPublicKey(kh.createKeys());
        }
        updateQRCode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        //    getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, Settings.class);
            settings.putExtra(INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);
            startActivityForResult(settings, Constants.REQUEST_CODE_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        System.out.println("Mainin onPause");
        super.onPause();
    }

    @Override
    public void onRestart(){
        updateUserInfoTextViews();
        System.out.println("Mainin onRestart");
        super.onRestart();
    }

    @Override
    public void onResume(){
        updateUserInfoTextViews();
        System.out.println("Mainin onResume");
        super.onResume();
    }

    @Override
    public void onDestroy(){
        System.out.println("Mainin onDestroy");
        super.onResume();
    }

}
