package app.security;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;

import app.barcodekey.R;
import app.contacts.ContactsHandler;
import app.preferences.SharedPreferencesService;
import app.util.Constants;

public class RemoteService extends Service {
    private SharedPreferencesService sh;
    private String setKey = "programsList";
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    private String appName;

    @Override
    public void onCreate() {
        super.onCreate();
        sh = new SharedPreferencesService(this.getApplicationContext());
        appName = this.getApplication().getApplicationInfo().processName;
    }

    @Override
    public IBinder onBind(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
        if(intent.getIntExtra(Intent.EXTRA_UID,0)== Binder.getCallingUid() && intent.hasExtra(EXTRA_PACKAGE_NAME)) {
            String name = intent.getStringExtra(Intent.EXTRA_UID);
            System.out.println("TÄÄLLÄ OLLAAN");
            builder.setMessage("Do you want " + appName + " to use encryption/decryption?")
                    .setTitle("Encryption/Decryption call");


            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    accept();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });

            AlertDialog dialog = builder.create();
        }
        return null;
    }
    public IBinder accept(){
       /* if(sh.getHashSet(setKey) == null){
            Set<String> set = new HashSet<String>();
            set.add(appName);
            sh.setHashSet(setKey, set);
        }else{
            sh.addHashSet(setKey, appName);
        }*/
        return mBinder;
    }

   private final IRemoteService.Stub mBinder = new IRemoteService.Stub(){

       @Override
       public byte[] encrypt(String keyType,byte[] data, String lookupKey){

           try {
               return CryptoHandler.encryptHandler(keyType,data,getPassphrase(lookupKey));
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }
       @Override
       public byte[] decrypt(String keyType,byte[] data,String lookupKey){
           try {
               return CryptoHandler.decryptHandler(keyType,data,getPassphrase(lookupKey));
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }



   };

    public String getPassphrase(String lookupKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException {
        // TODO: DOES THIS WORK??????
        String publicKey = new ContactsHandler(this.getApplicationContext()).readMimetypeData2(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);
        return KeyHandler.getSecret(publicKey, sh.getPrivateKey());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
