package se3350.habittracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.adapters.SubgoalListAdapter;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Subgoal;

public class ViewSubgoalListActivity extends ActionBarActivity {

    TextView emptyListText;
    ListView subgoalListView;
    List<Subgoal> subgoals;
    int goalId;

    SubgoalDao subgoalDao;
    SubgoalListAdapter adapter;

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subgoals);
        subgoals = new ArrayList<>();
        goalId = getIntent().getIntExtra("GOAL_ID", -1);

        subgoalListView = findViewById(R.id.list_subgoal);
        emptyListText = findViewById(R.id.text_empty_list);

        // Set the subgoal list adapter
        adapter = new SubgoalListAdapter(getBaseContext(), subgoals);
        subgoalListView.setAdapter(adapter);

        // Set the listener on item click
        subgoalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subgoal subgoal = subgoals.get(position);
                Intent intent = new Intent(getBaseContext(), ViewSubgoalActivity.class)
                        .putExtra("SUBGOAL_ID", subgoal.uid);
                startActivity(intent);
            }
        });

        int subgoalId = getIntent().getIntExtra("SUBGOAL_ID", -1);

        // Get Daos
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        subgoalDao = db.subgoalDao();

        // get subgoals and goals from database
        LiveData<Subgoal[]> subgoalList = subgoalDao.getSubgoalsByGoalId(goalId);

        // Get subgoal entries from database
        subgoalList.observe(this, newSubgoals -> setSubgoals(newSubgoals));


        addButton = findViewById(R.id.btn_add);

        // Set add button to open the add subgoal form
        addButton.setOnClickListener(event -> {
            Intent intent = new Intent(ViewSubgoalListActivity.this, AddSubgoalActivity.class).putExtra("SUBGOAL_ID", subgoalId);
            intent.putExtra("GOAL_ID", goalId);
            startActivity(intent);
        });

    }

    private void setSubgoals(Subgoal[] newSubgoals) {
        // If list is empty show the empty list message
        if (newSubgoals.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        subgoals.clear();
        subgoals.addAll(Arrays.asList(newSubgoals));
        adapter.notifyDataSetChanged();
    }
}
