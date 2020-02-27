package se3350.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Step4EntryActivity extends AppCompatActivity {

    // Data
    String step4Entry;
    JournalEntry journalEntry;

    // Elements
    EditText stepEntryInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4_entry);

        // Get the elements
        stepEntryInput = (EditText) findViewById(R.id.step4_journal);
        submitButton = (Button) findViewById(R.id.submitButton);

        //Get the journal entry from the database
        AppDatabase db = AppDatabase.getInstance(this);
        JournalEntryDao journalEntryDao = db.journalEntryDao();
        int journalId = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journalId);

        journalEntryLive.observe(this, entry -> setJournalEntry(entry));

        submitButton.setOnClickListener(v -> {
            // Save the step 4 journal entry text
            step4Entry = stepEntryInput.getText().toString();
            journalEntry.step4 = step4Entry;

            // Check if field is empty
            if(step4Entry.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_entry,Toast.LENGTH_SHORT).show();
                return;
            }

            //Update the journal entry with the step 4 entry text
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                journalEntryDao.updateJournalEntries(journalEntry);
            });

            //TODO: Use intents to move back to the habit page
        });

    }

    // Function to set the journal entry
    void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }
}
