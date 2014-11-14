package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class MainMenu extends Activity {

    private SharedPreferencesService sharedPreferencesService;
    private Contact user;
    private ImageView imageView;
    private boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialize();

        if (FileService.readQRfromInternalStorage(this, Constants.QR_FILENAME) != null) {
            this.imageView.setImageBitmap(FileService.readQRfromInternalStorage(this, Constants.QR_FILENAME));
        }
        updateUserInfoTextViews();
    }

    public void initialize(){
        if (!initialized){
            user = new Contact(this);
            sharedPreferencesService = new SharedPreferencesService(this);
            imageView = (ImageView) findViewById(R.id.QR_code);

            user.readFromSharedPreferences();
            if(user.getPublicKey() == null){ // If we don't have keys we have to make them
                resetKeys();
            }
            updateQRCode();
            initialized = true;
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

        textView = (TextView) findViewById(R.id.mainmenu_pubkey_view);
        if (user.getPublicKey() == null) {
            textView.setText(getText(R.string.user_has_no_public_key));
        } else {
            textView.setText(getText(R.string.your_public_key));
        }
    }

    public String getStringForUserInfoTextView(String fieldName, String emptyValueLabel) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = preferences.getString(fieldName, "error");
        if (value.equals("")) {
            return emptyValueLabel;
        }
        return value;
    }

    public void updateQRCode() {
        String vCard = user.toString();
        Bitmap image = QRMaker.createQRcodeBitmap(vCard, Constants.QR_WIDTH, Constants.QR_HEIGHT);
        this.imageView.setImageBitmap(image);
        FileService.storeQRtoInternalStorage(this, image, Constants.QR_FILENAME);
}
    public void scan(View view){
        Intent intent = new Intent(this, QRScanner.class);
        intent.putExtra(Constants.EXTRA_STARTED_FROM_QCB, false);
        startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
    }

    public void resetKeys(){
        KeyPair keyPair = KeyHandler.createKeys();
        String publicKey = KeyHandler.base64Encode(keyPair.getPublic().getEncoded());
        String privateKey = KeyHandler.base64Encode(keyPair.getPrivate().getEncoded());
        user.setPublicKey(publicKey);
        sharedPreferencesService.setPublicKey(publicKey);
        sharedPreferencesService.setPrivateKey(privateKey);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("tultu mainin onActivityResultiin");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);

        switch(requestCode){
            case Constants.REQUEST_CODE_SETTINGS:
                onActivityResultSettings(requestCode, resultCode, intent);
                break;
            case Constants.REQUEST_CODE_QRSCANNER:
                onActivityResultQRScanner(requestCode, resultCode, intent);
                break;
        }
    }

    public void onActivityResultQRScanner(int requestCode, int resultCode, Intent intent) {
        // do nothing
    }

    public void onActivityResultSettings(int requestCode, int resultCode, Intent intent){
        boolean change = false;
        switch(resultCode){
            case Constants.RESULT_CHANGED:
                user.readFromSharedPreferences();
                change = true;
            case Constants.RESULT_RESET_KEYS:
                resetKeys();
                change = true;
                break;
        }
        if (change){
            updateQRCode();
        }
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
            settings.putExtra(Constants.INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED, true);
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
