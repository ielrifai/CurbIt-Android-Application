package se3350.habittracker.activities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.models.Habit;
import se3350.habittracker.models.Progress;

public class MainActivity extends AppCompatActivity {
    long habit_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_information)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Insert Mock Data for Demo Purposes
        insertMockData(); // COMMENT OUT TO STOP GENERATING DATA
    }

    private void insertMockData(){
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        ProgressDao progressDao = db.progressDao();
        HabitDao habitDao = db.habitDao();

        // Insert a Habit
        Habit newHabit = new Habit("Example Habit", "This is about Example Habit.");
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
             habit_id = habitDao.insertOne(newHabit);
             habitDao.updateHabitNameDesc((int)habit_id, "Example Habit " + habit_id, "This is about Example Habit " + habit_id);

            // Insert a bunch of progresses for the habit
            // Make an array of progresses using progress constructor that uses a date
            Calendar calendar = Calendar.getInstance();
            List<Progress> progresses = new ArrayList<>();

            for (int i = 0; i < 15; i++ ){
                calendar.set(2020, 2, 12 + i);
                progresses.add(new Progress((int)habit_id, calendar.getTime(), new Random().nextInt(11)));
            }

            // Use insertAll for progress Dao
                progressDao.insertAll(progresses);
                habitDao.updateAvgScore((int)habit_id);
        });
    }
}
