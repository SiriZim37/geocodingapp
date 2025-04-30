package com.tlt.georepo.modules.pincode

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.invisible
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.nonuser.ForgotPincodeActivity
import com.tlt.georepo.util.LocaleManager
import com.tlt.georepo.util.PreferenceHelper
import com.tlt.georepo.view.KeyboardNumberWidget
import kotlinx.android.synthetic.main.activity_auth_pincode.*
import java.util.*

class AuthenPincodeActivity : BaseActivity() ,
   IsExitDialogFragment.Listener{
    var pincodeInvalidMessage = ""
//        ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect)
    private val TIME_TO_DISMISS_INVALID_MESSAGE = 3000L
    private var latitude: String = ""
    private var longitude: String = ""
    private val apiManager by lazy { GeoApiManager.getInstance() }
    val whenAuthSuccess = MutableLiveData<String>()
    val whenAuthFailure = MutableLiveData<String>()
    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AuthenPincodeViewModel::class.java)
    }

    private val isForgotPincodeEnabled by lazy {
        intent?.getBooleanExtra(IS_FORGOT_MENU_ENABLE_EXTRA, true) ?: true
    }
//
//    private val isClearStackEnabled by lazy {
//        intent?.getBooleanExtra(IS_CLEAR_STACK_EXTRA, false) ?: false
//    }

    override fun onRestart() {
        super.onRestart()
        Log.e("using_language" , Locale.getDefault().language)
        initViewModel()
        initInstances()
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (LocaleManager.getLanguagePref(this).equals("th")) {
            Log.e("Authen_getLanguagePref",LocaleManager.getLanguagePref(this) )
            LocaleManager.setNewLocale(this, LocaleManager.LANGUAGE_KEY_THAI)
        }
        setContentView(R.layout.activity_auth_pincode)
        initViewModel()
        initInstances()
        Log.e("onCreate","onCreate")
//        viewModel.checkFingerprintSetting()
    }

    override fun onBackPressedSupport() {
        if (isForgotPincodeEnabled) {
            IsExitDialogFragment.show(supportFragmentManager)
            return
        }
        finish()
    }


    private fun initViewModel() {
//        DatabaseManager.getInstance().saveisForgot(false)
//        Log.e("checkDataLoggedIn", DatabaseManager.getInstance().getUserInfo().userEmail)
//        viewModel.whenLoading.observe(this, Observer {
//            toggleLoadingScreenDialog(it!!)
//        })

//        viewModel.whenShowFingerprintAuth.observe(this, Observer {
//            if (!isForgotPincodeEnabled) {
//                return@Observer
//            }
//
//            FingerprintAuthDialogFragment.show(supportFragmentManager, object : FingerprintAuthDialogFragment.Listener {
//                override fun onFingerprintAuthSuccess() {
//                    viewModel.loginByFingerprint()
//                }
//            })
//        })

        viewModel.whenAuthFailure.observe(this, Observer {
            // AnalyticsManager.trackScreenError(AnalyticsScreenName.LOGIN_PINCODE_FAIL)
            txt_pincode_title.invisible()
            layout_pincode_invalid.visible()
            txt_pincode_invalid.text = it

            pincode_view.clearPincode()

            Handler().postDelayed({
                layout_pincode_invalid?.gone()
                txt_pincode_title?.visible()
            }, TIME_TO_DISMISS_INVALID_MESSAGE)
        })


        viewModel.whenAuthSuccess.observe(this, Observer { pincode: String? ->
            MainMenuActivity.open(this@AuthenPincodeActivity)
//
//            val intent = Intent().apply {
//                putExtra(AUTH_PINCODE_EXTRA, pincode)
//            }
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        })


        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
            }

        })

    }

    private fun initInstances() {
        viewModel.GetCurrentLocation()
//        isForgotPincodeEnabled.ifTrue {
//            txt_forgot_pin.visible()
//        }
//
//        isForgotPincodeEnabled.ifFalse {
//            txt_forgot_pin.gone()
//        }

        txt_forgot_pin.setOnClickListener {
            // AnalyticsManager.loginPincodeForgotClicked()
            ForgotPincodeActivity.open(this@AuthenPincodeActivity)

        }

        pincode_view.setOnPincodeCompleteListener { pincode ->
            // AnalyticsManager.loginPincodeClicked()
            viewModel.loginByPincode( pincode , CRRENT_LAT , CRRENT_LNG )
//            Log.e("setOnPincodeCompleteListener", pincode)
        }

        keyboard_number_view.setListener(object : KeyboardNumberWidget.Listener {
            override fun onNumberClick(number: String) {
                pincode_view.addPincode(number)
//                viewModel.checkLoginAttemptOverLimit()
            }

            override fun onDeleteClick() {
                pincode_view.removePincode()
            }
        })
    }

    override fun onIsExitCancelClicked() {

    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    fun localization(lang :String) {
        if(lang.equals("en")){
            LocaleManager.setNewLocale(this, LocaleManager.LANGUAGE_KEY_ENGLISH)
        }else{
            LocaleManager.setNewLocale(this, LocaleManager.LANGUAGE_KEY_THAI)
        }
    }

    companion object {
        const val AUTH_CODE = 88
        const val AUTH_PINCODE_EXTRA = "AUTH_PINCODE_EXTRA"
        private const val IS_FORGOT_MENU_ENABLE_EXTRA = "IS_FORGOT_MENU_ENABLE_EXTRA"
        private const val IS_CLEAR_STACK_EXTRA = "IS_CLEAR_STACK_EXTRA"

        fun open(  context: Context) {
            val intent = Intent(context, AuthenPincodeActivity::class.java)
//            intent.putExtra("ForgotPin" , "ForgotPin")
            context.startActivity(intent)
        }
    }
}
