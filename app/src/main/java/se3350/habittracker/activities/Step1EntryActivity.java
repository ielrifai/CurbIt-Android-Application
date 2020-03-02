package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.R;

public class Step1EntryActivity extends ActionBarActivity {

    // Data
    String step1Entry;
    JournalEntry journalEntry;

    // Elements
    EditText stepEntryInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1_entry);

        // Get the elements
        stepEntryInput = (EditText) findViewById(R.id.step1_journal);
        submitButton = (Button) findViewById(R.id.submitButton);


        //Get the journal entry from the database
        AppDatabase db = AppDatabase.getInstance(this);
        JournalEntryDao journalEntryDao = db.journalEntryDao();

        int journal_id = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journal_id);

        journalEntryLive.observe(this, entry -> setJournalEntry(entry));

        submitButton.setOnClickListener(v -> {
            // Save the step 1 journal entry text
            step1Entry = stepEntryInput.getText().toString();
            journalEntry.step1 = step1Entry;

            // Update the date
            journalEntry.date = new Date();

            // Check if field is empty
            if(step1Entry.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_entry,Toast.LENGTH_SHORT).show();
                return;
            }

            //Update the journal entry with the step 1 entry text
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                journalEntryDao.updateJournalEntries(journalEntry);
            });

            //TUse intents to move on to the next step activity
            Intent intent = new Intent(Step1EntryActivity.this, Step2EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
            startActivity(intent);
        });

    }

    // Function to set the journal entry
    void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }

}
