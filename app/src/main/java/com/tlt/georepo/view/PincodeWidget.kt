package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.widget_pincode.view.*

class PincodeWidget : FrameLayout {

    private val MAX_PINCODE = 6
    private val MIN_PINCODE = 0

    private var pincodeBox = ""
    private val pincodeViewList = ArrayList<View>()
    private lateinit var callbackPincodeComplete: (String) -> Unit

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
        LayoutInflater.from(context).inflate(R.layout.widget_pincode, this, true)

        pincodeViewList.add(pincode_one)
        pincodeViewList.add(pincode_two)
        pincodeViewList.add(pincode_three)
        pincodeViewList.add(pincode_four)
        pincodeViewList.add(pincode_five)
        pincodeViewList.add(pincode_six)
    }

    fun addPincode(pin: String) {
        if (pincodeBox.length >= MAX_PINCODE) {
            return
        }

        pincodeBox += pin

        changedUIState(true)
        sendPincodeToCallbackIfComplete()
    }

    fun removePincode() {
        if (pincodeBox.length == MIN_PINCODE) {
            return
        }

        changedUIState(false)

        pincodeBox = pincodeBox.substring(0, pincodeBox.lastIndex)
    }

    fun clearPincode() {
        pincodeBox = ""

        pincodeViewList.forEach { pincodeView ->
            pincodeView.background = ContextCompat.getDrawable(context, R.drawable.pincode_circle_shape_unactive)
        }
    }

    fun setOnPincodeCompleteListener(callback: (pincode: String) -> Unit) {
        callbackPincodeComplete = callback
    }

    private fun changedUIState(isActive: Boolean) {
        val currentIndex = pincodeBox.lastIndex
        val pincodeView = pincodeViewList[currentIndex]

        if (isActive) {
            pincodeView.background = ContextCompat.getDrawable(context, R.drawable.pincode_circle_shape_active)
        } else {
            pincodeView.background = ContextCompat.getDrawable(context, R.drawable.pincode_circle_shape_unactive)
        }
    }

    private fun sendPincodeToCallbackIfComplete() {
        if (this::callbackPincodeComplete.isInitialized
            && pincodeBox.length == MAX_PINCODE) {
            callbackPincodeComplete.invoke(pincodeBox)
        }
    }
}