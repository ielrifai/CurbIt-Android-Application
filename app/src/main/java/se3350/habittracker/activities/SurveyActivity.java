package se3350.habittracker.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.models.Habit;
import se3350.habittracker.models.Progress;

public class SurveyActivity extends ActionBarActivity {

    // Elements
    SeekBar surveySeekbar;
    TextView surveyProgressValue;
    TextView surveyProgressFeeling;
    Button submitButton;

    // Values
    int currentProgressValue = 5; // Variable to keep track of user's selected progress value
    boolean progressCreated = false;

    // The progress item that holds survey score for one day of a habit
    Progress progress;
    ProgressDao progressDao;

    Habit habit;
    HabitDao habitDao;

    int habit_id; // Id of the habit being surveyed on
    long newId; // For a new progress, keep track of its ID after being inserted

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Use database
        AppDatabase db = AppDatabase.getInstance(this);
        habit_id = getIntent().getIntExtra("HABIT_ID", -1 );

        // Check if there is an existing progress item for today's date and create a new one if there isn't
        // Start Date
        Date start = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        start = calendar.getTime();

        // End Date
        Date end = new Date();
        calendar.setTime(end);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        end = calendar.getTime();

        progressDao = db.progressDao();
        LiveData<Progress> progressLive = progressDao.getProgressByDate(habit_id, start, end);

        progressLive.observe(this, progressItem -> {
            if((progressItem == null) && !progressCreated) {
                progressCreated = true;
                // insert a new progress for the habit for today
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    newId = progressDao.insertOne(new Progress(habit_id));
                });
            }
            else {
                setProgress(progressItem); // Set progress item
            }
        });


        // Get the habit from the database
        habitDao = db.habitDao();
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

        // Alert user that only one progress can be logged a day and will be overwritten
        // If the back nav button is pressed alert user they will skip the survey
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_progress_may_overwrite_message)
                .setTitle(R.string.alert_progress_may_overwrite_title)
                .setPositiveButton(R.string.ok, ((dialog, which) -> {}));

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Submit Button Listener
        submitButton.setOnClickListener(v -> {
            if (progress == null){
                getProgress();
            }
            save();
            goToNext();
        });
    }


    // Function to set the habit
    private void setHabitEntry(Habit habit){
        this.habit = habit;
    }

    // Function to set the progress item
    private void setProgress(Progress progress){
        this.progress = progress;
    }

    // Add the progress value (points) to the progress entity and update the average score of the habit
    private void save(){

        // Update the progress
        progress.surveyScore = currentProgressValue;

        //Update the Progress entry with the progress score
        Executor pExecutor = Executors.newSingleThreadExecutor();
        pExecutor.execute(() -> {
            progressDao.updateProgress(progress);

            // Update the habit with the new avg score
            Executor hExecutor = Executors.newSingleThreadExecutor();
            hExecutor.execute(() -> {
                habitDao.updateAvgScore(habit_id);
            });
        });
    }

    private void goToNext(){
        // Go back to habit page
        Intent intent = new Intent(getBaseContext(), ViewHabitActivity.class).putExtra("HABIT_ID", habit_id);
        //completed journal entry - gamification
        intent.putExtra("GAMIFICATION_ID",1 );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // If needed to insert a new progress, get it here to set the progress
    private void getProgress(){
        Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    newId = progressDao.insertOne(new Progress(habit_id));
                    LiveData<Progress> progressLive = progressDao.getProgressById(newId);
                    progressLive.observe(this, progress -> setProgress(progress));
                });
    }

    @Override
    public void onBackPressed(){
        // If the back nav button is pressed alert user they will skip the survey
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_skip_survey_message)
                .setTitle(R.string.alert_skip_survey_title)
                .setNegativeButton(R.string.yes, ((dialog, which) -> {
                    // Discard new insertion and go back to habit page
                    discardProgress();
                    goToNext();
                }))
                .setPositiveButton(R.string.no, ((dialog, which) -> {}));

        // Show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void discardProgress(){
        // Delete the newly inserted progress if the user backs out
        if(progressCreated){
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                progressDao.delete(progress);
            });
        }
        // An existing progress won't be touched
    }
}
