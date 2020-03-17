package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se3350.habittracker.R;

public class TutorialSummaryActivity extends ActionBarActivity {

    private TextView rightText;
    private TextView bottomText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_summary);
        setTitle(R.string.tutorial);

        rightText = findViewById(R.id.tut_summary_text1);
        rightText.setMovementMethod(new ScrollingMovementMethod());
        bottomText = findViewById(R.id.tut_summary_text2);
        bottomText.setMovementMethod(new ScrollingMovementMethod());

        Button btn = (Button) findViewById(R.id.summary_to_home_button);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorialSummaryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
