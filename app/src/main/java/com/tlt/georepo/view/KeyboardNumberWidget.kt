package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tlt.georepo.R
import com.tlt.georepo.view.TLTButton
import kotlinx.android.synthetic.main.widget_keyboard_number.view.*

class KeyboardNumberWidget : FrameLayout {

    private var listener: Listener? = null

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
        LayoutInflater.from(context).inflate(R.layout.widget_keyboard_number, this, true)

        pin_number_1.setOnClickListener(onNumberButtonClicked)
        pin_number_2.setOnClickListener(onNumberButtonClicked)
        pin_number_3.setOnClickListener(onNumberButtonClicked)
        pin_number_4.setOnClickListener(onNumberButtonClicked)
        pin_number_5.setOnClickListener(onNumberButtonClicked)
        pin_number_6.setOnClickListener(onNumberButtonClicked)
        pin_number_7.setOnClickListener(onNumberButtonClicked)
        pin_number_8.setOnClickListener(onNumberButtonClicked)
        pin_number_9.setOnClickListener(onNumberButtonClicked)
        pin_number_0.setOnClickListener(onNumberButtonClicked)
        pin_number_del.setOnClickListener(onDeleteButtonClicked)
    }

    fun setListener(callback: Listener) {
        listener = callback
    }

    private val onNumberButtonClicked = object : View.OnClickListener {
        override fun onClick(v: View?) {
            val view = v!! as TLTButton

            listener?.let {
                listener!!.onNumberClick(view.text.toString())
            }
        }
    }

    private val onDeleteButtonClicked = object : View.OnClickListener {
        override fun onClick(v: View?) {
            listener?.let {
                listener!!.onDeleteClick()
            }
        }
    }

    interface Listener {
        fun onNumberClick(number: String)
        fun onDeleteClick()
    }
}