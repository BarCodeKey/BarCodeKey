package app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {
    public static void storeQRtoInternalStorage(Context context, Bitmap qrBitmap, String filename) {
        if (qrBitmap == null) {
            return;
        }


        FileOutputStream out = null;
        try {
            out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            boolean success = qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            if (success) {
                // Successfully saved!
            } else {
                // Something went wrong!
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap readQRfromInternalStorage(Context context, String filename) {
        try {
            FileInputStream input = context.openFileInput(filename);
            return BitmapFactory.decodeStream(input);
        } catch (java.io.IOException e) {
            return null;
        }
    }
}
