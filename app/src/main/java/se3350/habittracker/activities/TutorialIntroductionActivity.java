package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import se3350.habittracker.R;

public class TutorialIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_introduction);
        setTitle(R.string.tutorial);

    }
}
