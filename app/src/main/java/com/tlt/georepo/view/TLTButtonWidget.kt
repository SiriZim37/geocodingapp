package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_tlt_button.view.*
import com.tlt.georepo.R

class TLTButtonWidget : FrameLayout {

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
        LayoutInflater.from(context).inflate(R.layout.widget_tlt_button, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TLTButtonWidget, 0, 0)
            val title = typedArray.getString(R.styleable.TLTButtonWidget_title) ?: ""
            val iconDrawable = typedArray.getDrawable(R.styleable.TLTButtonWidget_iconDrawable)

            typedArray.recycle()

            txt_description.text = title
            img_icon.setImageDrawable(iconDrawable)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        if (enabled) {
            layout_button.isEnabled = true
            img_icon.isEnabled = true
            txt_description.isEnabled = true
            return
        }

        layout_button.isEnabled = false
        img_icon.isEnabled = false
        txt_description.isEnabled = false
    }
}