package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import java.util.Random;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.models.Goal;

public class ViewGoalActivity extends ActionBarActivity {

    int goalId;
    Goal goal;
    GoalDao goalDao;

    TextView goalDescriptionTextView;
    Button viewSubgoalButton;

    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        goalDescriptionTextView = findViewById(R.id.goal_description);
        viewSubgoalButton = findViewById(R.id.view_subgoals_btn);

        goalId = getIntent().getIntExtra("GOAL_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();

        // Get goal from database
        LiveData<Goal> goalLiveData = goalDao.getGoalById(goalId);
        goalLiveData.observe(this, goal ->{
            // If goal is not in database
            if(goal == null){
                return;
            }
            setGoal(goal);
        });

        viewSubgoalButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewGoalActivity.this, SubgoalActivity.class).putExtra("GOAL_ID", goalId);
            startActivity(intent);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.edit_goal:
                editGoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setGoal(Goal goal)
    {
        this.goal = goal;
        setTitle(goal.name);
        goalDescriptionTextView.setText(goal.description);
    }



    private void editGoal() {
        Intent intent = new Intent(ViewGoalActivity.this, EditGoalActivity.class).putExtra("GOAL_ID", goalId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

}
