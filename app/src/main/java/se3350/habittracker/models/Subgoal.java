package se3350.habittracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Subgoal implements Comparable<Subgoal>{

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "habitId")
    public int habitId;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "position")
    public int position;

    //public - can access outside package (folders)
    public Subgoal(String name, int position, int habitId){
        this.name = name;
        this.habitId = habitId;
        this.position = position;
        this.completed = false;
    }

    @Override
    public int compareTo(Subgoal o) {
        return Integer.compare(this.position, o.position);
    }
}
