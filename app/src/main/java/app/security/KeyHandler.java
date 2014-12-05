package app.security;


import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.util.encoders.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;



import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;

import app.preferences.SharedPreferencesService;


public class KeyHandler {


    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
;

    }

    /**
     * This creates public/private-key pair using Elliptic curve Diffie-Hellman
     * @param
     */

    public static KeyPair createKeys() {

        /* initializing elliptic curve with recommended curve and KeyPairGenerator with type of keys,
        provider(SpongyCastle) and  given elliptic curve.
         */
        ECGenParameterSpec esSpec = new ECGenParameterSpec("P-521");
        KeyPairGenerator keyPairGenerator = null;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC", "SC");
            keyPairGenerator.initialize(esSpec);
            keyPairGenerator.initialize(192);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

/* catch (NoSuchProviderException e) {
        e.printStackTrace();
    }*/
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;

    }

/**
 * Encodes bytes into Base64 in ASCII format
 * @param
 */
    public static String base64Encode(byte[] b) {
        try {
            return new String(Base64.encode(b), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public static PublicKey decodePublic(String publicKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");
        X509EncodedKeySpec x509ks = new X509EncodedKeySpec(
                Base64.decode(publicKey));
        PublicKey pubKey = kf.generatePublic(x509ks);
        return pubKey;
    }
    public static PrivateKey decodePrivate(String privateKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");
        PKCS8EncodedKeySpec p8ks = new PKCS8EncodedKeySpec(
                Base64.decode(privateKey));
        PrivateKey privKey = kf.generatePrivate(p8ks);
        return privKey;
    }
// generates secret from our private key and senders public key
    public static String getSecret(String publicKeyString, String privateKeyString) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {

        KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");

        X509EncodedKeySpec x509ks = new X509EncodedKeySpec(
                Base64.decode(publicKeyString));
        PublicKey pubKeyB = kf.generatePublic(x509ks);

        PKCS8EncodedKeySpec p8ks = new PKCS8EncodedKeySpec(
                Base64.decode(privateKeyString));
        PrivateKey privKeyA = kf.generatePrivate(p8ks);

        KeyAgreement aKA = KeyAgreement.getInstance("ECDH", "SC");
        aKA.init(privKeyA);
        aKA.doPhase(pubKeyB, true);

        return new String(aKA.generateSecret());
    }

}
