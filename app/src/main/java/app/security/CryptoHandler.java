package app.security;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.*;

import org.spongycastle.openpgp.operator.bc.BcPBEDataDecryptorFactory;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.jcajce.JcePBEKeyEncryptionMethodGenerator;
import org.spongycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.spongycastle.openpgp.PGPEncryptedDataGenerator;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.*;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchProviderException;

import app.barcodekey.MainMenu;

public class CryptoHandler{

    private static byte[] text = "ERROR".getBytes();
    private KeyHandler kh;



    public CryptoHandler(){
        kh = new KeyHandler((Activity)ContextHandler.getAppContext());
    }

    public byte[] encryptHandler(String type, byte[] data, String uri)throws Exception{

        int algorithm = 0;

        if(type.isEmpty() || data == null || uri.isEmpty()){
            return null;
        }
        char[] passphrase= "".toCharArray();

        // TODO:find uri from ContactsHandler with String uri

        /*byte[] data = kh.getSecret();
        passphrase = new String(data).toCharArray();
        */

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
        char[] passphrase = "".toCharArray();

        // TODO:find uri from ContactsHandler with String uri

        /*byte[] data = kh.getSecret();
        passphrase = new String(data).toCharArray();
        */

        if(Algorithm.AES128.getCurveNames().contains(type)){
            return decrypt(data, passphrase);
        }
        return null;
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

            //Returns names of all available algorithms
           public static List<String> getAlgorithms(){
               return Algorithm.AES128.getCurveNames();
           }
    /*
        MAIN MENUN TESTIKOODI
   CryptoHandler ch = new CryptoHandler();

        byte[] result = "".getBytes();
        char[] passphrase = "".toCharArray();
        try {
            byte[] secret = kh.getSecret("kukkuluuruu");
            Key key = new SecretKeySpec(secret,"AES");
            passphrase = new String(secret).toCharArray();
            System.out.println(new String(secret));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        if(passphrase != "".toCharArray()){
        try {
           result = ch.encryptHandler("AES_256", "kissa".getBytes(), "");
            System.out.println(new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            byte[] result2 = ch.decryptHandler("AES_256",result,"");
            System.out.println(new String(result2));
        } catch (Exception e) {
            e.printStackTrace();
        }}
*/
}
