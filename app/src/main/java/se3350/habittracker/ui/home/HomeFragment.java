package se3350.habittracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.Habit;
import se3350.habittracker.HabitDao;
import se3350.habittracker.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView title = root.findViewById(R.id.title_habit_list);

        Habit newHabit = new Habit("habit 1", "hello");

        AppDatabase db = AppDatabase.getInstance(getContext());

        HabitDao habitDao = db.habitDao();

        habitDao.insertAll(newHabit);

        //observables
        LiveData<Habit[]> habitList = habitDao.getAll();

        title.setText(habitList.getValue()[0].name+habitList.getValue()[0].description);

        return root;
    }
}