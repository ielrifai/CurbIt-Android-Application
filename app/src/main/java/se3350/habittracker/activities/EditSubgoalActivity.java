package se3350.habittracker.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.models.Subgoal;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.R;

public class EditSubgoalActivity extends ActionBarActivity {
    EditText subgoalDescriptionEditView;
    EditText subgoalNameEditView;

    Subgoal subgoal;
    int subgoalId;

    SubgoalDao subgoalDao;

    private void setSubgoal(Subgoal goal)
    {
        this.subgoal = goal;
    }

    Button applyChangesButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subgoalId = getIntent().getIntExtra("SUBGOAL_ID", -1);

        setContentView(R.layout.activity_edit_subgoal);
        subgoalDescriptionEditView = findViewById(R.id.subgoal_description);
        subgoalNameEditView = findViewById(R.id.subgoal_name);
        applyChangesButton = findViewById(R.id.apply_btn);

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        subgoalDao = db.subgoalDao();

        LiveData<Subgoal> goalLiveData = subgoalDao.getSubgoalById(subgoalId);
        goalLiveData.observe(this, subgoal -> {
            if(subgoal == null)
                return;

            setSubgoal(subgoal);
            subgoalDescriptionEditView.setText(subgoal.description);
            subgoalNameEditView.setText(subgoal.name);
        });

        applyChangesButton.setOnClickListener(event -> {
            saveSubgoal();
            onBackPressed();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_subgoal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.delete_subgoal:
                deleteSubgoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteSubgoal() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Add title and text to confirmation popup
        builder.setMessage(getString(R.string.confirm_delete_popup_message, subgoal.name))
                .setTitle(R.string.confirm_delete_popup_title);

        // Add the buttons
        builder.setPositiveButton(R.string.delete, ((dialog, which) -> {
            // Delete the habit if confirmed
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                // Delete subgoal and all its journal entries
                subgoalDao.delete(subgoal);

                // Go back to Subgoal List and clear task (clear all stacks)
                Intent intent = new Intent(EditSubgoalActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        }));

        builder.setNegativeButton(R.string.cancel, ((dialog, which) -> {}));

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveSubgoal() {
        subgoal.description = subgoalDescriptionEditView.getText().toString();
        subgoal.name = subgoalNameEditView.getText().toString();

        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            subgoalDao.updateSubgoals(subgoal);
        });
    }
}