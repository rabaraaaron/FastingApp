package com.example.myapplication.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.R

class ReminderBroadcast: BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onReceive(context: Context, intent: Intent?) {

        val quote = MotivationalStrings(context).getRandomQuote()
        val builder = NotificationCompat.Builder(context, context.getString(
            R.string.notifications_channel))
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Motivational Reminder")
            .setContentText(quote)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(quote))

        val notificationManger = NotificationManagerCompat.from(context)
        notificationManger.notify(context.getString(R.string.notifications_channel).toInt(), builder.build())
    }

}