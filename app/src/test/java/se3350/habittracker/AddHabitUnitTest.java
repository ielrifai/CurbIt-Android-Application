package se3350.habittracker;

import android.util.Log;

import org.junit.Test;

import se3350.habittracker.activities.AddHabitActivity;

import static org.junit.Assert.*;

public class AddHabitUnitTest {
    @Test
    public void testCheckEmptyHabitForm() {
        String name = "";
        String description = "";
        boolean expectedResult = false;
        boolean result = AddHabitActivity.checkEmptyHabitForm(name, description);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCheckNullHabitForm() {
        boolean expectedResult = false;
        boolean result = AddHabitActivity.checkNullHabitForm(null, null);
        assertFalse(result);
    }

}
