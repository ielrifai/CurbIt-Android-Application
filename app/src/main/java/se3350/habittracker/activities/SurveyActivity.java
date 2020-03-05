package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import se3350.habittracker.R;

public class SurveyActivity extends AppCompatActivity {

    // Elements
    SeekBar surveySeekbar;
    TextView surveyProgressValue;
    Button submitButton;

    // Values
    int currentProgressValue = 0; // Variable to keep track of user's selected progress value


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        // Get the entry from database

        // Get the elements
        surveySeekbar = (SeekBar) findViewById(R.id.survey_seekbar);
        surveyProgressValue = (TextView) findViewById(R.id.survey_progress_value);
        submitButton = (Button) findViewById(R.id.submitButton);

        // Seekbar setup and listener
        surveySeekbar.setProgress(0);
        surveySeekbar.incrementProgressBy(1);
        surveySeekbar.setMax(10); // Out of 10

        surveySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                surveyProgressValue.setText(String.valueOf(progress));
                currentProgressValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Submit Button Listener

        // Add the progress value (points) to the entry and total progress
    }
}
