package com.example.myapplication.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val builder = NotificationCompat.Builder(context, context.getString(
            R.string.notifications_channel))
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Scheduled Fasting Reminder")
            .setContentText("Keep going, you got this!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManger = NotificationManagerCompat.from(context)
        notificationManger.notify(200, builder.build())
    }

}