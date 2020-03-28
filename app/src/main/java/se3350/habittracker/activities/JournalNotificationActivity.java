package se3350.habittracker.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.ReminderBroadcast;
import se3350.habittracker.daos.HabitDao;

import static java.security.AccessController.getContext;


public class JournalNotificationActivity extends AppCompatActivity {
    TimePicker notificationTime;
    Switch notificationSwitch;
    TextView habitNotificationSettings;
    TextView habitNotificationDescription;
    TextView habitNotificationTitle;
    HabitDao habitDao;
    int habitId;
    String habitName;
    SharedPreferences notificationSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_notification);
        notificationTime = findViewById(R.id.notification_time_picker);
        notificationSwitch = findViewById(R.id.habit_notification_switch);
        habitNotificationTitle = findViewById(R.id.habit_notification_title);
        habitNotificationDescription = findViewById(R.id.habit_notification_description);
        habitNotificationDescription.setMovementMethod(new ScrollingMovementMethod());
        habitNotificationSettings = findViewById(R.id.habit_notification_title);
        createNotificationChannel();

        // Get Daos and DB
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        habitDao = db.habitDao();
        habitId = getIntent().getIntExtra("HABIT_ID", -1);
        habitDao.getHabitById(habitId).observe(this, habit -> habitName = habit.name);

        notificationSettings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        //Shared Preferences

        if (notificationSettings.contains(habitId + "reminder")) {
            boolean set = false;
            set = notificationSettings.getBoolean(habitId + "reminder", set);
            if(set)
                notificationSwitch.setChecked(true);
        }

        if (notificationSettings.contains(habitId + "timeHour")) {
            int hour = 0;
            hour = notificationSettings.getInt(habitId+"timeHour", hour);
            notificationTime.setHour(hour);
        }

        if (notificationSettings.contains(habitId + "timeMinute")) {
            int minute = 0;
            minute = notificationSettings.getInt(habitId+"timeMinute", minute);
            notificationTime.setMinute(minute);
        }

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(notificationSwitch.isChecked())
                    sendNotification(true);
                else
                    sendNotification(false);
            }
        });

        notificationTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                if(notificationSwitch.isChecked())
                    sendNotification(true);

                notificationSettings.edit().putInt(habitId +"timeHour", notificationTime.getHour()).commit();
                notificationSettings.edit().putInt(habitId +"timeMinute", notificationTime.getMinute()).commit();

            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "JournalReminderChannel";
            String description = "Channel for Journal Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("journal_notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(boolean isOn) {
        Toast.makeText(this, "Preferences Saved", Toast.LENGTH_SHORT).show();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, notificationTime.getHour());
        cal.set(Calendar.MINUTE, notificationTime.getMinute());
        cal.set(Calendar.SECOND, 0);

        Intent intent = new Intent(JournalNotificationActivity.this, ReminderBroadcast.class);
        intent.putExtra("HABIT_ID", habitId);
        intent.putExtra("HABIT_NAME", habitName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(JournalNotificationActivity.this, habitId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Boolean status = notificationSettings.edit().putBoolean(habitId + "reminder", true).commit();
        }
        else {
            alarmManager.cancel(pendingIntent);
            Boolean status = notificationSettings.edit().putBoolean(habitId + "reminder", false).commit();
        }

        boolean set = false;
        set = notificationSettings.getBoolean(habitId + "reminder", set);
    }
}
