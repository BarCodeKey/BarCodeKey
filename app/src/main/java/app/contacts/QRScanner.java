package app.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import app.util.Constants;

/* ULKOINEN SKANNIKIRJASTO IMPORTIT
import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import info.vividcode.android.zxing.CaptureResult;
*/

public class QRScanner extends Activity {

    private boolean startedFromQCB;
    private String id;

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
            default: //saa oman requestCoden kun päästään integraattorista eroon
                onActivityResultScan(requestCode, resultCode, intent);
                break;
        }
    }

    // SKANNAUKSEN VASTAANOTTO, vielä aika ruma, mutta siistitään kun integraattori pois
    public void onActivityResultScan(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK){
            // CaptureResult res = CaptureResult.parseResultIntent(data);
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            Intent i = new Intent(this, QRScanResultHandler.class);
            // i.putExtra(Constants.EXTRA_VCARD, res.getContents());
            i.putExtra(Constants.EXTRA_VCARD, scanResult.getContents().toString());

            if(startedFromQCB){
                System.out.println("startedFromQCB true, id: " + id);
                i.putExtra(Constants.EXTRA_ID, id);
                startActivityForResult(i, Constants.REQUEST_CODE_SCAN_FROM_QCB); // tämä johtaa ohjelman sulkemiseen
            } else {
                startActivityForResult(i, Constants.REQUEST_CODE_SCAN_FROM_MAIN); // tämä johtaa takaisin mainiin
            }
        }
    }


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
