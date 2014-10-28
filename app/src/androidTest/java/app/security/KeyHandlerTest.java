package app.security;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;

public class KeyHandlerTest extends ActivityInstrumentationTestCase2<MainMenu> {

    public KeyHandlerTest(){
        super(MainMenu.class);
    }

    public void testSetPrivateKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        kh.setPrivateKey("kilpikonna");
        assertEquals("kilpikonna", kh.getPrivateKey());
    }

    public void testSetPublicKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        kh.setPublicKey("virtahepo");
        assertEquals("virtahepo", kh.getPublicKey());
    }

    public void testCreateKeysCreatesSomething() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        String key = kh.createKeys();
        assertNotSame("Keymaking failed", key);
    }

    public void testCreateKeysChangesPublicKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        String firstKey = kh.getPublicKey();
        kh.createKeys();
        String secondKey = kh.getPublicKey();
        assertNotSame(firstKey, secondKey);
    }

    public void testCreateKeysChangesPrivateKey() throws Exception {
        Activity activity = getActivity();
        KeyHandler kh = new KeyHandler(activity);
        String firstKey = kh.getPrivateKey();
        kh.createKeys();
        String secondKey = kh.getPrivateKey();
        assertNotSame(firstKey, secondKey);
    }
}
