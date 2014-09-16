package app.domain;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KeyHandler {

    private SharedPreferences preferences;


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



}
