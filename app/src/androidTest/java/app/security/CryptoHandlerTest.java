package app.security;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;
import app.contacts.Contact;
import app.preferences.SharedPreferencesService;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class CryptoHandlerTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private CryptoHandler ch;
    private byte[] encrypt;
    private SharedPreferences preferences;
    private SharedPreferencesService sharedPreferencesService;
    private Contact contact;

    public CryptoHandlerTest() {
        super(MainMenu.class);
    }


    public void SetUp(){
        ch = new CryptoHandler();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.sharedPreferencesService = new SharedPreferencesService(getActivity());

        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString("first_name", "George");
        editor.putString("last_name", "Clooney");
        editor.putString("number", "+358401234567");
        editor.putString("email", "george.clooney@hollywood.com");
        editor.commit();
        //TODO: jostain pitäis se URI kaivaa...
        /*sharedPreferencesService.setPrivateKey("kissa");
        sharedPreferencesService.setPublicKey("koira");
        */

        contact = new Contact();
        contact.setGiven("George");
        contact.setFamily("Clooney");
        contact.setNumber("+358401234567");
        contact.setEmail("george.clooney@hollywood.com");
        //contact.setPublicKey();
    }

    public void encryptTest() throws Exception {
        /*byte[] kissa = "KISSA".getBytes();
        encrypt = ch.encryptHandler("P-521",kissa,"MATTI");
        assertNotSame("KISSA",output);*/
    }
    public void decryptTest() throws Exception {
        /*byte[] decrypt = ch.decryptHandler("P-521",encrypt, "MATTI");
        assertEquals("kissa", decrypt);*/
    }
}
