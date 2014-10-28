package app.security;


import static junit.framework.Assert.assertEquals;

public class CryptoHandlerTest {
    private CryptoHandler ch;

    public CryptoHandlerTest(){}

    public void SetUp(){
        ch = new CryptoHandler();
    }

    public void encryptSimpleTest(){
        String output = ch.encodeSimple("kissa".getBytes());
        assertEquals("KUKKULUURUUkissa",output);
    }
    public void decryptSimpleTest(){
        String output= ch.decryptSimple("KUKKULUURUUkissa".getBytes());
        assertEquals("kissa", output);
    }
}
