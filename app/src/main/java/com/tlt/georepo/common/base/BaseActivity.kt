package com.tlt.georepo.common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.StringRes
import android.view.inputmethod.InputMethodManager
import me.yokeyword.fragmentation.SupportActivity
import com.tlt.georepo.R
import com.tlt.georepo.common.eventbus.SendLocationEvent
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.lifecycleobserver.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@SuppressLint("Registered")
open class BaseActivity : SupportActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BaseViewModel::class.java)
    }

    private lateinit var baseLoadingScreenDialogFragment: BaseLoadingScreenDialogFragment

    private var savedInstanceState: Bundle? = null
    private lateinit var progressDialog: ProgressDialog
    private val pushPopupLifeCycleObserver = PushPopupLifeCycleObserver(this)
    private val reAuthLifeCycleObserver = ReAuthLifeCycleObserver(this)
    private val reOpenLifeCycleObserver = ResumingAppLifeCycleObserver(this)
    private val noInternetLifeCycleObserver = NoInternetLifeCycleobserver(this)
    private val LocationLifeCycleObserver = LocationLifeCycleObserver(this)

    override fun onResume() {
        super.onResume()
        viewModel.SetCurrentLocation()
//      // AnalyticsManager.startTime = Calendar.getInstance().time
    }

    override fun onPause() {
        super.onPause()
        viewModel.SetCurrentLocation()
//      // AnalyticsManager.endTime = Calendar.getInstance().time
    }

    override fun onBackPressedSupport() {
        viewModel.SetCurrentLocation()
//        super.onBackPressedSupport()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.savedInstanceState = savedInstanceState

        lifecycle.addObserver(pushPopupLifeCycleObserver)
        lifecycle.addObserver(reAuthLifeCycleObserver)
        lifecycle.addObserver(reOpenLifeCycleObserver)
        lifecycle.addObserver(noInternetLifeCycleObserver)
        lifecycle.addObserver(LocationLifeCycleObserver)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(pushPopupLifeCycleObserver)
        lifecycle.removeObserver(reAuthLifeCycleObserver)
        lifecycle.removeObserver(reOpenLifeCycleObserver)
        lifecycle.removeObserver(noInternetLifeCycleObserver)
        lifecycle.addObserver(LocationLifeCycleObserver)

        super.onDestroy()
    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(LocalizeManager.initDefaultLocalize(base))
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun isSavedInstanceStateNotNull() = savedInstanceState != null

    fun toggleLoadingScreenDialog(isEnable: Boolean? = false) {
        if (isEnable!!) {
            showLoadingScreenDialogFragment(BaseLoadingScreenDialogFragment.newInstance())
        } else {
            dismissLoadingScreenDialogFragment()
        }
    }

    fun toggleLoading(isEnable: Boolean? = false,
                      @StringRes description: Int = R.string.loading) {
        if (!this::progressDialog.isInitialized) {
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage(getString(description))
            progressDialog.setCancelable(false)
        }

        isEnable?.ifTrue { progressDialog.show() }
        isEnable?.ifFalse { progressDialog.dismiss() }
    }

    private fun showLoadingScreenDialogFragment(dialogFragment: BaseLoadingScreenDialogFragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(Companion.LOADING_DIALOG_TAG_ACTIVITY)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        dialogFragment.show(fragmentTransaction, Companion.LOADING_DIALOG_TAG_ACTIVITY)
    }

    private fun dismissLoadingScreenDialogFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val previous = supportFragmentManager.findFragmentByTag(Companion.LOADING_DIALOG_TAG_ACTIVITY)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.commit()
    }

    fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun SendLocationToServer(event: SendLocationEvent) {
        viewModel.SetCurrentLocation()
    }

    companion object {
        const val LOADING_DIALOG_TAG_ACTIVITY = "BaseLoadingScreenDialogFragmentActivity"
    }
}