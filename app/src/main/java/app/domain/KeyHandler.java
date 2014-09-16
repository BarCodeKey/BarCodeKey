package app.domain;


import android.app.Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;





public class KeyHandler {

    private SharedPreferences preferences;
    private String alias = "keys";


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
    //public void createKeys(Context context)throws NoSuchProviderException,
         //   NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        //Calendar cal = Calendar.getInstance();
        //Date now = cal.getTime();
        //cal.add(Calendar.YEAR, 1);
        //Date end = cal.getTime();

        //KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        //kpg.initialize(new KeyPairGeneratorSpec.Builder(context.getApplicationContext())
          //      .setAlias(alias)
            //    .setStartDate(now)
              //  .setEndDate(end)
                //.setSerialNumber(BigInteger.valueOf(1))
               // .setSubject(new X500Principal("CN=test1"))
                //.build());

        //KeyPair kp = kpg.generateKeyPair();
    //}



}
