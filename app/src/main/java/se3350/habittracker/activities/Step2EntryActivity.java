package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.R;

public class Step2EntryActivity extends AppCompatActivity {

    // Data
    String step2Entry;
    JournalEntry journalEntry;

    // Elements
    EditText stepEntryInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2_entry);

        // Get the elements
        stepEntryInput = (EditText) findViewById(R.id.step2_journal);
        submitButton = (Button) findViewById(R.id.submitButton);

        //Get the journal entry from the database
        AppDatabase db = AppDatabase.getInstance(this);
        JournalEntryDao journalEntryDao = db.journalEntryDao();
        int journalId = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journalId);

        journalEntryLive.observe(this, entry -> setJournalEntry(entry));

        submitButton.setOnClickListener(v -> {
            // Save the step 2 journal entry text
            step2Entry = stepEntryInput.getText().toString();
            journalEntry.step2 = step2Entry;

            // Check if field is empty
            if(step2Entry.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_entry,Toast.LENGTH_SHORT).show();
                return;
            }

            //Update the journal entry with the step 1 entry text
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                journalEntryDao.updateJournalEntries(journalEntry);
            });

            //Use intents to move on to the next step activity
            //TUse intents to move on to the next step activity
            Intent intent = new Intent(Step2EntryActivity.this, Step3EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
            startActivity(intent);
        });

    }

    // Function to set the journal entry
    void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }
}
