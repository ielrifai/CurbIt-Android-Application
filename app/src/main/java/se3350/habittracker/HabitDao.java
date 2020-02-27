package se3350.habittracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HabitDao {
    //get all habits - list
    @Query("SELECT * FROM habit")
    LiveData<Habit[]> getAll();

    //insert data
    //... - accept anything that is a list, array, or multiple arguments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Habit... habits);

    @Delete
    void delete(Habit habit);
}