package app.security;




import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;
import app.contacts.Contact;
import app.preferences.SharedPreferencesService;
import app.util.Constants;

import android.test.InstrumentationTestCase;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class CryptoHandlerTest extends InstrumentationTestCase {

        // Generated keypairs A and B
    private final String publicKeyA = "24EME4wEAYHKoZIzj0CAQYFK4EEACADOgAErR/y+WGcOJvezRTBQwDs8nMetI70bsC+NjGrpQUe5o0o+MpLOi+voO8/Cm1QOghH8gIocjNJYRE=";
    private final String privateKeyA = "24EMIGBAgEAMBAGByqGSM49AgEGBSuBBAAgBGowaAIBAQQcDwKrpsG8tZu/3nurtnjUM4C8Fnot4MxgVHO7xqAHBgUrgQQAIKE8AzoABK0f8vlhnDib3s0UwUMA7PJzHrSO9G7AvjYxq6UFHuaNKPjKSzovr6DvPwptUDoIR/ICKHIzSWER";
    private final String publicKeyB = "24EME4wEAYHKoZIzj0CAQYFK4EEACADOgAEMsv50UT8VeOtTNdwnAiRYOOSP80Zh7M9NBZXh9OKN/J+YiW8S+c34XKdtuZGcDdl8cIZz+G6wlc=";
    private final String privateKeyB = "24EMIGBAgEAMBAGByqGSM49AgEGBSuBBAAgBGowaAIBAQQc7RfO3Ecei3MLh6/dgOwo9V2vQh4/DwN+lg5v6KAHBgUrgQQAIKE8AzoABDLL+dFE/FXjrUzXcJwIkWDjkj/NGYezPTQWV4fTijfyfmIlvEvnN+FynbbmRnA3ZfHCGc/husJX";

    private String message = "Will it blend?\n That is the question.";
    private byte[] encrypted = null;
    private PublicKey pubA = null;
    private PrivateKey privA = null;
    private PublicKey pubB = null;
    private PrivateKey privB = null;



    public void setUp() throws Exception {
        //Transforiming keypairs into PublicKey/PrivateKey- objects
        pubA = KeyHandler.decodePublic(publicKeyA);
        pubB = KeyHandler.decodePublic(publicKeyB);
        privA = KeyHandler.decodePrivate(privateKeyA);
        privB = KeyHandler.decodePrivate(privateKeyB);
        encrypted = CryptoHandler.encryptECIES(message.getBytes(),pubA,privB);

    }

    public void testEncryptReturnsDifferent() throws Exception {
        String encryptedMessage = new String(CryptoHandler.encryptECIES(message.getBytes(), pubA,privB));
        assertFalse(message.equals(encryptedMessage));
    }

    public void testDecryptReturnsNotNull() throws Exception {
        assertNotNull(CryptoHandler.decryptECIES(encrypted,pubB,privA));
    }

    public void testEncryptReturnsNotNull() throws Exception {
        assertNotNull(CryptoHandler.encryptECIES(message.getBytes(),pubA,privB));
    }

    public void testEncryptedCanBeDecrypted() throws Exception {

        String decryptedMessage = new String(CryptoHandler.decryptECIES(encrypted,pubB,privA));
        Constants.log(decryptedMessage + "!!!!!!!!!!!!!!!!!!!!!!!");

        assertTrue(message.equals(decryptedMessage));
    }
    public void testPaddingRemovalWorks(){
        byte[] data = (message+"468dhdhe92inbcs").getBytes();
        byte[] resultData = CryptoHandler.removePadding(data);
        assertTrue(message.equals(new String(resultData)));
    }
}
