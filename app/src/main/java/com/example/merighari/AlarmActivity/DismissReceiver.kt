package com.example.merighari.AlarmActivity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.merighari.Pages.GeneralQuestion

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
//
//
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.cancel(AlarmService.NOTIFICATION_ID)
//        val activityIntent = Intent(context, GeneralQuestion::class.java)
//        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(activityIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(AlarmService.NOTIFICATION_ID)
    }
}
