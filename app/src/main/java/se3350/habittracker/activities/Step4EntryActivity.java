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

    protected void setEntryText(){
        stepEntryInput.setText(journalEntry.step4);
    }

    protected void save(){
        // Save the step 4 journal entry text
        journalEntry.step4 = stepEntryInput.getText().toString();
        journalEntry.isDraft = false;
        super.save();
    }

    @Override
    protected void goToNext()
    {
        //Use intents to move on to the next survey activity
        Intent intent = new Intent(Step4EntryActivity.this, SurveyActivity.class);
        intent.putExtra("JOURNAL_ID", journalEntry.uid);
        intent.putExtra("HABIT_ID", journalEntry.habitId);
        startActivity(intent);
    }
}
