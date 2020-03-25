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

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import se3350.habittracker.R;
import se3350.habittracker.models.Subgoal;

public class SubgoalEditListAdapter extends ArrayAdapter<Subgoal> {

    private final Context context;
    private final List<Subgoal> subgoals;

    private boolean showAlert;

    public SubgoalEditListAdapter(Context context, List<Subgoal> subgoals) {
        super(context, -1, subgoals);
        this.context = context;
        this.subgoals = subgoals;
        this.showAlert = false;
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
            if(showAlert){
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                // Add title and text to confirmation popup
                builder.setMessage(context.getString(R.string.confirm_delete_popup_message, subgoals.get(position).name))
                        .setTitle(R.string.confirm_delete_popup_title_subgoal);

                // Add the buttons
                builder.setNegativeButton(R.string.delete, ((dialog, which) -> {
                    // Delete the sub goal if confirmed
                    subgoals.remove(position);
                    this.notifyDataSetChanged();
                }));

                builder.setNeutralButton(R.string.cancel, ((dialog, which) -> {}));

                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                subgoals.remove(position);
                this.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void setDeleteAlert(boolean showAlert){
        this.showAlert = showAlert;
    }
}

