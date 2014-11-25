package app.security;

import android.test.InstrumentationTestCase;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

public class KeyHandlerTest extends InstrumentationTestCase{


    // Generated keypairs A and B
    private final String publicKeyA = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQA0fZrbXNwHi+vd7cVG6JIDs1OAYJ0/CMtjNzId6Bl/XcweTFXPbNWCxRikBBRQb8PC/FxfB8VyueqU8+NkVBcYfYACOQFVW0voaYVdLiXq5bd16rGUvZfVcsXjm7Hyqpqz3NQKVorobHC90yRy6ZYD/F6oiwsgNy7TYBy2hdJpX4HSk0=";
    private final String privateKeyA = "MIH2AgEAMBAGByqGSM49AgEGBSuBBAAjBIHeMIHbAgEBBEEG6T0SFfLGQVJ3GI7aMp+bBwEEX7EdoeBKoyVd5BXdELzbqztyzWXvZcWTtsANKtYNt9znqppz6b01Ekq67pEG4qAHBgUrgQQAI6GBiQOBhgAEANH2a21zcB4vr3e3FRuiSA7NTgGCdPwjLYzcyHegZf13MHkxVz2zVgsUYpAQUUG/DwvxcXwfFcrnqlPPjZFQXGH2AAjkBVVtL6GmFXS4l6uW3deqxlL2X1XLF45ux8qqas9zUClaK6GxwvdMkcumWA/xeqIsLIDcu02ActoXSaV+B0pN";
    private final String publicKeyB = "MIGbMBAGByqGSM49AgEGBSuBBAAjA4GGAAQBsUtpAxl2GvdJ422Q1/cMVirHedu7nBss05VwmS8hWQcdrFXNBJjSDbTIf0qONWmrH4oDZ8VMKi1S/KOcGWwpHcwBfSe5ey6wFgUyGk0ENpD4tOobD9+KKwOBFVDTHz2LwSHUI0zncYMZlBfhg5m1YC6WVZ4a5llZmcb3SpDOS8THNEA=";
    private final String privateKeyB = "MIH3AgEAMBAGByqGSM49AgEGBSuBBAAjBIHfMIHcAgEBBEIBg6AejU1LRjozTTH0w+RS1y57TTH4Vm9BQ9F47cNoHk7EunQCfUHOhoHIkeTTWW3LYauRKgJjRQrwanjenPSXRQegBwYFK4EEACOhgYkDgYYABAGxS2kDGXYa90njbZDX9wxWKsd527ucGyzTlXCZLyFZBx2sVc0EmNINtMh/So41aasfigNnxUwqLVL8o5wZbCkdzAF9J7l7LrAWBTIaTQQ2kPi06hsP34orA4EVUNMfPYvBIdQjTOdxgxmUF+GDmbVgLpZVnhrmWVmZxvdKkM5LxMc0QA==";


    private String publicKey;
    private String privateKey;


    public void createKeys(){
        KeyPair keyPair = KeyHandler.createKeys();
        publicKey = KeyHandler.base64Encode(keyPair.getPublic().getEncoded());
        privateKey = KeyHandler.base64Encode(keyPair.getPrivate().getEncoded());
    }

    public void testCreateKeysCreatesSomething() throws Exception {
        createKeys();

        assertTrue(true);
        assertNotSame("", publicKey);
        assertNotSame("", privateKey);
        assertNotNull(publicKey);
        assertNotNull(privateKey);
    }

    public void testCreateKeysCreatesUniqueKeys() throws Exception {
        createKeys();
        String tempPublicKey = publicKey;
        String tempPrivateKey = privateKey;

        createKeys();
        assertNotSame(publicKey, tempPublicKey);
        assertNotSame(privateKey, tempPrivateKey);
    }

    public void testBase64Encode(){
        KeyPair keyPair = KeyHandler.createKeys();

        String tempPublicKey = keyPair.getPublic().toString();
        String tempPublicKey2 = KeyHandler.base64Encode(keyPair.getPublic().getEncoded());

        assertNotSame(tempPublicKey, tempPublicKey2);
    }

    public void testGetSecret() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException {
        String secret = KeyHandler.getSecret(publicKeyB, privateKeyA);

        String secret2 = KeyHandler.getSecret(publicKeyA, privateKeyB);

        assertNotNull(secret);
        assertNotNull(secret2);
        assertTrue(secret.equals(secret2));

        secret2 = "sdfsdfsd";

        assertNotNull(secret2);
        assertFalse(secret.equals(secret2));
    }

}
