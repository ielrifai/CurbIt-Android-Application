package se3350.habittracker;

import org.junit.Test;

import se3350.habittracker.activities.JournalListActivity;

import static org.junit.Assert.assertEquals;

public class JournalListActivityTest {

    @Test
    public void testSearchValidator_CorrectFormat() {
        System.out.println("search validator for correct format");
        String newText = "3/16/20";
        boolean expectedResult = true;
        boolean result = JournalListActivity.searchValidator(newText);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchValidator_EmptySearch() {
        System.out.println("search validator for empty search");
        String newText = "";
        boolean expectedResult = false;
        boolean result = JournalListActivity.searchValidator(newText);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchValidator_WrongFormat1() {
        System.out.println("search validator for wrong format");
        String newText = "3-16-20";
        boolean expectedResult = false;
        boolean result = JournalListActivity.searchValidator(newText);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchValidator_WrongFormat2() {
        System.out.println("search validator for wrong format");
        String newText = "3/16/2020";
        boolean expectedResult = false;
        boolean result = JournalListActivity.searchValidator(newText);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchValidator_WrongFormat3() {
        System.out.println("search validator for wrong format");
        String newText = "03/16/20";
        boolean expectedResult = false;
        boolean result = JournalListActivity.searchValidator(newText);
        assertEquals(expectedResult, result);
    }




}