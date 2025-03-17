package com.example.merighari.AlarmActivity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.merighari.Pages.Games.GameTypes
import com.example.merighari.Pages.PuzzleActivity
import com.example.merighari.Pages.QuestionTypes

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
        Log.d("DismissReceiver", "Broadcast received!")

        val questionType = intent.getStringExtra("question_type")
        Log.d("DismissReceiver", "Question Type: $questionType")

        val activityIntent = when (questionType) {
            "generalQuestion" -> Intent(context, QuestionTypes::class.java)
            "gameType" -> Intent(context, GameTypes::class.java)
            "puzzleType" -> Intent(context, PuzzleActivity::class.java)
            else -> null
        }

        activityIntent?.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
            Log.d("DismissReceiver", "Starting activity: ${it.component?.className}")
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(AlarmService.NOTIFICATION_ID)
    }

}
