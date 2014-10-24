package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import app.domain.ContactsHandler;
import app.domain.KeyHandler;
import app.domain.ProfileHandler;
import app.domain.QR_handler;
/* ULKOINEN SKANNIKIRJASTO IMPORTIT
import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import info.vividcode.android.zxing.CaptureResult;
*/
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class Main_menu extends Activity {

    private static final String INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED = "finishActivityOnSaveCompleted";

    private QR_handler qrHandler;
    private KeyHandler kh ;
    private ProfileHandler profileHandler = null;
    private ImageView imageView;
    private boolean initialized = false;
    private int RESULT_CHANGED;
    private int RESULT_RESET_KEYS;
    private int REQUEST_CODE_SETTINGS;
    private int REQUEST_CODE_SCAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        System.out.println("RESSI " + RESULT_CHANGED);
        initialize();

        if (qrHandler.readQRfromInternalStorage(this)) {
            qrHandler.displayQRbitmapInImageView(imageView);
        }
    }

    public void initialize(){
        if (!initialized){
            RESULT_CHANGED = getResources().getInteger(R.integer.RESULT_CHANGED);
            RESULT_RESET_KEYS = getResources().getInteger(R.integer.RESULT_RESET_KEYS);
            REQUEST_CODE_SETTINGS = getResources().getInteger(R.integer.REQUEST_CODE_SETTINGS);
            REQUEST_CODE_SCAN = getResources().getInteger(R.integer.REQUEST_CODE_SCAN);
            profileHandler = new ProfileHandler(this);
            qrHandler = new QR_handler();
            kh = new KeyHandler(this);
            imageView = (ImageView) findViewById(R.id.QR_code);

            profileHandler.readFromSharedPreferences();
            profileHandler.setPublicKey(createKeyPair());
            updateQRCode();

            initialized = true;
        }
    }

    public void updateQRCode() {

        String vCard = profileHandler.toString();
        qrHandler.createQRcodeBitmap(vCard);
        qrHandler.displayQRbitmapInImageView(imageView);
        qrHandler.storeQRtoInternalStorage(this);
    }

    public String createKeyPair(){
        String key = kh.createKeys();
        return key;
    }

    /**
     * Väliaikanen metodi kokeilua varten
     */
    public void lisaaSami(View view) {
        Intent intent = new Intent(this, ContactsHandler.class);
        intent.putExtra("addSami", true);
        startActivity(intent);
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
            startActivityForResult(settings, REQUEST_CODE_SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        System.out.println("Mainin onResume");
        super.onResume();
    }

    public void scan(View view){
        /* KIRJASTON KAUTTA (EXTRAHIDAS BUILD)
        Intent captureIntent = new Intent(this, CaptureActivity.class);
        CaptureActivityIntents.setPromptMessage(captureIntent, "Scanning barcode...");
        startActivityForResult(captureIntent, getResources().getInteger(.integer.REQUEST_CODE_SCAN));
        */

        // INTENTINTEGRATORIN KAUTTA
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }




    // INTENTINTEGRATORIN KAUTTA SKANNAUKSEN VASTAANOTTO
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println("tultu mainin onActivityResultiin");
        System.out.println("requestCode: " + requestCode);
        System.out.println("resultCode: " + resultCode);

        if(requestCode == REQUEST_CODE_SETTINGS){
            onActivityResultSettings(requestCode, resultCode, intent);
        }else if(requestCode == REQUEST_CODE_SCAN) {
            onActivityResultScan(requestCode, resultCode, intent);
        }
    }

    public void onActivityResultSettings(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_CHANGED) {
            profileHandler.readFromSharedPreferences();
        } else if (resultCode == RESULT_RESET_KEYS) {
            profileHandler.setPublicKey(createKeyPair());
        }
        updateQRCode();
    }

    public void onActivityResultScan(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK){
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                /**
                 //laitetaan testimielessä luettu qr tekstinä main menuun
                 TextView textView = (TextView) findViewById(R.id.Testiteksti);
                 textView.setText("Luettu QR: " + scanResult.getContents());
                 **/

                Intent i = new Intent(this, ContactsHandler.class);
                i.putExtra("vcard", scanResult.getContents().toString());
                startActivity(i);
            }
            // else continue with any other code you need in the method
        }
    }

    /*
    public void onActivityResultScan(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK) {
            CaptureResult res = CaptureResult.parseResultIntent(data);

            //laitetaan testimielessä luettu qr tekstinä main menuun
            TextView textView = (TextView) findViewById(R.id.Testiteksti);
            textView.setText("Luettu QR: " + res.getContents());

            //ContactsHandler contactsHandler = new ContactsHandler(this);
            //contactsHandler.addOrEditContact(res.getContents());
        } else {
            //scan didn't work
        }
    }
    */

        /* KIRJASTON KAUTTA SKANNAUKSEN VASTAANOTTO
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                CaptureResult res = CaptureResult.parseResultIntent(data);

                //laitetaan testimielessä luettu qr tekstinä main menuun
                TextView textView = (TextView) findViewById(R.id.Testiteksti);
                textView.setText("Luettu QR: " + res.getContents());

                //ContactsHandler contactsHandler = new ContactsHandler(this);
                //contactsHandler.addOrEditContact(res.getContents());
            } else {
                //scan didn't work
            }
        }
    }
    */

    @Override
    public void onPause(){
        System.out.println("Mainin onPause");
        super.onPause();
    }

    @Override
    public void onRestart(){
        System.out.println("Mainin onRestart");
        super.onRestart();
    }


}
