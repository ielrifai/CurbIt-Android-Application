package se3350.habittracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.activities.ViewSubgoalActivity;
import se3350.habittracker.adapters.SubgoalListAdapter;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Subgoal;

public class SubgoalFragment extends Fragment {

    private List<Subgoal> subgoals;
    private SubgoalListAdapter subgoalAdapter;


    private Button addButton;
    private TextView emptyListText;
    private ListView subgoalListView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_subgoal, container, false);
        addButton = root.findViewById(R.id.btn_add);
        emptyListText = root.findViewById(R.id.text_empty_list);
        subgoalListView = root.findViewById(R.id.list_subgoal);

        subgoals = new ArrayList<>();

        // Set the goal list adapter
        subgoalAdapter = new SubgoalListAdapter(getContext(), subgoals);
        subgoalListView.setAdapter(subgoalAdapter);

        // Set the listener on item click
        subgoalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subgoal subgoal = subgoals.get(position);
                Intent intent = new Intent(getContext(), ViewSubgoalActivity.class).putExtra("SUBGOAL_ID", subgoal.uid);
                startActivity(intent);
            }
        });


        // Get the database and user dao
        AppDatabase db = AppDatabase.getInstance(getContext());
        SubgoalDao subgoalDao = db.subgoalDao();


        // get goals from database
        LiveData<Subgoal[]> subgoalList = subgoalDao.getAll();


        // observe the data, refresh goal list view each time it's updated
        subgoalList.observe(getViewLifecycleOwner(), newsubgoals -> setSubgoals(newsubgoals));


        // Set add button to open the add habit form
       /* addButton.setOnClickListener(event -> {
            Intent intent = new Intent(this.getContext(), AddSubgoalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
*/


        return root;
    }

    private void setSubgoals(Subgoal[] newsubgoals){
        // If list is empty show the empty list message
        if (newsubgoals.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        subgoals.clear();
        subgoals.addAll(Arrays.asList(newsubgoals));
        subgoalAdapter.notifyDataSetChanged();

    }
}