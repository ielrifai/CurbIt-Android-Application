package se3350.habittracker;

import org.junit.Test;

import se3350.habittracker.activities.CreatePasswordActivity;

import static org.junit.Assert.*;

public class CreatePasswordUnitTest {
    @Test
    public void testCheckEmptyPass_EmptyPass() {
        String pass = "";
        String pass2 ="";
        boolean expectedResult = false;
        boolean result = CreatePasswordActivity.checkEmptyPass(pass, pass2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckNullPass_NullPass() {
        boolean expectedResult = false;
        boolean result = CreatePasswordActivity.checkNullPass(null, null);
        assertFalse(result);
    }

    @Test
    public void testCheckEncryption_PassNotEncrypted() {
        String encryptedPass = "password";
        String pass = "password";
        boolean expectedResult = true;
        boolean result = CreatePasswordActivity.checkEmptyPass(encryptedPass, pass);
        assertEquals(expectedResult, result);
    }

}
