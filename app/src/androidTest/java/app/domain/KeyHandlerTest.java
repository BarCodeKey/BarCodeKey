package app.domain;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.Main_menu;

public class KeyHandlerTest extends ActivityInstrumentationTestCase2<Main_menu> {

    public KeyHandlerTest(){
        super(Main_menu.class);
    }

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
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
