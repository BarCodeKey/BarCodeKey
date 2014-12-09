package app.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
/*
scan via intent integrator

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
*/

import app.util.Constants;

/* scan via library */
import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import info.vividcode.android.zxing.CaptureResult;

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
        System.out.println("QRScannerin onCreate");
        super.onCreate(savedInstanceState);

        startedFromQCB = getIntent().getBooleanExtra("startedFromQCB", false);
        if (startedFromQCB){
            id = getIntent().getStringExtra("id");
        }
        scan();
    }

    /**
     * Launches the scanning activity provided by an external library.
     */
    public void scan(){
        // SCAN VIA capture-activity library: (VERY SLOW BUILD)
        Intent captureIntent = new Intent(this, CaptureActivity.class);
        CaptureActivityIntents.setPromptMessage(captureIntent, "Scanning barcode...");
        startActivityForResult(captureIntent, Constants.REQUEST_CODE_CAPTURE_ACTIVITY_LIB_SCAN);

        // SCAN VIA intent integrator
        // IntentIntegrator integrator = new IntentIntegrator(this);
        // integrator.initiateScan();
    }

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
                System.out.println("lopetetaan QRScanner qcb:n kautta! ");
                setResult(Constants.RESULT_FINISH_MAIN);
                finish();
                break;
            case Constants.REQUEST_CODE_SCAN_FROM_MAIN:
                System.out.println("lopetetaan QRScanner mainin kautta! ");
                setResult(Constants.RESULT_RETURN_MAIN);
                finish();
                break;
            case Constants.REQUEST_CODE_CAPTURE_ACTIVITY_LIB_SCAN:
                onActivityResultScan(requestCode, resultCode, intent);
                break;
        }
    }

    /**
     * Continued operations after the scanning has been done.
     *
     * @param requestCode an identifying code of the previous activity
     * @param resultCode a code that tells what happened in the activity
     * @param intent android's abstract description of an operation to be performed
     */
    public void onActivityResultScan(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK){
            //with capture-activity library:
            CaptureResult res = CaptureResult.parseResultIntent(intent);
            //with intent integrator:
            //IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            Intent i = new Intent(this, QRScanResultHandler.class);
            //with capture-activity library:
            i.putExtra(Constants.EXTRA_VCARD, res.getContents().toString());
            //with intent integrator:
            //i.putExtra(Constants.EXTRA_VCARD, scanResult.getContents().toString());

            if(startedFromQCB){
                System.out.println("startedFromQCB true, id: " + id);
                i.putExtra(Constants.EXTRA_ID, id);
                startActivityForResult(i, Constants.REQUEST_CODE_SCAN_FROM_QCB); // tämä johtaa ohjelman sulkemiseen
            } else {
                startActivityForResult(i, Constants.REQUEST_CODE_SCAN_FROM_MAIN); // tämä johtaa takaisin mainiin
            }
        } else {
            // else not needed when using intent integrator for scanning
            finish();
        }
    }


    /**
     * POIS LOPULLISESTA KOODISTA?
     */
    @Override
    public void onPause(){
        System.out.println("QRScannerin onPause");
        super.onPause();
    }

    @Override
    public void onRestart(){
        System.out.println("QRScannerin onRestart");
        super.onRestart();
    }

    @Override
    public void onResume(){
        System.out.println("QRScannerin onResume");
        super.onResume();
    }

    @Override
    public void onDestroy(){
        System.out.println("QRScannerin onDestroy");
        super.onResume();
    }

}
