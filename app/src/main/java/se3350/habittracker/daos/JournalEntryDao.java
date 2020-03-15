package se3350.habittracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import se3350.habittracker.models.JournalEntry;

@Dao
public interface JournalEntryDao {
    //Get all journal entries
    @Query("SELECT * FROM journalEntry")
    LiveData<JournalEntry[]> getAll();

    @Query("SELECT * FROM journalentry WHERE habitId=:habitId")
    LiveData<JournalEntry[]> getAllByHabit(int habitId);

    @Query("SELECT * FROM journalentry WHERE goalId=:goalId")
    LiveData<JournalEntry[]> getAllByGoal(int goalId);

    @Query("SELECT * FROM journalEntry WHERE uid=:uid")
    LiveData<JournalEntry> getById(int uid);

    @Query("SELECT * FROM journalentry WHERE isDraft=1 AND habitId=:habitId")
    LiveData<JournalEntry> getDraftOfHabit(int habitId);

    @Query("SELECT * FROM journalentry WHERE isDraft=1 AND goalId=:goalId")
    LiveData<JournalEntry> getDraftOfGoal(int goalId);

    @Query("SELECT * FROM journalentry WHERE isDraft=1 AND subgoalId=:subgoalId")
    LiveData<JournalEntry> getDraftOfSubgoal(int subgoalId);



    //insert data
    //... - accept anything that is a list, array, or multiple arguments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(JournalEntry... journalEntries);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournalEntries(JournalEntry... journalEntries);

    @Delete
    void delete(JournalEntry journalEntry);

    @Query("DELETE FROM journalentry WHERE habitId=:habitId")
    void deleteAllByHabitId(int habitId);

    @Query("DELETE FROM journalentry WHERE goalId=:goalId")
    void deleteAllByGoalId(int goalId);

    @Query("DELETE FROM journalentry WHERE subgoalId=:subgoalId")
    void deleteAllBySubgoalId(int subgoalId);

}
