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
import java.security.PrivateKey;
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
            Constants.log("encrypt");
            Constants.log("data: " + data);
            Constants.log("data stringinä: " + new String(data));
            Constants.log("lookupKey: " + lookupKey);
            try {
                PublicKey publicKey = getPublic(lookupKey);
                PrivateKey privateKey = getPrivate();
                return CryptoHandler.encrypt(data, publicKey, privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        public byte[] decrypt(byte[] data,String lookupKey){
            Constants.log("decrypt");
            Constants.log("data: " + data);
            Constants.log("data stringinä: " + new String(data));
            Constants.log("lookupKey: " + lookupKey);
            try {
                PublicKey publicKey = getPublic(lookupKey);
                PrivateKey privateKey = getPrivate();
                return CryptoHandler.decrypt(data, publicKey, privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    };

    @Override
    public void onCreate() {
        super.onCreate();
        Constants.log("remoteServicen onCreate!");
        sh = new SharedPreferencesService(this.getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Constants.log("remoteServicen onBind!!");
        if(intentCheck(intent)) {
            Constants.log("tää tulee vielä?");
            appName = intent.getStringExtra(EXTRA_PACKAGE_NAME);

            Constants.log("TÄÄLLÄ OLLAAN" + appName);
            accept();
            return mBinder;

        }
        return mBinder;
    }
    public Boolean intentCheck(Intent intent){
        Constants.log("checkataan!!!");
        if(intent.hasExtra(Intent.EXTRA_UID) && intent.hasExtra(EXTRA_PACKAGE_NAME)){
            Constants.log("checkkaus läpi!!! " + intent.getIntExtra(Intent.EXTRA_UID,0));
                return true;

        }
        return false;
    }
    private void accept(){
       if(sh.getHashSet(setKey) == null){
            Set<String> set = new HashSet<String>();
            set.add(appName);
            sh.setHashSet(setKey, set);
        }else{
            sh.addHashSet(setKey, appName);
        }
        Constants.log("binder palautetaan");

    }

    public PublicKey getPublic(String lookupKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException {
        // TODO: DOES THIS WORK??????
        String publicKey = new ContactsHandler(this.getApplicationContext()).readMimetypeData(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);
        Constants.log("getPublicKey: " + publicKey);
        return KeyHandler.decodePublic(publicKey);
    }

    public PrivateKey getPrivate() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        String privKey = sh.getPrivateKey();
        Constants.log("getPrivateKey:" + privKey);
        return KeyHandler.decodePrivate(privKey);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
