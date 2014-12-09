package app.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import app.util.Constants;

/**
    Creates QR codes from string data.
 */
public class QRMaker {

    /**
     * Generates a QR code bitmap to represent a string.
     *
     * @param data the string that is transformed in to a QR code
     * @param width the desired width of the code image
     * @param height the desired height of the code image
     * @return the data as a qr code, a bitmatrix that is turned in to a bitmap
     */
    public static Bitmap createQRcodeBitmap(String data, int width, int height) {
        BitMatrix bitMatrix = null;

        try {
            bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, Constants.QR_WIDTH, Constants.QR_HEIGHT);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitMatrixToBitmap(bitMatrix, width, height);
    }

    /**
     * Takes a bitmatrix that is created by the ZXing library's method and turns it in
     * to a bitmap that android can handle.
     *
     * @param matrix the bitmatrix QR code to be turned in to a bitmap
     * @param bitmapWidth the desired width of the code image
     * @param bitmapHeight the desired height of the code image
     * @return a bitmap representation of the bitmatrix
     */
    private static Bitmap bitMatrixToBitmap(BitMatrix matrix, int bitmapWidth, int bitmapHeight) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int[] pixels = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixels[(y * width) + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        bmp = Bitmap.createScaledBitmap(bmp, bitmapWidth, bitmapHeight, false);
        return bmp;
    }

}