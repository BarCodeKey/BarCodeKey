package app.barcodekey;


import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Boolean_handler {

    SharedPreferences pref;

    public Boolean_handler(Activity activity){
        pref = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setValuesChanged(){
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("values_changed", "false");
        editor.commit();
    }
    public String isValuesChanged(){
        String result = pref.getString("values_changed", "false");
        return result;
    }
}
