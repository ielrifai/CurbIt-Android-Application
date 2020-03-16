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

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "goalId")
    public int goalId;


    //public - can access outside package (folders)
    public Subgoal(String name, String description, int goalId){
        this.name = name;
        this.description = description;
        this.goalId = goalId;
    }

}
