package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import se3350.habittracker.R;

public class Step4EntryActivity extends StepActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_step4_entry);
        super.onCreate(savedInstanceState);
    }

    protected void save(){
        // Save the step 4 journal entry text
        journalEntry.step4 = stepEntryInput.getText().toString();
        journalEntry.isDraft = false;
        super.save();
    }

    @Override
    protected void goToNext() {
        goBackToHabitPage();
    }
}
