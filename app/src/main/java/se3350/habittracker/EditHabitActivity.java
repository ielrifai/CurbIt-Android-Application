package se3350.habittracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditHabitActivity extends AppCompatActivity {
    EditText habitDescriptionEditView;
    EditText habitNameEditView;
    Habit habit;
    int habitId;

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
        HabitDao habitDao = db.habitDao();
        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);

        habitLiveData.observe(this, habit -> {
            setHabit(habit);
            habitDescriptionEditView.setText(habit.description);
            habitNameEditView.setText(habit.name);
        });

        applyChangesButton.setOnClickListener(event -> {
            habit.description = habitDescriptionEditView.getText().toString();
            habit.name = habitNameEditView.getText().toString();

            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                habitDao.updateHabits(habit);
            });

            onBackPressed();
        });
    }
}
