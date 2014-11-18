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


/*
    QR_handler creates QR codes in bitmap format and fetches them from internal storage
    in .png format.

    The code is generated to represent the String that is given as a parameter to the
    createQRcodeBitmap() method.

    The displayQRbitmapInImageView method takes an ImageView parameter for setting the
    ImageView where the generated QR Code should be displayed, for example the ImageView spot
    in the main menu of the BarCodeKey app.
 */
public class QRMaker {

    public static Bitmap createQRcodeBitmap(String data, int x, int y) {
        BitMatrix bitMatrix = null;

        try {
            bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, x, y);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitMatrixToBitmap(bitMatrix);
    }

    private static Bitmap bitMatrixToBitmap(BitMatrix matrix) {
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
        bmp = Bitmap.createScaledBitmap(bmp, 1024, 1024, false);
        return bmp;
    }

}