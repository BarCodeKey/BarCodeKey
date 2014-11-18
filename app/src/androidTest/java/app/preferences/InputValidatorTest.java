package app.preferences;

import android.test.InstrumentationTestCase;

import app.util.InputValidator;


public class InputValidatorTest extends InstrumentationTestCase {

    private InputValidator inputValidator;
    private InputValidator customInputValidator;

    @Override
    protected void setUp() throws Exception {
        inputValidator = new InputValidator();
        customInputValidator = new InputValidator(10, 7, 10, 5, 10, 5, 10, 5);
    }

    public void testValidateEmailSyntax(){
        assertFalse(inputValidator.validateEmail("sdfsdfsdfsdf"));
        assertFalse(inputValidator.validateEmail("sdfsdfsdfsdf@"));
        assertFalse(inputValidator.validateEmail("sdfsdfsdfsdf@sdfsdfdsfd"));
        assertFalse(inputValidator.validateEmail("sdfsdfsdfsdf@@sddsfsdf.sdfsf"));
        assertFalse(inputValidator.validateEmail("sdfsdfs dfsdf@sddsfsdf.ssf"));

        assertTrue(inputValidator.validateEmail("sdfsdfsdfsd@sdfsd.cd"));
        assertTrue(inputValidator.validateEmail("sami@sami.sam"));
        assertTrue(inputValidator.validateEmail("sami.joku.jokutoinen.javiela@sami.sam"));
        assertTrue(inputValidator.validateEmail("sami@sami.co.uk"));
    }

    public void testNumberSyntax(){
        assertFalse(inputValidator.validateNumber("adsffdsfds"));
        assertFalse(inputValidator.validateNumber("1234f424j42"));
        assertFalse(inputValidator.validateNumber("040-4234343"));
        assertFalse(inputValidator.validateNumber("2423432+432423"));

        assertTrue(inputValidator.validateNumber("0409843520"));
        assertTrue(inputValidator.validateNumber("+3588523458"));

    }

    public void testValidateEmailLength(){
        assertTrue(inputValidator.validateEmail("a@s.fi"));
        assertFalse(inputValidator.validateEmail(multiplyString("abcdfefghijklmnopqrstuvwxyz", 20) + "@sami.fi"));
    }

    public void testNumberLength(){
        assertTrue(inputValidator.validateNumber(""));
        assertFalse(inputValidator.validateNumber(multiplyString("012", 10)));
    }

    public void testFirstNameLength(){
        assertTrue(inputValidator.validateFirstName(""));
        assertFalse(inputValidator.validateFirstName(multiplyString("sami", 20)));
    }

    public void testLastNameLength(){
        assertTrue(inputValidator.validateLastName(""));
        assertFalse(inputValidator.validateLastName(multiplyString("paras", 15)));
    }

    public String multiplyString(String s, int n){
        String multipliedString = s;
        for (int i = 0; i < n; i++){
            multipliedString = multipliedString + s;
        }
        return multipliedString;
    }

    public void testValidateFunctionEmail(){
        assertTrue(inputValidator.validate("email", "esa@paras.fi"));
        assertFalse(inputValidator.validate("email", multiplyString("abcdfefghijklmnopqrstuvwxyz", 20) + "@esa.fi"));
    }

    public void testValidateFunctionFirstname(){
        assertTrue(inputValidator.validate("first_name", "hyväesa"));
        assertFalse(inputValidator.validate("first_name", multiplyString("esa", 20)));
    }

    public void testValidateFunctionLastname(){
        assertTrue(inputValidator.validate("last_name", "parastyyppi"));
        assertFalse(inputValidator.validate("last_name", multiplyString("esa", 20)));
    }

    public void testValidateFunctionNumber(){
        assertTrue(inputValidator.validate("somethingrandom", "0409843520"));
        assertTrue(inputValidator.validate("somethingrandom", "+358409843520"));

        assertFalse(inputValidator.validate("somethingrandom", "040-4234343"));
    }

    public void testCustomValidatorEmail(){
        assertTrue(customInputValidator.validateEmail("esa@esa.fi"));

        assertFalse(customInputValidator.validateEmail("esa@huonodomain.fi"));
        assertFalse(customInputValidator.validateEmail("e@e.fi"));
    }

    public void testCustomValidatorFirstName(){
        assertTrue(customInputValidator.validateFirstName("hyväesa"));

        assertFalse(customInputValidator.validateFirstName("esa"));
        assertFalse(customInputValidator.validateFirstName("huononimiesa"));
    }

    public void testCustomValidatorLastName(){
        assertTrue(customInputValidator.validateLastName("hyvämies"));

        assertFalse(customInputValidator.validateLastName("mies"));
        assertFalse(customInputValidator.validateLastName("huononimimies"));
    }

    public void testCustomValidatorNumber(){
        assertTrue(customInputValidator.validateNumber("040111222"));

        assertFalse(customInputValidator.validateNumber("0401"));
        assertFalse(customInputValidator.validateNumber("0400123456789"));
    }
}