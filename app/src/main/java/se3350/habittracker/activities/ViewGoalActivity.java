package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import android.os.Bundle;

import com.baoyachi.stepview.VerticalStepView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.SubgoalDao;
import se3350.habittracker.models.Subgoal;

public class ViewGoalActivity extends ActionBarActivity {

    int habitId;
    SubgoalDao subgoalDao;
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

        // Get subgoals from database
        LiveData<Subgoal[]> subgoalLiveData = subgoalDao.getSubgoalsByHabitId(habitId);
        subgoalLiveData.observe(this, subgoals -> setSubgoal(subgoals));
    }

    private void setSubgoal(Subgoal[] subgoals) {
        List<Subgoal> subgoalList = new ArrayList<>(Arrays.asList(subgoals));
        this.subgoals = getOrderedSubgoalList(subgoalList);

        // set the view

        List<String> stepList = new ArrayList<>();
        this.subgoals.forEach(subgoal -> stepList.add(subgoal.name));

        verticalStepView.setStepsViewIndicatorComplectingPosition(stepList.size() - 5)//设置完成的步数
                .reverseDraw(false)//default is true
                .setStepViewTexts(stepList)//总步骤
                .setLinePaddingProportion(0.85f)//设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, R.color.colorAccent))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.textColor))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, R.color.textColor))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.textColor))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon
    }

    private List<Subgoal> getOrderedSubgoalList(List<Subgoal> subgoals){
        Collections.sort(subgoals);
        return subgoals;
    }
}
