package app.barcodekey;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import app.contacts.QRMaker;
import app.contacts.QRScanner;
import app.preferences.SharedPreferencesService;
import app.security.CryptoHandler;
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



        //System.out.println(new String(encrypted));
            //byte[] decrypted = CryptoHandler.decryptECIES(encrypted,Akp.getPublic(),Bkp.getPrivate(),random);
            //System.out.println(new String(decrypted));


        if (FileService.readQRfromInternalStorage(this, Constants.QR_FILENAME) != null) {
            this.imageView.setImageBitmap(FileService.readQRfromInternalStorage(this, Constants.QR_FILENAME));
        }
        updateUserInfoTextViews();

        /*KeyPair Akp = KeyHandler.createKeys();
        KeyPair Bkp = KeyHandler.createKeys();

        System.out.println("Avaimet luotu!!!!"+ new String(Akp.getPublic().getEncoded()));


        try {
            byte[] encrypted = CryptoHandler.encrypt("trololoo","Kissa".getBytes(),Akp.getPublic(),Bkp.getPrivate());
            System.out.println(new String(encrypted)+ "??????");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void initialize(){
        if (!initialized){
            user = new Contact();
            sharedPreferencesService = new SharedPreferencesService(this);
            imageView = (ImageView) findViewById(R.id.QR_code);

            user = sharedPreferencesService.getUser();
            initialized = true;
        }
    }

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

    public void updateQRCode() {
        String vCard = user.toString();
        Bitmap image = QRMaker.createQRcodeBitmap(vCard, Constants.QR_BITMAP_WIDTH, Constants.QR_BITMAP_HEIGHT);
        this.imageView.setImageBitmap(image);
        FileService.storeQRtoInternalStorage(this, image, Constants.QR_FILENAME);
}
    public void scan(View view){
        Intent intent = new Intent(this, QRScanner.class);
        intent.putExtra(Constants.EXTRA_STARTED_FROM_QCB, false);
        startActivityForResult(intent, Constants.REQUEST_CODE_QRSCANNER);
    }

    public void setKeys(){
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
                user = sharedPreferencesService.getUser();
                if (user.getPublicKey() == null || user.getPublicKey().equals("")){
                    setKeys();
                }
                updateQRCode();
                break;
            case Constants.RESULT_RESET_KEYS:
                setKeys();
                updateQRCode();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        //    getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
