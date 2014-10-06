package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import app.domain.ContactsHandler;
import app.domain.KeyHandler;




public class Main_menu extends Activity {

    QR_handler qrHandler = new QR_handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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
   /* @Override
    public void onResume() {
        try {
            createQRcode();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/


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

}
