package app.contacts;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;

public class ContactTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private SharedPreferences preferences;
    private Contact ph;

    public ContactTest() {
        super(MainMenu.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString("first_name", "George");
        editor.putString("last_name", "Clooney");
        editor.putString("number", "+358401234567");
        editor.putString("email", "george.clooney@hollywood.com");
        editor.commit();
        ph = new Contact(getActivity());
        ph.readFromSharedPreferences();
    }
/*
    public void testAddingFirstName() {
        assertEquals("George", ph.getVCard().getStructuredName().getGiven());
    }

    public void testAddingAllProfileSettings() {
        assertEquals("George", ph.getVCard().getStructuredName().getGiven());
        assertEquals("Clooney", ph.getVCard().getStructuredName().getFamily());
        assertEquals("+358401234567", ph.getVCard().getTelephoneNumbers().get(0).getText());
        assertEquals("george.clooney@hollywood.com", ph.getVCard().getEmails().get(0).getValue());

    }

    public void testGetVCard() {
        Contact anotherHandler = new Contact(getActivity());
        assertNotSame(ph.getVCard(), anotherHandler.getVCard());
    }

    public void testCleanAndGetPublicKeyReturnsCleanedVCard() {
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

        assertEquals(cleanSami, ph.cleanPublicKeyFromStringAndGetPublicKey(dirtySami)[0]);
    }

    public void testCleanAndGetPublicKeyReturnsPublicKey() {
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
