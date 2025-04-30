package com.tlt.georepo.modules.nonuser

import android.app.AlertDialog
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.substring
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.sangcomz.fishbun.BaseActivity
import com.tlt.georepo.R
import com.tlt.georepo.manager.api.GeoApiConnector
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.model.request.NonCustomerRequest
import com.tlt.georepo.model.response.ResponseFromService
import com.tlt.georepo.model.response.ResponseStatus
import com.tlt.georepo.model.response.ResultsGetBanner
import com.tlt.georepo.modules.pincode.SetupPincodeViewModel
import com.tlt.georepo.util.PreferenceHelper
import kotlinx.android.synthetic.main.forgot_pincode_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPincodeActivity : BaseActivity() {
//    private var results: List<Result>? = null
    private var results: List<ResultsGetBanner>? = null
    val context: Context? = null
    var key: String = ""
    var proDialog: ProgressDialog? = null
    var title = ForgotPincodeViewModel().setLanguage(R.string.alert_loading)
    var msg = ForgotPincodeViewModel().setLanguage(R.string.alert_waiting)
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ForgotPincodeViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_pincode_layout)
        initViewModel()
        initInstances()
    }

    private fun initViewModel() {
//        DatabaseManager.getInstance().saveisForgot(true)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initInstances() {
        var phone = DatabaseManager.getInstance().getUserInfo().userPhone
        var phone_front = phone.substring(0,3)
        var phone_last = phone.substring(phone.length -4 , phone.length)
        forgot_phone.text = phone_front + "-xxx-" + phone_last
        btn_signup_forgot.setOnClickListener {

            if (isValidPhone(edit_phone.text)) {
                proDialog = ProgressDialog.show(this, title, msg)
                viewModel.apiSendOTP(edit_phone.text.toString())
            } else {
                showAlert()
            }
        }

        viewModel.isSuccessOTP.observe(this , Observer {
            proDialog!!.dismiss()
            OTPActivity.open("ForgotPin",this@ForgotPincodeActivity)
        })
    }



    fun isValidPhone(phone: CharSequence): Boolean {
        return !(TextUtils.isEmpty(phone) || phone.length != 10)
    }

    fun showAlert() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Please try again")
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                edit_phone.setText("")
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Invalid Phone")
        alert.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy" , "onDestroy")
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("onStop", "onStop")
    }



    companion object {
        
        fun open(context: Context) {
            val intent = Intent(context, ForgotPincodeActivity::class.java)
            context.startActivity(intent)
        }
    }
}


