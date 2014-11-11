package app.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import app.barcodekey.R;
import app.util.Constants;

/**
 * Created by szetk on 11/11/14.
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
        if (requestCode == Constants.REQUEST_CODE_SCAN_FROM_QCB){
            System.out.println("lopetetaan QRScanner qcb:n kautta! ");
            setResult(Constants.RESULT_FINISH_MAIN);
            finish();
        } else if (requestCode == Constants.REQUEST_CODE_SCAN_FROM_MAIN) {
            System.out.println("lopetetaan QRScanner mainin kautta! ");
            setResult(Constants.RESULT_RETURN_MAIN);
            finish();
        } else {
            onActivityResultScan(requestCode, resultCode, intent);

        }
    }

    // SKANNAUKSEN VASTAANOTTO, vielä aika ruma, mutta siistitään
    public void onActivityResultScan(int requestCode, int resultCode, Intent intent){
        if (resultCode == RESULT_OK){
            // CaptureResult res = CaptureResult.parseResultIntent(data);
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            Intent i = new Intent(this, QRResultHandler.class);
            // i.putExtra("vcard", res.getContents());
            i.putExtra("vcard", scanResult.getContents().toString());

            if(startedFromQCB){
                System.out.println("startedFromQCB laitettu, id: " + id);
                i.putExtra("id", id);
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
