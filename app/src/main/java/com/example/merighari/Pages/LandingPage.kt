package com.example.merighari.Pages

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.merighari.MainActivity
import com.example.merighari.Objects.AnimationHelperRightToLeft
import com.example.merighari.R
import com.example.merighari.databinding.ActivityLandingPageBinding
import com.example.merighari.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class LandingPage : AppCompatActivity() {
    private lateinit var binding: ActivityLandingPageBinding
    private val handler = Handler(Looper.getMainLooper())
    val runnable = object : Runnable {
        override fun run() {
            val currentTime = SimpleDateFormat("hh:mm", Locale.getDefault()).format(Date())
            binding.autoclock.text = currentTime
            handler.postDelayed(this, 1000) // Update every second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val gifImageView = findViewById<ImageView>(R.id.gifimage)
        val gifImageView2 = findViewById<ImageView>(R.id.gifimage2)

        Glide.with(this)
            .asGif()
            .load(R.drawable.snowfall)
            .into(gifImageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.walking)
            .into(gifImageView2)

        binding.cardView3.setOnClickListener{
            startActivity(Intent(this@LandingPage,MainActivity::class.java))
        }





        AnimationHelperRightToLeft.animateImage(binding.animatedImage, 7000, 0)
        AnimationHelperRightToLeft.animateImage(binding.animatedImage2, 10000, 2000)  // Delay added
        AnimationHelperRightToLeft.animateImageLeftToRight(binding.animate3, 4000, 500)
        AnimationHelperRightToLeft.animateImageLeftToRight(binding.animate4, 9000, 1000)
        AnimationHelperRightToLeft.animateImage(binding.animate5, 7000, 1500)
        AnimationHelperRightToLeft.animateImageLeftToRight(binding.animate6, 9000, 2500) // Delayed
        AnimationHelperRightToLeft.animateImage(binding.animate7,6000,1000)
        AnimationHelperRightToLeft.bubblyeffect(binding.imageView6)
        AnimationHelperRightToLeft.bubblyeffect(binding.sparkling1)
        AnimationHelperRightToLeft.bubblyeffect(binding.sparkling2)



        val imageView = findViewById<ImageView>(R.id.imageView6)
        val drawable = ContextCompat.getDrawable(this, R.drawable.moonhalf)

        if (drawable != null) {
            imageView.setImageBitmap(addGlowEffect(drawable))
        }


        binding.autoclock.setShadowLayer(15f, 0f, 0f, Color.YELLOW)
        val batteryPercentage = getBatteryPercentage(this)
        binding.bettarypercentage.text = "$batteryPercentage%"
        binding.bettarypercentage.setShadowLayer(15f, 0f, 0f, Color.YELLOW)
        binding.battery.setShadowLayer(15f, 0f, 0f, Color.YELLOW)
//        Toast.makeText(this, "Battery: $batteryPercentage%", Toast.LENGTH_SHORT).show()



        val batteryStatus: Intent? = registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val temperature = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val tempCelsius = temperature / 10.0

        binding.temperature.text = "Temperature: $tempCelsius°C"
        binding.temperature.setShadowLayer(15f, 0f, 0f, Color.YELLOW)

        handler.post(runnable)
    }

    private fun addGlowEffect(drawable: Drawable): Bitmap {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.YELLOW // Change glow color
            maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER) // Adjust glow radius
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return bitmap
    }


    fun getBatteryPercentage(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }


}