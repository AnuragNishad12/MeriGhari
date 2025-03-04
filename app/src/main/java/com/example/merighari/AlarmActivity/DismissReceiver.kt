package com.example.merighari.AlarmActivity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

//class DismissReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//
//        AlarmReceiver.Companion.mediaPlayer?.stop()
//        AlarmReceiver.Companion.mediaPlayer?.release()
//        AlarmReceiver.Companion.mediaPlayer = null
//
//
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.cancel(1)
//    }
//}

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP_ALARM
        }
        context.startService(serviceIntent)

        // Cancel notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(AlarmService.NOTIFICATION_ID)
    }
}
