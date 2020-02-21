package se3350.habittracker;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "habitTrackerDB.db";

    private static final String TABLE_NAME = "habit";
    public static final String COLUMN_NAME = "name";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";

    SQLiteDatabase database;



    public DatabaseHandler(@Nullable Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
        //create db
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //add table to db
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_ID + "INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT, " + COLUMN_DESCRIPTION + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        //onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        //onCreate(db);
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, item);

        Log.d("SQLtest","DBhelper: adding this "+ item);

        long result = db.insert(TABLE_NAME,null, cv);

        if(result == -1){

            return false;
        }
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
