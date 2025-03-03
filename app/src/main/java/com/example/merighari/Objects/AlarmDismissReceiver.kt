package com.example.merighari.Objects

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("ALARM_ID", -1L)

        // Stop the ringing service
        val stopIntent = Intent(context, AlarmRingingService::class.java)
        stopIntent.action = "STOP_ALARM"
        context.stopService(stopIntent)

        // Remove the notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(alarmId.toInt())

        // Show a toast that the alarm was dismissed
        Toast.makeText(context, "Alarm dismissed", Toast.LENGTH_SHORT).show()
    }
}