package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se3350.habittracker.R;

public class Step1TutorialActivity extends AppCompatActivity {

    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step1_tutorial);
        setTitle(R.string.tutorial);
        text = findViewById(R.id.tut_step1_steve_says);
        text.setMovementMethod(new ScrollingMovementMethod());

        Button btn = (Button) findViewById(R.id.tut_to_step2_button);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Step1TutorialActivity.this, Step2TutorialActivity.class);
                startActivity(intent);
            }
        });
    }
}
