package app.contacts;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import app.preferences.SettingsHandler;
import ezvcard.VCard;
import ezvcard.property.StructuredName;

/**
 * Class for turning info QR-code appropriate
 */
public class VCardHandler {

    private static final String KEY_FORMAT = "KEY;ENCODING=B:";
    private VCard vCard;
    private String publicKey;
    private SettingsHandler settingsHandler;

    public VCardHandler(Activity activity) {
        this.vCard = new VCard();
        this.settingsHandler = new SettingsHandler(activity);
    }

    public void readFromSharedPreferences(){
        this.vCard.setStructuredName(settingsHandler.getName());
        this.vCard.addEmail(settingsHandler.getEmail());
        this.vCard.addTelephoneNumber(settingsHandler.getNumber());
    }

    public void setPublicKey(String keyValue){
        this.publicKey = keyValue;
    }

    @Override
    public String toString(){
        return cleanPublicKeyFromString(this.vCard.write());
    }

    public VCard getVCard(){
        return vCard;
    }

    public String cleanPublicKeyFromString(String string) {
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

    /**
     * This method cleans public key off from given vCard formatted String and returns clened vCard and the public key in an array
     * @param vCardString String in vCard to be cleaned
     * @return String array where 1st object is cleaned vCard and 2nd object is the public key
     */
    public static String[] cleanPublicKeyFromStringAndGetPublicKey(String vCardString) {
        String[] lines = vCardString.split("\\r?\\n");
        String publicKey = "";
        String cleanVCard= "";

        // Add lines to cleanVCard (expect the key and the last line)
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].startsWith(KEY_FORMAT)) {
                publicKey = lines[i].replace(KEY_FORMAT, "");
            }  else {
                cleanVCard += lines[i] + "\n";
            }
        }
        // Add the last line (we don't want to add newline at the end of cleanVCard)
        cleanVCard += lines[lines.length - 1];
        return new String[]{cleanVCard, publicKey};
    }

}
