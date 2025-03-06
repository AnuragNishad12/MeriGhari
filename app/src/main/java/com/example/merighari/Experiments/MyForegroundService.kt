package com.example.merighari.Experiments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.merighari.R


class MyForegroundService : Service(){


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "channel_Id")
            .setContentTitle("Foreground Services Running")
            .setContentText("This service is running from  background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
       startForeground(1,notification)

       Thread{
           try {
               for (i in 1..10){
                   Thread.sleep(1000)
                   print("Running $i ")
               }
              stopSelf()
           }catch (e : InterruptedException){
               e.printStackTrace()
           }
       }

        return START_STICKY

    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }



    fun  createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "channel_Id",
                "NotificationChannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }

    }




}