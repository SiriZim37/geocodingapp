package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_notification.view.*
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.visible

class NotificationWidget : FrameLayout {

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
        LayoutInflater.from(context).inflate(R.layout.widget_notification, this, true)
    }

    fun setNotification(badge: Int) {
        when (badge) {
            0 -> badge_txt.gone()
            else -> {
                badge_txt.visible()
                badge_txt.text = badge.toString()
            }
        }
    }

}