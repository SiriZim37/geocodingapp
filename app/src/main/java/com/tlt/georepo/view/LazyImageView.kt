package com.tlt.georepo.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.loadImageByDrawableRes

class LazyImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.LazyImageView, 0, 0)
            val imageDrawable = typedArray.getResourceId(R.styleable.LazyImageView_imageResource, 0)

            typedArray.recycle()

            if (imageDrawable != 0) {
                loadImageByDrawableRes(imageDrawable)
            }
        }
    }
}