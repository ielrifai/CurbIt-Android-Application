package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import se3350.habittracker.R;

public class Step3EntryActivity extends StepActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_step3_entry);
        super.onCreate(savedInstanceState);
    }

    protected void setEntryText(){
        stepEntryInput.setText(journalEntry.step3);
    }

    protected void save(){
        // Save the step 3 journal entry text
        journalEntry.step3 = stepEntryInput.getText().toString();
        super.save();
    }

    @Override
    protected void goToNext() {
        //Use intents to move on to the next step activity
        Intent intent = new Intent(Step3EntryActivity.this, Step4EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
        startActivity(intent);
    }
}
