package app.security;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import app.barcodekey.MainMenu;
import app.contacts.Contact;
import app.preferences.SharedPreferencesService;
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
    private final String publicKeyC = "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEfvOgZ2pAdMVGCU1jDGSZDDh6BhPQhebruR/CSYpHzS80AzPmiQtwglj5NFqIkJNL";
    private final String privateKeyC = "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBisC2S5uv16Y/2lmd/3upV+2SLfrPmg2UygCgYIKoZIzj0DAQGhNAMyAAR+86BnakB0xUYJTWMMZJkMOHoGE9CF5uu5H8JJikfNLzQDM+aJC3CCWPk0WoiQk0s=";
    private final String publicKeyD = "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAERCmY26A3o8BRCF5xQv0XrsKkbsnHnXVUBXfoCGMI8paJHXA9L75V170SEC48RgTU";
    private final String privateKeyD = "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBj2C+K54MYWuo9xydsbVCtrX/0JIV1qD4GgCgYIKoZIzj0DAQGhNAMyAAREKZjboDejwFEIXnFC/ReuwqRuyceddVQFd+gIYwjylokdcD0vvlXXvRIQLjxGBNQ=";
    private String secret = "";
    private String message = "Will it blend?\n That is the question.";
    private byte[] encrypted = null;
    private PublicKey pubA = null;
    private PrivateKey privA = null;
    private PublicKey pubB = null;
    private PrivateKey privB = null;
    private PublicKey pubC = null;
    private PrivateKey privC = null;
    private PublicKey pubD = null;
    private PrivateKey privD = null;
    public void setUp() throws Exception {
//Transforiming keypairs into PublicKey/PrivateKey- objects
        pubA = KeyHandler.decodePublic(publicKeyA);
        pubB = KeyHandler.decodePublic(publicKeyB);
        privA = KeyHandler.decodePrivate(privateKeyA);
        privB = KeyHandler.decodePrivate(privateKeyB);
        pubC = KeyHandler.decodePublic(publicKeyC);
        pubD = KeyHandler.decodePublic(publicKeyD);
        privC = KeyHandler.decodePrivate(privateKeyC);
        privD = KeyHandler.decodePrivate(privateKeyD);
        encrypted = CryptoHandler.encrypt(message.getBytes(),pubA,privB);
    }
    public void testEncryptReturnsDifferent() throws Exception {
        String encryptedMessage = new String(CryptoHandler.encrypt(message.getBytes(), pubA,privB));
        assertFalse(message.equals(encryptedMessage));
    }
    public void testDecryptReturnsNotNull() throws Exception {
        assertNotNull(CryptoHandler.decrypt(encrypted,pubB,privA));
    }
    public void testEncryptReturnsNotNull() throws Exception {
        assertNotNull(CryptoHandler.encrypt(message.getBytes(),pubA,privB));
    }
    public void testEncryptedCanBeDecrypted() throws Exception {
        byte[] encrypted = CryptoHandler.encrypt(message.getBytes(),pubA,privB);
        String decryptedMessage = new String(CryptoHandler.decrypt(encrypted,pubB,privA));
        assertTrue(message.equals(decryptedMessage));
    }
    public void testEncryptedCanBeDecrypted2() throws Exception {
        byte[] encrypted = CryptoHandler.encrypt(message.getBytes(),pubC,privD);
        String decryptedMessage = new String(CryptoHandler.decrypt(encrypted,pubD,privC));
        assertTrue(message.equals(decryptedMessage));
    }
    public void testPaddingRemovalWorks(){
        byte[] data = ("What happens to this message?"+"468dhdhe92inbcs").getBytes();
        byte[] resultData = CryptoHandler.removePadding(data);
        assertTrue("What happens to this message?".equals(new String(resultData)));
    }

}
