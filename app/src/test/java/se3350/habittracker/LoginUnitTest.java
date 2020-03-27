package se3350.habittracker;

import android.util.Log;

import org.junit.Test;

import se3350.habittracker.activities.LoginActivity;

import static org.junit.Assert.*;

public class LoginUnitTest {

    @Test
    public void testIsPasswordEmpty_EmptyPass() {
        String pass = "";
        boolean result = LoginActivity.isPasswordEmpty(pass);
        assertTrue(result);
    }

    @Test
    public void testIsPasswordEmpty_EmptyWithSpacePass() {
        String pass = "         ";
        boolean result = LoginActivity.isPasswordEmpty(pass);
        assertTrue(result);
    }

    @Test
    public void testIsPasswordEmpty_NotEmptyPass() {
        String pass = "a password";
        boolean result = LoginActivity.isPasswordEmpty(pass);
        assertFalse(result);
    }

}
