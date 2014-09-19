package app.barcodekey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;
//import org.apache.commons.codec.binary.Base64;


public class Keys extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String privateKey = "jotainSatunnaisiaMerkkejä";
        TextView keyText = new TextView(this);
        keyText.setTextSize(20);
        keyText.setText(privateKey);
        setContentView(keyText);

        //setContentView(R.layout.activity_keys);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.keys, menu);
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


    public void createKeys(Context context) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {



        //Generating 1024 RSA keypair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        String alias = "keys";

        KeyPair kp = kpg.generateKeyPair();
        KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());

        PrivateKey privateKey = kp.getPrivate();
        String key = privateKey.toString();
        //byte[] encodedKey = Base64.encodeBase64(key.getBytes());


        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry((javax.crypto.SecretKey) privateKey);
        //store.setKeyEntry(alias,privateKey,null);


         /* String alias = "keys";
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        Date end = cal.getTime();*/
         /* kpg.initialize(new KeyPairGeneratorSpec.Builder(context.getApplicationContext())
            .setAlias(alias)
           .setStartDate(now)
           .setEndDate(end)
           .setSerialNumber(BigInteger.valueOf(1))
           .setSubject(new X500Principal("CN=test1"))
           .build());*/
    }

}
