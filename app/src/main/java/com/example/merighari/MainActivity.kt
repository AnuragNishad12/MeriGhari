package com.example.merighari

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.merighari.Objects.AnimationHelperRightToLeft
import com.example.merighari.Pages.GeneralQuestion
import com.example.merighari.Pages.SetAlarmActivity
import com.example.merighari.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    val runnable = object : Runnable {
        override fun run() {
            val currentTime = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())
            binding.autoclock.text = currentTime
            handler.postDelayed(this, 1000) // Update every second
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.autoclock.setShadowLayer(15f, 0f, 0f, Color.YELLOW)
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        for (i in 1..20) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.starsimage)

            val size = (50..150).random()
            val layoutParams = FrameLayout.LayoutParams(size, size)
            imageView.layoutParams = layoutParams

            val xPos = (0 until screenWidth - size).random()
            val yPos = (0 until screenHeight - size).random()
            imageView.x = xPos.toFloat()
            imageView.y = yPos.toFloat()

            Log.d("ImageView", "Adding image at x=$xPos, y=$yPos") // Debugging

            frameLayout.addView(imageView)
            startAnimation(imageView)

            handler.post(runnable)

    }




            AnimationHelperRightToLeft.bubblyeffect(binding.moonimage)

        binding.generalcard.setOnClickListener{
            startActivity(Intent(this@MainActivity,SetAlarmActivity::class.java))
        }
        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        val gifUrl = "https://i.pinimg.com/originals/f4/34/e8/f434e86cdc40ef068b7783d150f8a65c.gif"

        Glide.with(this)
            .asGif()
            .load(gifUrl)
            .into(gifImageView)



    }


    private fun startAnimation(imageView: ImageView) {
        val fadeInOut = AlphaAnimation(0f, 1f).apply {
            duration = (500..1500).random().toLong()
            repeatMode = AlphaAnimation.REVERSE
            repeatCount = AlphaAnimation.INFINITE
        }

        val scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.5f, 1f).apply {
            duration = (1000..2000).random().toLong()
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }

        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.5f, 1f).apply {
            duration = (1000..2000).random().toLong()
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }

        imageView.startAnimation(fadeInOut)
        scaleX.start()
        scaleY.start()
    }
}