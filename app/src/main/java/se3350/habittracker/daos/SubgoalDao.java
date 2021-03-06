package se3350.habittracker.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import se3350.habittracker.models.Subgoal;

@Dao
public interface SubgoalDao {

    //get all subgoals - list
    @Query("SELECT * FROM subgoal")
    LiveData<Subgoal[]> getAll();

    @Query("SELECT * FROM subgoal WHERE uid=:id")
    LiveData<Subgoal> getSubgoalById(int id);

    @Query("SELECT * FROM subgoal WHERE habitId=:habitId")
    LiveData<Subgoal[]> getSubgoalsByHabitId(int habitId);

    //insert data
    //... - accept anything that is a list, array, or multiple arguments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Subgoal... subgoals);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSubgoals(Subgoal... subgoals);

    @Delete
    void delete(Subgoal subgoal);

    @Query("DELETE FROM subgoal WHERE habitId=:habitId")
    void deleteAllByHabitId(int habitId);
}
