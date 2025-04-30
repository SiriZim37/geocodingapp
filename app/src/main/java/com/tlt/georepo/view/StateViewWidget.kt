package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.layout_state_view_loading.view.*
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.visible
import java.lang.ref.WeakReference

class StateViewWidget : FrameLayout {

    private val childViews = ArrayList<WeakReference<View?>>()

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.widget_state_view, this, true)

        attrs?.let {

        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        childViews.add(WeakReference(child))

//        if (childViews.size > 2) {
//            throw Exception("StateViewWidget should has 1 child !!")
//        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        val stateViewLayout = childViews.first().get() as ViewGroup
        val loadingLayout = stateViewLayout.getChildAt(0)

        loadingLayout.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                loadingLayout.loading.progress = progress.toFloat() * 0.001f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
//        if (childViews.isEmpty()) {
//            return
//        }
//
//        childViews.first().get()?.gone()
    }

    fun loading() {
        hideContent()
        showLoading()
    }

    private fun hideContent() {
        childViews[1].get()?.gone()
    }

    private fun showContent() {
        childViews[1].get()?.visible()
    }

    private fun showLoading() {
        val stateViewLayout = childViews.first().get() as ViewGroup
        val loadingLayout = stateViewLayout.getChildAt(0)
        val connectionErrorLayout = stateViewLayout.getChildAt(1)
        val noDataLayout = stateViewLayout.getChildAt(2)

        loadingLayout.visible()
        connectionErrorLayout.gone()
        noDataLayout.gone()
    }

    private fun hideLoading() {
        val stateViewLayout = childViews.first().get() as ViewGroup
        val loadingLayout = stateViewLayout.getChildAt(0)

        loadingLayout.gone()
    }
}