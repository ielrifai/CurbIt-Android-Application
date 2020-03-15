package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import se3350.habittracker.R;

public class Step2EntryActivity extends StepActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_step2_entry);
        super.onCreate(savedInstanceState);
    }

    protected void setEntryText(){
        stepEntryInput.setText(journalEntry.step2);
    }

    protected void save(){
        // Save the step 2 journal entry text
        journalEntry.step2 = stepEntryInput.getText().toString();
        super.save();
    }

    @Override
    protected void goToNext() {
        //Use intents to move on to the next step activity
        Intent intent = new Intent(Step2EntryActivity.this, Step3EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
        startActivity(intent);
    }

    @Override
    protected void goToPrevious() {
        //Use intents to move on to the previous step activity
        Intent intent = new Intent(Step2EntryActivity.this, Step1EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
