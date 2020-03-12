package se3350.habittracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import se3350.habittracker.R;
import se3350.habittracker.models.Goal;

public class GoalListAdapter extends ArrayAdapter<Goal> {

    private final Context context;
    private final List<Goal> goals;

    public GoalListAdapter(Context context, List<Goal> goals) {
        super(context, -1, goals);
        this.context = context;
        this.goals = goals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_goal_list, parent, false);


        TextView goalNameText = rowView.findViewById(R.id.goal_name);
        TextView goalDescriptionText = rowView.findViewById(R.id.goal_description);

        goalNameText.setText(goals.get(position).name);
        goalDescriptionText.setText(goals.get(position).description);

        return rowView;
    }
}

