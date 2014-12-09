package app.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import app.util.Constants;

/* ULKOINEN SKANNIKIRJASTO IMPORTIT
import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import info.vividcode.android.zxing.CaptureResult;
*/

/**
 * Handles the usage of external libraries that provide the QR code scanning functionality
 * and performs the scans.
 */
public class QRScanner extends Activity {

    private boolean startedFromQCB;
    private String id;

    /**
     * Executed when the view is started. Checks where the user comes from, performs needed
     * operations and starts the scan.
     *
     * @param savedInstanceState saved application state to be recreated if present
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(Constants.LOG_TAG, "QRScannerin onCreate");
        super.onCreate(savedInstanceState);

        startedFromQCB = getIntent().getBooleanExtra("startedFromQCB", false);
        if (startedFromQCB){
            id = getIntent().getStringExtra("id");
        }
        scan();
    }

    /**
     * Launches the scanning acitivity provided by an external library.
     */
    public void scan(){
        /* KIRJASTON KAUTTA (EXTRAHIDAS BUILD)
        Intent captureIntent = new Intent(this, CaptureActivity.class);
        CaptureActivityIntents.setPromptMessage(captureIntent, "Scanning barcode...");
        startActivityForResult(captureIntent, getResources().getInteger(.integer.REQUEST_CODE_SCAN));
        */

        // INTENTINTEGRATORIN KAUTTA
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    // siistitään

    /**
     * Performs operations after the scanning has been done.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode){
            case Constants.REQUEST_CODE_SCAN_FROM_QCB:
                Log.v(Constants.LOG_TAG, "lopetetaan QRScanner qcb:n kautta");
                setResult(Constants.RESULT_FINISH_MAIN);
                finish();
                break;
            case Constants.REQUEST_CODE_SCAN_FROM_MAIN:
                Log.v(Constants.LOG_TAG, "lopetetaan QRScanner mainin kautta");
                setResult(Constants.RESULT_RETURN_MAIN);
                finish();
                break;
            default: //saa oman requestCoden kun päästään integraattorista eroon
                onActivityResultScan(requestCode, resultCode, intent);
                break;
        }
    }

    // SKANNAUKSEN VASTAANOTTO, vielä aika ruma, mutta siistitään kun integraattori pois

    /**
     * Continued operations after the scanning has been done.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    public void onActivityResultScan(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK){
            // CaptureResult res = CaptureResult.parseResultIntent(data);
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            Intent i = new Intent(this, QRScanResultHandler.class);
            // i.putExtra(Constants.EXTRA_VCARD, res.getContents());
            i.putExtra(Constants.EXTRA_VCARD, scanResult.getContents().toString());

            if(startedFromQCB){
                Log.v(Constants.LOG_TAG, "startedFromQCB true, id: " + id);
                i.putExtra(Constants.EXTRA_ID, id);
                startActivityForResult(i, Constants.REQUEST_CODE_SCAN_FROM_QCB); // tämä johtaa ohjelman sulkemiseen
            } else {
                startActivityForResult(i, Constants.REQUEST_CODE_SCAN_FROM_MAIN); // tämä johtaa takaisin mainiin
            }
        }
    }


    /**
     * POIS LOPULLISESTA KOODISTA?
     */
    @Override
    public void onPause(){
        Log.v(Constants.LOG_TAG, "QRScannerin onPause");
        super.onPause();
    }

    @Override
    public void onRestart(){
        Log.v(Constants.LOG_TAG, "QRScannerin onRestart");
        super.onRestart();
    }

    @Override
    public void onResume(){
        Log.v(Constants.LOG_TAG, "QRScannerin onResume");
        super.onResume();
    }

    @Override
    public void onDestroy(){
        System.out.println("QRScannerin onDestroy");
        Log.v(Constants.LOG_TAG, "QRScannerin onDestroy");
        super.onResume();
    }

}
