package com.tlt.georepo.modules.nonuser

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.modules.pincode.SetupPincodeActivity
import com.tlt.georepo.modules.pincode.SetupPincodeViewModel
import com.tlt.georepo.view.KeyboardNumberWidget
import kotlinx.android.synthetic.main.request_otp_layout.*

class OTPActivity : BaseActivity() {
    var errormessage = ""
    var phone = ""
    var isForgotPin = false
    var status = 0
    var title = SetupPincodeViewModel().setLanguage(R.string.alert_loading)
    var msg = SetupPincodeViewModel().setLanguage(R.string.alert_waiting)
    var proDialog: ProgressDialog? = null
    private val TIME_TO_DISMISS_INVALID_MESSAGE = 1500L
    private val apiManager by lazy { GeoApiManager.getInstance() }
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(OTPViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_otp_layout)
        initViewModel()
        initInstances()
    }

    private fun initViewModel() {
        var checkForgot = intent.getStringExtra("ForgotPin")
        isForgotPin = checkForgot.equals("ForgotPin")
        if(!isForgotPin){
            phone = DatabaseManager.getInstance().getUserInfo().userPhone
            proDialog = ProgressDialog.show(this, title, msg)
            viewModel.apiSendOTP(phone)
        }
        Log.e("checkForgotFromForgot", checkForgot)
        viewModel.whenAuthOTPSuccess.observe(this, Observer { pincode: String? ->
            SetupPincodeActivity.open(checkForgot, this@OTPActivity)
        })

        viewModel.isSuccessOTP.observe(this, Observer {
            proDialog!!.dismiss()
        })

        viewModel.isFailedOTP.observe(this, Observer {
            proDialog!!.dismiss()
        })
        viewModel.whenAuthOTPFailure.observe(this, Observer {
            Log.e("whenAuthOTPFailure", it)
            layout_otp_invalid.visible()
            txt_otp_invalid.visible()
            txt_otp_invalid.text = it
            OTP.clearOTP()
            Handler().postDelayed({
                txt_otp_invalid?.gone()
//                txt_pincode_title?.visible()
            }, TIME_TO_DISMISS_INVALID_MESSAGE)
        })
    }

    private fun initInstances() {
        OTP.setOnPincodeCompleteListener { otp ->
            // AnalyticsManager.loginPincodeClicked()
            viewModel.verifyOTP(otp)
        }
        keyboard_otp_view.setListener(object : KeyboardNumberWidget.Listener {
            override fun onNumberClick(number: String) {
                OTP.addOTP(number)
//                viewModel.checkLoginAttemptOverLimit()
            }
            override fun onDeleteClick() {
                OTP.removePincode()
            }
        })

        tv_resendOTP.setOnClickListener {
            proDialog = ProgressDialog.show(this, title, msg)
            viewModel.apiSendOTP(phone)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val intent = Intent(this, Disclosure::class.java)
//        startActivity(intent)
        if(!isForgotPin){
        val intent = Intent(this, TermsAndConditions::class.java)
        startActivity(intent)}

    }

    companion object {
        fun open(forgotPin: String, context: Context) {
            val intent = Intent(context, OTPActivity::class.java)
            intent.putExtra("ForgotPin", forgotPin)
            context.startActivity(intent)
        }
    }


}
