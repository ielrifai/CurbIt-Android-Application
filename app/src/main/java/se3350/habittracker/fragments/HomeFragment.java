package se3350.habittracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.activities.AddHabitActivity;
import se3350.habittracker.activities.ViewGoalActivity;
import se3350.habittracker.activities.ViewHabitActivity;
import se3350.habittracker.adapters.GoalListAdapter;
import se3350.habittracker.adapters.HabitListAdapter;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.models.Habit;

public class HomeFragment extends Fragment {

    private List<Habit> habits;
    private HabitListAdapter habitAdapter;
    private List<Goal> goals;
    private GoalListAdapter goalAdapter;


    private ListView habitListView;
    private Button addButton;
    private TextView emptyListText;
    private ListView goalListView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        habitListView = root.findViewById(R.id.list_habit);
        addButton = root.findViewById(R.id.btn_add);
        emptyListText = root.findViewById(R.id.text_empty_list);
        goalListView = root.findViewById(R.id.list_goal);


        habits = new ArrayList<>();
        goals = new ArrayList<>();

        // Set the habit list adapter
        habitAdapter = new HabitListAdapter(getContext(), habits);
        habitListView.setAdapter(habitAdapter);

        // Set the goal list adapter
        goalAdapter = new GoalListAdapter(getContext(), goals);
        habitListView.setAdapter(goalAdapter);

        // Set the listener on item click
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habit habit = habits.get(position);
                Intent intent = new Intent(getContext(), ViewHabitActivity.class).putExtra("HABIT_ID", habit.uid);
                startActivity(intent);
            }
        });

        // Set the listener on item click
        goalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal goal = goals.get(position);
                Intent intent = new Intent(getContext(), ViewGoalActivity.class).putExtra("GOAL_ID", goal.uid);
                startActivity(intent);
            }
        });


        // Get the database and user dao
        AppDatabase db = AppDatabase.getInstance(getContext());
        HabitDao habitDao = db.habitDao();
        GoalDao goalDao = db.goalDao();


        // get habits and goals from database
        LiveData<Habit[]> habitList = habitDao.getAll();
        LiveData<Goal[]> goalList = goalDao.getAll();


        // observe the data, refresh habit and goal list view each time it's updated
        habitList.observe(getViewLifecycleOwner(), newHabits -> setHabits(newHabits));
        goalList.observe(getViewLifecycleOwner(), newGoals -> setGoals(newGoals));


        // Set add button to open the add habit form
        addButton.setOnClickListener(event -> {
            Intent intent = new Intent(this.getContext(), AddHabitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });



        return root;
    }

    private void setHabits(Habit[] newHabits){
        // If list is empty show the empty list message
        if (newHabits.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        habits.clear();
        habits.addAll(Arrays.asList(newHabits));
        habitAdapter.notifyDataSetChanged();
    }

    private void setGoals(Goal[] newGoals){
        // If list is empty show the empty list message
        if (newGoals.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        goals.clear();
        goals.addAll(Arrays.asList(newGoals));
        goalAdapter.notifyDataSetChanged();

    }
}