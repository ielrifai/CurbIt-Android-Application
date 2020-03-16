package se3350.habittracker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.models.Subgoal;

public class ViewSubgoalActivity extends ActionBarActivity {

    String goal_description;
    int goalId;
    int subgoalId;
    Goal goal;
    Subgoal subgoal;
    JournalEntry draft;

    TextView goalDescriptionTextView, subgoalDescriptionTextView, viewProgressTextView;
    Button editSubgoalButton, resume4StepButton, editGoalButton;


    private SubgoalDao subgoalDao;
    private CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subgoal);

        subgoalDescriptionTextView = findViewById(R.id.subgoal_description);
        //viewProgressTextView = findViewById(R.id.view_progress);
        editSubgoalButton = findViewById(R.id.edit_subgoal);
        checkBox = findViewById(R.id.checkBox);

        subgoalId = getIntent().getIntExtra("SUBGOAL_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        subgoalDao = db.subgoalDao();

        // Get subgoal from database
        LiveData<Subgoal> subgoalLiveData = subgoalDao.getSubgoalById(subgoalId);
        subgoalLiveData.observe(this, subgoal -> {
            // If subgoal is not in database
            if(subgoal == null){
                return;
            }
            setSubgoal(subgoal);
        });

        SharedPreferences settings = getSharedPreferences("mysettings", 0);
        SharedPreferences.Editor editor = settings.edit();

        boolean checkBoxValue = checkBox.isChecked();
        editor.putBoolean("checkBox", checkBoxValue);
        editor.commit();

        checkBox.setChecked(settings.getBoolean("checkBox", false));


        //CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        //boolean checked = settings.getBoolean("checkBox", false);
        //checkBox.setChecked(checked);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_subgoal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.edit_subgoal:
                editSubgoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSubgoal(Subgoal subgoal)
    {
        this.subgoal = subgoal;
        setTitle(subgoal.name);
        subgoalDescriptionTextView.setText(subgoal.description);
    }

    /*public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.checkBox:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putBoolean("checkBox", checked).commit();
                break;
        }
    }*/


    private void editSubgoal() {
        Intent intent = new Intent(ViewSubgoalActivity.this, EditSubgoalActivity.class).putExtra("SUBGOAL_ID", subgoalId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
