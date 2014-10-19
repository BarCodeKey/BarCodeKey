package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
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

    private static final int SETTINGS = 0;
    private QR_handler qrHandler;
    private KeyHandler kh ;
    private ProfileHandler profileHandler = null;
    private ImageView imageView;
    private boolean initialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        initialize();

        if(getIntent().getBooleanExtra("reset_keys", false) || getIntent().getBooleanExtra("change", false)){
                updateQRCode();

        }else if (qrHandler.readQRfromInternalStorage(this)) {
            qrHandler.displayQRbitmapInImageView(imageView);
        }
    }

    public void initialize(){
        if (!initialized){
            profileHandler = new ProfileHandler(this);
            qrHandler = new QR_handler();
            kh = new KeyHandler(this);
            imageView = (ImageView) findViewById(R.id.QR_code);
            initialized = true;
            this.getIntent().putExtra("reset_keys", true);
            this.getIntent().putExtra("change", true);
        }
    }

    public void updateQRCode() {
        if(getIntent().getBooleanExtra("reset_keys", false)){
            profileHandler.setPublicKey(createKeyPair());
        }
        if (getIntent().getBooleanExtra("change", false)) {
            profileHandler.readFromSharedPreferences();
        }
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
            settings.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //    startActivityForResult(settings, SETTINGS);
            startActivity(settings);
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
        startActivityForResult(captureIntent, 1);
        */

        // INTENTINTEGRATORIN KAUTTA
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }


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

    // INTENTINTEGRATORIN KAUTTA SKANNAUKSEN VASTAANOTTO
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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
