package com.tlt.georepo.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.widget.SeekBar
import com.tlt.georepo.R

class SliderWidget @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var minProgress = 0
    private var maxProgress = 100
    private var minimumComplete = 70
    private var callback: () -> Unit = {}

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SliderWidget, 0, 0)
            minProgress = typedArray.getInt(R.styleable.SliderWidget_minProgress, minProgress)
            maxProgress = typedArray.getInt(R.styleable.SliderWidget_maxProgress, maxProgress)
            minimumComplete = typedArray.getInt(R.styleable.SliderWidget_minimumComplete, minimumComplete)

            typedArray.recycle()
        }

        downToMin()

        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isComplete(progress)) {
                    downToMin()
                    return
                }

                upToMax({
                    callback.invoke()
                    downToMin()
                })
            }
        })
    }

    fun onCompleteListener(callback: () -> Unit) {
        this.callback = callback
    }

    private fun isComplete(progress: Int = minProgress) = progress >= minimumComplete

    private fun downToMin() {
        animateProgress(progress.toFloat(), minProgress.toFloat(), {})
    }

    private fun upToMax(callback: () -> Unit) {
        animateProgress(progress.toFloat(), maxProgress.toFloat(), callback)
    }

    private fun animateProgress(start: Float, end: Float, callbackAnimateEnd: () -> Unit) {
        val animator = ValueAnimator
                .ofFloat(start, end)
                .setDuration(300)

        animator.removeAllUpdateListeners()

        animator.addUpdateListener { animation ->
            progress = (animation.animatedValue as Float).toInt()
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                callbackAnimateEnd.invoke()
            }
        })

        animator.start()
    }
}