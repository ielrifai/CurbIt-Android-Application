package se3350.habittracker;

import org.junit.Test;
import se3350.habittracker.activities.StepActivity;

import static org.junit.Assert.*;

public class StepActivityTest {

    // Tests for journal entry input - that no entry input is blank or empty

    // Test non-empty strings are accepted
    @Test
    public void testNotStepEmpty(){
        boolean expectedResult = false;
        boolean result = StepActivity.stepEmpty("This string is not empty");
        assertEquals(expectedResult, result);
    }

    // Test empty strings are not accepted
    @Test
    public void testStepEmpty(){
        boolean expectedResult = true;
        boolean result = StepActivity.stepEmpty("");
        assertEquals(expectedResult, result);
    }

    // Test blank space strings are not accepted
    @Test
    public void testStepEmptySpace(){
        boolean expectedResult = true;
        boolean result = StepActivity.stepEmpty(" ");
        assertEquals(expectedResult, result);
    }
}
