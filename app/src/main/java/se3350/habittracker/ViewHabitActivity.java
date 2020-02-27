package se3350.habittracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewHabitActivity extends AppCompatActivity {

    String habit_description;
    int habitId;
    Habit habit;
    TextView habitDescriptionTextView, viewProgressTextView, editHabitButton;
    Button seeJournalButton, begin4StepsButton;
    private HabitDao habitDao;
    private JournalEntryDao journalEntryDao;


    private void setHabit(Habit habit)
    {
        this.habit = habit;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        habitDescriptionTextView = findViewById(R.id.habit_description);
        viewProgressTextView = findViewById(R.id.view_progress);
        seeJournalButton = findViewById(R.id.see_journal_btn);
        begin4StepsButton = findViewById(R.id.begin_4_steps_btn);
        editHabitButton = findViewById(R.id.edit_habit);

        habitId = getIntent().getIntExtra("HABIT_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        habitDao = db.habitDao();
        journalEntryDao = db.journalEntryDao();

        // Get habit from database
        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);

        habitLiveData.observe(this, habit -> {
            setHabit(habit);
            setTitle(habit.name);
            habitDescriptionTextView.setText(habit.description);
        });


        seeJournalButton.setOnClickListener(event -> {
            //TODO launch journal view
        });

        begin4StepsButton.setOnClickListener(event -> begin4Steps());

        editHabitButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, EditHabitActivity.class).putExtra("HABIT_ID", habitId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }

    private void begin4Steps() {
        JournalEntry journalEntry = new JournalEntry(habitId);

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            int id = (int) journalEntryDao.insertOne(journalEntry);
            Intent intent = new Intent(ViewHabitActivity.this, Step1EntryActivity.class).putExtra("JOURNAL_ID", id);
            startActivity(intent);
        });
    }
}
