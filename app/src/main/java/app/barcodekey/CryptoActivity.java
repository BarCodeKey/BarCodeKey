package app.barcodekey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import app.domain.CryptoHandler;
/*
 *Class for handling application requests for encrypt/decrypt
 * Is it working...?
 */
public class CryptoActivity extends Activity {

    private CryptoHandler ch = new CryptoHandler();
    private final boolean[] answer = {false};
    private PackageManager pm;


    /* or String EXTRA_ENCRYPT
    static final String ACTION_ENCRYPT = "com.barcodekey.action.encrypt";
    static final String ACTION_DECRYPT = "com.barcodekey.action.decrypt";
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //finds the intent that started this activity
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

       /* String name = getApplicationName(intent);
        if(!name.isEmpty())
        alertMessage(name);*/

       //checking if application is allowed to encrypt and intent has text
        if (answer[0]) {
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (("text/plain").equals(type)) {
                    handleSendText(intent);
                }
            }
        }
    }
// handles encryption and sending result back to application
    void handleSendText(Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (text != null) {
            //encoding given string
            String encoded = ch.encodeSimple(text.getBytes());

            //sending encoded string back to application
            Intent result = new Intent().putExtra(Intent.EXTRA_TEXT, encoded);
            setResult(Activity.RESULT_OK, result);
            finish();
        }

    }

    //Finding application name from intent
    public String getApplicationName(Intent intent) {
        String packageName = intent.getComponent().getPackageName();
        System.out.println("Paketin nimi: " + packageName);
        pm = getApplicationContext().getPackageManager();
        String applicationName = "";
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            applicationName = pm.getApplicationLabel(ai).toString();
            return applicationName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }


// asking user, if another application is allowed to encrypt
   public void alertMessage(String name){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.confirm);
        builder.setMessage("Allow "+name+" to encrypt?");
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        answer[0] = true;
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
       AlertDialog alert = builder.create();
       alert.show();
    }

}
