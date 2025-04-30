package com.tlt.georepo.modules.pincode

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.nonuser.RegisterViewModel
import kotlinx.android.synthetic.main.activity_setup_pincode.*
import android.app.ProgressDialog
import com.tlt.georepo.R
import android.widget.TextView
import com.tlt.georepo.modules.main.InfoViewModel
import com.tlt.georepo.modules.nonuser.OTPActivity
import com.tlt.georepo.modules.nonuser.RegisterActivity
import com.tlt.georepo.modules.nonuser.TermsAndConditions
import com.tlt.georepo.util.LocaleManager


class SetupPincodeActivity : BaseActivity() {
    var isForgotPin = false
    var status = 0
    var proDialog: ProgressDialog? = null
    var isSuccess = false
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SetupPincodeViewModel::class.java)
    }
    val userID by lazy {
        intent.getStringExtra("USER_ID")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        LocaleManager.setNewLocale(this, LocaleManager.LANGUAGE_KEY_THAI)
        setContentView(R.layout.activity_setup_pincode)
        initViewModel()
        initInstances()
    }

    private fun initViewModel() {
        var checkForgot = intent.getStringExtra("ForgotPin")
        Log.e("checkForgotSetup", checkForgot)
        isForgotPin = checkForgot.equals("ForgotPin")

        viewModel.whenValidatePasswordSuccess.observe(this, Observer {
            val pincode = input_create_pin.text.toString()
            viewModel.sendPincodeToSave("1", pincode)
            proDialog!!.dismiss()
        })

        viewModel.whenPasswordNotMatchMessage.observe(this, Observer {
            proDialog!!.dismiss()
            val message = getString(R.string.error_password_not_match)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.e("whenPasswordNotMatchMessage", "")
        })

        viewModel.whenPasswordRequire6DigitsMessage.observe(this, Observer {
            proDialog!!.dismiss()
            val message = getString(R.string.error_password_require_6_digits)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.e("whenPasswordRequire6DigitsMessage", "")

        })

        viewModel.whenValidatePasswordFailure.observe(this, Observer {
            proDialog!!.dismiss()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Log.e("whenValidatePasswordFailure", "")

        })

        viewModel.whenSetupPincodeFailure.observe(this, Observer {
            Log.e("whenSetupPincodeFailure", "")
            proDialog!!.dismiss()

            //            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSetupPincodeSuccess.observe(this, Observer {
            proDialog!!.dismiss()
            Log.e("whenSetupPincodeSuccess", "")
            var dataSave = RegisterViewModel()
            val pincode = input_create_pin.text.toString()
            dataSave.getDataLogin("1", pincode)
            MainMenuActivity.open(this@SetupPincodeActivity)
        })
    }

    override fun onRestart() {
        super.onRestart()
        initViewModel()
        initInstances()
    }

    private fun initInstances() {
        var title = viewModel.setLanguage(R.string.alert_loading)
        var msg = viewModel.setLanguage(R.string.alert_waiting)
        btn_create_pin_success.setOnClickListener {
            proDialog = ProgressDialog.show(this, title, msg)
            val password = input_create_pin.text.toString().trim()
            val confirmPassword = input_create_pin_confirm.text.toString().trim()
            if (isForgotPin) {
                viewModel.validatePassword(password, confirmPassword, true)
            } else {
                viewModel.validatePassword(password, confirmPassword, false)
            }
        }
    }

//    override fun onBackPressedSupport() {
//        super.onBackPressedSupport()
//        val intent = Intent(this, OTPActivity::class.java)
//        startActivity(intent)
//    }

    companion object {
        fun open(forgotPin: String, context: Context) {
            val intent = Intent(context, SetupPincodeActivity::class.java)
            intent.putExtra("ForgotPin", forgotPin)
            context.startActivity(intent)
        }
    }
}
