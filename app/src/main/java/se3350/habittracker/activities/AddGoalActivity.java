package se3350.habittracker.activities;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.adapters.SubgoalEditListAdapter;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.Subgoal;

public class AddGoalActivity extends ActionBarActivity {

    //vars to hold input data
    String goalName, goalDescription;
    int habitId;

    GoalDao goalDao;
    SubgoalDao subgoalDao;

    ArrayList<Subgoal> subgoals;
    SubgoalEditListAdapter subgoalAdapter;

    EditText goalNameInput, goalDescriptionInput;
    ListView subgoalListView;
    Button submitButton, addSubgoalButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        //link to id with xml files
        goalNameInput = findViewById(R.id.goal_name);
        goalDescriptionInput = findViewById(R.id.goal_description);
        //links goal to habit
        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        submitButton = findViewById(R.id.submit_btn);
        addSubgoalButton = findViewById(R.id.add_subgoal_btn);
        subgoalListView = findViewById(R.id.list_subgoal);

        AppDatabase db = AppDatabase.getInstance(this);

        goalDao = db.goalDao();
        subgoalDao = db.subgoalDao();

        submitButton.setOnClickListener(v -> save());

        addSubgoalButton.setOnClickListener(v -> addSubgoal());

        subgoals = new ArrayList<>();
        // Set the subgoal list adapter
        subgoalAdapter = new SubgoalEditListAdapter(getBaseContext(), subgoals);
        subgoalListView.setAdapter(subgoalAdapter);
    }

    // Pre add subgoal into the list
    private void addSubgoal() {
        subgoals.add(new Subgoal("", 0, habitId));
        subgoalAdapter.notifyDataSetChanged();
    }

    // Insert new goal and its subgoal in database
    private void save(){
        goalName = goalNameInput.getText().toString();
        goalDescription = goalDescriptionInput.getText().toString();

        //if goal name, or description is left blank
        if(goalName.length() == 0 || goalDescription.length() == 0){
            Toast.makeText(getApplicationContext(), R.string.error_add_goal,Toast.LENGTH_SHORT).show();
            return;
        }

        //add goal to db
        Goal newGoal = new Goal(goalName, goalDescription, habitId);
        Executor goalExecutor = Executors.newSingleThreadExecutor();
        goalExecutor.execute(() -> {
            goalDao.insertOne(newGoal);
        });


        //clean subgoals list of subgoals with empty names
        subgoals.removeIf(subgoal -> subgoal.name.isEmpty());

        //get final position of subgoal
        subgoals.forEach(subgoal -> subgoal.position = subgoals.indexOf(subgoal));

        //add subgoals to db
        Executor subExecutor = Executors.newSingleThreadExecutor();
        subExecutor.execute(() -> {
            subgoalDao.insertAll(subgoals.toArray(new Subgoal[0]));
        });

        onBackPressed();
    }

}
