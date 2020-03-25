package se3350.habittracker.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import se3350.habittracker.R;
import se3350.habittracker.models.Subgoal;

public class SubgoalEditListAdapter extends ArrayAdapter<Subgoal> {

    private final Context context;
    private final List<Subgoal> subgoals;

    public SubgoalEditListAdapter(Context context, List<Subgoal> subgoals) {
        super(context, -1, subgoals);
        this.context = context;
        this.subgoals = subgoals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_subgoal_edit_list, parent, false);

        EditText subgoalEditText = view.findViewById(R.id.subgoal_name_edit);
        Button subgoalDeleteButton = view.findViewById(R.id.delete_subgoal_btn);

        subgoalEditText.setText(subgoals.get(position).name);

        // Change the name of the subgoal when the name is being edited
        subgoalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subgoals.get(position).name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        subgoalDeleteButton.setOnClickListener(v -> {
            subgoals.remove(position);
            this.notifyDataSetChanged();
        });

        return view;
    }
}

