package se3350.habittracker.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.R;

public class JournalListAdapter extends ArrayAdapter<JournalEntry> {
    private final Context context;
    private final List<JournalEntry> journalEntries;

    private DateFormat dateFormat;

    public JournalListAdapter(Context context, List<JournalEntry> journalEntries) {
        super(context, -1, journalEntries);
        this.context = context;
        this.journalEntries = journalEntries;
        this.dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_journal_list, parent, false);

        TextView journalTitle = rowView.findViewById(R.id.journal_title);

        String title = dateFormat.format(journalEntries.get(position).date);
        journalTitle.setText(title);

        // If draft change the display
        if(journalEntries.get(position).isDraft) {
            journalTitle.setTextColor(rowView.getResources().getColor(R.color.colorAccent, getDropDownViewTheme()));
            title = journalTitle.getText() + " (draft)";
            journalTitle.setText(title);
        }

        return rowView;
    }
}
