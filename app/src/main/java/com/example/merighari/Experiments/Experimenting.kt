package com.example.merighari.Experiments

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.example.merighari.R

class Experimenting : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experimenting)

        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)


        startButton.setOnClickListener{
            startForegroundService(Intent(this,MyForegroundService::class.java))
        }

        stopButton.setOnClickListener{
            stopService(Intent(this,MyForegroundService::class.java))
        }



    }
}