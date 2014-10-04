package app.domain;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;

import app.barcodekey.R;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.StructuredName;


/**
 * Created by szetk on 10/4/14.
 */
public class ContactsHandler{

    private Context context;

    public ContactsHandler(Context context){
        this.context = context;
    }

    public void addOrEditContact(String string) {
        VCard vCard = Ezvcard.parse(string).first();

        Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        i.putExtra(ContactsContract.Intents.Insert.NAME, vCard.getStructuredName().getGiven() + " " + vCard.getStructuredName().getFamily());
        i.putExtra(ContactsContract.Intents.Insert.PHONE, vCard.getTelephoneNumbers().get(0).getText());
        i.putExtra(ContactsContract.Intents.Insert.EMAIL, vCard.getEmails().get(0).getValue());
        i.putExtra(ContactsContract.Intents.Insert.NOTES, R.string.has_public_key);
        context.startActivity(i);
        // tallenna vielä henkilölle public key

    }

    public void addSami()  {
        VCard vcard = new VCard();

        StructuredName n = new StructuredName();
        n.setFamily("Parasmies");
        n.setGiven("Sami");
        vcard.setStructuredName(n);
        vcard.addTelephoneNumber("+358432398212433");
        vcard.addEmail("fsdfdsfsd.fds@sami.sami");

        addOrEditContact(vcard.write());
    }

    /**
    public String[] getFields(String vCard){
        String[] fields = new String[4];
        String[] lines = vCard.split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++){
            String line = lines[i];
            if (line.startsWith("N:")){
                fields[0] = parseName(line);
            } else if (line.startsWith("TEL")){
                fields[1] = parseNumber(line);
            } else if (line.startsWith("EMAIL")){
                fields[2] = parseEmail(line);
            } else if (line.startsWith("KEY")){
                saveKey(line);
            }
        }
        return fields;
    }

     **/


}
