package se3350.habittracker;

import android.util.Log;

import org.junit.Test;

import se3350.habittracker.activities.LoginActivity;

import static org.junit.Assert.*;

public class LoginUnitTest {

    @Test
    public void testCheckEmptyPass_EmptyPass() {
        String pass = "";
        boolean expectedResult = false;
        boolean result = LoginActivity.checkEmptyPass(pass);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckNullPass_NullPass() {
        boolean expectedResult = false;
        boolean result = LoginActivity.checkNullPass(null);
        assertFalse(result);
    }

}
