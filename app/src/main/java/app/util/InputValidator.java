package app.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates user-inputted text
 */
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

    /**
     * A validator with fixed field lengths
     */
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

    /**
     * A validator where field lenghts are set with parameters.
     *
     * @param maxEmailLength
     * @param minEmailLength
     * @param maxFirstNameLength
     * @param minFirstNameLength
     * @param maxLastNameLength
     * @param minLastNameLength
     * @param maxNumberLength
     * @param minNumberLength
     */
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

    /**
     * Validates text fields
     *
     * @param field the field where the text should go in
     * @param value the text that is validated
     * @return is the text valid or not
     */
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

    /**
     * Validates an email address
     *
     * @param email the email address
     * @return is the address valid or not
     */
    public boolean validateEmail(String email){
        if(email.length() < this.minEmailLength || email.length() > this.maxEmailLength){
            return false;
        }

        matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validates a first name text
     *
     * @param firstName the first name
     * @return valid/not valid
     */
    public boolean validateFirstName(String firstName){
        if(firstName.length() < this.minFirstNameLength || firstName.length() > this.maxFirstNameLength){
            return false;
        }
        return true;
    }

    /**
     * Validates a last name text
     *
     * @param lastName the last name
     * @return valid/not valid
     */
    public boolean validateLastName(String lastName){
        if(lastName.length() < this.minLastNameLength || lastName.length() > this.maxLastNameLength){
            return false;
        }
        return true;
    }

    /**
     * Validates a phone number
     *
     * @param number the phone number
     * @return valid/not valid
     */
    public boolean validateNumber(String number){
        if(number.length() < this.minNumberLength || number.length() > this.maxNumberLength){
            return false;
        }
        return containsOnlyValidChars(number);
    }

    /**
     * Checks a phone number's characters and numbers
     *
     * @param number the phone number
     * @return valid/not valid
     */
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
