package app.barcodekey;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import app.domain.KeyHandler;

/**
 * Class for turning info QR-code appropriate
 */
public class vCard_maker {

    private String key;
    private String first_name;
    private String last_name;
    private String number;
    private String email;
    private String defVal = "kissa";
    private SharedPreferences pref;
    private KeyHandler keyHandler;

    public vCard_maker(Activity activity) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, UnsupportedEncodingException {
       this.pref =  PreferenceManager.getDefaultSharedPreferences(activity);
        this.keyHandler = new KeyHandler(activity);

        //set values to be shown in QR-code
        this.first_name = pref.getString("first_name",defVal);
        this.last_name = pref.getString("last_name",defVal);
        this.email = pref.getString("email",defVal);
        this.number = pref.getString("number",defVal);
        this.key = keyHandler.createKeys();

    }

    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getKey(){
        return "A" + key;
    }
    public void resetKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException, UnsupportedEncodingException {
        this.key = keyHandler.createKeys();
    }
    public void setAll(){
        this.first_name = pref.getString("first_name",defVal);
        this.last_name = pref.getString("last_name",defVal);
        this.email = pref.getString("email",defVal);
        this.number = pref.getString("number",defVal);
    }


    public String toVCard(){
        String info;
        info = "BEGIN:VCARD/n";
        info ="VERSION 3.0/n";
        if(!getLast_name().isEmpty() && !getFirst_name().isEmpty()) {
            info += "N:" + getLast_name() + ";" + getFirst_name() + "/n";
        }
        if(!getNumber().isEmpty()) {
            info += "TEL:" + getNumber() + "/n";
        }
        if(!getEmail().isEmpty()) {
            info += "EMAIL:" + getEmail() + "/n";
        }
        if(!getKey().isEmpty()) {
            info += "KEY:" + getKey() + "/n";
        }
        info +="END:VCARD";
        return info;
    }
    /*public void parseVCard(String card){

    }*/
}
