package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.widget_otp.view.*
import kotlinx.android.synthetic.main.activity_splashscreen.view.*
import kotlinx.android.synthetic.main.fragment_dialog_normal.view.*
import kotlinx.android.synthetic.main.widget_otp.view.*

class OTP : FrameLayout {

    private val MAX_OTP = 6
    private val MIN_OTP = 0

    private var OTPBox = ""
    private val OTPViewList = ArrayList<Button>()
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
        LayoutInflater.from(context).inflate(R.layout.widget_otp, this, true)

        OTPViewList.add(otp_one)
        OTPViewList.add(otp_two)
        OTPViewList.add(otp_three)
        OTPViewList.add(otp_four)
        OTPViewList.add(otp_five)
        OTPViewList.add(otp_six)
    }
    fun addOTP(pin: String) {
        Log.e("OTPBox.length",OTPBox.length.toString())
        if (OTPBox.length >= MAX_OTP) {
            return
        }

        OTPBox += pin

        changedUIState(true,pin)
        sendPincodeToCallbackIfComplete()
    }

    fun removePincode() {
        if (OTPBox.length == MIN_OTP) {
            return
        }

        changedUIState(false,"")

        OTPBox = OTPBox.substring(0, OTPBox.lastIndex)
    }

    fun clearPincode() {
        OTPBox = ""

        OTPViewList.forEach { otpView ->
            otpView.background = ContextCompat.getDrawable(context, R.drawable.pincode_circle_shape_unactive)
        }
    }

    fun clearOTP() {
        for(i in OTPViewList){
            i.setText("")
        }
        changedUIState(false,"")
        OTPBox = ""
    }
    

    fun setOnPincodeCompleteListener(callback: (otp: String) -> Unit) {
        callbackPincodeComplete = callback
    }

    private fun changedUIState(isActive: Boolean, pin: String) {
        val currentIndex = OTPBox.lastIndex
        val otpView = OTPViewList[currentIndex]
        if (isActive) {
            otpView.text = pin

        } else {
            otpView.text = ""
        }
    }

    private fun sendPincodeToCallbackIfComplete() {
        if (this::callbackPincodeComplete.isInitialized
            && OTPBox.length == MAX_OTP) {
            callbackPincodeComplete.invoke(OTPBox)
        }
    }
}