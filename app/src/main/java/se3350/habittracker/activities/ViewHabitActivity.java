package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.Habit;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.models.Progress;
import se3350.habittracker.models.Subgoal;

public class ViewHabitActivity extends ActionBarActivity {

    int habitId;
    Habit habit;
    JournalEntry draft;
    Goal goal;
    List<Subgoal> subgoals;

    TextView habitDescriptionTextView, progressAverageTextView, progressAverageMessageTextView;
    Button seeJournalButton, begin4StepsButton, resume4StepButton,
            seeProgressButton, viewGoalsButton, addGoalButton, completeGoalButton;

    private HabitDao habitDao;
    private JournalEntryDao journalEntryDao;
    private GoalDao goalDao;
    private ProgressDao progressDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);


        habitDescriptionTextView = findViewById(R.id.habit_description);
        habitDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
        progressAverageTextView = findViewById(R.id.progress_average);
        progressAverageMessageTextView = findViewById(R.id.progress_average_message);
        seeJournalButton = findViewById(R.id.see_journal_btn);
        begin4StepsButton = findViewById(R.id.begin_4_steps_btn);
        resume4StepButton = findViewById(R.id.resume_4_steps_btn);
        seeProgressButton = findViewById(R.id.see_progress_btn);
        viewGoalsButton = findViewById(R.id.view_goals_btn);
        addGoalButton = findViewById(R.id.add_goal_button);
        completeGoalButton = findViewById(R.id.complete_goal_btn);

        habitId = getIntent().getIntExtra("HABIT_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        habitDao = db.habitDao();
        journalEntryDao = db.journalEntryDao();
        goalDao = db.goalDao();
        progressDao = db.progressDao();

        // Get habit from database
        LiveData<Habit> habitLiveData = habitDao.getHabitById(habitId);
        habitLiveData.observe(this, habit -> {
            // If habit is not in database
            if(habit == null){
                return;
            }
            setHabit(habit);
        });

        // Get progress from database
        LiveData<Progress[]> progressLiveData = progressDao.getAllByHabit(habitId);
        progressLiveData.observe(this, progresses -> {
            setProgressText(progresses);
        });


        // Get draft from database
        LiveData<JournalEntry> journalEntryLiveData = journalEntryDao.getDraftOfHabit(habitId);
        journalEntryLiveData.observe(this, journalEntry -> setDraft(journalEntry));

        // Get goal from database
        LiveData<Goal> goalLiveData = goalDao.getGoalByHabitId(habitId);
        goalLiveData.observe(this, goal -> setGoal(goal));

        seeJournalButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, JournalListActivity.class).putExtra("HABIT_ID", habitId);
            startActivity(intent);
        });

        completeGoalButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, CompleteGoalActivity.class).putExtra("HABIT_ID", habitId);
            startActivity(intent);
        });

        viewGoalsButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, ViewGoalActivity.class).putExtra("HABIT_ID", habitId);
            startActivity(intent);
        });

        addGoalButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewHabitActivity.this, AddGoalActivity.class).putExtra("HABIT_ID", habitId);
            startActivity(intent);
        });

        begin4StepsButton.setOnClickListener(event -> begin4Steps());
        resume4StepButton.setOnClickListener(event -> begin4Steps(draft.uid));
        seeProgressButton.setOnClickListener(event -> goToProgress());
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.edit_habit:
                editHabit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setHabit(Habit habit)
    {
        this.habit = habit;
        setTitle(habit.name);
        habitDescriptionTextView.setText(habit.description);
        progressAverageTextView.setText(getString(R.string.avg_progress_score, Math.round(habit.avgScore * 100.0) / 100.0));
    }

    private void setDraft(JournalEntry journalEntry){
        draft = journalEntry;
        // if there is no draft, hide the resume button from layout
        if(draft == null) {
            resume4StepButton.setVisibility(View.GONE);
        }
    }

    private void setGoal(Goal goal){
        this.goal = goal;

        // Hide the corresponding button to no goal / goal
        if(goal == null) {
            viewGoalsButton.setVisibility(View.GONE);
            completeGoalButton.setVisibility(View.GONE);
            addGoalButton.setVisibility(View.VISIBLE);
        }
        else {
            addGoalButton.setVisibility(View.GONE);
            viewGoalsButton.setVisibility(View.VISIBLE);
            completeGoalButton.setVisibility(View.VISIBLE);
        }
    }

    private void begin4Steps() {
        JournalEntry journalEntry = new JournalEntry(habitId);

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
        Intent intent = new Intent(ViewHabitActivity.this, Step1EntryActivity.class).putExtra("JOURNAL_ID", journalId);
        startActivity(intent);
    }


    private void editHabit() {
        Intent intent = new Intent(ViewHabitActivity.this, EditHabitActivity.class).putExtra("HABIT_ID", habitId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void goToProgress(){
        Intent intent = new Intent(ViewHabitActivity.this, ViewHabitProgressActivity.class).putExtra("HABIT_ID", habitId);
        startActivity(intent);
    }

    private void setProgressText(Progress[] progresses){
        if(progresses.length == 0){
            progressAverageMessageTextView.setText(R.string.no_progress_logged);
            progressAverageTextView.setVisibility(TextView.INVISIBLE);
        }
        else{
            progressAverageMessageTextView.setText(R.string.overall_progress_message);
        }
    }

}
