package se3350.habittracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;

import se3350.habittracker.models.Progress;

@Dao
public interface ProgressDao {
    @Query("SELECT * FROM progress WHERE habitId=:habitId ORDER BY date ASC")
    LiveData<Progress[]> getAllByHabit(int habitId);

    @Query("SELECT * FROM progress WHERE habitId=:habitId AND date BETWEEN :start AND :end")
    LiveData<Progress> getProgressByDate(int habitId, Date start, Date end);

    @Query("SELECT * FROM progress WHERE uid=:uid")
    LiveData<Progress> getProgressById(long uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOne(Progress progress);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProgress(Progress progress);

    @Delete
    void delete(Progress progress);
}
