package com.knowledge.newapp.ui.framgment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.knowledge.newapp.R;

public class TaskReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        int id = intent.getIntExtra("id", 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TASK_REMINDER_CHANNEL")
                .setSmallIcon(R.drawable.mic_icon)
                .setContentTitle("Task Reminder")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(id, builder.build());
    }
}