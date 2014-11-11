package app.security;

import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.*;

import org.spongycastle.openpgp.operator.PGPDataDecryptorFactory;
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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptoHandler {

    private static byte[] text = "KISSA".getBytes();
    private static KeyHelper helper = new KeyHelper();
    public CryptoHandler(){

    }

    public byte[] encryptHandler(String type, byte[] data)throws Exception{
        if(type.isEmpty()){
            return null;
        }else if(data == null){
            return null;
        }
        else if(type.equals("AES")){
            byte[] rawKey = helper.getRawKey("seed".getBytes());
            return encryptAES(data, rawKey);
        }else if(type.equals("Openpgp")){
            return encrypt(data,"salasana".toCharArray(),"file",PGPEncryptedDataGenerator.AES_256,false);
        }
        return text;
    }

    public byte[] decryptHandler(String type, byte[] data)throws Exception{
        if(type.isEmpty()){
            return null;
        }else if(data == null){
            return null;
        }
        else if(type.equals("AES")){
            byte[] rawKey = helper.getRawKey("seed".getBytes());
            return null;
        }else if(type.equals("Openpgp")){
            return decrypt(data, "salasana".toCharArray());
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
    private static byte[] encryptAES(byte[] data, byte[] raw)throws Exception{

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(data);
    }

    public static String encryptSimple(byte[] secret){
        if(secret.length == 0){
            return new String(text);
        }
        byte[] encoded = new byte[text.length+secret.length];
        int j = text.length;
        for (int i = 0; i < text.length; i++) {
                    encoded[i] = text[i];
            }
        for (int i = j; i < secret.length; i++) {
            encoded[i] = secret[i];
        }

        return new String(encoded);
    }
     public static String decryptSimple(byte[] bytes){
            String keycode = new String(bytes);
            keycode = keycode.substring(text.length,bytes.length);

            return keycode;
    }
}
