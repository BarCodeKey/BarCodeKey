package app.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    private Pattern emailPattern;
    private Matcher matcher;
    private final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private int maxEmailLength;
    private int minEmailLength;
    private int maxFirstNameLength;
    private int minFirstNameLength;
    private int maxLastNameLength;
    private int minLastNameLength;
    private int maxNumberLength;
    private int minNumberLength;

    public InputValidator(){
        this.emailPattern = Pattern.compile(EMAIL_PATTERN);
        this.maxEmailLength = 300;
        this.minEmailLength = 0;
        this.maxFirstNameLength = 50;
        this.minFirstNameLength = 0;
        this.maxLastNameLength = 50;
        this.minLastNameLength = 0;
        this.maxNumberLength = 25;
        this.minNumberLength = 0;
    }

    public InputValidator(int maxEmailLength, int minEmailLength, int maxFirstNameLength, int minFirstNameLength, int maxLastNameLength, int minLastNameLength, int maxNumberLength, int minNumberLength) {
        this.emailPattern = Pattern.compile(EMAIL_PATTERN);
        this.maxEmailLength = maxEmailLength;
        this.minEmailLength = minEmailLength;
        this.maxFirstNameLength = maxFirstNameLength;
        this.minFirstNameLength = minFirstNameLength;
        this.maxLastNameLength = maxLastNameLength;
        this.minLastNameLength = minLastNameLength;
        this.maxNumberLength = maxNumberLength;
        this.minNumberLength = minNumberLength;
    }

    public boolean validate(String field, String value) {
        if (field.equals("email")){
            return validateEmail(value);
        } else if (field.equals("first_name")){
            return validateFirstName(value);
        } else if(field.equals("last_name")){
            return validateLastName(value);
        }  else{ // is number
            return validateNumber(value);
        }

    }

    public boolean validateEmail(String email){
        if(email.length() < this.minEmailLength || email.length() > this.maxEmailLength){
            return false;
        }

        matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public boolean validateFirstName(String firstName){
        if(firstName.length() < this.minFirstNameLength || firstName.length() > this.maxFirstNameLength){
            return false;
        }
        return true;
    }

    public boolean validateLastName(String lastName){
        if(lastName.length() < this.minLastNameLength || lastName.length() > this.maxLastNameLength){
            return false;
        }
        return true;
    }

    public boolean validateNumber(String number){
        if(number.length() < this.minNumberLength || number.length() > this.maxNumberLength){
            return false;
        }
        return containsOnlyValidChars(number);
    }

    public boolean containsOnlyValidChars(String number){
        for(int i = 0; i < number.length(); i++){
            if (i == 0){
                if(!"+0123456789".contains("" + number.charAt(i))) {
                    return false;
                }
            } else if(!"0123456789".contains("" + number.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
