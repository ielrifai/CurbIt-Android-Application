package se3350.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se3350.habittracker.adapters.HabitListAdapter;
import se3350.habittracker.adapters.JournalListAdapter;

public class JournalListActivity extends AppCompatActivity {

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
        journalEntries.clear();
        journalEntries.addAll(Arrays.asList(newJournalEntries));
        adapter.notifyDataSetChanged();
    }
}
