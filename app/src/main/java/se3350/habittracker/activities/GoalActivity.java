package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.adapters.GoalListAdapter;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.models.Goal;

public class GoalActivity extends ActionBarActivity {

    TextView emptyListText;
    ListView goalListView;
    List<Goal> goals;

    GoalDao goalDao;
    GoalListAdapter adapter;

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        goals = new ArrayList<>();

        goalListView = findViewById(R.id.list_goal);
        emptyListText = findViewById(R.id.text_empty_list);

        // Set the journal list adapter
        adapter = new GoalListAdapter(getBaseContext(), goals);
        goalListView.setAdapter(adapter);

        // Set the listener on item click
        goalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal = goals.get(position);
                Intent intent = new Intent(getBaseContext(), ViewGoalActivity.class)
                        .putExtra("GOAL_ID", goal.uid);
                startActivity(intent);
            }
        });


        int goalId = getIntent().getIntExtra("GOAL_ID", -1);

        // Get Daos
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();

        // get habits and goals from database
        LiveData<Goal[]> goalList = goalDao.getAll();

        // Get journal entries from database
        goalList.observe(this, newGoals -> setGoals(newGoals));



        addButton = findViewById(R.id.btn_add);

        // Set add button to open the add goal form
        addButton.setOnClickListener(event -> {
            Intent intent = new Intent(GoalActivity.this, AddGoalActivity.class).putExtra("GOAL_ID", goalId);
            startActivity(intent);
        });

    }

    private void setGoals(Goal[] newGoals) {
        // If list is empty show the empty list message
        if (newGoals.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        goals.clear();
        goals.addAll(Arrays.asList(newGoals));
        adapter.notifyDataSetChanged();
    }
}

    /*TextView emptyListText;

    ListView goalListView;
    List<Goal> goals;

    GoalDao goalDao;
    GoalListAdapter adapter;

    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        goals = new ArrayList<>();
        addButton = findViewById(R.id.btn_add);

        goalListView = findViewById(R.id.list_goal);
        emptyListText = findViewById(R.id.text_empty_list);

        // Set the journal list adapter
        adapter = new GoalListAdapter(getBaseContext(), goals);
        goalListView.setAdapter(adapter);

        // Set the listener on item click
        goalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal = goals.get(position);
                Intent intent = new Intent(getBaseContext(), ViewGoalActivity.class)
                        .putExtra("GOAL_ID", goal.uid);
                startActivity(intent);
            }
        });

        int goalId = getIntent().getIntExtra("GOAL_ID", -1);

        // Get Daos
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        goalDao = db.goalDao();

        // Get journal entries from database
        goalDao.getAllByGoal(goalId)
                .observe(this, newGoals -> setGoals(newGoals));

        // Set add button to open the add goal form
        addButton.setOnClickListener(event -> {
            Intent intent = new Intent(GoalActivity.this, AddGoalActivity.class).putExtra("GOAL_ID", goalId);
            startActivity(intent);
        });


    }

    private void setGoals(Goal[] newGoals) {
        // If list is empty show the empty list message
        if (newGoals.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        goals.clear();
        goals.addAll(Arrays.asList(newGoals));
        adapter.notifyDataSetChanged();
    }*/

