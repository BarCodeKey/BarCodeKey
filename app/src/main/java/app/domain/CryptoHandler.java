package app.domain;

public class CryptoHandler {

    public CryptoHandler(){

    }

    public static String encodeSimple(byte[] secret){
        byte[] key = "KUKKULUURUU".getBytes();
        byte[] encoded = new byte[key.length+secret.length];
        int j = key.length;
        for (int i = 0; i < key.length; i++) {
                    encoded[i] = key[i];
            }
        for (int i = j; i < secret.length; i++) {
            encoded[i] = secret[i];
        }

        return new String(encoded);
    }
     public static String decryptSimple(byte[] bytes){
            byte[] keycode = new byte[bytes.length];
            for (int i = 11; i < bytes.length; i++) {
            keycode[i] = bytes[i];
            }
            String decoded = new String(keycode);
            return decoded;
    }
}
