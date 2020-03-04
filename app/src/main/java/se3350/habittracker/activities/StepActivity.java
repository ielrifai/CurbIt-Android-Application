package se3350.habittracker.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.models.JournalEntry;

public abstract class StepActivity extends ActionBarActivity{

    // Elements
    EditText stepEntryInput;
    Button submitButton;

    JournalEntry journalEntry;
    JournalEntryDao journalEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the journal entry from the database
        AppDatabase db = AppDatabase.getInstance(this);
        journalEntryDao = db.journalEntryDao();

        int journal_id = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journal_id);

        journalEntryLive.observe(this, entry -> setJournalEntry(entry));

        // Get the elements
        stepEntryInput = (EditText) findViewById(R.id.step_journal);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            if (stepEmpty()) return;
            save();
            goToNext();
        });
    }

    @Override
    public void onBackPressed() {
        // Save Draft



        super.onBackPressed();
    }

    // Function to set the journal entry
    void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }

    // Check if step text entry is empty
    protected boolean stepEmpty(){
        // Check if field is empty
        String step1Entry = stepEntryInput.getText().toString();
        if(step1Entry.length() == 0){
            Toast.makeText(getApplicationContext(), R.string.error_add_entry,Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    protected void save(){
        // Update the date
        journalEntry.date = new Date();

        //Update the journal entry with the step 1 entry text
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            journalEntryDao.updateJournalEntries(journalEntry);
        });
    }

    protected abstract void goToNext();

}
