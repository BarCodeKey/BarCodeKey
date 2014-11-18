package app.contacts;

import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;

public class QRMakerTest extends ActivityInstrumentationTestCase2<MainMenu> {

    public QRMakerTest(){
        super(MainMenu.class);
    }
/*
    public void testCreateBitmap() throws Exception {
        QRMaker qr = new QRMaker();
        Bitmap first = qr.getQrBitmap();
        qr.createQRcodeBitmap("lohikaarme");
        Bitmap second = qr.getQrBitmap();
        assertNotSame(first, second);
    }

    public void testStoreQR() throws Exception {
        QRMaker qr = new QRMaker();
        Activity activity = getActivity();
        qr.createQRcodeBitmap("leijona");
        qr.storeQRtoInternalStorage(activity);
        assertTrue(qr.readQRfromInternalStorage(activity));
    }
    */
}