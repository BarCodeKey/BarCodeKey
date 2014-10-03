package app.domain;


import android.app.Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;

import org.spongycastle.util.encoders.Base64;


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
        editor.commit();
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

    public String createKeys() throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnsupportedEncodingException {

        /* initializing elliptic curve with SEC 2 recommended curve and KeyPairGenerator with type of keys,
        provider(SpongyCastle) and  given elliptic curve.
         */
        ECGenParameterSpec esSpec = new ECGenParameterSpec("secp224k1");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDH", "SC");
        kpg.initialize(esSpec);

        KeyPair kp = kpg.generateKeyPair();
        String publicKey = base64Encode(kp.getPublic().getEncoded());
        String privateKey = base64Encode(kp.getPrivate().getEncoded());

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



}
