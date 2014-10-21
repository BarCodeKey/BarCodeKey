package app.domain;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Adapter;


public class ContactsHandlerTest extends ActivityInstrumentationTestCase2<ContactsHandler> {

    private ContactsHandler handler;


    public ContactsHandlerTest(){
        super(ContactsHandler.class);
    }
    ContactsHandler ch = new ContactsHandler();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


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

        assertEquals(cleanSami, ch.cleanAndGetPublicKey(dirtySami)[0]);
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

        assertEquals(publicKey, ch.cleanAndGetPublicKey(dirtySami)[1]);
    }


}
