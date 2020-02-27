package se3350.habittracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.AddHabitActivity;
import se3350.habittracker.AppDatabase;
import se3350.habittracker.Habit;
import se3350.habittracker.HabitDao;
import se3350.habittracker.R;
import se3350.habittracker.adapters.HabitListAdapter;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ListView habitListView = root.findViewById(R.id.list_habit);
        Button addButton = root.findViewById(R.id.btn_add);

        List<Habit> habits = new ArrayList<>();

        // Set the habit list adapter
        HabitListAdapter adapter = new HabitListAdapter(getContext(), habits);
        habitListView.setAdapter(adapter);


        // Get the database and user dao
        Habit newHabit = new Habit("habit", "hello");
        AppDatabase db = AppDatabase.getInstance(getContext());
        HabitDao habitDao = db.habitDao();

        // get habits from database
        LiveData<Habit[]> habitList = habitDao.getAll();

        // observe the data, refresh habit list view each time it's updated
        habitList.observe(getViewLifecycleOwner(), newHabits -> {
            habits.clear();
            habits.addAll(Arrays.asList(newHabits));
            adapter.notifyDataSetChanged();
        });


        // Set add button to open the add habit form
        addButton.setOnClickListener(event -> {
            Intent intent = new Intent(this.getContext(), AddHabitActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });


        return root;
    }
}