package se3350.habittracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.ProgressDao;
import se3350.habittracker.models.Progress;

public class ViewHabitProgressActivity extends ActionBarActivity {

    AnyChartView anyChartView;
    TextView viewProgressMessage;

    List<DataEntry> data;

    ProgressDao progressDao;
    HabitDao habitDao;
    DateFormat dateFormat;

    int habitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_habit_progress);
        setTitle(getString(R.string.habit_progress_header));
        // DateFormat to show the short dates without times
        dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);

        // Get the progresses for this habit
        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        progressDao = db.progressDao();
        habitDao = db.habitDao();

        habitDao.getHabitById(habitId).observe(this, habit -> setTitle(getString(R.string.view_progress_title, habit.name)));

        viewProgressMessage = findViewById(R.id.view_progress_message);

        // Get progresses from database
        progressDao.getAllByHabit(habitId)
                .observe(this, newProgresses -> setProgressesInChart(newProgresses));

    }

    private void setProgressesInChart(Progress[] progresses)
    {
        // If there is no progress, set a message
        if (progresses.length == 0){
            viewProgressMessage.setText(R.string.no_progress_show);
            viewProgressMessage.setVisibility(TextView.VISIBLE);
        }
        else{
            viewProgressMessage.setVisibility(TextView.INVISIBLE);

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

            lineChart.yScale().minimum(0);
            lineChart.yScale().maximum(10);

            lineChart.yScale().ticks().interval(1);

            // Set the data into a series and Markers
            Line lineChartLine = lineChart.line(data);
            lineChartLine.markers().enabled(true);
            lineChartLine.markers().fill("#FFFFFF");
            lineChartLine.stroke("3 white");

            // Series Names
            lineChart.getSeriesAt(0).name(getString(R.string.chart_score));

            // Scroller
            lineChart.xScroller(true);

            // Set the gradient background
            String[] colours = { "#B22222" , "#FFA500",  "#FFD700", "#8BC34A" };
            lineChart.dataArea().background().enabled(true);
            lineChart.dataArea().background().fill(colours, 90, true, 100);

            // Set back background
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                lineChart.background().fill("#292929");
            } else {
                lineChart.background().fill("#FFFFFF");
            }

            // Put the chart in the  view
            anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
            anyChartView.setChart(lineChart);
            anyChartView.setVisibility(View.VISIBLE);
        }
    }
}
