package se3350.habittracker;

import org.junit.Test;

import se3350.habittracker.activities.CreatePasswordActivity;

import static org.junit.Assert.*;

public class CreatePasswordUnitTest {
    @Test
    public void testIsPasswordEmpty_EmptyPass() {
        String pass = "";
        boolean result = CreatePasswordActivity.isPasswordEmpty(pass);
        assertTrue(result);
    }

    @Test
    public void testIsPasswordEmpty_EmptyWithSpacePass() {
        String pass = "         ";
        boolean result = CreatePasswordActivity.isPasswordEmpty(pass);
        assertTrue(result);
    }

    @Test
    public void testIsPasswordEmpty_NotEmptyPass() {
        String pass = "a password";
        boolean result = CreatePasswordActivity.isPasswordEmpty(pass);
        assertFalse(result);
    }

    @Test
    public void testPasswordMatch_PassMatch() {
        boolean result = CreatePasswordActivity.passwordsMatch("ab", "ab");
        assertTrue(result);
    }

    @Test
    public void testPasswordMatch_PassNotMatch() {
        boolean result = CreatePasswordActivity.passwordsMatch("abba", "ab");
        assertFalse(result);
    }
}
