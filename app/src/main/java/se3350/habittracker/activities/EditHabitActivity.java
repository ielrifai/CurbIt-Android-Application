package se3350.habittracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.models.Habit;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.R;

public class EditHabitActivity extends ActionBarActivity {
    EditText habitDescriptionEditView;
    EditText habitNameEditView;

    Habit habit;
    int habitId;

    HabitDao habitDao;
    JournalEntryDao journalEntryDao;
    ProgressDao progressDao;

    private void setHabit(Habit habit)
    {
        this.habit = habit;
    }

    Button applyChangesButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        setContentView(R.layout.activity_edit_habit);
        habitDescriptionEditView = findViewById(R.id.habit_description);
        habitNameEditView = findViewById(R.id.habit_name);
        applyChangesButton = findViewById(R.id.apply_btn);

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        habitDao = db.habitDao();
        journalEntryDao = db.journalEntryDao();
        progressDao = db.progressDao();

        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);
        habitLiveData.observe(this, habit -> {
            if(habit == null)
                return;

            setHabit(habit);
            habitDescriptionEditView.setText(habit.description);
            habitNameEditView.setText(habit.name);
        });

        applyChangesButton.setOnClickListener(event -> {
            saveHabit();
            onBackPressed();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.delete_habit:
                deleteHabit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteHabit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Add title and text to confirmation popup
        builder.setMessage(getString(R.string.confirm_delete_popup_message, habit.name))
                .setTitle(R.string.confirm_delete_popup_title);

        // Add the buttons
        builder.setNegativeButton(R.string.delete, ((dialog, which) -> {
            // Delete the habit if confirmed
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                // Delete habit and all its journal entries
                journalEntryDao.deleteAllByHabitId(habit.uid);
                progressDao.deleteAllByHabitId(habit.uid);
                habitDao.delete(habit);

                // Go back to Habit List and clear task (clear all stacks)
                Intent intent = new Intent(EditHabitActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }));

        builder.setNeutralButton(R.string.cancel, ((dialog, which) -> {}));

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveHabit() {
        habit.description = habitDescriptionEditView.getText().toString();
        habit.name = habitNameEditView.getText().toString();

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            habitDao.updateHabits(habit);
        });
    }
}
