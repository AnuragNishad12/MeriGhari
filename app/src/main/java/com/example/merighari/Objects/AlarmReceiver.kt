package com.example.merighari.Objects

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.merighari.Pages.SetAlarmActivity
import com.example.merighari.R

// AlarmReceiver.kt
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("ALARM_ID", -1L)
        val alarmTime = intent.getStringExtra("ALARM_TIME") ?: "Alarm"

        // Acquire wake lock to keep device awake
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "AlarmApp:AlarmWakeLock"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)

        // Create notification channel
        createNotificationChannel(context)

        // Show notification
        showAlarmNotification(context, alarmId, alarmTime)

        // Start the alarm ringing service
        val serviceIntent = Intent(context, AlarmRingingService::class.java)
        serviceIntent.putExtra("ALARM_ID", alarmId)
        serviceIntent.putExtra("ALARM_TIME", alarmTime)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }

        // Release wake lock
        wakeLock.release()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Notifications"
            val descriptionText = "Channel for alarm notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("ALARM_CHANNEL", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build())
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showAlarmNotification(context: Context, alarmId: Long, alarmTime: String) {
        // Intent to dismiss the alarm
        val dismissIntent = Intent(context, AlarmDismissReceiver::class.java).apply {
            putExtra("ALARM_ID", alarmId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId.toInt() + 1000, // Unique request code
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent to open the app
        val contentIntent = Intent(context, SetAlarmActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            alarmId.toInt(), // Unique request code
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, "ALARM_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Create this drawable resource
            .setContentTitle("Alarm Ringing")
            .setContentText(alarmTime)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(contentPendingIntent, true)
            .setOngoing(true)
            .addAction(R.drawable.ic_launcher_foreground, "Close", dismissPendingIntent) // Create this drawable resource
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(false)

        // Show the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(alarmId.toInt(), builder.build())
    }
}