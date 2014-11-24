package app.security;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class CryptoHandlerTest {
    private CryptoHandler ch;
    private byte[] encrypt;

    public CryptoHandlerTest(){}

    public void SetUp(){
        ch = new CryptoHandler();
    }

    public void encryptSimpleTest() throws Exception {
        /*byte[] kissa = "KISSA".getBytes();
        encrypt = ch.encryptHandler("P-521",kissa,"MATTI");
        assertNotSame("KISSA",output);*/
    }
    public void decryptSimpleTest() throws Exception {
        /*byte[] decrypt = ch.decryptHandler("P-521",encrypt, "MATTI");
        assertEquals("kissa", decrypt);*/
    }
}
