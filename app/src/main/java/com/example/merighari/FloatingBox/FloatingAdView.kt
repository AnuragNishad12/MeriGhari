package com.example.merighari.FloatingBox

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.example.merighari.R

class FloatingAdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var xDelta = 0f
    private var yDelta = 0f
    private val adContainer: CardView
    private val closeButton: ImageButton
    private val adContentContainer: FrameLayout

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.floating_ad_view, this, true)
        adContainer = view.findViewById(R.id.floating_ad_container)
        closeButton = view.findViewById(R.id.btn_close_ad)
        adContentContainer = view.findViewById(R.id.ad_content_container)

        // Set up touch listener for dragging
        adContainer.setOnTouchListener { _, event ->
            val x = event.rawX
            val y = event.rawY

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Record the initial touch position
                    xDelta = x - adContainer.x
                    yDelta = y - adContainer.y
                }
                MotionEvent.ACTION_MOVE -> {
                    // Move the view
                    adContainer.x = x - xDelta
                    adContainer.y = y - yDelta
                }
            }
            true
        }

        // Set up close button
        closeButton.setOnClickListener {
            (parent as? ViewGroup)?.removeView(this)
        }
    }

    // Method to set your actual ad content
    fun setAdContent(adView: View) {
        adContentContainer.removeAllViews()
        adContentContainer.addView(adView)
    }
}