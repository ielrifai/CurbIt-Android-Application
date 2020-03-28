package se3350.habittracker;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import se3350.habittracker.daos.GoalDao;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.JournalEntryDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Goal;
import se3350.habittracker.models.Habit;
import se3350.habittracker.models.JournalEntry;
import se3350.habittracker.models.Progress;
import se3350.habittracker.models.Subgoal;

//import android.database.sqlite.SQLiteDatabase;
//use sqlcipher instead of sqlite to create db

@Database(entities = {Habit.class, JournalEntry.class, Progress.class, Goal.class, Subgoal.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    //declare Daos - habit,user,etc
    public abstract HabitDao habitDao();
    public abstract JournalEntryDao journalEntryDao();
    public abstract ProgressDao progressDao();
    public abstract GoalDao goalDao();
    public abstract SubgoalDao subgoalDao();

    private static final String DB_NAME = "habitTracker.db";
    private static volatile AppDatabase instance;

    private static String password;

    public static void setPassword(String password){
        AppDatabase.password = password;
    }

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context){

        // Check if a password was set before creating, throw error if not and stop.
        if (AppDatabase.password == null){
            throw new IllegalStateException("Trying to create the database without a password!");
        }

        //Comment the following line to not destroy the database on launch
     //context.getApplicationContext().deleteDatabase(DB_NAME);

        //connect Room to SQLCipher API - now Room uses SQLCipher
        SQLiteDatabase.loadLibs(context);
        final byte[] passphrase = SQLiteDatabase.getBytes(password.toCharArray());
        final SupportFactory factory = new SupportFactory(passphrase);

        return Room.databaseBuilder(context,AppDatabase.class,DB_NAME)
                .openHelperFactory(factory)
                .build();
    }


}
