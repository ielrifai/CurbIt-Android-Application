package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anychart.charts.Gantt;

import java.util.Calendar;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.ReminderBroadcast;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.JournalEntryDao;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;


public class JournalNotificationActivity extends AppCompatActivity {
    TimePicker notificationTime;
    Switch notificationSwitch;
    TextView habitNotificationSettings;
    TextView habitNotificationDescription;
    TextView habitNotificationTitle;
    HabitDao habitDao;
    int habitId;
    String habitName;



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
        System.out.println("The habit id is: " + habitId);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(notificationSwitch.isChecked())
                    sendNotification();
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

    private void sendNotification() {
        Toast.makeText(this, "Reminder Set!", Toast.LENGTH_SHORT).show();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, notificationTime.getHour());
        cal.set(Calendar.MINUTE, notificationTime.getMinute());
        cal.set(Calendar.SECOND, 0);

        //System.out.println("The current time is: " + System.currentTimeMillis());
        //System.out.println("The time the notification should go off at is: " + cal.getTimeInMillis());

        Intent intent = new Intent(JournalNotificationActivity.this, ReminderBroadcast.class);
        intent.putExtra("HABIT_ID", habitId);
        intent.putExtra("HABIT_NAME", habitName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(JournalNotificationActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
}
