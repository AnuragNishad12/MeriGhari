package com.example.merighari.Objects

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView

object AnimationHelperRightToLeft {

    fun animateImage(imageView: ImageView, duration: Long, startDelay: Long = 0) {
        imageView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val parentView = imageView.parent as? ViewGroup
                val screenWidth = parentView?.width ?: return

                val startX = screenWidth.toFloat()  // Start from right (off-screen)
                val endX = -imageView.width.toFloat()  // Move off-screen (left)

                val animator = ObjectAnimator.ofFloat(imageView, "translationX", startX, endX)
                animator.duration = duration
                animator.startDelay = startDelay
                animator.repeatCount = ValueAnimator.INFINITE
                animator.repeatMode = ValueAnimator.RESTART
                animator.interpolator = AccelerateDecelerateInterpolator() // Smooth effect

                // Add slight fade-in/out effect
                val alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0.5f, 1f, 0.5f)
                alphaAnimator.duration = duration
                alphaAnimator.repeatCount = ValueAnimator.INFINITE
                alphaAnimator.repeatMode = ValueAnimator.REVERSE

                animator.start()
                alphaAnimator.start()
            }
        })
    }


    fun animateImageLeftToRight(imageView: ImageView, duration: Long, startDelay: Long = 0) {
        imageView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val parentView = imageView.parent as? ViewGroup
                val screenWidth = parentView?.width ?: return

                val startX = -imageView.width.toFloat()  // Start from left (off-screen)
                val endX = screenWidth.toFloat()  // Move off-screen (right)

                val animator = ObjectAnimator.ofFloat(imageView, "translationX", startX, endX)
                animator.duration = duration
                animator.startDelay = startDelay
                animator.repeatCount = ValueAnimator.INFINITE
                animator.repeatMode = ValueAnimator.RESTART
                animator.interpolator = AccelerateDecelerateInterpolator()

                // Add slight fade-in/out effect
                val alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0.5f, 1f, 0.5f)
                alphaAnimator.duration = duration
                alphaAnimator.repeatCount = ValueAnimator.INFINITE
                alphaAnimator.repeatMode = ValueAnimator.REVERSE

                animator.start()
                alphaAnimator.start()
            }
        })
    }

    fun bubblyeffect(textview :ImageView){
        val animator = ObjectAnimator.ofFloat(textview, "scaleX", 1f, 1.1f, 1f)
        animator.duration = 800
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()

        val animatorY = ObjectAnimator.ofFloat(textview, "scaleY", 1f, 1.1f, 1f)
        animatorY.duration = 800
        animatorY.repeatCount = ObjectAnimator.INFINITE
        animatorY.repeatMode = ObjectAnimator.REVERSE
        animatorY.start()
    }



}
