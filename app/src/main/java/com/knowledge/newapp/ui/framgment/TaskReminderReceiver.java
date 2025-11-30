package com.knowledge.newapp.ui.framgment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.knowledge.newapp.R;

public class TaskReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "TASK_REMINDER_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        int id = intent.getIntExtra("id", 0);

        // ===== NotificationChannel (Android 8+) =====
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Task Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for task reminders");
            channel.setSound(alarmSound, audioAttributes);
            channel.enableVibration(true);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        // ===== Notification Builder =====
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.reminder_icon)
                .setContentTitle("Task Reminder")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)      // Alarm sound
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 500, 500, 500}); // Vibration pattern

        NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (managerCompat != null) {
            managerCompat.notify(id, builder.build());
        }
    }
}
