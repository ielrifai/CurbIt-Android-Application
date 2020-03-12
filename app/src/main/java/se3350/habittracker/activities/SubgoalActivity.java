package se3350.habittracker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import se3350.habittracker.R;

public class SubgoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subgoals);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_subgoal)
                .build();

    }

}
