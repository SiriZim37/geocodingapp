package com.tlt.georepo.modules.menu.job.request.detail.subrequest

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import me.yokeyword.fragmentation.SupportFragment
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.request.JobRequestMapActivity
import com.tlt.georepo.modules.menu.job.request.RequestDialogFragment
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestDetailItem
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestProfileItem
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestRemarkItem
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.android.synthetic.main.request_main_detail_activity.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import java.lang.Exception



class RequestMainDetailActivity : BaseActivity() , RequestDialogFragment.Listener {

    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RequestMainDetailViewModel::class.java)
    }

    private val defaultTabPosition by lazy {
        intent?.getIntExtra(TAB_SELECT_POSITION, 1) ?: 1
    }

    private val mainIDExtra by lazy {
        intent?.getStringExtra(MAIN_ID_EXTRA) ?: ""
    }

    private val contractStatus by lazy {
        intent?.getStringExtra(CONTRACT_STATUS) ?: ""
    }

    private var lastTabPosition = defaultTabPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_main_detail_activity)

        initInstance()
        initViewModel()

    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoadedFailure.observe(this, Observer {
            Toast.makeText(this, it + "lex", Toast.LENGTH_SHORT).show()
        })


        viewModel.whenSendToApiFailure.observe(this, Observer {
            it?.let{
                    showSuccessDialog("fail")
            }
        })

        viewModel.whenSendToApiLimit.observe(this, Observer {
            it?.let{
                showSuccessDialog("limit")
            }
        })

        viewModel.whenSendToApiSuccess.observe(this, Observer {
            it?.let {
                showSuccessDialog("success")
            }
        })

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })

        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                viewModel.getData(mainIDExtra , CRRENT_LAT , CRRENT_LNG)
            }

        })

        viewModel.whenDataLoadedSuccess.observe(this, Observer {
            it?.let {
                //                setupDataIntoViews(it)
                jobRequestDetailListData = it.jobDetailList
                jobRequestRemarkListData = it.jobRemarkList
                jobRequestProfileListData = it.jobProfileList
                initTabs()
            }

        })

    }

    override fun onSuccessClickListener() {
        JobRequestMapActivity.Open(this)
    }

    private fun showSuccessDialog(msgStatus : String ) {
        try {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            if(msgStatus.equals("success"))
                mDialogView.txt_status.text = getString(R.string.dia_req_status_successful_record)
            else if(msgStatus.equals("limit"))
                mDialogView.txt_status.text = getString(R.string.dia_req_status_limit)
            else
                mDialogView.txt_status.text = getString(R.string.dia_req_status_try_again)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                JobRequestMapActivity.Open(this)
            }
        }catch (e : Exception){
            e.message
        }
    }
    private fun initInstance(){


        viewModel.GetCurrentLocation()


        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

        btnAcceptJob.setOnClickListener {
            try {
                val actionType = "ACCEPT"
                viewModel.sendActionJob( actionType, mainIDExtra , CRRENT_LAT , CRRENT_LNG)
            }catch (e : Exception){
                Log.e("Onclick Accept Job" , e.message)
            }
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
    private fun initTabs() {
        val fragmentList = ArrayList<SupportFragment>()

        fragmentList.add(findFragment(RequestMainFragment::class.java)
                ?: RequestMainFragment.newInstance())
        fragmentList.add(findFragment(RequestDetailFragment::class.java)
                ?: RequestDetailFragment.newInstance())
        fragmentList.add(findFragment(RequestRemarkFragment::class.java)
                ?: RequestRemarkFragment.newInstance())

        isSavedInstanceStateNotNull().ifFalse {
            loadMultipleRootFragment(content_container.id, 1, fragmentList[0], fragmentList[1], fragmentList[2])
        }

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                lastTabPosition = tab!!.position
                showHideFragment(fragmentList[tab.position])
            }
        })

        tablayout.getTabAt(defaultTabPosition)?.select()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(TAB_SELECT_POSITION, tablayout.selectedTabPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        tablayout.getTabAt(savedInstanceState?.getInt(TAB_SELECT_POSITION, 0)!!)?.select()
    }

    override fun onBackPressedSupport() {
        OnClickBacktoLastActivity()
    }

    fun OnClickBacktoLastActivity(){
        try {
            JobRequestMapActivity.Open(this)
        }catch (e : Exception){
            e.message
        }
    }

    companion object {
        var jobRequestDetailListData : List<RequestDetailItem>? = null
        var jobRequestRemarkListData : List<RequestRemarkItem>? = null
        var jobRequestProfileListData :  List<RequestProfileItem>? = null
        const val CONTRACT_POSITION = 0

        private const val MAIN_ID_EXTRA = "MAIN_ID_EXTRA"
        private const val TAB_SELECT_POSITION = "tablayout_current_position"
        private const val CONTRACT_STATUS = "contract_status"

        fun start(context: Context?,
                  mainID: String,
                  position: Int = CONTRACT_POSITION,
                  contractStatus: String = "") {
            val intent = Intent(context, RequestMainDetailActivity::class.java).apply {
                putExtra(TAB_SELECT_POSITION, position)
                putExtra(MAIN_ID_EXTRA, mainID)
                putExtra(CONTRACT_STATUS, contractStatus)
            }

            context?.startActivity(intent)
        }
    }
}
