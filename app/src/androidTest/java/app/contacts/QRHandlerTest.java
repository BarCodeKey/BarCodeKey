package app.contacts;

import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;
import app.contacts.QRHandler;

public class QRHandlerTest extends ActivityInstrumentationTestCase2<MainMenu> {

    public QRHandlerTest(){
        super(MainMenu.class);
    }

    public void testCreateBitmap() throws Exception {
        QRHandler qr = new QRHandler();
        Bitmap first = qr.getQrBitmap();
        qr.createQRcodeBitmap("lohikaarme");
        Bitmap second = qr.getQrBitmap();
        assertNotSame(first, second);
    }

    public void testStoreQR() throws Exception {
        QRHandler qr = new QRHandler();
        Activity activity = getActivity();
        qr.createQRcodeBitmap("leijona");
        qr.storeQRtoInternalStorage(activity);
        assertTrue(qr.readQRfromInternalStorage(activity));
    }
}