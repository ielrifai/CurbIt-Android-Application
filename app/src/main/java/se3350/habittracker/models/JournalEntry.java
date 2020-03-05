package se3350.habittracker.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "habitId") // Foreign key to reference the habit this entry belongs to
    public int habitId;

    @ColumnInfo(name = "step1") // Step 1 entry
    public String step1;

    @ColumnInfo(name = "step2") // Step 2 entry
    public String step2;

    @ColumnInfo(name = "step3") // Step 3 entry
    public String step3;

    @ColumnInfo(name = "step4") // Step 4 entry
    public String step4;

    @ColumnInfo(name = "date") // Date of last modification / creation
    public Date date;

    @ColumnInfo(name = "isDraft")
    public boolean isDraft;

    @ColumnInfo(name = "surveyScore")
    public int surveyScore;

    public JournalEntry(int habitId){
        this.habitId = habitId;
        this.date = new Date();
        this.isDraft = true;
    }
}
