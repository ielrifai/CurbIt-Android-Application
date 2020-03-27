package se3350.habittracker;

import android.util.Log;

import org.junit.Test;

import se3350.habittracker.activities.AddHabitActivity;

import static org.junit.Assert.*;

public class AddHabitUnitTest {
    @Test
    public void testIsHabitFormEmpty__EmptyField() {
        String name = "";
        boolean expectedResult = true;
        boolean result = AddHabitActivity.isHabitFormEmpty(name);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsHabitFormEmpty_EmptyWithSpaceField() {
        String name = "         ";
        boolean result = AddHabitActivity.isHabitFormEmpty(name);
        assertTrue(result);
    }
    @Test
    public void testIsHabitFormEmpty_NotEmptyField() {
        String name = "a habit name";
        boolean result = AddHabitActivity.isHabitFormEmpty(name);
        assertFalse(result);
    }

}
