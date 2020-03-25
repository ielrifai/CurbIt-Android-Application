package se3350.habittracker.activities;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.models.Habit;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.R;

public class AddHabitActivity extends ActionBarActivity {

    //vars to hold input data
    String habitName, habitDescription;

    EditText habitNameInput, habitDescriptionInput;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);

        habitNameInput = (EditText) findViewById(R.id.habit_name);
        habitDescriptionInput = (EditText) findViewById(R.id.habit_description);

        submitButton = (Button) findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(v -> {
            //save habit info
            habitName = habitNameInput.getText().toString();
            habitDescription = habitDescriptionInput.getText().toString();

            if(habitName.length() == 0 || habitDescription.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_habit,Toast.LENGTH_SHORT).show();
                return;
            }

            //add habit to db
            Habit newHabit = new Habit(habitName, habitDescription);

            AppDatabase db = AppDatabase.getInstance(this);

            HabitDao habitDao = db.habitDao();
            // asynchronous insert using an executor
            //to use Java 8 - go to project settings and change target compatibility to 1.8
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                habitDao.insertAll(newHabit);
            });

            onBackPressed();
        });

    }



}
