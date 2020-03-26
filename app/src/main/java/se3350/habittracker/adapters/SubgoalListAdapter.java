package se3350.habittracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Subgoal;

public class SubgoalListAdapter extends ArrayAdapter<Subgoal> {

    private final Context context;
    private final List<Subgoal> subgoals;

    private SubgoalDao subgoalDao;

    public SubgoalListAdapter(Context context, List<Subgoal> subgoals) {
        super(context, -1, subgoals);
        this.context = context;
        this.subgoals = subgoals;

        // Get the subgoal Dao
        AppDatabase db = AppDatabase.getInstance(context);
        subgoalDao = db.subgoalDao();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_subgoal_list, parent, false);

        TextView subgoalNameText = view.findViewById(R.id.subgoal_name);
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener((v, isChecked) -> {
            subgoals.get(position).completed = isChecked;
        });

        //if checked - subgoal complete
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if checked -- subgoal completed: gamification alert
                if(subgoals.get(position).completed == true){
                    Snackbar.make(parent, R.string.alert_subgoal_gami,
                            Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });

        checkBox.setChecked(subgoals.get(position).completed);
        subgoalNameText.setText(subgoals.get(position).name);

        return view;
    }
}

