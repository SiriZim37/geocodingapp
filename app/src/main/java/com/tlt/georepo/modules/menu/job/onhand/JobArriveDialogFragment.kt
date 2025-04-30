package com.tlt.georepo.modules.menu.job.onhand

import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_sts_arrive.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.tlt.georepo.manager.api.GeoApiManager
import com.tlt.georepo.model.request.ActionJobRequest
import com.tlt.georepo.modules.main.MainMenuActivity
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.*
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.btn_yes
import kotlinx.android.synthetic.main.fragment_dialog_alert_temp.view.txt_status
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JobArriveDialogFragment : BaseDialogFragment() {
    private val apiManager by lazy { GeoApiManager.getInstance() }
    val whenOvertime = MutableLiveData<Boolean>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_sts_arrive, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initInstances() {

        btn_no.setOnClickListener {
            fragmentManager?.let {
                sendActionJob("NOT_FOUND" )

            }
        }

        btn_yes.setOnClickListener {
            fragmentManager?.let {
                sendActionJob("FOUND")

            }
        }

        btn_cancel.setOnClickListener {
            fragmentManager?.let {
                dismiss()
            }
        }
    }

    fun sendActionJob(type: String) {

        GlobalScope.launch(Dispatchers.Main) {
            try {

                val request = ActionJobRequest.build(
                    type = type ?: "",
                    main_id = arguments?.getString(MAINID, "") ?: "",
                    lat = arguments?.getString(CRRENT_LAT, "") ?: "",
                    lng = arguments?.getString(CRRENT_LNG, "") ?: "",
                    dateHold = "",
                    remark = "" ?: ""
                )
                apiManager.setActionJob(request) { isError: Boolean, result: String ->
                    if (isError) {
                        if (result.equals("overtime")) {
                            showOvertimeDialog()
                        } else {
                            showTryAgainDialog()
                        }
                        return@setActionJob
                    }
                    showSuccessDialog()
                    dismiss()
                    return@setActionJob
                }
            } catch (e: Exception) {
                e.message
            }
        }
    }
    private fun showOvertimeDialog() {
        try{
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.txt_time)
            val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GO TO MAIN */
                MainMenuActivity.open(context!!)
            }
        }catch (e : Exception){
            e.message
        }
    }



    private fun showSuccessDialog() {
        try {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_successful_record)
            val mBuilder = AlertDialog.Builder(context!!).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
            }
        } catch (e: Exception) {
            e.message
        }
    }

    private fun showTryAgainDialog() {
        try {
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_try_again)
            val mBuilder = AlertDialog.Builder(this!!.context!!).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
            }
        } catch (e: Exception) {
            e.message
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName!!
        val CRRENT_LAT = "CRRENT_LAT"
        val CRRENT_LNG = "CRRENT_LNG"
        val MAINID = "MAINID"

        fun newInstance() = JobArriveDialogFragment()

        fun show(fragmentManager: FragmentManager, mainID: String, lat: String, lng: String) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString(MAINID, mainID)
                    putString(CRRENT_LAT, lat)
                    putString(CRRENT_LNG, lng)
                }
               show(fragmentManager,TAG)
            }
        }

    }


}