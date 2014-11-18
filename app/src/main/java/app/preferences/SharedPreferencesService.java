package app.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesService {
    private SharedPreferences preferences;
    private final String defVal = "";

    public SharedPreferencesService(Context context){
    this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

}

    public String getFamily(){
        return preferences.getString("last_name", defVal);
    }

    public String getGiven(){
        return preferences.getString("first_name", defVal);
    }

    public String getEmail(){
        return preferences.getString("email", defVal);
    }

    public String getNumber(){
        return preferences.getString("number", defVal);
    }

    public void setPublicKey(String value){
        setKey("public_key", value);
    }

    public void setPrivateKey(String value){
        setKey("private_key", value);
    }

    public String getPublicKey(){
        return getKey("public_key");
    }

    public String getPrivateKey(){
        return getKey("private_key");
    }

    public void setKey(String key, String value) {
        SharedPreferences.Editor editor = this.preferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public String getKey(String key) {
        String value = "";
        value = this.preferences.getString(key, "");

        return value;
    }
}