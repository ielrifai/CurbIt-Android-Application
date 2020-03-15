package se3350.habittracker.activities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import se3350.habittracker.AppDatabase;
import se3350.habittracker.models.Goal;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.R;

public class AddGoalActivity extends ActionBarActivity {
    String goalName, goalDescription;

    EditText goalNameInput, goalDescriptionInput;

    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);

        goalNameInput = (EditText) findViewById(R.id.goal_name);
        goalDescriptionInput = (EditText) findViewById(R.id.goal_description);

        submitButton = (Button) findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(v -> {
            //save habit info
            goalName = goalNameInput.getText().toString();
            goalDescription = goalDescriptionInput.getText().toString();

            if(goalName.length() == 0 || goalDescription.length() == 0){
                Toast.makeText(getApplicationContext(), R.string.error_add_goal,Toast.LENGTH_SHORT).show();
                return;
            }

            //add habit to db
            Goal newGoal = new Goal(goalName, goalDescription);

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



