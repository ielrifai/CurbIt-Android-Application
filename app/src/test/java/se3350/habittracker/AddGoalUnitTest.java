package se3350.habittracker;

import org.junit.Test;

import se3350.habittracker.activities.AddGoalActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddGoalUnitTest {

    @Test
    public void testIsGoalFormEmpty_EmptyField() {
        String name = "";
        boolean expectedResult = true;
        boolean result = AddGoalActivity.isGoalFormEmpty(name);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsGoalFormEmpty_EmptyWithSpaceField() {
        String name = "         ";
        boolean result = AddGoalActivity.isGoalFormEmpty(name);
        assertTrue(result);
    }
    @Test
    public void testIsGoalFormEmpty_NotEmptyField() {
        String name = "a goal name";
        boolean result = AddGoalActivity.isGoalFormEmpty(name);
        assertFalse(result);
    }
}
