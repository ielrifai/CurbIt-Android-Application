package se3350.habittracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import se3350.habittracker.models.Habit;

@Dao
public interface HabitDao {
    //get all habits - list
    @Query("SELECT * FROM habit")
    LiveData<Habit[]> getAll();

    @Query("SELECT * FROM habit WHERE uid=:id")
    LiveData<Habit> getHabitById(int id);

    //insert data
    //... - accept anything that is a list, array, or multiple arguments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Habit... habits);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateHabits(Habit... habits);

    @Delete
    void delete(Habit habit);

    // SQL query to get the avg of the scores of progresses for this habit and update the habit
    @Query("UPDATE habit " +
            "SET avgScore = (" +
                "SELECT AVG(surveyScore) " +
                "from progress as p " +
                "WHERE p.habitId = :id)" +
                "WHERE uid=:id")
    void updateAvgScore(int id);

}
