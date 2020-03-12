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
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.JournalEntry;

public class ViewGoalActivity extends ActionBarActivity {

    String goal_description;
    int goalId;
    Goal goal;
    JournalEntry draft;

    TextView goalDescriptionTextView, viewProgressTextView;
    Button seeJournalButton, begin4StepsButton, resume4StepButton, editGoalButton;

    private GoalDao goalDao;
    private JournalEntryDao journalEntryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        goalDescriptionTextView = findViewById(R.id.goal_description);
        viewProgressTextView = findViewById(R.id.view_progress);
        seeJournalButton = findViewById(R.id.see_journal_btn);
        begin4StepsButton = findViewById(R.id.begin_4_steps_btn);
        resume4StepButton = findViewById(R.id.resume_4_steps_btn);
        editGoalButton = findViewById(R.id.edit_goal_btn);


        goalId = getIntent().getIntExtra("GOAl_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();
        journalEntryDao = db.journalEntryDao();

        // Get goal from database
        LiveData<Goal> goalLiveData = goalDao.getGoalById(goalId);
        goalLiveData.observe(this, goal -> {
            // If habit is not in database
            if(goal == null){
                return;
            }
            setGoal(goal);
        });

        // Get draft from database
        LiveData<JournalEntry> journalEntryLiveData = journalEntryDao.getDraftOfGoal(goalId);
        journalEntryLiveData.observe(this, journalEntry -> setDraft(journalEntry));

        seeJournalButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewGoalActivity.this, JournalListActivity.class).putExtra("HABIT_ID", goalId);
            startActivity(intent);
        });

        begin4StepsButton.setOnClickListener(event -> begin4Steps());
        resume4StepButton.setOnClickListener(event -> begin4Steps(draft.uid));
        editGoalButton.setOnClickListener(event -> editGoal());
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

    private void setDraft(JournalEntry journalEntry){
        draft = journalEntry;
        Log.d("DEBUG", "setDraft: "+draft);
        // if there is no draft, hide the resume button from layout
        if(draft == null) {
            resume4StepButton.setVisibility(View.GONE);
        }
    }

    private void begin4Steps() {
        JournalEntry journalEntry = new JournalEntry(goalId);

        if(draft!= null) {
            // Ask the user if they want to erase the current draft
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.confirm_begin_4_steps_message)
                    .setTitle(R.string.confirm_begin_4_steps_title)
                    .setPositiveButton(R.string.begin_4_steps, ((dialog, which) -> {
                        // Delete old draft and create a new entry
                        Executor myExecutor = Executors.newSingleThreadExecutor();
                        myExecutor.execute(() -> {
                            journalEntryDao.delete(draft);
                            int id = (int) journalEntryDao.insertOne(journalEntry);
                            begin4Steps(id);
                        });
                    }))
                    .setNegativeButton(R.string.cancel, ((dialog, which) -> {}));

            // Show the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            // Create a new entry
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                int id = (int) journalEntryDao.insertOne(journalEntry);
                begin4Steps(id);
            });
        }
    }

    private void begin4Steps(int journalId) {
        Intent intent = new Intent(ViewGoalActivity.this, Step1EntryActivity.class).putExtra("JOURNAL_ID", journalId);
        startActivity(intent);
    }


    private void editGoal() {
        Intent intent = new Intent(ViewGoalActivity.this, EditGoalActivity.class).putExtra("GOAL_ID", goalId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

 /*String habit_description;
    int habitId;
    Habit habit;
    JournalEntry draft;

    TextView habitDescriptionTextView, viewProgressTextView;
    Button seeJournalButton, begin4StepsButton, resume4StepButton;

    private HabitDao habitDao;
    private JournalEntryDao journalEntryDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);

        habitDescriptionTextView = findViewById(R.id.habit_description);
        viewProgressTextView = findViewById(R.id.view_progress);
        seeJournalButton = findViewById(R.id.see_journal_btn);
        begin4StepsButton = findViewById(R.id.begin_4_steps_btn);
        resume4StepButton = findViewById(R.id.resume_4_steps_btn);

        habitId = getIntent().getIntExtra("HABIT_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        habitDao = db.habitDao();
        journalEntryDao = db.journalEntryDao();

        // Get habit from database
        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);
        habitLiveData.observe(this, habit -> {
            // If habit is not in database
            if(habit == null){
                return;
            }
            setHabit(habit);
        });

        // Get draft from database
        LiveData<JournalEntry> journalEntryLiveData = journalEntryDao.getDraftOfHabit(habitId);
        journalEntryLiveData.observe(this, journalEntry -> setDraft(journalEntry));

        seeJournalButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, JournalListActivity.class).putExtra("HABIT_ID", habitId);
            startActivity(intent);
        });

        begin4StepsButton.setOnClickListener(event -> begin4Steps());
        resume4StepButton.setOnClickListener(event -> begin4Steps(draft.uid));
    }
*/