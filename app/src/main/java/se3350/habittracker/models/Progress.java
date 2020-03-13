package se3350.habittracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Progress {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "habitId") // Foreign key to reference the habit this entry belongs to
    public int habitId;

    @ColumnInfo(name = "surveyScore")
    public int surveyScore;

    @ColumnInfo(name = "date") // Date should not be updated
    public Date date;

    public Progress(int habitId){
        this.habitId = habitId;
        this.date = new Date();
    }
}
