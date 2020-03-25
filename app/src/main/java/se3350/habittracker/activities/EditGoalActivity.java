package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.adapters.SubgoalEditListAdapter;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.Subgoal;

public class EditGoalActivity extends ActionBarActivity {
    EditText goalDescriptionEditView;
    EditText goalNameEditView;

    Goal goal;
    int habitId;

    ArrayList<Subgoal> subgoals;
    SubgoalEditListAdapter subgoalAdapter;

    GoalDao goalDao;
    SubgoalDao subgoalDao;

    Button applyChangesButton, addSubgoalButton;
    ListView subgoalListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        goalDescriptionEditView = findViewById(R.id.goal_description);
        goalNameEditView = findViewById(R.id.goal_name);
        applyChangesButton = findViewById(R.id.apply_btn);
        addSubgoalButton = findViewById(R.id.add_subgoal_btn);
        subgoalListView = findViewById(R.id.list_subgoal);

        // Get Daos
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();
        subgoalDao = db.subgoalDao();

        // Get goal
        LiveData<Goal> goalLiveData = goalDao.getGoalByHabitId(habitId);
        goalLiveData.observe(this, goal -> {
            if(goal == null)
                return;
            setGoal(goal);
        });

        // Get subgoals
        LiveData<Subgoal[]> subgoalLiveData = subgoalDao.getSubgoalsByHabitId(habitId);
        subgoalLiveData.observe(this, subgoals -> setSubgoals(subgoals));

        // Set the subgoal list adapter
        subgoals = new ArrayList<>();
        subgoalAdapter = new SubgoalEditListAdapter(getBaseContext(), subgoals);
        subgoalAdapter.setDeleteAlert(true);
        subgoalListView.setAdapter(subgoalAdapter);


        applyChangesButton.setOnClickListener(event -> save());

        addSubgoalButton.setOnClickListener(v -> addSubgoal());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_goal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.delete_goal:
                deleteGoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setGoal(Goal goal)
    {
        this.goal = goal;
        goalDescriptionEditView.setText(goal.description);
        goalNameEditView.setText(goal.name);
    }

    private void setSubgoals(Subgoal[] newSubgoals)
    {
        subgoals.clear();
        subgoals.addAll(Arrays.asList(newSubgoals));
        subgoalAdapter.notifyDataSetChanged();
    }

    private void deleteGoal() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Add title and text to confirmation popup
        builder.setMessage(getString(R.string.confirm_delete_popup_message, goal.name))
                .setTitle(R.string.confirm_delete_popup_title_goal);

        // Add the buttons
        builder.setNegativeButton(R.string.delete, ((dialog, which) -> {
            // Delete the goal if confirmed
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                // Delete goal and all its sub goals
                goalDao.delete(goal);
                subgoalDao.deleteAllByHabitId(habitId);

                // Go back to habit view and clear task (clear all stacks)
                Intent intent = new Intent(EditGoalActivity.this, ViewHabitActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }));

        builder.setNeutralButton(R.string.cancel, ((dialog, which) -> {}));

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Pre add subgoal into the list
    private void addSubgoal() {
        subgoals.add(new Subgoal("", habitId));
        subgoalAdapter.notifyDataSetChanged();
    }

    // Update goal and its subgoal in database
    private void save(){
        goal.name = goalNameEditView.getText().toString();
        goal.description = goalDescriptionEditView.getText().toString();

        //if goal name, or description is left blank
        if(goal.name.length() == 0 || goal.description.length() == 0){
            Toast.makeText(getApplicationContext(), R.string.error_add_goal,Toast.LENGTH_SHORT).show();
            return;
        }

        //update goal in db
        Executor goalExecutor = Executors.newSingleThreadExecutor();
        goalExecutor.execute(() -> {
            goalDao.updateGoals(goal);
        });


        //clean subgoals list of subgoals with empty names
        subgoals.removeIf(subgoal -> subgoal.name.isEmpty());

        //add subgoals to db
        Executor subExecutor = Executors.newSingleThreadExecutor();
        subExecutor.execute(() -> {
            subgoalDao.deleteAllByHabitId(habitId);
            subgoalDao.insertAll(subgoals.toArray(new Subgoal[0]));
        });

        onBackPressed();
    }
}