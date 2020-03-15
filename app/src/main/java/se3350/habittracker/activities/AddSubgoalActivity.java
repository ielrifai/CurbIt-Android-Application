package se3350.habittracker.activities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import se3350.habittracker.AppDatabase;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Subgoal;
import se3350.habittracker.R;

public class AddSubgoalActivity extends ActionBarActivity {

        String subgoalName, subgoalDescription;

        EditText subgoalNameInput, subgoalDescriptionInput;

        Button submitButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_subgoal);
            // getSupportActionBar().setDisplayShowHomeEnabled(true);

            subgoalNameInput = (EditText) findViewById(R.id.subgoal_name);
            subgoalDescriptionInput = (EditText) findViewById(R.id.subgoal_description);

            submitButton = (Button) findViewById(R.id.submit_btn);

            submitButton.setOnClickListener(v -> {
                //save habit info
                subgoalName = subgoalNameInput.getText().toString();
                subgoalDescription = subgoalDescriptionInput.getText().toString();

                if(subgoalName.length() == 0 || subgoalDescription.length() == 0){
                    Toast.makeText(getApplicationContext(), R.string.error_add_goal,Toast.LENGTH_SHORT).show();
                    return;
                }

                //add habit to db
                Subgoal newSubgoal = new Subgoal(subgoalName, subgoalDescription);

                AppDatabase db = AppDatabase.getInstance(this);

                SubgoalDao subgoalDao = db.subgoalDao();

                // asynchronous insert using an executor
                //to use Java 8 - go to project settings and change target compatibility to 1.8
                Executor myExecutor = Executors.newSingleThreadExecutor();
                myExecutor.execute(() -> {
                    subgoalDao.insertAll(newSubgoal);
                });

                onBackPressed();
            });

        }



    }


