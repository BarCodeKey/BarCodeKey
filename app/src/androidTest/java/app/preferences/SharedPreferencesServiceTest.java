package app.preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import java.util.Set;

import app.barcodekey.MainMenu;
import app.contacts.Contact;


public class SharedPreferencesServiceTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private SharedPreferences preferences;
    private SharedPreferencesService sharedPreferencesService;

    public SharedPreferencesServiceTest() {
        super(MainMenu.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.sharedPreferencesService = new SharedPreferencesService(getActivity());

        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString("first_name", "George");
        editor.putString("last_name", "Clooney");
        editor.putString("number", "+358401234567");
        editor.putString("email", "george.clooney@hollywood.com");
        editor.commit();
    }

    public void testAddingFirstName() {
        assertEquals("George", sharedPreferencesService.getGiven());
    }

    public void testAddingAllProfileSettings() {
        assertEquals("George", sharedPreferencesService.getGiven());
        assertEquals("Clooney", sharedPreferencesService.getFamily());
        assertEquals("+358401234567", sharedPreferencesService.getNumber());
        assertEquals("george.clooney@hollywood.com", sharedPreferencesService.getEmail());
    }

    public void testGetUser(){
        Contact user = sharedPreferencesService.getUser();

        assertEquals("George", user.getGiven());
        assertEquals("Clooney", user.getFamily());
        assertEquals("+358401234567", user.getNumber());
        assertEquals("george.clooney@hollywood.com", user.getEmail());
    }

    public void testSetAndGetKey(){
        sharedPreferencesService.setKey("avain", "koira");
        assertEquals("koira", sharedPreferencesService.getKey("avain"));
    }

    public void testSetAndGetPrivateKey(){
        sharedPreferencesService.setPrivateKey("jänis");
        assertEquals("jänis", sharedPreferencesService.getPrivateKey());
    }

    public void testSetAndGetPublicKey(){
        sharedPreferencesService.setPublicKey("pupu");
        assertEquals("pupu", sharedPreferencesService.getPublicKey());
    }

}
