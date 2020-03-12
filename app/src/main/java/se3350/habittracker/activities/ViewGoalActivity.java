package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.models.Subgoal;

public class ViewGoalActivity extends ActionBarActivity {

    String goal_description;
    int goalId;
    int subgoalId;
    Goal goal;
    Subgoal subgoal;
    JournalEntry draft;

    TextView goalDescriptionTextView, viewProgressTextView;
    Button editSubgoalButton, resume4StepButton, editGoalButton;


    private GoalDao goalDao;
    private SubgoalDao subgoalDao;
    private JournalEntryDao journalEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        goalDescriptionTextView = findViewById(R.id.goal_description);
        viewProgressTextView = findViewById(R.id.view_progress);
        editGoalButton = findViewById(R.id.edit_goal_btn);


        goalId = getIntent().getIntExtra("GOAl_ID", -1 );
        subgoalId = getIntent().getIntExtra("SUBGOAl_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();
        subgoalDao = db.subgoalDao();
        journalEntryDao = db.journalEntryDao();

        // Get goal from database
        LiveData<Goal> goalLiveData = goalDao.getGoalById(goalId);
        goalLiveData.observe(this, goal -> {
            // If goal is not in database
            if(goal == null){
                return;
            }
            setGoal(goal);
        });

        // Get subgoal from database
        LiveData<Subgoal> subgoalLiveData = subgoalDao.getSubgoalById(subgoalId);
        subgoalLiveData.observe(this, subgoal -> {
            // If subgoal is not in database
            if(subgoal == null){
                return;
            }
            setSubgoal(subgoal);
        });

        // Get draft from database
        LiveData<JournalEntry> journalEntryLiveData = journalEntryDao.getDraftOfGoal(goalId);
        journalEntryLiveData.observe(this, journalEntry -> setDraft(journalEntry));


        editGoalButton.setOnClickListener(event -> editGoal());
        editSubgoalButton.setOnClickListener(event -> editSubgoal());
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

    private void setSubgoal(Subgoal subgoal)
    {
        this.subgoal = subgoal;
        setTitle(subgoal.name);
    }

    private void setDraft(JournalEntry journalEntry){
        draft = journalEntry;
        Log.d("DEBUG", "setDraft: "+draft);
        // if there is no draft, hide the resume button from layout
        if(draft == null) {
            resume4StepButton.setVisibility(View.GONE);
        }
    }

    private void editGoal() {
        Intent intent = new Intent(ViewGoalActivity.this, EditGoalActivity.class).putExtra("GOAL_ID", goalId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void editSubgoal() {
        Intent intent = new Intent(ViewGoalActivity.this, EditSubgoalActivity.class).putExtra("SUBGOAL_ID", subgoalId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
