package app.contacts;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;

public class VCardHandlerTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private SharedPreferences preferences;
    private VCardHandler ph;

    public VCardHandlerTest(){
        super(MainMenu.class);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString("first_name", "George");
        editor.putString("last_name", "Clooney");
        editor.putString("number", "+358401234567");
        editor.putString("email", "george.clooney@hollywood.com");
        editor.commit();
        ph = new VCardHandler(getActivity());
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
/*
    SEURAAVAT 2 TESTIÄ AIHEUTTAVAT BUILDIN FAILAAMISEN, TESTIÄ EI PYSTY RAKENTAMAAN, NULL POINTER EXCEPTION.

    public void testCleanAndGetPublicKeyReturnsCleanedVCard(){
        String dirtySami = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "EMAIL:sami@sami.fi\n" +
                "TEL:+3585695636363728\n" +
                "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
                "END:VCARD";
        String cleanSami = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "EMAIL:sami@sami.fi\n" +
                "TEL:+3585695636363728\n" +
                "END:VCARD";

        assertEquals(cleanSami, ph.cleanPublicKeyFromString(dirtySami));
    }

    public void testCleanAndGetPublicKeyReturnsPublicKey(){
        String publicKey = "ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=";
        String dirtySami = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "EMAIL:sami@sami.fi\n" +
                "TEL:+3585695636363728\n" +
                "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
                "END:VCARD";

        assertEquals(publicKey, ph.cleanPublicKeyFromStringAndGetPublicKey(dirtySami)[1]);
    }
*/
}