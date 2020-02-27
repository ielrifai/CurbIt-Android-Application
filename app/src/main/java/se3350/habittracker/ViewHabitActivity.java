package se3350.habittracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.widget.Button;
import android.widget.TextView;

public class ViewHabitActivity extends AppCompatActivity {

    String habit_description;
    int habitId;
    Habit habit;

    TextView habitDescriptionTextView, viewProgressTextView, editHabitButton;

    Button seeJournalButton, begin4StepsButton;
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

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        HabitDao habitDao = db.habitDao();


        // TODO  REMOVE
        habitId = 1;


        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);

        habitLiveData.observe(this, habit -> {
            setHabit(habit);
            setTitle(habit.name);
            habitDescriptionTextView.setText(habit.description);
        });
        seeJournalButton.setOnClickListener(event -> {
            //TODO
        });
        begin4StepsButton.setOnClickListener(event -> {
            //TODO
        });

        editHabitButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, EditHabitActivity.class).putExtra("HABIT_ID", habitId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

    }
}
