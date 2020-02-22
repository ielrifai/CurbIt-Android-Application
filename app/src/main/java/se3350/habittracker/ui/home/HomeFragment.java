package se3350.habittracker.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.Habit;
import se3350.habittracker.HabitDao;
import se3350.habittracker.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView title = root.findViewById(R.id.title_habit_list);

        Habit newHabit = new Habit("habit", "hello");

        AppDatabase db = AppDatabase.getInstance(getContext());

        HabitDao habitDao = db.habitDao();

        // asynchronous insert using an executor
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            habitDao.insertAll(newHabit);
        });

        // asynchronous get using live data
        LiveData<Habit[]> habitList = habitDao.getAll();

        // observe the data, set title each time it's updated
        habitList.observe(getViewLifecycleOwner(), habits -> {
            if(habits.length > 0){
                String str = "";
                for (Habit habit : habits){
                    str += habit.name + " " + habit.description + "\n";
                }
                title.setText(str);
            }
        });

        return root;
    }
}