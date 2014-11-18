package app.contacts;

import android.content.Context;
import app.preferences.SharedPreferencesService;
import app.util.Constants;
import ezvcard.Ezvcard;
import ezvcard.VCard;

/**
 * Class for turning info QR-code appropriate
 */
public class Contact {

    private SharedPreferencesService sharedPreferencesService;

    private String given;
    private String family;
    private String email;
    private String number;
    private String publicKey;

    public Contact(Context context) {
        this.sharedPreferencesService = new SharedPreferencesService(context);
    }

    public Contact(Context context, String vCardString){
        this(context);
        setPublicKey(getPublicKey(vCardString));
        vCardString = removePublicKeyFromString(vCardString);

        VCard vCard = Ezvcard.parse(vCardString).first();
        setGiven(vCard.getStructuredName().getGiven());
        setFamily(vCard.getStructuredName().getFamily());
        setNumber(vCard.getTelephoneNumbers().get(0).getText());
        setEmail(vCard.getEmails().get(0).getValue());
    }

    public void readFromSharedPreferences(){
        setGiven(sharedPreferencesService.getGiven());
        setFamily(sharedPreferencesService.getFamily());
        setNumber(sharedPreferencesService.getNumber());
        setEmail(sharedPreferencesService.getEmail());
        setPublicKey(sharedPreferencesService.getPublicKey());
    }

    /**
     * This method cleans public key off from given vCard formatted String and returns cleaned vCard and the public key in an array
     * @param vCardString String in vCard to be cleaned
     * @return String array where 1st object is cleaned vCard and 2nd object is the public key
     */
    public String getPublicKey(String vCardString) {
        String[] lines = vCardString.split("\\r?\\n");

        // If line starts with key format, it contains
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(Constants.KEY_FORMAT_BASE64)) {
                return lines[i].replace(Constants.KEY_FORMAT_BASE64 + ":", "");
            }
        }
        return null;
    }

    public String removePublicKeyFromString(String vCardString){
        String[] lines = vCardString.split("\\r?\\n");
        String result = "";

        // Add lines to result (expect the key and the last line)
        for (int i = 0; i < lines.length - 1; i++) {
            if (!lines[i].startsWith(Constants.KEY_FORMAT_BASE64)) {
                result += lines[i] + "\n";
            }
        }
        return result;
    }

    @Override
    public String toString(){
        return "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:" + getFamily() + ";" + getGiven() + ";;;\n" +
                "EMAIL:" + getEmail() + "\n" +
                "TEL:" + getNumber() + "\n" +
                "KEY;" + Constants.KEY_FORMAT_BASE64 + ":" + getPublicKey() + "\n" +
                "END:VCARD";
    }

    /* This can be used if we want ez-vcard to build the output */
    /*
    @Override
     public String toString(){
        return addPublicKeyToVCardString(buildEZVCard().write());
    }

        public VCard buildEZVCard(){
        VCard vcard = new VCard();
        StructuredName name = new StructuredName();
        name.setGiven(getGiven());
        name.setFamily(getFamily());

        vcard.setStructuredName(name);
        vcard.addTelephoneNumber(getNumber());
        vcard.addEmail(getEmail());
        return vcard;
    }

    public String addPublicKeyToVCardString(String string) {
        String[] lines = string.split("\\r?\\n");
        String cleanString = "";

        for (int i = 0; i < lines.length; i++){
            if (lines[i].startsWith("PRODID")){ // this is dumb
                cleanString += Constants.KEY_FORMAT_BASE64 + this.publicKey + "\n";
            } else{
                cleanString += lines[i] + "\n";
            }
        }
        return cleanString;
    }

*/

    public String getPublicKey(){
        return this.publicKey;
    }

    public String getGiven() {
        return given;
    }

    public String getFamily() {
        return family;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public void setPublicKey(String publicKey){
        this.publicKey = publicKey;
    }

    public void setGiven(String given) {
        if (given == null){
            this.given = "";
        }
        this.given = given;
    }

    public void setFamily(String family) {
        if (family == null){
            this.family = "";
        }
        this.family = family;
    }

    public void setEmail(String email) {
        if (email == null){
            this.email = "";
        }
        this.email = email;
    }

    public void setNumber(String number) {
        if (number == null){
            this.number = "";
        }
        this.number = number;
    }

}
