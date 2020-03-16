package se3350.habittracker.activities;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.models.Goal;

public class AddGoalActivity extends ActionBarActivity {

    //vars to hold input data
    String goalName, goalDescription;
    int habitId;

    EditText goalNameInput, goalDescriptionInput;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        //link to id with xml files
        goalNameInput = (EditText) findViewById(R.id.goal_name);
        goalDescriptionInput = (EditText) findViewById(R.id.goal_description);
        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        submitButton = (Button) findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(v -> {
            //save goal info
            goalName = goalNameInput.getText().toString();
            goalDescription = goalDescriptionInput.getText().toString();
            //if goal name or description is left blank
            if(goalName.length() == 0 || goalDescription.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_goal,Toast.LENGTH_SHORT).show();
                return;
            }

            //add goal to db
            Goal newGoal = new Goal(goalName, goalDescription, habitId);

            AppDatabase db = AppDatabase.getInstance(this);

            GoalDao goalDao = db.goalDao();

            // asynchronous insert using an executor
            //to use Java 8 - go to project settings and change target compatibility to 1.8
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                goalDao.insertAll(newGoal);
            });

            onBackPressed();
        });

    }



}
