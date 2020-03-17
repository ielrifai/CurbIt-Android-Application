package se3350.habittracker.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.adapters.JournalListAdapter;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.models.JournalEntry;

public class JournalListActivity extends ActionBarActivity {

    TextView emptyListText;

    ListView journalListView;
    List<JournalEntry> journalEntries;

    JournalEntryDao journalEntryDao;
    JournalListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        journalEntries = new ArrayList<>();

        journalListView = findViewById(R.id.list_journal);
        emptyListText = findViewById(R.id.text_empty_list);

        // Set the journal list adapter
        adapter = new JournalListAdapter(getBaseContext(), journalEntries);
        journalListView.setAdapter(adapter);

        // Set the listener on item click
        journalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JournalEntry journalEntry = journalEntries.get(position);
                Intent intent = new Intent(getBaseContext(), ViewJournalEntryActivity.class)
                        .putExtra("JOURNAL_ID", journalEntry.uid);
                startActivity(intent);
            }
        });

        int habitId = getIntent().getIntExtra("HABIT_ID", -1);

        // Get Daos
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        journalEntryDao = db.journalEntryDao();

        // Get journal entries from database
        journalEntryDao.getAllByHabit(habitId)
                .observe(this, newJournalEntries -> setJournalEntries(newJournalEntries));


    }

    private void setJournalEntries(JournalEntry[] newJournalEntries) {
        // If list is empty show the empty list message
        if (newJournalEntries.length == 0)
            emptyListText.setVisibility(View.VISIBLE);
        else
            emptyListText.setVisibility(View.INVISIBLE);

        journalEntries.clear();
        journalEntries.addAll(Arrays.asList(newJournalEntries));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default


        return true;
    }
}
