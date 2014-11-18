package app.security;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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


public class KeyHandler {

    private SharedPreferences preferences;

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public KeyHandler(Activity activity) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setPublicKey(String value){
        setKey("public_key", value);
    }

    public void setPrivateKey(String value){
        setKey("private_key", value);
    }

    public String getPublicKey(){
        return getKey("public_key");
    }

    public String getPrivateKey(){
        return getKey("private_key");
    }

    public void setKey(String key, String value) {
        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public String getKey(String key) {
        String value = "";
        value = this.preferences.getString(key, "");

        return value;
    }
    /**
     * This creates public/private-key pair using Elliptic curve Diffie-Hellman
     * @param
     */

    public String createKeys() {

        /* initializing elliptic curve with SEC 2 recommended curve and KeyPairGenerator with type of keys,
        provider(SpongyCastle) and  given elliptic curve.
         */
        ECGenParameterSpec esSpec = new ECGenParameterSpec("P-521");
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("ECDH", "SC");
            kpg.initialize(esSpec);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        KeyPair kp = kpg.generateKeyPair();

        //kokeillaan encrypt/decrypt
        /*String publicKey = encryptSimple(kp.getPublic().getEncoded());
        String privateKey = ecryptSimple(kp.getPrivate().getEncoded());
        */
        String publicKey = base64Encode(kp.getPublic().getEncoded());
        String privateKey = base64Encode(kp.getPrivate().getEncoded());

        System.out.println(publicKey+"!!!!!!!!!!!!!!!!");

        setPublicKey(publicKey);
        setPrivateKey(privateKey);

        if(getPublicKey().equals(publicKey) && getPrivateKey().equals(privateKey)) {
            return base64Encode(kp.getPublic().getEncoded());
        }
        return "Keymaking failed";

    }
    /**
     * Encodes bytes into Base64 in ASCII format
     * @param
     */
    static String base64Encode(byte[] b) {
        try {
            return new String(Base64.encode(b), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    static byte[] base64Decode(String s) {
            return Base64.decode(s);
    }


    public byte[] getSecret(String sender) throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException {
        String pubKeyStr = preferences.getString("public_key", "");
        String privKeyStr = sender;

        KeyFactory kf = KeyFactory.getInstance("ECDH", "SC");

        X509EncodedKeySpec x509ks = new X509EncodedKeySpec(
                Base64.decode(pubKeyStr));
        PublicKey pubKeyA = kf.generatePublic(x509ks);

        PKCS8EncodedKeySpec p8ks = new PKCS8EncodedKeySpec(
                Base64.decode(privKeyStr));
        PrivateKey privKeyB = kf.generatePrivate(p8ks);

        KeyAgreement aKA = KeyAgreement.getInstance("ECDH", "SC");
        aKA.init(privKeyB);
        aKA.doPhase(pubKeyA, true);

        return aKA.generateSecret();
    }

}
