package com.tlt.georepo.common.base

import android.app.ProgressDialog
import android.support.v4.app.Fragment
import me.yokeyword.fragmentation.SupportFragment

import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.manager.LocationManager
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.SetDataLocationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class BaseFragment : SupportFragment() {

    private lateinit var baseLoadingScreenDialogFragment: BaseLoadingScreenDialogFragment

    var progressDialog: ProgressDialog? = null

    override fun onSupportVisible() {
        super.onSupportVisible()
        SetCurrentLocation()
      // AnalyticsManager.startTime = Calendar.getInstance().time
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        SetCurrentLocation()
      // AnalyticsManager.endTime = Calendar.getInstance().time
    }

    fun toggleLoadingScreenDialog(isEnable: Boolean? = false, fragment: Fragment?) {
        if (isEnable!!) {
            showLoadingScreenDialogFragment(
                    BaseLoadingScreenDialogFragment.newInstance(),
                    fragment
            )
        } else {
            dismissLoadingScreenDialogFragment()
        }
    }

    private fun showLoadingScreenDialogFragment(dialogFragment: BaseLoadingScreenDialogFragment,
                                                fragment: Fragment?) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag(LOADING_DIALOG_TAG_FRAGMENT)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        dialogFragment.show(fragmentTransaction, LOADING_DIALOG_TAG_FRAGMENT)
        dialogFragment.setTargetFragment(fragment, 1)
    }

    private fun dismissLoadingScreenDialogFragment() {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val previous = fragmentManager!!.findFragmentByTag(LOADING_DIALOG_TAG_FRAGMENT)
        if (previous != null) {
            fragmentTransaction.remove(previous)
        }
        fragmentTransaction.commit()
    }

    fun toggleLoading(isEnable: Boolean? = false) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Loading")
            progressDialog!!.setCancelable(false)
        }

        isEnable?.ifTrue { progressDialog?.show() }
        isEnable?.ifFalse { progressDialog?.dismiss() }
    }

    fun SetCurrentLocation() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val location = LocationManager.getLastKnowLocation()
                val request = SetDataLocationRequest.build(
                    lat = location.latitude.toString(),
                    lng = location.longitude.toString()
                )
                GeoApiManager.getInstance().setDataLocation(request) { isError, result ->

                    if (isError) {
                        return@setDataLocation
                    }
                }
            } catch (error: Exception) {
                error.printStackTrace()
            }
        }
    }

    companion object {
        const val LOADING_DIALOG_TAG_FRAGMENT = "BaseLoadingScreenDialogFragment"
    }
}