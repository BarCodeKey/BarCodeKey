package app.contacts;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;
import app.contacts.ProfileHandler;

public class ProfileHandlerTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private SharedPreferences preferences;
    private ProfileHandler ph;

    public ProfileHandlerTest(){
        super(MainMenu.class);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString("first_name", "George");
        editor.putString("last_name", "Clooney");
        editor.putString("number", "+358401234567");
        editor.putString("email", "george.clooney@hollywood.com");
        editor.commit();
        ph = new ProfileHandler(getActivity());
        ph.readFromSharedPreferences();
    }

    public void addingFirstNameTest(){
        assertEquals("George", ph.getVCard().getStructuredName().getGiven());
    }
    public void addingAllProfileSettingsTest(){
        assertEquals("George", ph.getVCard().getStructuredName().getGiven());
        assertEquals("Clooney", ph.getVCard().getStructuredName().getFamily());
        assertEquals("+358401234567", ph.getVCard().getTelephoneNumbers().get(0));
        assertEquals("george.clooney@hollywood.com", ph.getVCard().getEmails().get(0));

    }

}
