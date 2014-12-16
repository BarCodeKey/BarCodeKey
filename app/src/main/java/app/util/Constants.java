package app.util;

import android.util.Log;

/**
 * A helper class that contains various fixed values that are used throughout the code.
 */
public class Constants {

    public static boolean DEBUG_MODE_ON = true;

    /* Strings */

    /* Extras */
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_VCARD = "vcard";
    public static final String EXTRA_STARTED_FROM_QCB = "startedFromQCB";
    public static final String EXTRA_SETTINGS_CHANGED = "changed";

    /* Random strings */
    public static final String LOG_TAG = "Logitagi";
    public static final String QR_FILENAME= "BarCodeKey_QRcode.png";
    public static final String INTENT_KEY_FINISH_ACTIVITY_ON_SAVE_COMPLETED = "finishActivityOnSaveCompleted";
    public static final String MIMETYPE_PUBLIC_KEY = "vnd.android.cursor.item/publicKey";
    public static final String KEY_FORMAT_BASE64 = "ENCODING=B";

    /* Integers */

    /* Request codes */
    public static final int REQUEST_CODE_INSERT_OR_EDIT = 0;
    public static final int REQUEST_CODE_SETTINGS = 1;
    public static final int REQUEST_CODE_EDIT = 2;
    public static final int REQUEST_CODE_QRSCANNER = 3;
    public static final int REQUEST_CODE_SCAN_FROM_QCB = 4;
    public static final int REQUEST_CODE_SCAN_FROM_MAIN = 5;
    public static final int REQUEST_CODE_PICK_CONTACT = 6;

    /* Result codes */
    public static final int RESULT_CHANGED = 1;
    public static final int RESULT_RESET_KEYS = 2;
    public static final int RESULT_FINISH_MAIN = 3;
    public static final int RESULT_RETURN_MAIN = 4;

    /* Dimensions */
    public static final int QR_WIDTH = 256;
    public static final int QR_HEIGHT = 256;

    public static final int QR_BITMAP_WIDTH = 1024;
    public static final int QR_BITMAP_HEIGHT = 1024;


    public static void log(Object message){
        if (DEBUG_MODE_ON){
            Log.v(LOG_TAG, message.toString());
        }
    }
}


