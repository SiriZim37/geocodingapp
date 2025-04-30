package com.tlt.georepo.view

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.TextViewCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import com.tlt.georepo.R

class TLTButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatButton(context, attrs, defStyleAttr) {

    private val fonts = hashMapOf(0 to R.font.custom_regular,
            1 to R.font.custom_bold,
            2 to R.font.custom_bold)
    private var useFont = fonts.get(0)
    private var isEnableTextAutoSizing = false

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TLTButton, 0, 0)
            useFont = fonts.get(typedArray.getInt(R.styleable.TLTButton_fontType, 0))
            isEnableTextAutoSizing = typedArray.getBoolean(R.styleable.TLTButton_textAutoSizing, false)

            typedArray.recycle()
        }

        initTypeface()
        initAutosizing()
    }

    private fun initTypeface() {
        val typeface = ResourcesCompat.getFont(context, useFont!!)
        setTypeface(typeface)
    }

    private fun initAutosizing() {
        if (isEnableTextAutoSizing) {
            TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        }
    }
}