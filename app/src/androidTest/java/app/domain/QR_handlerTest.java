package app.domain;

import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.Main_menu;
import app.domain.QR_handler;

public class QR_handlerTest extends ActivityInstrumentationTestCase2<Main_menu> {

    public QR_handlerTest(){
        super(Main_menu.class);
    }

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }

    public void testCreateBitmap() throws Exception {
        QR_handler qr = new QR_handler();
        Bitmap first = qr.getQrBitmap();
        qr.createQRcodeBitmap("lohikaarme");
        Bitmap second = qr.getQrBitmap();
        assertNotSame(first, second);
    }

    public void testStoreQR() throws Exception {
        QR_handler qr = new QR_handler();
        Activity activity = getActivity();
        qr.createQRcodeBitmap("leijona");
        qr.storeQRtoInternalStorage(activity);
        assertTrue(qr.readQRfromInternalStorage(activity));
    }
}