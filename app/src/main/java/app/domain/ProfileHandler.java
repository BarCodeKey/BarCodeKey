package app.domain;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.KeyType;
import ezvcard.property.Key;
import ezvcard.property.StructuredName;

/**
 * Class for turning info QR-code appropriate
 */
public class ProfileHandler {

    private static final String KEY_FORMAT = "KEY;ENCODING=B:";
    private final String defVal = "";
    private SharedPreferences preferences;
    private VCard vCard;
    private String publicKey;

    public ProfileHandler(Activity activity) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.vCard = new VCard();
    }

    public void readFromSharedPreferences(){
        StructuredName name = new StructuredName();
        name.setFamily(preferences.getString("last_name", defVal));
        name.setGiven(preferences.getString("first_name", defVal));

        this.vCard.setStructuredName(name);
        this.vCard.addEmail(preferences.getString("email", defVal));
        this.vCard.addTelephoneNumber(preferences.getString("number", defVal));
    }

    public void setPublicKey(String keyValue){
        this.publicKey = keyValue;
    }

    @Override
    public String toString(){
        return clean(this.vCard.write());
    }

    public VCard getVCard(){
        return vCard;
    }

    public String clean(String string){
        String[] lines = string.split("\\r?\\n");
        String cleanString = "";

        for (int i = 0; i < lines.length; i++){
            if (lines[i].startsWith("PRODID")){
                cleanString += KEY_FORMAT + this.publicKey + "\n";
            } else{
                cleanString += lines[i] + "\n";
            }
        }
        return cleanString;
    }
}
