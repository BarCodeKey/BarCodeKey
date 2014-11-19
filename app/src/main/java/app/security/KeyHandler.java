package app.security;

import android.content.Context;

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
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


import javax.crypto.KeyAgreement;


import app.preferences.SharedPreferencesService;

public class KeyHandler {

    private SharedPreferencesService sharedPreferencesService;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public KeyHandler(Context context) {
        this.sharedPreferencesService = new SharedPreferencesService(context);
    }

    /**
     * This creates public/private-key pair using Elliptic curve Diffie-Hellman
     * @param
     */

    public static KeyPair createKeys() {

        /* initializing elliptic curve with SEC 2 recommended curve and KeyPairGenerator with type of keys,
        provider(SpongyCastle) and  given elliptic curve.
         */
        ECGenParameterSpec esSpec = new ECGenParameterSpec("P-521");
        KeyPairGenerator keyPairGenerator = null;

        try {
            keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "SC");
            keyPairGenerator.initialize(esSpec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


        KeyPair keyPair = keyPairGenerator.generateKeyPair();


        //kokeillaan encrypt/decrypt
        /*String publicKey = encryptSimple(kp.getPublic().getEncoded());
        String privateKey = ecryptSimple(kp.getPrivate().getEncoded());
       */

        // jätin vielä kun en oo ihan varma toimiiko tää
        /*String publicKey = base64Encode(kp.getPublic().getEncoded());
        String privateKey = base64Encode(kp.getPrivate().getEncoded());

        System.out.println(publicKey + "!!!!!!!!!!!!!!!!");
        setPublicKey(publicKey);
        setPrivateKey(privateKey);

        if(getPublicKey().equals(publicKey) && getPrivateKey().equals(privateKey)) {
            return base64Encode(kp.getPublic().getEncoded());
        }
        return "Keymaking failed";*/
        return keyPair;/*

    }
    /**
     * Encodes bytes into Base64 in ASCII format
     * @param
     */
    }

    public static String base64Encode(byte[] b) {
        try {
            return new String(Base64.encode(b), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] base64Decode(String s) {
            return Base64.decode(s);
    }


    public byte[] getSecret(String sender) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        String pubKeyStr = sender;
        String privKeyStr = sharedPreferencesService.getPrivateKey();

        KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");

        X509EncodedKeySpec x509ks = new X509EncodedKeySpec(
                Base64.decode(pubKeyStr));
        PublicKey pubKeyB = kf.generatePublic(x509ks);

        PKCS8EncodedKeySpec p8ks = new PKCS8EncodedKeySpec(
                Base64.decode(privKeyStr));
        PrivateKey privKeyA = kf.generatePrivate(p8ks);

        KeyAgreement aKA = KeyAgreement.getInstance("ECDH", "SC");
        aKA.init(privKeyA);
        aKA.doPhase(pubKeyB, true);

        return aKA.generateSecret();
    }

}
