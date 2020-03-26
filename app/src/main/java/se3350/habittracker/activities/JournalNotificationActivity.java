package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import se3350.habittracker.AppDatabase;
import se3350.habittracker.R;
import se3350.habittracker.daos.HabitDao;
import se3350.habittracker.daos.JournalEntryDao;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;


public class JournalNotificationActivity extends AppCompatActivity {
    private int notificationId, savedNotificationId;
    TimePicker setTime;
    Switch notificationSwitch;
    JournalEntryDao journalEntryDao;
    TextView habitNotificationSettings;
    TextView habitNotificationDescription;
    TextView habitNotficationTitle;
    HabitDao habitDao;
    Button saveNotificationSettingsBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_notification);
        saveNotificationSettingsBtn = findViewById(R.id.save_notification_settings_button);
        setTime = findViewById(R.id.notification_time_picker);
        notificationSwitch = findViewById(R.id.habit_notification_switch);
        habitNotficationTitle = findViewById(R.id.habit_notification_title);
        habitNotificationDescription = findViewById(R.id.habit_notification_description);
        habitNotificationSettings = findViewById(R.id.habit_notification_title);
        createNotificationChannel();

        //get daos
        AppDatabase db = AppDatabase.getInstance(getBaseContext());
        journalEntryDao = db.journalEntryDao();
        habitDao = db.habitDao();


        Intent resultIntent = new Intent(this, JournalNotificationActivity.class);  //this currently bypasses the lock screen of the app - consider revising
        // Create the TaskStackBuilder and add the intent, which inflates the back stack

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);


        /*
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

         */



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main_notifications")  //check channel ID
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Journal Entry")
                .setContentText("Remember to fill out your daily journal!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("REMINDER"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
        //savedNotificationId = notificationId;



        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    //turn on notifications for the habit
                    System.out.println("Notifications turned on");  //remove this after we get it working
                }

                else {
                    //turn off notifications for the habit
                    System.out.println("Notifications turned off");  //remove this after we get it working
                }

            }
        });

    }


    private void createNotificationChannel() {  //this should probably be called on opening app
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("main_notifications", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            }
        }
    }

