package app.preferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ezvcard.property.StructuredName;

/**
 * Created by szetk on 10/28/14.
 */
public class SettingsHandler {
    private SharedPreferences preferences;
    private final String defVal = "";


    public SettingsHandler(Activity activity){
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);

    }

    public String getFamily(){
        return preferences.getString("last_name", defVal);
    }

    public String getGiven(){
        return preferences.getString("first_name", defVal);
    }

    public StructuredName getName(){
        StructuredName name = new StructuredName();
        name.setFamily(getFamily());
        name.setGiven(getGiven());
        return name;
    }
    public String getEmail(){
        return preferences.getString("email", defVal);
    }

    public String getNumber(){
        return preferences.getString("number", defVal);
    }
}