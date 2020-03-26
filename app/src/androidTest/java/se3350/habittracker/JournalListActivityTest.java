package se3350.habittracker;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import se3350.habittracker.activities.JournalListActivity;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class JournalListActivityTest {

    @Test
    public void testSearchValidator() {
        System.out.println("search validator for correct format");
        String newText = "03/16/20";
        boolean result = JournalListActivity.searchValidator(newText);
        assertThat(result, is(true));
    }

    @Test
    public void testSearchValidator1() {
        System.out.println("search validator for empty search");
        String newText = "";
        boolean result = JournalListActivity.searchValidator(newText);
        assertThat(result, is(false));
    }

    @Test
    public void testSearchValidator2() {
        System.out.println("search validator for wrong format");
        String newText = "3-16-20";
        boolean result = JournalListActivity.searchValidator(newText);
        assertThat(result, is(false));
    }

    @Test
    public void testSearchValidator3() {
        System.out.println("search validator for wrong format");
        String newText = "3/16/2020";
        boolean result = JournalListActivity.searchValidator(newText);
        assertThat(result, is(false));
    }

    @Test
    public void testSearchValidator4() {
        System.out.println("search validator for wrong format");
        String newText = "03/16/20";
        boolean result = JournalListActivity.searchValidator(newText);
        assertThat(result, is(false));
    }




}