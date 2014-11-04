package app.security;

public class CryptoHandler {

    private static byte[] text = "KISSA".getBytes();
    public CryptoHandler(){

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
