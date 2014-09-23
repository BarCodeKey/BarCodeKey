package app.domain;


import android.app.Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;


public class KeyHandler {

    private SharedPreferences preferences;
    private String alias = "keys";

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    public KeyHandler(Activity activity) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setPublicKey(String value){
        setKey("public_key", value);
    }

    public String getPublicKey(){
        return getKey("public_key");
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
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {
        ECParams ecp = ECParams.getParams("secp224k1");


        ECFieldFp fp = new ECFieldFp(ecp.getP());
        EllipticCurve ec = new EllipticCurve(fp, ecp.getA(), ecp.getB());
        ECParameterSpec esSpec = new ECParameterSpec(ec, ecp.getG(),
                ecp.getN(), ecp.h);
        //no such provider sc?
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECDH", "SC");
        kpg.initialize(esSpec);

        KeyPair kp = kpg.generateKeyPair();

        String pub = new String(Base64.encodeToString(kp.getPublic().getEncoded(), Base64.DEFAULT));
        return pub;


//ECDH _> Spongecastle, chipher does encrypt/decrypt


    }
    /*static String base64Encode(byte[] b) {
        try {
            return new String(Base64.encode(b), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }*/



}
