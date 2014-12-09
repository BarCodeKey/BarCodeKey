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
import java.security.PublicKey;
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


    private final IRemoteService.Stub mBinder = new IRemoteService.Stub(){

        @Override
        public byte[] encrypt(byte[] data, String lookupKey){
            try {
                return CryptoHandler.encrypt(data, getKeyString(lookupKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        public byte[] decrypt(byte[] data,String lookupKey){
            try {
                return CryptoHandler.decrypt(data, getKeyString(lookupKey));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("remoteServicen onCreate!");
        sh = new SharedPreferencesService(this.getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("remoteServicen onBind!!");
        if(intentCheck(intent)) {
            System.out.println("tää tulee vielä?");
            appName = intent.getStringExtra(EXTRA_PACKAGE_NAME);

            System.out.println("TÄÄLLÄ OLLAAN" + appName);
            accept();
            return mBinder;

        }
        return mBinder;
    }
    public Boolean intentCheck(Intent intent){
        System.out.println("checkataan!!!");
        if(intent.hasExtra(Intent.EXTRA_UID) && intent.hasExtra(EXTRA_PACKAGE_NAME)){
            System.out.println("checkkaus läpi!!! " + intent.getIntExtra(Intent.EXTRA_UID,0));
                return true;

        }
        return false;
    }
    public void accept(){
       if(sh.getHashSet(setKey) == null){
            Set<String> set = new HashSet<String>();
            set.add(appName);
            sh.setHashSet(setKey, set);
        }else{
            sh.addHashSet(setKey, appName);
        }
        System.out.println("binder palautetaan");

    }

    public String getKeyString(String lookupKey) {

        String publicKey = new ContactsHandler(this.getApplicationContext()).readMimetypeData2(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);
        return publicKey;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
