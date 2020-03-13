package se3350.habittracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.models.Habit;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.models.Progress;

@Database(entities = {Habit.class, JournalEntry.class, Progress.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    //declare Daos - habit,user,etc
    public abstract HabitDao habitDao();
    public abstract JournalEntryDao journalEntryDao();
    public abstract ProgressDao progressDao();


    private static final String DB_NAME = "habitTracker.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context){
        //Comment the following line to not destroy the database on launch
     //context.getApplicationContext().deleteDatabase(DB_NAME);

        return Room.databaseBuilder(context,AppDatabase.class,DB_NAME).build();
    }
}
