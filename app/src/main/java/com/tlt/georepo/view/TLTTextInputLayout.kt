package com.tlt.georepo.view


import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import com.tlt.georepo.R

class TLTTextInputLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : TextInputLayout(context, attrs, defStyleAttr) {

    private val fonts = hashMapOf(0 to R.font.custom_regular,
            1 to R.font.custom_bold,
            2 to R.font.custom_bold)
    private var useFont = fonts[0]

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TLTTextInputLayout, 0, 0)
            useFont = fonts[typedArray.getInt(R.styleable.TLTTextInputLayout_fontType, 0)]

            typedArray.recycle()
        }

        initTypeface()
    }

    private fun initTypeface() {
        val typeface = ResourcesCompat.getFont(context, useFont!!)
        setTypeface(typeface)
    }
}