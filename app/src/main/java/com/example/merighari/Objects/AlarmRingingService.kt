package com.example.merighari.Objects

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import androidx.core.app.NotificationCompat
import com.example.merighari.Pages.SetAlarmActivity
import com.example.merighari.R

class AlarmRingingService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var alarmId: Long = -1
    private var alarmTime: String = "Alarm"
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()

        // Initialize vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Acquire wake lock
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "AlarmApp:AlarmServiceWakeLock"
        )
        wakeLock?.acquire(10*60*1000L /*10 minutes*/)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_ALARM") {
            stopSelf()
            return START_NOT_STICKY
        }

        alarmId = intent?.getLongExtra("ALARM_ID", -1L) ?: -1L
        alarmTime = intent?.getStringExtra("ALARM_TIME") ?: "Alarm"

        // Create notification for foreground service
        val notification = createForegroundNotification()
        startForeground(alarmId.toInt(), notification)

        // Start playing alarm sound
        playAlarmSound()

        // Start vibration
        startVibration()

        return START_STICKY
    }

    private fun createForegroundNotification(): Notification {
        // Create notification channel if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ALARM_SERVICE_CHANNEL",
                "Alarm Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to dismiss the alarm
        val dismissIntent = Intent(this, AlarmDismissReceiver::class.java).apply {
            putExtra("ALARM_ID", alarmId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this,
            alarmId.toInt() + 2000, // Unique request code
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent to open the app
        val contentIntent = Intent(this, SetAlarmActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            alarmId.toInt() + 3000, // Unique request code
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        return NotificationCompat.Builder(this, "ALARM_SERVICE_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Alarm Ringing")
            .setContentText(alarmTime)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .addAction(R.drawable.ic_launcher_foreground, "Close", dismissPendingIntent)
            .setContentIntent(contentPendingIntent)
            .build()
    }

    private fun playAlarmSound() {
        // Initialize and start the media player with alarm sound
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(this@AlarmRingingService, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to notification sound if alarm sound fails
                try {
                    reset()
                    setDataSource(this@AlarmRingingService, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    isLooping = true
                    prepare()
                    start()
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }
    }

    private fun startVibration() {
        // Create vibration pattern
        val pattern = longArrayOf(0, 500, 500)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, 0)
        }
    }

    override fun onDestroy() {
        // Stop and release media player
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null

        // Stop vibration
        vibrator?.cancel()

        // Release wake lock
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}