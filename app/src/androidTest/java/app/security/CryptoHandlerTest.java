package app.security;


import static junit.framework.Assert.assertEquals;

public class CryptoHandlerTest {
    private CryptoHandler ch;

    public CryptoHandlerTest(){}

    public void SetUp(){
        ch = new CryptoHandler();
    }

    public void encryptSimpleTest(){
        String output = ch.encryptSimple("kissa".getBytes());
        assertEquals("KISSAkissa",output);
    }
    public void decryptSimpleTest(){
        String output= ch.decryptSimple("KISSAkissa".getBytes());
        assertEquals("kissa", output);
    }
}
