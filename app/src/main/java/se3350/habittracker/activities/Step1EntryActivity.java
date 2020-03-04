package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import se3350.habittracker.R;

public class Step1EntryActivity extends StepActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_step1_entry);
        super.onCreate(savedInstanceState);
    }

    protected void save(){
        // Save the step 1 journal entry text
        journalEntry.step1 = stepEntryInput.getText().toString();
        super.save();
    }

    @Override
    protected void goToNext() {
        //Use intents to move on to the next step activity
        Intent intent = new Intent(Step1EntryActivity.this, Step2EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
        startActivity(intent);
    }
}
