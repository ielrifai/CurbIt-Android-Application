package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se3350.habittracker.R;

public class Step4TutorialActivity extends AppCompatActivity {

    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step4_tutorial);
        setTitle(R.string.tutorial);
        text = findViewById(R.id.tut_step4_steve_says);
        text.setMovementMethod(new ScrollingMovementMethod());

        Button btn = (Button) findViewById(R.id.tut_to_summary_button);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Step4TutorialActivity.this, TutorialSummaryActivity.class);
                startActivity(intent);
            }
        });
    }
}
