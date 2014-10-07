package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import app.domain.ContactsHandler;
import app.domain.KeyHandler;
import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import info.vividcode.android.zxing.CaptureResult;


public class Main_menu extends Activity {

    QR_handler qrHandler = new QR_handler();
    ContactsHandler contactsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        contactsHandler = new ContactsHandler(this);

        if(getIntent().getBooleanExtra("reset_keys", false)){
            resetKeyPair();
        } else if (qrHandler.readQRfromInternalStorage(this)) {
            ImageView imageView = (ImageView) findViewById(R.id.QR_code);
            qrHandler.displayQRbitmapInImageView(imageView);
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
            settings.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String QRCodeKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, UnsupportedEncodingException {
        KeyHandler kh = new KeyHandler(this);
        String key = kh.createKeys();
        return key;
    }

    public void resetKeyPair(){
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

    public void lisaaSami(View view) throws RemoteException, OperationApplicationException {
        this.contactsHandler.addSami();
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
                CaptureResult res = CaptureResult.parseResultIntent(data);
                System.out.println("TEKSTI JOKA LUKASTIIN: " + res.getContents());
                //Toast.makeText(this, res.getContents() + " (" + res.getFormatName() + ")", Toast.LENGTH_LONG).show();
                this.contactsHandler.addOrEditContact(res.getContents());
            } else {
                // Process comes here when “back” button was clicked for instance.
            }
        }
    }
}
