package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se3350.habittracker.R;

public class TutorialIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_introduction);
        setTitle(R.string.tutorial);
        Button btn = (Button) findViewById(R.id.begin_tutorial_button);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialIntroductionActivity.this, Step1TutorialActivity.class);
                startActivity(intent);
            }
        });
    }


}
