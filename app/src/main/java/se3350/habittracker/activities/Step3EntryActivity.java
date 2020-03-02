package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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

public class Step3EntryActivity extends ActionBarActivity {

    // Data
    String step3Entry;
    JournalEntry journalEntry;

    // Elements
    EditText stepEntryInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3_entry);

        // Get the elements
        stepEntryInput = (EditText) findViewById(R.id.step3_journal);
        submitButton = (Button) findViewById(R.id.submitButton);

        //Get the journal entry from the database
        AppDatabase db = AppDatabase.getInstance(this);
        JournalEntryDao journalEntryDao = db.journalEntryDao();
        int journalId = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journalId);

        journalEntryLive.observe(this, entry -> setJournalEntry(entry));

        submitButton.setOnClickListener(v -> {
            // Save the step 3 journal entry text
            step3Entry = stepEntryInput.getText().toString();
            journalEntry.step3 = step3Entry;

            // Check if field is empty
            if(step3Entry.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_entry,Toast.LENGTH_SHORT).show();
                return;
            }

            //Update the journal entry with the step 3 entry text
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                journalEntryDao.updateJournalEntries(journalEntry);
            });

            //Use intents to move on to the next step activity
            //TUse intents to move on to the next step activity
            Intent intent = new Intent(Step3EntryActivity.this, Step4EntryActivity.class).putExtra("JOURNAL_ID", journalEntry.uid);
            startActivity(intent);
        });

    }



    // Function to set the journal entry
    void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }
}
