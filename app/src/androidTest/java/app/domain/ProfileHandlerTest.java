package app.domain;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.Main_menu;

public class ProfileHandlerTest extends ActivityInstrumentationTestCase2<Main_menu> {
    private SharedPreferences preferences;
    private ProfileHandler ph;

    public ProfileHandlerTest(){
        super(Main_menu.class);
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
        assertEquals("George", ph.getvCard().getStructuredName().getGiven());
    }
    public void addingAllProfileSettingsTest(){
        assertEquals("George", ph.getvCard().getStructuredName().getGiven());
        assertEquals("Clooney", ph.getvCard().getStructuredName().getFamily());
        assertEquals("+358401234567", ph.getvCard().getTelephoneNumbers().get(0));
        assertEquals("george.clooney@hollywood.com", ph.getvCard().getEmails().get(0));

    }

}
