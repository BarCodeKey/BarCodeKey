package app.security;

import android.test.InstrumentationTestCase;

import org.spongycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

public class KeyHandlerTest extends InstrumentationTestCase{


    // Generated keypairs A and B
    private final String publicKeyA = "ME4wEAYHKoZIzj0CAQYFK4EEACADOgAErR/y+WGcOJvezRTBQwDs8nMetI70bsC+NjGrpQUe5o0o+MpLOi+voO8/Cm1QOghH8gIocjNJYRE=";
    private final String privateKeyA = "MIGBAgEAMBAGByqGSM49AgEGBSuBBAAgBGowaAIBAQQcDwKrpsG8tZu/3nurtnjUM4C8Fnot4MxgVHO7xqAHBgUrgQQAIKE8AzoABK0f8vlhnDib3s0UwUMA7PJzHrSO9G7AvjYxq6UFHuaNKPjKSzovr6DvPwptUDoIR/ICKHIzSWER";
    private final String publicKeyB = "ME4wEAYHKoZIzj0CAQYFK4EEACADOgAEMsv50UT8VeOtTNdwnAiRYOOSP80Zh7M9NBZXh9OKN/J+YiW8S+c34XKdtuZGcDdl8cIZz+G6wlc=";
    private final String privateKeyB = "MIGBAgEAMBAGByqGSM49AgEGBSuBBAAgBGowaAIBAQQc7RfO3Ecei3MLh6/dgOwo9V2vQh4/DwN+lg5v6KAHBgUrgQQAIKE8AzoABDLL+dFE/FXjrUzXcJwIkWDjkj/NGYezPTQWV4fTijfyfmIlvEvnN+FynbbmRnA3ZfHCGc/husJX";


    private String publicKey;
    private String privateKey;


    public void createKeys(){
        KeyPair keyPair = KeyHandler.createKeys(Curve.SECP224.getCurve());
        publicKey = KeyHandler.base64Encode(keyPair.getPublic().getEncoded(),Curve.SECP224.getId());
        privateKey = KeyHandler.base64Encode(keyPair.getPrivate().getEncoded(),Curve.SECP224.getId());
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
        KeyPair keyPair = KeyHandler.createKeys(Curve.SECP224.getCurve());

        String tempPublicKey = keyPair.getPublic().toString();
        String tempPublicKey2 = KeyHandler.base64Encode(keyPair.getPublic().getEncoded(), Curve.SECP224.getId());

        assertNotSame(tempPublicKey, tempPublicKey2);
    }

    public void testGetCurveIdWorks(){
        String pub = Curve.SECP224.getId() + publicKeyA;
        assertTrue(Curve.SECP224.getId().equals(KeyHandler.getCurveId(pub)));
    }

    public void testGetCurveIdReturnsNull(){
        String pub = "HEH" + publicKeyA;
        assertNull(KeyHandler.getCurveId(pub));
    }

    public void testDecodePublicWorks() throws Exception{
        createKeys();
        String pub = publicKey.substring(3);

         assertTrue(pub.equals(encode(KeyHandler.decodePublic(publicKey).getEncoded())));
    }

    public void testDecodePrivateWorks() throws Exception{
        createKeys();
        String priv = privateKey.substring(3);

         assertTrue(priv.equals(encode(KeyHandler.decodePrivate(privateKey).getEncoded())));
    }

    public String encode(byte[] b) throws UnsupportedEncodingException {
        return new String(Base64.encode(b), "ASCII");
    }

}
