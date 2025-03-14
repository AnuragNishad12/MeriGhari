package com.example.merighari.AlarmActivity

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.merighari.Pages.LandingPage
import com.example.merighari.R

class AlarmService : Service() {
    private var selectedQuestionType: String? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        selectedQuestionType = intent?.getStringExtra("question_type")
        Log.d("SelectedQuestion", selectedQuestionType ?: "No question type received")

        if (intent?.action == ACTION_START_ALARM) {
            startForeground() // Ensure this happens after selectedQuestionType is set
            startAlarm()
        } else if (intent?.action == ACTION_STOP_ALARM) {
            stopAlarm()
        }

        return START_STICKY
    }

    private fun startForeground() {
        val notificationIntent = Intent(this, LandingPage::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(this, DismissReceiver::class.java).apply {
            putExtra("question_type", selectedQuestionType)
        }
        Log.d("SelectedQuestion2", selectedQuestionType ?: "No question type received")

        val dismissPendingIntent = PendingIntent.getBroadcast(
            this, 0, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "alarm_channel")
            .setContentTitle("Alarm Ringing!")
            .setContentText("Tap to dismiss the alarm.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(R.drawable.ic_launcher_foreground, "Dismiss", dismissPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(pendingIntent, true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun startAlarm() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.ringalarm)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val ACTION_START_ALARM = "com.example.ACTION_START_ALARM"
        const val ACTION_STOP_ALARM = "com.example.ACTION_STOP_ALARM"
    }
}
