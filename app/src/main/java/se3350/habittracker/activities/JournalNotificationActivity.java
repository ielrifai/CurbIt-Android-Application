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
    private int notificationId, savedNotificationId, hour, minute;
    TimePicker notificationTime;
    Switch notificationSwitch;
    TextView habitNotificationSettings;
    TextView habitNotificationDescription;
    TextView habitNotificationTitle;
    HabitDao habitDao;
    Button saveNotificationSettingsBtn;
    TimePicker.OnTimeChangedListener onTimeChangedListener;
    PendingIntent mPendingIntent;


    Boolean notificationsOn = false;  //holds whether the user has turned on notifications
    Boolean previouslySet = false;  //holds whether the user has previously set notifications


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_notification);
        saveNotificationSettingsBtn = findViewById(R.id.save_notification_settings_button);
        notificationTime = findViewById(R.id.notification_time_picker);
        notificationTime.setIs24HourView(false);
        notificationSwitch = findViewById(R.id.habit_notification_switch);
        notificationSwitch.setChecked(notificationsOn);  //edit this to automatically set the switch to the value the user has previously specified - if no prior notification settings, set to false
        habitNotificationTitle = findViewById(R.id.habit_notification_title);
        habitNotificationDescription = findViewById(R.id.habit_notification_description);
        habitNotificationSettings = findViewById(R.id.habit_notification_title);


        saveNotificationSettingsBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Reminder Set!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(JournalNotificationActivity.this, ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(JournalNotificationActivity.this, 0, intent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            long timeAtButtonClock = System.currentTimeMillis();

            long tenSecondsInMillis = 1000 * 10;

            alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClock + tenSecondsInMillis, pendingIntent);
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
}
