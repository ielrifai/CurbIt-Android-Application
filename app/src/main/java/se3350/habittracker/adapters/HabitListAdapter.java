package se3350.habittracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import se3350.habittracker.models.Habit;
import se3350.habittracker.R;

public class HabitListAdapter extends ArrayAdapter<Habit> {
    private final Context context;
    private final List<Habit> habits;

    public HabitListAdapter(Context context, List<Habit> habits) {
        super(context, -1, habits);
        this.context = context;
        this.habits = habits;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_habit_list, parent, false);


        TextView habitNameText = rowView.findViewById(R.id.habit_name);
        TextView habitDescriptionText = rowView.findViewById(R.id.habit_description);

        habitNameText.setText(habits.get(position).name);
        habitDescriptionText.setText(habits.get(position).description);

        return rowView;
    }
}
