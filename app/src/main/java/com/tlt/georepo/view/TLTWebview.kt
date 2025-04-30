package com.tlt.georepo.view

import android.content.Context
import android.support.v4.view.MotionEventCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebChromeClient
import android.webkit.WebView

class TLTWebview @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : WebView(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEventCompat.findPointerIndex(event, 0) == -1) {
            return super.onTouchEvent(event)
        }

        if (event.pointerCount >= 2) {
            requestDisallowInterceptTouchEvent(true)
        } else {
            requestDisallowInterceptTouchEvent(false)
        }

        return super.onTouchEvent(event)
    }

    fun enableSupportZoom() {
        settings.apply {
            builtInZoomControls = true
            displayZoomControls = false
        }

        webChromeClient = WebChromeClient()
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
        requestDisallowInterceptTouchEvent(true)
    }
}