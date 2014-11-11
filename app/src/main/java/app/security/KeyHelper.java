package app.security;



import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.*;

import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyHelper {

    public KeyHelper(){
        Security.addProvider(new BouncyCastleProvider());
    }
    public byte[] getRawKey(byte[] seed)throws Exception{

        KeyGenerator kgen = KeyGenerator.getInstance("AES","SC");
        SecureRandom sr = null;
        sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);

        try {
            kgen.init(256, sr);
        } catch (Exception e) {
            //trying 197 bits, if doesn't support 256 bits
            try {
                kgen.init(192, sr);
            } catch (Exception e1) {
                kgen.init(128, sr);
            }
        }
        SecretKey secretKey = kgen.generateKey();
        byte[] raw = secretKey.getEncoded();
        return raw;
    }
}
