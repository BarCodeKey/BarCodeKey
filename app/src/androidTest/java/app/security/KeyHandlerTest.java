package app.security;

import android.test.InstrumentationTestCase;
import java.security.KeyPair;

public class KeyHandlerTest extends InstrumentationTestCase{


    private String publicKey;
    private String privateKey;


    public void createKeys(){
        KeyPair keyPair = KeyHandler.createKeys();
        publicKey = KeyHandler.base64Encode(keyPair.getPublic().getEncoded());
        privateKey = KeyHandler.base64Encode(keyPair.getPrivate().getEncoded());
    }

    public void testCreateKeysCreatesSomething() throws Exception {
        createKeys();

        assertTrue(true);
        assertNotSame("", publicKey);
        assertNotSame("", privateKey);
        assertNotNull(publicKey);
        assertNotNull(privateKey);
    }

    public void testCreateKeysCreatesUniqueKeys() throws Exception {
        createKeys();
        String tempPublicKey = publicKey;
        String tempPrivateKey = privateKey;

        createKeys();
        assertNotSame(publicKey, tempPublicKey);
        assertNotSame(privateKey, tempPrivateKey);

    }

    public void testBase64Encode(){
        KeyPair keyPair = KeyHandler.createKeys();

        String tempPublicKey = keyPair.getPublic().toString();
        String tempPublicKey2 = KeyHandler.base64Encode(keyPair.getPublic().getEncoded());

        assertNotSame(tempPublicKey, tempPublicKey2);
    }

}
