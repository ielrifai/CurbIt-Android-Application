package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.models.Goal;

public class EditGoalActivity extends ActionBarActivity {
    EditText goalDescriptionEditView;
    EditText goalNameEditView;

    Goal goal;
    int goalId;

    GoalDao goalDao;

    private void setGoal(Goal goal)
    {
        this.goal = goal;
    }

    Button applyChangesButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goalId = getIntent().getIntExtra("GOAL_ID", -1);

        setContentView(R.layout.activity_edit_goal);
        goalDescriptionEditView = findViewById(R.id.goal_description);
        goalNameEditView = findViewById(R.id.goal_name);
        applyChangesButton = findViewById(R.id.apply_btn);

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();

        LiveData<Goal> goalLiveData = goalDao.getGoalById(goalId);
        goalLiveData.observe(this, goal -> {
            if(goal == null)
                return;

            setGoal(goal);
            goalDescriptionEditView.setText(goal.description);
            goalNameEditView.setText(goal.name);
        });

        applyChangesButton.setOnClickListener(event -> {
            saveGoal();
            onBackPressed();
        });
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

    private void deleteGoal() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Add title and text to confirmation popup
        builder.setMessage(getString(R.string.confirm_delete_popup_message, goal.name))
                .setTitle(R.string.confirm_delete_popup_title_goal);

        // Add the buttons
        builder.setNegativeButton(R.string.delete, ((dialog, which) -> {
            // Delete the habit if confirmed
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                // Delete habit and all its journal entries
                goalDao.delete(goal);

                // Go back to Habit List and clear task (clear all stacks)
                Intent intent = new Intent(EditGoalActivity.this, ViewGoalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }));

        builder.setNeutralButton(R.string.cancel, ((dialog, which) -> {}));

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveGoal() {
        goal.description = goalDescriptionEditView.getText().toString();
        goal.name = goalNameEditView.getText().toString();

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            goalDao.updateGoals(goal);
        });
    }
}