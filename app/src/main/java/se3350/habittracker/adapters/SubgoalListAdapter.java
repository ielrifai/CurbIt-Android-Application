package se3350.habittracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import se3350.habittracker.R;
import se3350.habittracker.models.Subgoal;

public class SubgoalListAdapter extends ArrayAdapter<Subgoal> {

    private final Context context;
    private final List<Subgoal> subgoals;

    public SubgoalListAdapter(Context context, List<Subgoal> subgoals) {
        super(context, -1, subgoals);
        this.context = context;
        this.subgoals = subgoals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View checkedTextView= inflater.inflate(R.layout.activity_view_goal, parent, false);

        TextView subgoalNameText = checkedTextView.findViewById(R.id.subgoal_name);

        subgoalNameText.setText(subgoals.get(position).name);

        return checkedTextView;
    }
}

