package se3350.habittracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.widget.Button;
import android.widget.TextView;

public class ViewHabit extends AppCompatActivity {

    String habit_description;
    int habitId;
    Habit habit;

    TextView habitDescriptionTextView, viewProgressTextView;

    Button seeJournalButton, begin4StepsButton;
    private void setHabit(Habit habit)
    {
        this.habit = habit;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        habitDescriptionTextView = findViewById(R.id.habitName);
        viewProgressTextView = findViewById(R.id.view_progress);
        seeJournalButton = findViewById(R.id.see_journal_btn);
        begin4StepsButton = findViewById(R.id.begin_4_steps_btn);

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        HabitDao habitDao = db.habitDao();
        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);

        habitLiveData.observe(this, habit -> {
            setHabit(habit);
            habitDescriptionTextView.setText(habit.description);
        });
        seeJournalButton.setOnClickListener(event -> {
            //TODO
        });
        begin4StepsButton.setOnClickListener(event -> {
            //TODO
        });
        Intent intent = new Intent(ViewHabit.this, ViewHabit.class);
        startActivity(intent);
    }
}
