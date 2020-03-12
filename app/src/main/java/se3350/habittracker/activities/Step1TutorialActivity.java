package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import se3350.habittracker.R;

public class Step1TutorialActivity extends AppCompatActivity {

    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1_tutorial);
        text = findViewById(R.id.tut_step1_steve_says);
        text.setMovementMethod(new ScrollingMovementMethod());
    }
}
