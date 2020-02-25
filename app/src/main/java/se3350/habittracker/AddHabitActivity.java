package se3350.habittracker;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddHabitActivity extends AppCompatActivity {

    //vars to hold input data
    String habitName, habitDescription;

    EditText habitNameInput, habitDescriptionInput;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        habitNameInput = (EditText) findViewById(R.id.habitName);
        habitDescriptionInput = (EditText) findViewById(R.id.habitDescription);

        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            //save habit info
            habitName = habitNameInput.getText().toString();
            habitDescription = habitDescriptionInput.getText().toString();
        });

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
    }

}
