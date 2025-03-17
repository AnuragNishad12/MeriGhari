package com.example.merighari.Pages.Games

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.merighari.AlarmActivity.AlarmService
import com.example.merighari.R
import kotlin.random.Random

class RabbitGame : AppCompatActivity() {
    private lateinit var scoreText: TextView
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())
    private val holeIds = arrayOf(
        R.id.rabbit1, R.id.rabbit2, R.id.rabbit3, R.id.rabbit4, R.id.rabbit5, R.id.rabbit6
    )
    private var currentRabbit: ImageView? = null

    private val rabbitRunnable = object : Runnable {
        override fun run() {
            currentRabbit?.visibility = ImageView.GONE

            val randomHoleIndex = Random.nextInt(holeIds.size)
            val newRabbit = findViewById<ImageView>(holeIds[randomHoleIndex])

            newRabbit.visibility = ImageView.VISIBLE
            currentRabbit = newRabbit

            newRabbit.setOnClickListener {
                score++
                scoreText.text = "Score: $score"
                newRabbit.visibility = ImageView.GONE

                // Check if score reaches 20
                if (score == 20) {
                    stopAlarmService()
                }
            }

            // Run again after 1 second
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rabbit_game)
        supportActionBar?.hide()

        scoreText = findViewById(R.id.scoreText)

        handler.post(rabbitRunnable)
    }

    private fun stopAlarmService() {
        val serviceIntent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP_ALARM
        }
        startService(serviceIntent)
        showGameOverDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(rabbitRunnable)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacks(rabbitRunnable)
        finish()
    }


    private fun showGameOverDialog() {
        handler.removeCallbacks(rabbitRunnable)

        val dialogView = layoutInflater.inflate(R.layout.dialog_game_over, null)
        val dialog = Dialog(this)

        dialog.setContentView(dialogView)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val messageText = dialogView.findViewById<TextView>(R.id.gameOverMessage)
        val okButton = dialogView.findViewById<Button>(R.id.okButton)


        messageText.text = "Congratulations! You scored $score points.\nSee you next time!"

        okButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

}
