package com.example.merighari.AlarmActivity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.merighari.R

//class AlarmReceiver : BroadcastReceiver() {
//    companion object {
//        var mediaPlayer: MediaPlayer? = null
//    }
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//
//        mediaPlayer?.release()
//        mediaPlayer = MediaPlayer.create(context, R.raw.ringalarm)
//        mediaPlayer?.isLooping = true
//        mediaPlayer?.start()
//
//
//        val dismissIntent = Intent(context, DismissReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            dismissIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val notification = NotificationCompat.Builder(context, "alarm_channel")
//            .setContentTitle("Alarm Ringing!")
//            .setContentText("Tap to dismiss the alarm.")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .addAction(R.drawable.ic_launcher_foreground, "Dismiss", pendingIntent)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_ALARM)
//            .setFullScreenIntent(pendingIntent, true) // For high-priority notifications
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .setAutoCancel(true)
//            .build()
//
//        notificationManager.notify(1, notification)
//    }
//}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val questionType = intent.getStringExtra("question_type")
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            action = AlarmService.ACTION_START_ALARM
            putExtra("question_type", questionType)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}