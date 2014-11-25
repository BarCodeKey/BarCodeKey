package app.security;

import
        android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.IEKeySpec;
import org.spongycastle.jce.spec.IESParameterSpec;
import org.spongycastle.openpgp.*;

import org.spongycastle.openpgp.operator.bc.BcPBEDataDecryptorFactory;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.jcajce.JcePBEKeyEncryptionMethodGenerator;
import org.spongycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.spongycastle.openpgp.PGPEncryptedDataGenerator;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
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

import app.barcodekey.MainMenu;
import app.contacts.ContactsHandler;
import app.util.Constants;

public class CryptoHandler{

    private static byte[] text = "ERROR".getBytes();
    private KeyHandler kh;
    private Context context;



    public CryptoHandler(){
        context = ContextHandler.getAppContext();
        kh = new KeyHandler();
    }

    public byte[] encryptHandler(String type, byte[] data, String lookupKey)throws Exception{

        int algorithm = 0;

        if(type.isEmpty() || data == null || lookupKey.isEmpty()){
            return null;
        }
        char[] passphrase= getPassphrase(lookupKey);

        if(type.equals(Algorithm.AES256.getCurveName())){
            algorithm = Algorithm.AES256.getValue();
        }
        else if(type.equals(Algorithm.AES192.getCurveName())){
            algorithm = Algorithm.AES192.getValue();
        }
        else if(type.equals(Algorithm.AES128.getCurveName())){
            algorithm = Algorithm.AES128.getValue();
        }
        else{
            return text;
        }
        return encrypt(data,passphrase,"file",algorithm,false);

    }

    public byte[] decryptHandler(String type, byte[] data,String uri)throws Exception{

        if(type.isEmpty() || data == null || uri.isEmpty()){
            return null;
        }
        char[] passphrase = getPassphrase(uri);

        if(Algorithm.AES128.getCurveNames().contains(type)){
            return decrypt(data, passphrase);
        }
        return null;
    }
    public char[] getPassphrase(String lookupKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchProviderException {
        // TODO: DOES THIS WORK??????
        String key = new ContactsHandler(context).readMimetypeData2(lookupKey, Constants.MIMETYPE_PUBLIC_KEY);
        return kh.getSecret(key).toCharArray();
    }

    //does something to byte array
    public static byte[] decrypt(byte[] data, char[] passPhrase) throws Exception{
        InputStream in = new ByteArrayInputStream(data);

        in = PGPUtil.getDecoderStream(in);
        PGPObjectFactory  pgpF = new PGPObjectFactory(in);
        PGPEncryptedDataList   enc = null;

        Object o = pgpF.nextObject();

        if (o instanceof PGPEncryptedDataList){
            enc = (PGPEncryptedDataList)o;
        }
        else{
            enc = (PGPEncryptedDataList)pgpF.nextObject();
         }
        PGPPBEEncryptedData pbe = (PGPPBEEncryptedData)enc.get(0);
        InputStream clear = pbe.getDataStream(new BcPBEDataDecryptorFactory(passPhrase,new BcPGPDigestCalculatorProvider()));

        PGPObjectFactory pgpFact = new PGPObjectFactory(clear);
        PGPCompressedData   cData = (PGPCompressedData)pgpFact.nextObject();

        pgpFact = new PGPObjectFactory(cData.getDataStream());
        PGPLiteralData  ld = (PGPLiteralData)pgpFact.nextObject();

        InputStream unc = ld.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int ch;
        while ((ch = unc.read()) >= 0){
            out.write(ch);
         }
        byte[] returnBytes = out.toByteArray();
        out.close();

        return returnBytes;
    }
    //does something to byte array
    public static byte[] encrypt(
                       byte[]     data,
                       char[] passPhrase,
                       String         fileName,
                       final int            algorithm,
                       boolean     armor)
               throws IOException, PGPException, NoSuchProviderException
           {
               if (fileName == null) {
                   fileName= PGPLiteralData.CONSOLE;
               }
               ByteArrayOutputStream    encryptOut = new ByteArrayOutputStream();
               OutputStream out = encryptOut;

               if (armor) {
                   out = new ArmoredOutputStream(out);
               }
               ByteArrayOutputStream   byteOut = new ByteArrayOutputStream();

               PGPCompressedDataGenerator compressedData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
               OutputStream cos = compressedData.open(byteOut);
               PGPLiteralDataGenerator literalData = new PGPLiteralDataGenerator();

               OutputStream  pOut = literalData.open(cos,PGPLiteralData.BINARY,fileName,data.length,new Date());

               pOut.write(data);

               compressedData.close();
               literalData.close();


               BouncyCastleProvider bc = new BouncyCastleProvider();
               PGPEncryptedDataGenerator  cPk =  new PGPEncryptedDataGenerator(new JcePGPDataEncryptorBuilder(algorithm).setSecureRandom(new SecureRandom()).setProvider(bc));
               cPk.addMethod(new JcePBEKeyEncryptionMethodGenerator(passPhrase));

               byte[] bytes = byteOut.toByteArray();
               OutputStream    cOut = cPk.open(out, bytes.length);

               cOut.write(bytes);
               cOut.close();
               out.close();
               return  encryptOut.toByteArray();

           }
    //encrypts/decrypts using ECIES-keypair keys and ECIES algorithm
   /* public static byte[] encryptECIES(byte[] data, PublicKey pubKey, PrivateKey privKey, SecureRandom random) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("ECIES", "SC");

        //create vectors for derivation and encoding
        //TODO: creating vectors from individual id's or something???
        byte[] d = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] e = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1 };
        IESParameterSpec iesParams = new IESParameterSpec(d,e, 256);
        cipher.init(Cipher.ENCRYPT_MODE, new IEKeySpec(privKey, pubKey), random);
        return cipher.doFinal(data);
    }
    public static byte[] decrypttECIES(byte[] data, PublicKey pubKey, PrivateKey privateKey, SecureRandom random) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("ECIES", "SC");

        // create derivation and encoding vectors
        byte[] d = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] e = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1 };
        IESParameterSpec param = new IESParameterSpec(d, e, 256);

        cipher.init(Cipher.DECRYPT_MODE, new IEKeySpec(privateKey, pubKey), random);
        return cipher.doFinal(data);
    }*/

            //Returns names of all available algorithms
           public static List<String> getAlgorithms(){
               return Algorithm.AES128.getCurveNames();
           }
}
