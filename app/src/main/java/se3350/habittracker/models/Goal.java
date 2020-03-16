package se3350.habittracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Goal {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;
    //goal is linked to a habit
    @ColumnInfo(name = "habitId")
    public int habitId;

    //public - can access outside package (folders)

   public Goal(String name, String description,int habitId){
        this.name = name;
        this.description = description;
        this.habitId = habitId;
    }



}
