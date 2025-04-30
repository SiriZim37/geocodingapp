package com.tlt.georepo.view

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

class OwaspTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private fun init() {
        isLongClickable = false
    }

}