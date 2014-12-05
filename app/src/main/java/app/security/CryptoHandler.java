package app.security;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.jcajce.provider.asymmetric.ec.IESCipher;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.IEKeySpec;
import org.spongycastle.jce.spec.IESParameterSpec;
import org.spongycastle.openpgp.*;

import org.spongycastle.openpgp.operator.bc.BcPBEDataDecryptorFactory;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.jcajce.JcePBEKeyEncryptionMethodGenerator;
import org.spongycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.spongycastle.openpgp.PGPEncryptedDataGenerator;



import org.spongycastle.util.encoders.Hex;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.DESKeySpec;

import app.barcodekey.MainMenu;
import app.contacts.ContactsHandler;
import app.preferences.SharedPreferencesService;


public class CryptoHandler{

    private static byte[] text = "ERROR".getBytes();
    private static byte[] padding = "468dhdhe92inbcs".getBytes();
    public CryptoHandler(){
        Security.addProvider(new BouncyCastleProvider());
    }

    /*
          * Checks that given parameters for encryption are valid
          *
          * @Param data given data to be encrypted
          * @Param pubKey receivers public key
         */
    public static byte[] encrypt(byte[] data, PublicKey pubKey)  throws Exception{
        //TODO: checking if keytypes match???
        PrivateKey privKey = getPrivateKey();
        if(data == null || pubKey == null || privKey == null){
            return null;
        }

        if(pubKey.getAlgorithm().equals(privKey.getAlgorithm())){
            return encryptECIES(data, pubKey, privKey);
        }

        return null;
    }
    /*
      * Checks that given parameters for decryption are valid
      *
      * @Param data given data to be decrypted
      * @Param pubKey senders public key
     */
    public static byte[] decrypt(byte[] data, PublicKey pubKey) throws Exception{
        PrivateKey privKey = getPrivateKey();
        if(data == null || pubKey == null || privKey == null){
            System.out.println("jäätin tänne!!");
            return null;
        }
        if(pubKey.getAlgorithm().equals(privKey.getAlgorithm())){
            return decryptECIES(data, pubKey, privKey);
        }

        return null;
    }

    /*
      * Encryption for testing
     */
    public static byte[] encryptHelper(byte[] data, PublicKey pubKey, PrivateKey privKey) throws Exception {
        if(data == null || pubKey == null || privKey == null){
            return null;
        }

        if(pubKey.getAlgorithm().equals(privKey.getAlgorithm())){
            return encryptECIES(data, pubKey, privKey);
        }
        return null;
    }
    /*
    * Decryption for testing
   */
    public static byte[] decryptHelper(byte[] data, PublicKey pubKey, PrivateKey privKey) throws Exception {
        if(data == null || pubKey == null || privKey == null){
            return null;
        }

        if(pubKey.getAlgorithm().equals(privKey.getAlgorithm())){
            return decryptECIES(data, pubKey, privKey);
        }
        return null;
    }

    /*
      * Encrypts/decrypts using ECIES-keypair keys and ECIES algorithm
      *
      * @Param data given data to be encrypted
      * @Param pubKey Public key of the receiver
      * @Param privKey Private Key of the sender
      * @Param random Random used to intialize cipher
     */
    public static byte[] encryptECIES(byte[] data, PublicKey pubKey, PrivateKey privKey) throws Exception{
        Cipher cipher = Cipher.getInstance("ECIES");

        //create vectors for derivation and encoding
        byte[] d = Hex.decode("202122232425262728292a2b2c2d2e2f");
        byte[] e = Hex.decode("303132333435363738393a3b3c3d3e3f");
        IESParameterSpec iesParams = new IESParameterSpec(d,e,256);

        cipher.init(Cipher.ENCRYPT_MODE, new IEKeySpec(privKey, pubKey), iesParams);

        //adding extra bits to ensure properly working encryption
        byte[] encryption = new byte[data.length + padding.length];
        System.arraycopy(padding,0,encryption,data.length,padding.length);
        System.arraycopy(data, 0, encryption, 0, data.length);


        return cipher.doFinal(encryption,0, encryption.length);
    }
    /*
     * @Param data given encrypted byte- array
     * @Param pubKey Public key of the sender
     * @Param privKey Private Key of the receiver
     * @Param random Random used to intialize cipher
    */
    public static byte[] decryptECIES(byte[] data, PublicKey pubKey, PrivateKey privKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ShortBufferException {
        Cipher cipher = Cipher.getInstance("ECIES");

        // create derivation and encoding vectors
        byte[] d = Hex.decode("202122232425262728292a2b2c2d2e2f");
        byte[] e = Hex.decode("303132333435363738393a3b3c3d3e3f");
        IESParameterSpec param = new IESParameterSpec(d, e, 256);
        // B-key private, A-key public
        cipher.init(Cipher.DECRYPT_MODE, new IEKeySpec(privKey, pubKey), param);

        return removePadding(cipher.doFinal(data, 0, data.length));
    }
    /* Removes extra-bits added in encryption
    * @Param data given decrypted data
   */
    public static byte[] removePadding(byte[] data){
        for (int i = data.length-padding.length; i < padding.length; i++) {
            if(data[i] != padding[i]){
                return null;
            }
        }
        byte[] realData = new byte[data.length-padding.length];
        System.arraycopy(data,0,realData,0,realData.length);

        return realData;
    }
    /*Finds users private key and decodes it
    */
    public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        SharedPreferencesService sh = new SharedPreferencesService(ContextHandler.getAppContext());
        String privKey = sh.getPrivateKey();

        return KeyHandler.decodePrivate(privKey);
    }
}
