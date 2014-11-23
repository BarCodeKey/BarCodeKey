package app.contacts;

import android.test.ActivityInstrumentationTestCase2;

import app.barcodekey.MainMenu;

public class ContactTest extends ActivityInstrumentationTestCase2<MainMenu> {
    private Contact contact;
    private final String dirtySami = "BEGIN:VCARD\n" +
            "VERSION:3.0\n" +
            "N:Parasmies;Sami;;;\n" +
            "EMAIL:sami@sami.fi\n" +
            "TEL:+3585695636363728\n" +
            "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
            "END:VCARD";

    private final String cleanSami = "BEGIN:VCARD\n" +
            "VERSION:3.0\n" +
            "N:Parasmies;Sami;;;\n" +
            "EMAIL:sami@sami.fi\n" +
            "TEL:+3585695636363728\n" +
            "END:VCARD";

    public ContactTest() {
        super(MainMenu.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        contact = new Contact();
        contact.setGiven("George");
        contact.setFamily("Clooney");
        contact.setNumber("+358401234567");
        contact.setEmail("george.clooney@hollywood.com");

    }

    public void testConstructor() {
        assertEquals("George", contact.getGiven());
        assertEquals("Clooney", contact.getFamily());
        assertEquals("+358401234567", contact.getNumber());
        assertEquals("george.clooney@hollywood.com", contact.getEmail());
    }

    public void testConstructors(){
        Contact contact = new Contact();
        contact.setGiven(null);
        contact.setFamily(null);
        contact.setNumber(null);
        contact.setEmail(null);

        assertEquals("", contact.getGiven());
        assertEquals("", contact.getFamily());
        assertEquals("", contact.getNumber());
        assertEquals("", contact.getEmail());
    }

        public void testConstructorWithVCard(){
        Contact contact = new Contact(dirtySami);
        assertEquals("Sami", contact.getGiven());
        assertEquals("Parasmies", contact.getFamily());
        assertEquals("+3585695636363728", contact.getNumber());
        assertEquals("sami@sami.fi", contact.getEmail());
        assertEquals("ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=", contact.getPublicKey());
    }

    public void testConstructorWithVCard2(){
        String vcard = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "\n" +
                "EMAIL:sami@sami.fi\n" +
                "\n" +
                "TEL:+3585695636363728\n" +
                "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
                "END:VCARD" +
                "\n";
        Contact contact = new Contact(vcard);
        assertEquals("Sami", contact.getGiven());
        assertEquals("Parasmies", contact.getFamily());
        assertEquals("+3585695636363728", contact.getNumber());
        assertEquals("sami@sami.fi", contact.getEmail());
        assertEquals("ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=", contact.getPublicKey());
    }

    public void testConstructorWithVCard3() {
        String vcard = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "TEL:+3585695636363728\n" +
                "EMAIL:sami@sami.fi\n" +
                "KEY;ENCODING=B:ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=\n" +
                "N:Parasmies;Sami;;;\n" +
                "END:VCARD";
        Contact contact = new Contact(vcard);
        assertEquals("Sami", contact.getGiven());
        assertEquals("Parasmies", contact.getFamily());
        assertEquals("+3585695636363728", contact.getNumber());
        assertEquals("sami@sami.fi", contact.getEmail());
        assertEquals("ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=", contact.getPublicKey());
    }

    public void testConstructorWithVCard4(){
        String vcard = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:Parasmies;Sami;;;\n" +
                "END:VCARD";
        Contact contact = new Contact(vcard);
        assertEquals("Sami", contact.getGiven());
        assertEquals("Parasmies", contact.getFamily());
        assertEquals("", contact.getNumber());
        assertEquals("", contact.getEmail());
        assertEquals("", contact.getPublicKey());
    }

    public void testConstructorWithVCard5(){
        String vcard = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "END:VCARD";
        Contact contact = new Contact(vcard);
        assertEquals("", contact.getGiven());
        assertEquals("", contact.getFamily());
        assertEquals("", contact.getNumber());
        assertEquals("", contact.getEmail());
        assertEquals("", contact.getPublicKey());
    }

    public void testReadPublicKey(){
        assertEquals("ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEgJ13oJGD1KSRhjMVF/qJ001XP3pyS/9mzs08aQXrkex+m68RB+qYzJJMh2UNU4EYHvHZU4GVFek=", Contact.readPublicKey(dirtySami));
    }

    public void testReadPublicKey2(){
        assertEquals("", Contact.readPublicKey(cleanSami));
    }

    public void testRemovePublicKeyFromString(){
        assertEquals(cleanSami, Contact.removePublicKeyFromString(dirtySami));
    }

    public void testRemovePublicKeyFromString2(){
        assertEquals(cleanSami, Contact.removePublicKeyFromString(dirtySami + "\n"));
    }

}
