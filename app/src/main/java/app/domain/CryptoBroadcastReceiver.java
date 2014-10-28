package app.domain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//Receives intents from other applications (right now BCK-messager)
public class CryptoBroadcastReceiver extends BroadcastReceiver {

    private CryptoHandler ch = new CryptoHandler();
    //CryptoBroadcastReceivers actions
    public static final String ACTION_ENCRYPT = "app.barcodekey.action.encryptAPI";
    public static final String ACTION_DECRYPT = "app.barcodekey.action.decryptAPI";
    //2nd application actions and extras
    public final static String EXTRA_MESSAGE = "barcodekey.bck_messager.MESSAGE";
    public final static String ACTION_RECEIVE = "barcodekey.bck_messager.RECEIVE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        System.out.println("Tultu kryptaukseen!!!!!");

        if (action.equals(ACTION_ENCRYPT) && type != null) {
            //checking if type is correct
            if (intent.hasExtra(EXTRA_MESSAGE)) {
                handleEncrypt(context,intent);
            }
        }
        else if(action.equals(ACTION_DECRYPT) && type != null){
            if (intent.hasExtra(EXTRA_MESSAGE)) {
                handleDecrypt(context,intent);
            }
        }
    }


    private void handleEncrypt(Context context, Intent intent) {
        String text = intent.getStringExtra(EXTRA_MESSAGE);
        if (text != null) {
            //encoding given string
            String encrypted = ch.encryptSimple(text.getBytes());

            //sending encrypted string back to application
            Intent result = new Intent(ACTION_RECEIVE).putExtra(EXTRA_MESSAGE, encrypted);
            context.sendBroadcast(result);
        }
    }

    private void handleDecrypt(Context context,Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (text != null) {
            //decoding given string
            String secret = ch.decryptSimple(text.getBytes());

            //sending decoded string back to application
            Intent result = new Intent(ACTION_RECEIVE).putExtra(EXTRA_MESSAGE, secret);
            context.sendBroadcast(result);
        }
    }
}
