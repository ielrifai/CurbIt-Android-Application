package se3350.habittracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import se3350.habittracker.models.Habit;

public class GoalDao {

    //get all goals - list
    @Query("SELECT * FROM goal")
    LiveData<Goal[]> getAll();

    @Query("SELECT * FROM goal WHERE uid=:id")
    LiveData<Goal> getGoalById(int id);

    //insert data
    //... - accept anything that is a list, array, or multiple arguments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Goal... goals);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGoals(Goal... goals);

    @Delete
    void delete(Goal goal);
}
