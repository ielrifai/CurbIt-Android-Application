package se3350.habittracker.activities;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.models.Progress;

public class ViewHabitProgress extends ActionBarActivity {

    AnyChartView anyChartView;

    List<DataEntry> data;

    ProgressDao progressDao;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_progress);

        // DateFormat to show the short dates without times
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);

        // Get the progresses for this habit
        int habitId = getIntent().getIntExtra("HABIT_ID", -1);

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        progressDao = db.progressDao();

        // Get progresses from database
        progressDao.getAllByHabit(habitId)
                .observe(this, newProgresses -> setProgresses(newProgresses));

    }

    private void setProgresses(Progress[] progresses)
    {
        // Use AnyChart to make a chart that displays data from the progress items for the habit
        data = new ArrayList<>();
        for(Progress progress : progresses){
            String date = dateFormat.format(progress.date);
            data.add(new ValueDataEntry(date, progress.surveyScore));
        }
        Cartesian lineChart = AnyChart.line();
        lineChart.xAxis(0).title(getString(R.string.progress_chart_xaxis));
        lineChart.yAxis(0).title(getString(R.string.progress_chart_yaxis));
        lineChart.title(getString(R.string.progress_chart_title));
        lineChart.yScale().maximum(10);

        lineChart.data(data);

        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(lineChart);

        //TODO: Notify if no progress has been logged
    }

}
