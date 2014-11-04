package app.preferences;

import android.test.InstrumentationTestCase;

import app.preferences.Validator;


public class ValidatorTest extends InstrumentationTestCase {

    private Validator validator;
    private Validator customValidator;

    @Override
    protected void setUp() throws Exception {
        validator = new Validator();
        customValidator = new Validator(10, 7, 10, 5, 10, 5, 10, 5);
    }

    public void testValidateEmailSyntax(){
        assertFalse(validator.validateEmail("sdfsdfsdfsdf"));
        assertFalse(validator.validateEmail("sdfsdfsdfsdf@"));
        assertFalse(validator.validateEmail("sdfsdfsdfsdf@sdfsdfdsfd"));
        assertFalse(validator.validateEmail("sdfsdfsdfsdf@@sddsfsdf.sdfsf"));
        assertFalse(validator.validateEmail("sdfsdfs dfsdf@sddsfsdf.ssf"));

        assertTrue(validator.validateEmail("sdfsdfsdfsd@sdfsd.cd"));
        assertTrue(validator.validateEmail("sami@sami.sam"));
        assertTrue(validator.validateEmail("sami.joku.jokutoinen.javiela@sami.sam"));
        assertTrue(validator.validateEmail("sami@sami.co.uk"));
    }

    public void testNumberSyntax(){
        assertFalse(validator.validateNumber("adsffdsfds"));
        assertFalse(validator.validateNumber("1234f424j42"));
        assertFalse(validator.validateNumber("040-4234343"));
        assertFalse(validator.validateNumber("2423432+432423"));

        assertTrue(validator.validateNumber("0409843520"));
        assertTrue(validator.validateNumber("+3588523458"));

    }

    public void testValidateEmailLength(){
        assertTrue(validator.validateEmail("a@s.fi"));
        assertFalse(validator.validateEmail(multiplyString("abcdfefghijklmnopqrstuvwxyz", 20) + "@sami.fi"));
    }

    public void testNumberLength(){
        assertTrue(validator.validateNumber(""));
        assertFalse(validator.validateNumber(multiplyString("012", 10)));
    }

    public void testFirstNameLength(){
        assertTrue(validator.validateFirstName(""));
        assertFalse(validator.validateFirstName(multiplyString("sami", 20)));
    }

    public void testLastNameLength(){
        assertTrue(validator.validateLastName(""));
        assertFalse(validator.validateLastName(multiplyString("paras", 15)));
    }

    public String multiplyString(String s, int n){
        String multipliedString = s;
        for (int i = 0; i < n; i++){
            multipliedString = multipliedString + s;
        }
        return multipliedString;
    }

    public void testValidateFunctionEmail(){
        assertTrue(validator.validate("email", "esa@paras.fi"));
        assertFalse(validator.validate("email", multiplyString("abcdfefghijklmnopqrstuvwxyz", 20) + "@esa.fi"));
    }

    public void testValidateFunctionFirstname(){
        assertTrue(validator.validate("first_name", "hyväesa"));
        assertFalse(validator.validate("first_name", multiplyString("esa", 20)));
    }

    public void testValidateFunctionLastname(){
        assertTrue(validator.validate("last_name", "parastyyppi"));
        assertFalse(validator.validate("last_name", multiplyString("esa", 20)));
    }

    public void testValidateFunctionNumber(){
        assertTrue(validator.validate("somethingrandom", "0409843520"));
        assertTrue(validator.validate("somethingrandom", "+358409843520"));

        assertFalse(validator.validate("somethingrandom", "040-4234343"));
    }

    public void testCustomValidatorEmail(){
        assertTrue(customValidator.validateEmail("esa@esa.fi"));

        assertFalse(customValidator.validateEmail("esa@huonodomain.fi"));
        assertFalse(customValidator.validateEmail("e@e.fi"));
    }

    public void testCustomValidatorFirstName(){
        assertTrue(customValidator.validateFirstName("hyväesa"));

        assertFalse(customValidator.validateFirstName("esa"));
        assertFalse(customValidator.validateFirstName("huononimiesa"));
    }

    public void testCustomValidatorLastName(){
        assertTrue(customValidator.validateLastName("hyvämies"));

        assertFalse(customValidator.validateLastName("mies"));
        assertFalse(customValidator.validateLastName("huononimimies"));
    }

    public void testCustomValidatorNumber(){
        assertTrue(customValidator.validateNumber("040111222"));

        assertFalse(customValidator.validateNumber("0401"));
        assertFalse(customValidator.validateNumber("0400123456789"));
    }
}