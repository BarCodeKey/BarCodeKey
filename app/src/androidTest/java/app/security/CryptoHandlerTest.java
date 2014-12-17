package app.security;



import app.util.Constants;

import android.test.InstrumentationTestCase;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class CryptoHandlerTest extends InstrumentationTestCase {

        // Generated keypairs A and B
    private final String publicKeyA = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQA0fZrbXNwHi+vd7cVG6JIDs1OAYJ0/CMtjNzId6Bl/XcweTFXPbNWCxRikBBRQb8PC/FxfB8VyueqU8+NkVBcYfYACOQFVW0voaYVdLiXq5bd16rGUvZfVcsXjm7Hyqpqz3NQKVorobHC90yRy6ZYD/F6oiwsgNy7TYBy2hdJpX4HSk0=";
    private final String privateKeyA = "MIH2AgEAMBAGByqGSM49AgEGBSuBBAAjBIHeMIHbAgEBBEEG6T0SFfLGQVJ3GI7aMp+bBwEEX7EdoeBKoyVd5BXdELzbqztyzWXvZcWTtsANKtYNt9znqppz6b01Ekq67pEG4qAHBgUrgQQAI6GBiQOBhgAEANH2a21zcB4vr3e3FRuiSA7NTgGCdPwjLYzcyHegZf13MHkxVz2zVgsUYpAQUUG/DwvxcXwfFcrnqlPPjZFQXGH2AAjkBVVtL6GmFXS4l6uW3deqxlL2X1XLF45ux8qqas9zUClaK6GxwvdMkcumWA/xeqIsLIDcu02ActoXSaV+B0pN";
    private final String publicKeyB = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBsUtpAxl2GvdJ422Q1/cMVirHedu7nBss05VwmS8hWQcdrFXNBJjSDbTIf0qONWmrH4oDZ8VMKi1S/KOcGWwpHcwBfSe5ey6wFgUyGk0ENpD4tOobD9+KKwOBFVDTHz2LwSHUI0zncYMZlBfhg5m1YC6WVZ4a5llZmcb3SpDOS8THNEA=";
    private final String privateKeyB = "MIH3AgEAMBAGByqGSM49AgEGBSuBBAAjBIHfMIHcAgEBBEIBg6AejU1LRjozTTH0w+RS1y57TTH4Vm9BQ9F47cNoHk7EunQCfUHOhoHIkeTTWW3LYauRKgJjRQrwanjenPSXRQegBwYFK4EEACOhgYkDgYYABAGxS2kDGXYa90njbZDX9wxWKsd527ucGyzTlXCZLyFZBx2sVc0EmNINtMh/So41aasfigNnxUwqLVL8o5wZbCkdzAF9J7l7LrAWBTIaTQQ2kPi06hsP34orA4EVUNMfPYvBIdQjTOdxgxmUF+GDmbVgLpZVnhrmWVmZxvdKkM5LxMc0QA==";

    private String secret = "";
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
        secret = KeyHandler.getSecret(publicKeyB, privateKeyA);
        encrypted = CryptoHandler.encryptECIES(message.getBytes(),pubA,privB);

    }

    public void testEncryptReturnsDifferent() throws Exception {
        String encryptedMessage = new String(CryptoHandler.encrypt(message.getBytes(), pubA,privB));
        assertFalse(message.equals(encryptedMessage));
    }

    public void testDecryptReturnsNotNull() throws Exception {
        assertNotNull(CryptoHandler.decrypt(encrypted,pubB,privA));
    }
    public void testDecryptReturnsNotNull2() throws Exception {
        assertNotNull(CryptoHandler.decryptECIES(encrypted,pubB,privA));
    }

    public void testEncryptReturnsNotNull() throws Exception {
        assertNotNull(CryptoHandler.encrypt(message.getBytes(),pubA,privB));
    }

    public void testEncryptedCanBeDecrypted() throws Exception {
        String decryptedMessage = new String(CryptoHandler.decryptECIES(encrypted,pubB,privA));
        Constants.log(decryptedMessage + "!!!!!!!!!!!!!!!!!!!!!!!");
        assertTrue(message.equals(decryptedMessage));
    }
    public void testPaddingRemovalWorks(){
        String data = (message+"468dhdhe92inbcs");
        String resultData = new String(CryptoHandler.removePadding(data.getBytes()));
        assertTrue(message.equals(new String(resultData)));
    }


}
