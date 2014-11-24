package app.contacts;

import android.graphics.Bitmap;
import android.test.InstrumentationTestCase;

import com.google.zxing.WriterException;

import app.util.Constants;


public class QRMakerTest extends InstrumentationTestCase {

    private Bitmap first;
    private Bitmap second;

    public void setUp() {
        first = QRMaker.createQRcodeBitmap("pekka", Constants.QR_BITMAP_WIDTH, Constants.QR_BITMAP_HEIGHT);
        second = QRMaker.createQRcodeBitmap("lohikäärme", Constants.QR_BITMAP_WIDTH, Constants.QR_BITMAP_HEIGHT);
    }

    public void testCreateBitmapCreatesSomething(){
        assertNotNull(first);
        assertNotNull(second);
    }

    public void testCreateBitmapCreatesCorrectDimensions(){
        assertEquals(Constants.QR_BITMAP_HEIGHT, first.getHeight());
        assertEquals(Constants.QR_BITMAP_WIDTH, first.getWidth());
    }

    public void testCreateBitmapCreatesSomethingUnique() {
        assertNotSame(first, second);
    }


    public void testCreateBitmapCreatesSamePixelsWithSameData() throws WriterException {
        Bitmap third = QRMaker.createQRcodeBitmap("pekka", Constants.QR_BITMAP_WIDTH, Constants.QR_BITMAP_HEIGHT);

        int[] firstPixels = new int[Constants.QR_WIDTH * Constants.QR_HEIGHT];
        first.getPixels(firstPixels, 0, Constants.QR_WIDTH, 0, 0, Constants.QR_WIDTH, Constants.QR_HEIGHT);

        int[] thirdPixels = new int[Constants.QR_WIDTH * Constants.QR_HEIGHT];
        third.getPixels(thirdPixels, 0, Constants.QR_WIDTH, 0, 0, Constants.QR_WIDTH, Constants.QR_HEIGHT);

        for (int x = 0; x < Constants.QR_WIDTH; x++) {
            for (int y = 0; y < Constants.QR_HEIGHT; y++) {
                assertEquals(firstPixels[(y * Constants.QR_WIDTH) + x], thirdPixels[(y * Constants.QR_WIDTH) + x]);
            }
        }

    }

}