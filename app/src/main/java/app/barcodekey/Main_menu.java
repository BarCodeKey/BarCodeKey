package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import app.domain.KeyHandler;
import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import info.vividcode.android.zxing.CaptureResult;



public class Main_menu extends Activity {

    QR_handler qrHandler = new QR_handler();
    Boolean valuesChanged = false;
    KeyHandler kh ;
    vCard_maker info;

    Boolean_handler boolean_handler ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        boolean_handler = new Boolean_handler(this);

        if(info == null) {
            try {
                info = new vCard_maker(this);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        boolean_handler.setValuesChanged();
        if(getIntent().getBooleanExtra("reset_keys", false)){
            resetKeyPair();
        }else if (qrHandler.readQRfromInternalStorage(this)) {
            ImageView imageView = (ImageView) findViewById(R.id.QR_code);
            qrHandler.displayQRbitmapInImageView(imageView);
        }
    }

    public void refresh(View view){
        info.setAll();
        createQRCode();
    }

    public void createQRCode() {
        String vCard = info.toVCard();
        ImageView imageView = (ImageView) findViewById(R.id.QR_code);
        qrHandler.createQRcodeBitmap(vCard);
        qrHandler.displayQRbitmapInImageView(imageView);
        qrHandler.storeQRtoInternalStorage(this);
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
            startActivityForResult(settings, 7);
            onActivityResult(7,RESULT_OK);
            return true;
        };
        return super.onOptionsItemSelected(item);
    }
    //skips this part for some reason...
    public void onActivityResult(int requestCode, int resultCode){
            if ((boolean_handler.isValuesChanged()).contains("true")) {
                info.setAll();
                createQRCode();
                boolean_handler.setValuesChanged();
                System.out.println("arvot muutettu!!!!!!!!!!!!");
            }else{
                System.out.println("meni ohi :((((");
            }
    }

    public String QRCodeKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, UnsupportedEncodingException {
        String key = kh.createKeys();
        return key;
    }


    public void resetKeyPair() {
        ImageView imageView = (ImageView) findViewById(R.id.QR_code);
        try {
            qrHandler.createQRcodeBitmap(QRCodeKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        qrHandler.displayQRbitmapInImageView(imageView);
        qrHandler.storeQRtoInternalStorage(this);

    }

    public void scan(View view){
        // Create intent.
        Intent captureIntent = new Intent(this, CaptureActivity.class);
        // Using `CaptureActivityIntents`, set parameters to an intent.
        // (There is no requisite parameter to set to an intent.)
        // For instance, `setPromptMessage` method set prompt message displayed on `CaptureActivity`.
        CaptureActivityIntents.setPromptMessage(captureIntent, "Scanning barcode...");
        // Start activity.
        startActivityForResult(captureIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                TextView textView = (TextView) findViewById(R.id.Testiteksti);
                CaptureResult res = CaptureResult.parseResultIntent(data);
                textView.setText(res.getContents());
                this.contactsHandler.addOrEditContact(res.getContents());
            } else {
                TextView textView = (TextView) findViewById(R.id.Testiteksti);
                textView.setText("mentiin elseen onactivityresult");
            }
        }
    }
}
