package se3350.habittracker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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
    private static boolean mockDataLoaded = false;
    Switch themeSwitch;

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
        if(!mockDataLoaded){
            //insertMockData(); // COMMENT OUT TO STOP GENERATING DATA
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Use the switch item for theme changing switch
        MenuItem item = menu.findItem(R.id.theme_switch);
        themeSwitch = item.getActionView().findViewById(R.id.theme_switch);

        // Check if Night Mode is on to set the switch
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            themeSwitch.setChecked(true);
        } else {
            themeSwitch.setChecked(false);
        }

        // Listener for theme switch
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                SharedPreferences sharedPref = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                // If toggle switch to on, set Night Mode and save preference
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("NIGHT_MODE", true);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("NIGHT_MODE", false);
                    editor.apply();
                }
            }
        });
        return true;
    }

    private void insertMockData(){
        mockDataLoaded = true;
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

            for (int i = 0; i < 10; i++ ){
                calendar.set(2020, 2, 7 + i); // Start at March 7th
                progresses.add(new Progress((int)habit_id, calendar.getTime(), new Random().nextInt(11)));
            }

            // Use insertAll for progress Dao
                progressDao.insertAll(progresses);
                habitDao.updateAvgScore((int)habit_id);
        });
    }
}
