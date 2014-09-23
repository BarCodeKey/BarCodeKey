package app.barcodekey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class Main_menu extends Activity {

    QR_handler qrHandler = new QR_handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (qrHandler.readQRfromInternalStorage(this)) {
            ImageView imageView = (ImageView) findViewById(R.id.QR_code);
            qrHandler.displayQRbitmapInImageView(imageView);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void editSettings(View view) {
        Intent editInfo = new Intent(this, Settings.class);
        startActivity(editInfo);
    }

    public void createKeys(View view){

        /*
        KeyHandler kh = new KeyHandler(this);
        kh.setPublicKey("kissa");
        String publicKey = kh.getPublicKey();

        TextView textView = (TextView) findViewById(R.id.public_key);
        textView.setText(publicKey);
        */
        /**
         Intent intent = new Intent(this, Keys.class);
         startActivity(intent);
         **/

    }

    public void createQRcode(View view) {
        ImageView imageView = (ImageView) findViewById(R.id.QR_code);
        qrHandler.createQRcodeBitmap("QR-luonti toimii!");
        qrHandler.displayQRbitmapInImageView(imageView);
        qrHandler.storeQRtoInternalStorage(this);
    }

}
