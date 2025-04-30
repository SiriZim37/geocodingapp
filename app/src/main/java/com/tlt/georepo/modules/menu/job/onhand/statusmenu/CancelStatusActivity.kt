package com.tlt.georepo.modules.menu.job.onhand.statusmenu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.dialog.NormalDialogFragment
import com.tlt.georepo.common.extension.*
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.onhand.JobOnHandBtnSheetActivity
import com.tlt.georepo.util.LocaleManager
import kotlinx.android.synthetic.main.activity_onhand_job_cancel.*
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*

class CancelStatusActivity : BaseActivity(), NormalDialogFragment.Listener {
    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""
    private lateinit var masterActionCode : ArrayList<CancelStatusViewModel.RejectJob>
    private val mainID by lazy {
        intent.getStringExtra("MAIN_ID") ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CancelStatusViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onhand_job_cancel)

        initViewModel()
        initInstances()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {

            it?.let{
                masterActionCode = it
                spinner_data_cancel.setItems(it.map { it.MS_DESC })
            }

        })

        viewModel.whenSendToApiFailure.observe(this, Observer {
            showToast("Send Fail.")
        })

        viewModel.whenSendToApiSuccess.observe(this, Observer {
            showSuccessDialog()
        })

        btn_confirm.setOnClickListener {
            try {
                if(watcherEnableConfirm()){
                    val actionType = "REJECT"
                    var commect = edittext_detail.text.toString()
                    viewModel.sendActionJob( actionType, mainID , CRRENT_LAT , CRRENT_LNG , commect)
                }
            }catch (e : Exception){
                e.message
            }
        }

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })


        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                var language = "TH"
                if ("en".equals(LocaleManager.getLanguagePref(this))){
                    language = "EN"
                }else  language = "TH"
                viewModel.getDataMaster(language)
            }

        })
    }

    private fun initInstances() {
        viewModel.GetCurrentLocation()

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

    }


    override fun onDialogConfirmClick() {
        finish()
    }

    override fun onDialogCancelClick() {

    }

    private fun showSuccessDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
                mDialogView.txt_status.text = getString(R.string.dia_req_status_successful_record)
            val mBuilder = AlertDialog.Builder(this!!).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                    mAlertDialog.dismiss()
                    MainMenuActivity.open(this)
            }
        }catch (e : Exception){
            e.message
        }
    }

    private fun showDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_successful_record)
            val mBuilder = AlertDialog.Builder(this!!).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }catch (e : Exception){
            e.message
        }
    }

    private fun watcherEnableConfirm() : Boolean {
        if (spinner_data_cancel.isSelectHint()) {
//            btn_confirm.isEnabled = false
            showPopupRequireFieldDialog()
            return false
        }
//        btn_confirm.isEnabled = true
        return true
    }


    private fun showPopupRequireFieldDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.dia_req_status_please_fill_all_field)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)

            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }catch (e : Exception){
            e.message
        }

    }

    override fun onBackPressedSupport() {
        try {
            JobOnHandBtnSheetActivity.Open(this)
        }catch (e : Exception){
            e.message
        }
    }

    fun OnClickBacktoLastActivity(){
        try {
            JobOnHandBtnSheetActivity.Open(this)
        }catch (e : Exception){
            e.message
        }
    }
    private fun showOvertimeDialog() {
        try{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            mDialogView.txt_status.text = getString(R.string.txt_time)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                /* GO TO MAIN */
                MainMenuActivity.open(this)
            }
        }catch (e : Exception){
            e.message
        }
    }

    companion object {
        fun start(context: Context? , mainID : String ){
            val intent = Intent(context, CancelStatusActivity::class.java).apply {
                putExtra("MAIN_ID", mainID)
            }
            context?.startActivity(intent)

        }
    }

}
