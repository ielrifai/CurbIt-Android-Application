package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.adapters.SubgoalListAdapter;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.Subgoal;

public class CompleteGoalActivity extends ActionBarActivity {

    int habitId;
    Goal goal;
    GoalDao goalDao;
    SubgoalDao subgoalDao;

    ArrayList<Subgoal> subgoals;
    SubgoalListAdapter subgoalAdapter;

    TextView goalDescriptionTextView;
    ListView subgoalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_goal);

        goalDescriptionTextView = findViewById(R.id.goal_description);
        subgoalListView = findViewById(R.id.list_subgoal);

        habitId = getIntent().getIntExtra("HABIT_ID", -1 );


        subgoals = new ArrayList<>();
        // Set the habit list adapter
        subgoalAdapter = new SubgoalListAdapter(getBaseContext(), subgoals);
        subgoalListView.setAdapter(subgoalAdapter);


        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();
        subgoalDao = db.subgoalDao();

        // Get goal from database
        LiveData<Goal> goalLiveData = goalDao.getGoalByHabitId(habitId);
        goalLiveData.observe(this, goal ->{
            // If goal is not in database
            if(goal == null){
                return;
            }
            setGoal(goal);
        });

        // Get subgoals from database
        LiveData<Subgoal[]> subgoalLiveData = subgoalDao.getSubgoalsByHabitId(habitId);
        subgoalLiveData.observe(this, newSubgoals ->{
            // If goal is not in database
            if(newSubgoals == null){
                return;
            }
            setSubgoals(newSubgoals);
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

    private void setSubgoals(Subgoal[] newSubgoals)
    {
        subgoals.clear();
        subgoals.addAll(Arrays.asList(newSubgoals));
        subgoalAdapter.notifyDataSetChanged();
    }

    private void editGoal() {
        Intent intent = new Intent(CompleteGoalActivity.this, EditGoalActivity.class).putExtra("HABIT_ID", habitId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //update subgoal in db
        Executor subExecutor = Executors.newSingleThreadExecutor();
        subExecutor.execute(() -> {
            subgoalDao.updateSubgoals(subgoals.toArray(new Subgoal[0]));
        });
    }
}
