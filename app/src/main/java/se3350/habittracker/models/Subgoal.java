package se3350.habittracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subgoal {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "habitId")
    public int habitId;

    @ColumnInfo(name = "completed")
    public boolean completed;

    //public - can access outside package (folders)
    public Subgoal(String name, int habitId){
        this.name = name;
        this.habitId = habitId;
        this.completed = false;
    }
}
