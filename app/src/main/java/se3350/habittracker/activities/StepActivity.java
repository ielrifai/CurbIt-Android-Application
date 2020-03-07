package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    Button nextButton, previousButton;

    JournalEntry journalEntry;
    JournalEntryDao journalEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_cross);
        setTitle(R.string.four_steps);

        //Get the journal entry from the database
        AppDatabase db = AppDatabase.getInstance(this);
        journalEntryDao = db.journalEntryDao();

        int journal_id = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journal_id);

        journalEntryLive.observe(this, entry -> {
            if(entry == null) return;
            setJournalEntry(entry);
        });

        // Get the elements
        stepEntryInput = (EditText) findViewById(R.id.step_journal);
        nextButton = (Button) findViewById(R.id.next_btn);
        previousButton = (Button) findViewById(R.id.previous_btn);

        nextButton.setOnClickListener(v -> {
            if (stepEmpty()) return;
            save();
            goToNext();
        });

        previousButton.setOnClickListener(v -> {
            save();
            goToPrevious();
        });
    }

    @Override
    public void onBackPressed() {
        // On Back press ask the user if want to save or discard the current entry
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_save_draft_message)
                .setTitle(R.string.confirm_save_draft_title)
                .setPositiveButton(R.string.save, ((dialog, which) -> {
                    journalEntry.isDraft = true;
                    save();
                    goBackToHabitPage();
                }))
                .setNeutralButton(R.string.discard, ((dialog, which) -> {
                    discardDraft();
                    goBackToHabitPage();
                }))
                .setNegativeButton(R.string.cancel, ((dialog, which) -> {}));

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void goBackToHabitPage() {
        //Use intents to move on to the habit page, clearing the stack
        Intent intent = new Intent(getBaseContext(), ViewHabitActivity.class).putExtra("HABIT_ID", journalEntry.habitId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void discardDraft() {
        // Delete the journal entry from the DB as it is discarded
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            journalEntryDao.delete(journalEntry);
        });
    }

    // Function to set the journal entry
    private void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
        setEntryText();
    }

    protected abstract void setEntryText();

    // Check if step text entry is empty
    private boolean stepEmpty(){
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

    protected abstract void goToPrevious();

    protected abstract void goToNext();

}
