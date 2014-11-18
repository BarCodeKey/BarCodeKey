package app.security;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import java.security.KeyPair;

import app.barcodekey.MainMenu;

public class KeyHandlerTest extends ActivityInstrumentationTestCase2<MainMenu> {

    public KeyHandlerTest(){
        super(MainMenu.class);
    }

    public void testSetPrivateKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        KeyPair kp = kh.createKeys();
        String privateKey = KeyHandler.base64Encode(kp.getPrivate().getEncoded());
        assertNotNull(privateKey);
    }

    public void testSetPublicKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        KeyPair kp = kh.createKeys();
        String publicKey = KeyHandler.base64Encode(kp.getPublic().getEncoded());
        assertNotNull(publicKey);
    }

    public void testCreateKeysCreatesSomething() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        KeyPair kp = kh.createKeys();
        String publicKey = KeyHandler.base64Encode(kp.getPublic().getEncoded());
        assertNotSame("Keymaking failed", publicKey);
    }

    public void testCreateKeysChangesPublicKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        KeyPair kp = kh.createKeys();
        String firstKey = KeyHandler.base64Encode(kp.getPublic().getEncoded());

        kp = kh.createKeys();
        String secondKey = KeyHandler.base64Encode(kp.getPublic().getEncoded());
        assertNotSame(firstKey, secondKey);
    }

    public void testCreateKeysChangesPrivateKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        KeyPair kp = kh.createKeys();
        String firstKey = KeyHandler.base64Encode(kp.getPrivate().getEncoded());

        kp = kh.createKeys();
        String secondKey = KeyHandler.base64Encode(kp.getPrivate().getEncoded());
        assertNotSame(firstKey, secondKey);
    }
}
