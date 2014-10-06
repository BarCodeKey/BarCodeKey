package app.barcodekey;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class for turning info QR-code appropriate
 */
public class QR_info {

    private String key;
    private String first_name;
    private String last_name;
    private String number;
    private String email;
    private String defVal = "kissa";
    private SharedPreferences pref;


    public QR_info(SharedPreferences pref){
       this.pref = pref;
        //set values to be shown in QR-code
       this.first_name = pref.getString("first_name",defVal);
        this.last_name = pref.getString("last_name",defVal);
        this.email = pref.getString("email",defVal);
        this.number = pref.getString("number",defVal);

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
    public void setKey(String key){
        this.key = key;
    }


    public String toVCard(){
        String info;
        info = "BEGIN:VCARD/n";
        info +="VERSION 3.0/n";
        info += "N:"+getLast_name()+";"+getFirst_name()+"/n";
        info += "TEL:"+getNumber()+"/n";
        info += "EMAIL:"+getEmail()+"/n";
        info += "KEY:"+getKey()+"/n";
        info +="END:VCARD";
        return info;
    }
    /*public void parseVCard(String card){

    }*/
}
