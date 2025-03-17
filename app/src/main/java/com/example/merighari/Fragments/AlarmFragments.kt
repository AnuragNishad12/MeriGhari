package com.example.merighari.Fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.merighari.Pages.SetAlarmActivity
import com.example.merighari.R
import com.example.merighari.databinding.FragmentAlarmFragmentsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmFragmentsBinding
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            val currentTime = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())
            binding.autoclock.text = currentTime
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmFragmentsBinding.inflate(inflater, container, false)

        binding.autoclock.setShadowLayer(15f, 0f, 0f, Color.YELLOW)
        val frameLayout = binding.frameLayout

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        for (i in 1..20) {
            val imageView = ImageView(requireContext())
            imageView.setImageResource(R.drawable.starsimage)

            val size = (50..150).random()
            val layoutParams = FrameLayout.LayoutParams(size, size)
            imageView.layoutParams = layoutParams

            val xPos = (0 until screenWidth - size).random()
            val yPos = (0 until screenHeight - size).random()
            imageView.x = xPos.toFloat()
            imageView.y = yPos.toFloat()

            Log.d("ImageView", "Adding image at x=$xPos, y=$yPos")

            frameLayout.addView(imageView)
            startAnimation(imageView)
        }

        handler.post(runnable)

        binding.generalcard.setOnClickListener {
            val intent = Intent(requireContext(), SetAlarmActivity::class.java)
            intent.putExtra("question_type", "generalQuestion")
            startActivity(intent)
        }

        binding.gametype.setOnClickListener {
            val intent = Intent(requireContext(), SetAlarmActivity::class.java)
            intent.putExtra("question_type", "gameType")
            startActivity(intent)
        }

        binding.puzzletype.setOnClickListener {
            val intent = Intent(requireContext(), SetAlarmActivity::class.java)
            intent.putExtra("question_type", "puzzleType")
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
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
