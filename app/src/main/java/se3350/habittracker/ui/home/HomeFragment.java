package se3350.habittracker.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import se3350.habittracker.DatabaseHandler;
import se3350.habittracker.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView t = root.findViewById(R.id.title_habit_list);

        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());

        //get the data and append to a list
        Cursor data = databaseHandler.getData();
        //moves to next row as cursor starts position -1 of table
        data.moveToNext();

        Log.d("dd", data.getString(1));
        t.setText(data.getString(data.getColumnIndex(DatabaseHandler.COLUMN_NAME)));

        return root;
    }
}