package se3350.habittracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Habit {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "avgScore")
    public double avgScore;

    //public - can access outside package (folders)
    public Habit(String name, String description){
        this.name = name;
        this.description = description;
    }

}
