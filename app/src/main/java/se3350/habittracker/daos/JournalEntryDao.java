package se3350.habittracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import se3350.habittracker.models.Habit;
import se3350.habittracker.models.JournalEntry;

@Dao
public interface JournalEntryDao {
    //Get all journal entries
    @Query("SELECT * FROM journalEntry")
    LiveData<JournalEntry[]> getAll();

    @Query("SELECT * FROM journalentry WHERE habitId=:habitId")
    LiveData<JournalEntry[]> getAllByHabit(int habitId);

    @Query("SELECT * FROM journalEntry WHERE uid=:uid")
    LiveData<JournalEntry> getById(int uid);

    //insert data
    //... - accept anything that is a list, array, or multiple arguments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(JournalEntry... journalEntries);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournalEntries(JournalEntry... journalEntries);

    @Delete
    void delete(Habit habit);
}
