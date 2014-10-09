package app.domain;

import android.test.InstrumentationTestCase;

import app.domain.Validator;

public class ValidatorTest extends InstrumentationTestCase {

    private Validator validator = new Validator();

    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
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
}