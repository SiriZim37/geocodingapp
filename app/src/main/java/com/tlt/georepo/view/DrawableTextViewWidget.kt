package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_drawable_textview.view.*
import com.tlt.georepo.R

class DrawableTextViewWidget : FrameLayout {

    var text: String = ""
        set(value) {
            text_display.text = value
        }

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
        LayoutInflater.from(context).inflate(R.layout.widget_drawable_textview, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.DrawableTextViewWidget, 0, 0)
            val text = typedArray.getString(R.styleable.DrawableTextViewWidget_text) ?: ""
            val iconDrawable = typedArray.getDrawable(R.styleable.DrawableTextViewWidget_iconDrawable)

            typedArray.recycle()

            this.text = text

            iconDrawable?.let {
                text_fake?.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null)
            }
        }
    }
}