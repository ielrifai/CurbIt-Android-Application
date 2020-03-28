package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import android.os.Bundle;

import com.baoyachi.stepview.VerticalStepView;
import com.baoyachi.stepview.bean.StepBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Subgoal;

public class ViewGoalActivity extends ActionBarActivity {

    int habitId;
    SubgoalDao subgoalDao;
    HabitDao habitDao;
    List<Subgoal> subgoals;

    VerticalStepView verticalStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        verticalStepView = findViewById(R.id.step_view);

        habitId = getIntent().getIntExtra("HABIT_ID", -1 );

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        subgoalDao = db.subgoalDao();
        habitDao = db.habitDao();

        habitDao.getHabitById(habitId).observe(this, habit -> setTitle(getString(R.string.view_goal_progress_title, habit.name)));

        // Get subgoals from database
        LiveData<Subgoal[]> subgoalLiveData = subgoalDao.getSubgoalsByHabitId(habitId);
        subgoalLiveData.observe(this, subgoals -> setSubgoal(subgoals));
    }

    private void setSubgoal(Subgoal[] subgoals) {
        List<Subgoal> subgoalList = new ArrayList<>(Arrays.asList(subgoals));
        this.subgoals = getOrderedSubgoalList(subgoalList);

        // set the view

        // List of the steps/subgoals
        List<String> stepList = new ArrayList<>();

        // Add the "Start" step to the list first
        stepList.add(getString(R.string.start));

        // Add subgoals to list
        this.subgoals.forEach(subgoal -> stepList.add(subgoal.name));

        // Add the "Complete" step to the list last
        stepList.add(getString(R.string.complete));

        int lastCompletedPosition = 0;
        for(Subgoal s : subgoals){
            if(s.completed){
                lastCompletedPosition++;
            }
        }
        verticalStepView.setStepsViewIndicatorComplectingPosition(lastCompletedPosition + 1)
                .reverseDraw(false)//default is true
                .setStepViewTexts(stepList)
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.textColor))
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.textColor))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.textColor))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.ic_check_circle))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_circle));
    }

    private List<Subgoal> getOrderedSubgoalList(List<Subgoal> subgoals){
        Collections.sort(subgoals);
        return subgoals;
    }
}
