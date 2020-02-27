package se3350.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Step1EntryActivity extends AppCompatActivity {

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
        addNewJournalEntry(); // TODO: May be removed later
        AppDatabase db = AppDatabase.getInstance(this);
        JournalEntryDao journalEntryDao = db.journalEntryDao();
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(1); // TODO: How to get the id of the new journal entry (intents?)

        journalEntryLive.observe(this, entry -> setJournalEntry(entry));

        submitButton.setOnClickListener(v -> {
            // Save the step 1 journal entry text
            step1Entry = stepEntryInput.getText().toString();
            journalEntry.step1 = step1Entry;

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

    // Insert a new Journal Entry for the habit
    //TODO: May be removed later if entry is created before the 4 steps
    void addNewJournalEntry(){
        JournalEntry testEntry = new JournalEntry(123);
        AppDatabase db = AppDatabase.getInstance(this);
        JournalEntryDao journalEntryDao = db.journalEntryDao();
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            journalEntryDao.insertAll(testEntry);
        });
    }

    // Function to set the journal entry
    void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }


}
