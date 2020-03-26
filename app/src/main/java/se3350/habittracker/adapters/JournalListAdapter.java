package se3350.habittracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import se3350.habittracker.R;
import se3350.habittracker.models.JournalEntry;

public class JournalListAdapter extends ArrayAdapter<JournalEntry> implements Filterable {
    private final Context context;
    private EntryFilter entryFilter;
    private final List<JournalEntry> journalEntries;
    private List<JournalEntry> filteredList;

    private DateFormat dateFormat;

    public JournalListAdapter(Context context, List<JournalEntry> journalEntries) {
        super(context, -1, journalEntries);
        this.context = context;
        this.journalEntries = journalEntries;
        this.dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        this.filteredList = journalEntries;

        getFilter();
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

    /**
     * Get size of entry list
     * @return entryList size
     */
    @Override
    public int getCount() {
        return filteredList.size();
    }

    /**
     * Get specific item from entry list
     * @param i item index
     * @return list item
     */
    @Override
    public JournalEntry getItem(int i) {
        return filteredList.get(i);
    }

    /**
     * Get user list item id
     * @param i item index
     * @return current item id
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Get custom filter
     * @return filter
     */
    @Override
    public Filter getFilter() {
        if (entryFilter == null) {
            entryFilter = new EntryFilter();
        }

        return entryFilter;
    }


    /**
     * Custom filter for Journal Entry list
     * Filter content in Journal Entry list according to the search text
     */
    private class EntryFilter extends Filter {

        SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE");
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("M/dd/yy");

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint!=null && constraint.length()>0) {
                ArrayList<JournalEntry> tempList = new ArrayList<JournalEntry>();


                // search content in list
                for (JournalEntry entry : journalEntries) {
                    String weekDay = dayOfWeek.format(entry.getDate());
                    String date = simpleDateformat.format(entry.getDate());

                    if (weekDay.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(entry);
                    }

                    else if (date.contains(constraint.toString())) {
                        tempList.add(entry);
                    }

                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
                Log.e("VALUES", filterResults.values.toString());
            } else {
                filterResults.count = journalEntries.size();
                filterResults.values = journalEntries;
            }

            return filterResults;
        }

        /**
         * Notify about filtered list to ui
         * @param constraint text
         * @param results filtered result
         */
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<JournalEntry>) results.values;
            notifyDataSetChanged();
        }
    }


}