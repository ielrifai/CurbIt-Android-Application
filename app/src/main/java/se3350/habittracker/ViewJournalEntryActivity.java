package se3350.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.widget.TextView;

public class ViewJournalEntryActivity extends AppCompatActivity {

    JournalEntry journalEntry;
    JournalEntryDao journalEntryDao;

    TextView relabelTextView;
    TextView refocusTextView;
    TextView revalueTextView;
    TextView reframeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal_entry);

        relabelTextView = findViewById(R.id.relabel_text);
        refocusTextView = findViewById(R.id.refocus_text);
        reframeTextView = findViewById(R.id.reframe_text);
        revalueTextView = findViewById(R.id.revalue_text);

        // Get journalId from the intent extra
        int journalId = getIntent().getIntExtra("JOURNAL_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        journalEntryDao = db.journalEntryDao();

        // Get journal from database
        LiveData<JournalEntry> journalEntryLiveData = journalEntryDao.getById(journalId);

        journalEntryLiveData.observe(this, journalEntry -> setJournalEntry(journalEntry));
    }

    private void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;

        relabelTextView.setText(journalEntry.step1);
        reframeTextView.setText(journalEntry.step2);
        refocusTextView.setText(journalEntry.step3);
        revalueTextView.setText(journalEntry.step4);
    }
}
