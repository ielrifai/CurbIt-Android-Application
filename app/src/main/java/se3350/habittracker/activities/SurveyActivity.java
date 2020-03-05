package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.models.Habit;
import se3350.habittracker.models.JournalEntry;

public class SurveyActivity extends AppCompatActivity {

    // Elements
    SeekBar surveySeekbar;
    TextView surveyProgressValue;
    TextView surveyProgressFeeling;
    Button submitButton;

    // Values
    int currentProgressValue = 0; // Variable to keep track of user's selected progress value

    JournalEntry journalEntry;
    JournalEntryDao journalEntryDao;

    Habit habit;
    HabitDao habitDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Get the journal entry from database
        AppDatabase db = AppDatabase.getInstance(this);
        journalEntryDao = db.journalEntryDao();

        int journal_id = getIntent().getIntExtra("JOURNAL_ID", -1 );
        LiveData<JournalEntry> journalEntryLive = journalEntryDao.getById(journal_id);

        journalEntryLive.observe(this, entry -> {
            if(entry == null) return;
            setJournalEntry(entry);
        });

        // Get the habit from the database
        habitDao = db.habitDao();

        int habit_id = getIntent().getIntExtra("HABIT_ID", -1 );
        LiveData<Habit> habitLive = habitDao.getHabitById(habit_id);

        habitLive.observe(this, entry -> {
            if(entry == null) return;
            setHabitEntry(entry);
        });

        // Get the elements
        surveySeekbar = (SeekBar) findViewById(R.id.survey_seekbar);
        surveyProgressValue = (TextView) findViewById(R.id.survey_progress_value);
        surveyProgressFeeling = (TextView) findViewById(R.id.survey_progress_feeling);
        submitButton = (Button) findViewById(R.id.submitButton);

        // Seekbar setup and listener
        surveySeekbar.setProgress(5);
        surveySeekbar.setMax(10); // Out of 10

        surveySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                surveyProgressValue.setText(String.valueOf(progress));
                // Show a feeling based on progress value
                String feeling = "";
                switch(progress){
                    case 0:
                        feeling = getString(R.string.level_0);
                        break;
                    case 1:
                        feeling = getString(R.string.level_1);
                        break;
                    case 2:
                        feeling = getString(R.string.level_2);
                        break;
                    case 3:
                        feeling = getString(R.string.level_3);
                        break;
                    case 4:
                        feeling = getString(R.string.level_4);
                        break;
                    case 5:
                        feeling = getString(R.string.level_5);
                        break;
                    case 6:
                        feeling = getString(R.string.level_6);
                        break;
                    case 7:
                        feeling = getString(R.string.level_7);
                        break;
                    case 8:
                        feeling = getString(R.string.level_8);
                        break;
                    case 9:
                        feeling = getString(R.string.level_9);
                        break;
                    case 10:
                        feeling = getString(R.string.level_10);
                        break;
                }
                surveyProgressFeeling.setText(feeling);
                currentProgressValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Submit Button Listener
        submitButton.setOnClickListener(v -> {
            save();
            goToNext();
        });

    }

    // Function to set the journal entry
    private void setJournalEntry(JournalEntry journalEntry){
        this.journalEntry = journalEntry;
    }

    // Function to set the habit
    private void setHabitEntry(Habit habit){
        this.habit = habit;
    }

    // Add the progress value (points) to the entry and update the average score of the habit
    private void save(){
        // Update the date
        journalEntry.date = new Date();

        // Update the progress
        journalEntry.surveyScore = currentProgressValue;

        //Update the journal entry with the progress score
        Executor jExecutor = Executors.newSingleThreadExecutor();
        jExecutor.execute(() -> {
            journalEntryDao.updateJournalEntries(journalEntry);

            // Update the habit with the new avg score
            Executor hExecutor = Executors.newSingleThreadExecutor();
            hExecutor.execute(() -> {
                habitDao.updateAvgScore(habit.uid);
            });

        });
    }

    private void goToNext(){
        // Go back to habit page
        Intent intent = new Intent(getBaseContext(), ViewHabitActivity.class).putExtra("HABIT_ID", journalEntry.habitId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //TODO: Add ability to go back???
}
