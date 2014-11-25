package app.security;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashSet;
import java.util.Set;

import app.barcodekey.R;
import app.preferences.SharedPreferencesService;

public class RemoteService extends Service {
    private CryptoHandler ch;
    private SharedPreferencesService sh;
    private String setKey = "programsList";
    private String extra = "application name";
    private String appName;

    @Override
    public void onCreate() {
        super.onCreate();
        ch = new CryptoHandler();
        sh = new SharedPreferencesService(ContextHandler.getAppContext());
        appName = this.getApplication().getApplicationInfo().processName;
    }

    @Override
    public IBinder onBind(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContextHandler.getAppContext());

        if(intent.hasExtra(extra)){

            builder.setMessage("Do you want "+appName+" to use encryption/decryption?")
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
        if(sh.getHashSet(setKey) == null){
            Set<String> set = new HashSet<String>();
            set.add(appName);
            sh.setHashSet(setKey, set);
        }else{
            sh.addHashSet(setKey, appName);
        }
        return mBinder;
    }

   private final IRemoteService.Stub mBinder = new IRemoteService.Stub(){

       @Override
       public byte[] encrypt(String type, byte[] data, String uri){

           try {
               return ch.encryptHandler(type,data,uri);
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }
       @Override
       public byte[] decrypt(String type, byte[] data, String uri){
           try {
               return ch.decryptHandler(type,data,uri);
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }

   };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
