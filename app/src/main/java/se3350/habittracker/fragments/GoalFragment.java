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
import se3350.habittracker.activities.ViewGoalActivity;
import se3350.habittracker.adapters.GoalListAdapter;
import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.models.Goal;

public class GoalFragment extends Fragment {

    private List<Goal> goals;
    private GoalListAdapter goalAdapter;


    private Button addButton;
    private TextView emptyListText;
    private ListView goalListView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_goal, container, false);
        addButton = root.findViewById(R.id.btn_add);
        emptyListText = root.findViewById(R.id.text_empty_list);
        goalListView = root.findViewById(R.id.list_goal);

        goals = new ArrayList<>();

        // Set the goal list adapter
        goalAdapter = new GoalListAdapter(getContext(), goals);
        goalListView.setAdapter(goalAdapter);

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
        GoalDao goalDao = db.goalDao();


        // get goals from database
        LiveData<Goal[]> goalList = goalDao.getAll();


        // observe the data, refresh goal list view each time it's updated
        goalList.observe(getViewLifecycleOwner(), newGoals -> setGoals(newGoals));


        // Set add button to open the add habit form
        /*addButton.setOnClickListener(event -> {
            Intent intent = new Intent(this.getContext(), AddGoalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });*/



        return root;
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