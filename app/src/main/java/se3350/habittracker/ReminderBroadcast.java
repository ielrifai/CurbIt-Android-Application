package se3350.habittracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;

import se3350.habittracker.activities.JournalNotificationActivity;
import se3350.habittracker.activities.LoginActivity;
import se3350.habittracker.daos.HabitDao;

public class ReminderBroadcast extends BroadcastReceiver {

    HabitDao habitDao;
    int habitId;
    String name;

    @Override
    public void onReceive(Context context, Intent intent) {
        habitId = intent.getIntExtra("HABIT_ID", -1);
        name = intent.getStringExtra("HABIT_NAME");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "journal_notification")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Journaling Reminder!")
                .setContentText("Hows it going with your habit: " + name +"?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(200, builder.build());
    }
}
