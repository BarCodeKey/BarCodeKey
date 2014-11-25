package app.security;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashSet;
import java.util.Set;

import app.barcodekey.R;
import app.preferences.SharedPreferencesService;

public class RemoteService extends Service {
    private CryptoHandler ch;
    private SharedPreferencesService sh;
    private String setKey = "programsList";
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    private String appName;

    @Override
    public void onCreate() {
        super.onCreate();
        ch = new CryptoHandler();
        sh = new SharedPreferencesService(this.getApplicationContext());
        appName = this.getApplication().getApplicationInfo().processName;
    }

    @Override
    public IBinder onBind(Intent intent) {

        if(intentCheck(intent)) {
            appName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            System.out.println("TÄÄLLÄ OLLAAN" + appName);
            accept();

        }
        return null;
    }
    public Boolean intentCheck(Intent intent){
        System.out.println("checkataan!!!");
        if(intent.hasExtra(Intent.EXTRA_UID) && intent.hasExtra(EXTRA_PACKAGE_NAME)){
            System.out.println("TÄSTÄ LÄPI!!!" + intent.getIntExtra(Intent.EXTRA_UID,0));
                return true;

        }
        return false;
    }
    public IBinder accept(){
       if(sh.getHashSet(setKey) == null){
            Set<String> set = new HashSet<String>();
            set.add(appName);
            sh.setHashSet(setKey, set);
        }else{
            sh.addHashSet(setKey, appName);
        }
        System.out.println("binder palautetaan");
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
