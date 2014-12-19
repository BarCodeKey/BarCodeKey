package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import java.security.KeyPair;

import app.contacts.QRMaker;
import app.contacts.QRScanner;
import app.preferences.SharedPreferencesService;
import app.security.KeyHandler;
import app.contacts.Contact;
import app.preferences.Settings;

import app.util.Constants;
import app.util.FileService;


/**
 * Handles the operations needed in the main view of the app when the app is started and
 * when the app is used.
 */
public class MainMenu extends Activity {

    private SharedPreferencesService sharedPreferencesService;
    private Contact user;
    private ImageView imageView;
    private boolean initialized = false;

    /**
     * This method is run when the main view is "created" = started. Sets everything up.
     *
     * @param savedInstanceState saved application state to be recreated if present
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialize();



        //Constants.log(new String(encrypted));
            //byte[] decrypted = CryptoHandler.decryptECIES(encrypted,Akp.getPublic(),Bkp.getPrivate(),random);
            //Constants.log(new String(decrypted));


        if (FileService.readQRfromInternalStorage(this, Constants.QR_FILENAME) != null) {
            this.imageView.setImageBitmap(FileService.readQRfromInternalStorage(this, Constants.QR_FILENAME));
        }
        updateUserInfoTextViews();

        /*KeyPair Akp = KeyHandler.createKeys();
        KeyPair Bkp = KeyHandler.createKeys();

        Constants.log("Avaimet luotu!!!!"+ new String(Akp.getPublic().getEncoded()));


        try {
            byte[] encrypted = CryptoHandler.encrypt("trololoo","Kissa".getBytes(),Akp.getPublic(),Bkp.getPrivate());
            Constants.log(new String(encrypted)+ "??????");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * A helper method that sets values for some useful variables for the class
     * when the main view is created.
     */
    public void initialize(){
        if (!initialized){
            user = new Contact();
            sharedPreferencesService = new SharedPreferencesService(this);
            imageView = (ImageView) findViewById(R.id.QR_code);
            user = sharedPreferencesService.getUser();
            initialized = true;
        }
    }

    /**
     * Checks the user information that is set in the app settings and the public key
     * of the user. Updates the text fields in the main view that show said information.
     */
    public void updateUserInfoTextViews() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView textView = (TextView) findViewById(R.id.mainmenu_name_view);
        textView.setText(preferences.getString("first_name", "No first name set"));
        textView.append(" ");
        textView.append(preferences.getString("last_name", ", no last name set"));

        textView = (TextView) findViewById(R.id.mainmenu_phone_number_view);
        textView.setText(preferences.getString("number", "No phone number set"));
        if (textView.getText().length() == 0) {
            textView.setText("No phone number set");
        }

        textView = (TextView) findViewById(R.id.mainmenu_email_view);
        textView.setText(preferences.getString("email", "No e-mail address set"));

        textView = (TextView) findViewById(R.id.mainmenu_pubkey_view);
        if (user.getPublicKey().equals("")) {
            textView.setText(getText(R.string.user_has_no_public_key));
        } else {
            textView.setText(getText(R.string.your_public_key));
        }
    }

    /**
     * Updates the viewed QR code so it corresponds to the user's information.
     */
    public void updateQRCode() {
        String vCard = user.toString();
        Bitmap image = QRMaker.createQRcodeBitmap(vCard, Constants.QR_BITMAP_WIDTH, Constants.QR_BITMAP_HEIGHT);
        this.imageView.setImageBitmap(image);
        FileService.storeQRtoInternalStorage(this, image, Constants.QR_FILENAME);
}

    /**
     * Starts the QR code scanning when the "scan" button is pressed.
     *
     * @param view
     */
    public void scan(View view){
        Intent intent = new Intent(this, QRScanner.class);
        intent.putExtra(Constants.EXTRA_STARTED_FROM_QCB, false);
        startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
    }

    /**
     * Creates a secure key pair for the user.
     */
    public void setKeys()throws Exception{
        KeyPair keyPair = KeyHandler.createKeys();
        String publicKey = KeyHandler.base64Encode(keyPair.getPublic().getEncoded());
        String privateKey = KeyHandler.base64Encode(keyPair.getPrivate().getEncoded());
        user.setPublicKey(new String(publicKey));
        sharedPreferencesService.setPublicKey(new String(publicKey));
        sharedPreferencesService.setPrivateKey(new String(privateKey));
        Constants.log("setKeys(), publicKey: " + publicKey);
        Constants.log("setKeys(), privateKey: " + privateKey);

    }

    /**
     * Tells the main activity how to handle the situation when the user is returned back
     * from scanning or the settings view.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        Constants.log("tultu mainin onActivityResultiin");
        Constants.log("requestCode: " + requestCode);
        Constants.log("resultCode: " + resultCode);

        switch(requestCode){
            case Constants.REQUEST_CODE_SETTINGS:
                try {
                    onActivityResultSettings(requestCode, resultCode, intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_CODE_QRSCANNER:
                onActivityResultQRScanner(requestCode, resultCode, intent);
                break;
            case Constants.REQUEST_CODE_PICK_CONTACT:
                onActivityResultPickContact(requestCode, resultCode, intent);
        }
    }

    /**
     * Operations to perform after the user returns from QR code scanning.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    public void onActivityResultPickContact(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK){
            Uri uri = intent.getData();
            Constants.log("Pickeriltä saatiin URI: " + uri);

            Intent i = new Intent(this, QRActivity.class);
            i.setData(uri);
            i.putExtra("entity", true);
            startActivity(i);
        }
    }

    public void onActivityResultQRScanner(int requestCode, int resultCode, Intent intent) {
        // do nothing
    }

    /**
     * Operations to perform when the user returns from the app's settings view.
     * If there has been changes, updates the QR code that is displayed in the main
     * view accordingly.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    public void onActivityResultSettings(int requestCode, int resultCode, Intent intent)throws Exception{
        switch(resultCode){
            case Constants.RESULT_CHANGED:
                user = sharedPreferencesService.getUser();
                if (user.getPublicKey() == null || user.getPublicKey().equals("")){
                    setKeys();
                }
                updateQRCode();
                break;
            case Constants.RESULT_RESET_KEYS:
                user = sharedPreferencesService.getUser();
                setKeys();
                updateQRCode();
                break;
        }
        System.out.println("jotain on muutettu, tulostetaan käyttäjä");
        System.out.println(user);
    }

    /**
     * Initializes the menu that is displayed when the user presses the android's
     * menu button.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        //    getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles the menu clicks, takes the user to the settings view when he/she
     * presses the "Settings" button etc.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, Settings.class);
            settings.putExtra(Constants.INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);
            startActivityForResult(settings, Constants.REQUEST_CODE_SETTINGS);
            return true;
        } else if (id == R.id.action_show_contact){
            showContact();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * POISTETAAN LOPULLISESTA?
     */
    public void showContact(){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, Constants.REQUEST_CODE_PICK_CONTACT);
    }

    @Override
    public void onPause(){
        Constants.log("Mainin onPause");
        super.onPause();
    }

    /**
     * Refreshes the user information text views when the main view is restarted.
     */
    @Override
    public void onRestart(){
        updateUserInfoTextViews();
        Constants.log("Mainin onRestart");
        super.onRestart();
    }

    /**
     * Refreshes the user information text views when the main view is resumed from
     * a suspended state.
     */
    @Override
    public void onResume(){
        updateUserInfoTextViews();
        Constants.log("Mainin onResume");
        super.onResume();
    }

    /**
     * POISTETAAN LOPULLISESTA?
     */
    @Override
    public void onDestroy(){
        Constants.log("Mainin onDestroy");
        super.onResume();
    }

}
