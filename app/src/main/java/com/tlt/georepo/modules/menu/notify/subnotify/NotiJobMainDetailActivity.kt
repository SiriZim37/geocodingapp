package com.tlt.georepo.modules.menu.notify.subnotify

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
import com.tlt.georepo.modules.menu.notify.NotifyMainActivity
import com.tlt.georepo.modules.menu.notify.common.NotiJobDetailItem
import com.tlt.georepo.modules.menu.notify.common.NotiJobProfileItem
import com.tlt.georepo.modules.menu.notify.common.NotiJobRemarkItem
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.android.synthetic.main.request_main_detail_activity.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*
import java.lang.Exception



class NotiJobMainDetailActivity : BaseActivity() , NotifyJobDialogFragment.Listener {

    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NotiJobMainDetailViewModel::class.java)
    }

    private val mainIDExtra by lazy {
        intent?.getStringExtra("MAINID") ?: ""
    }

    private val defaultTabPosition by lazy {
        intent?.getIntExtra(TAB_SELECT_POSITION, 1) ?: 1
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
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })


        viewModel.whenSendToApiFailure.observe(this, Observer {
            it?.let{
                    showSuccessDialog("fail")
            }
        })

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })


        viewModel.whenSendToApiSuccess.observe(this, Observer {
            it?.let {
                if(!it.equals("success"))
                    showSuccessDialog(it)
                else
                    showSuccessDialog("success")
            }
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
                jobNotiJobDetailListData = it.jobDetailList
                jobNotiJobRemarkListData = it.jobRemarkList
                jobNotiJobProfileListData = it.jobProfileList
                initTabs()
            }

        })

    }

    override fun onSuccessClickListener() {
        NotifyMainActivity.Open(this)
    }

    private fun showSuccessDialog(msgStatus : String ) {
        try {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_alert_temp, null)
            if(msgStatus.equals("success"))
                mDialogView.txt_status.text = getString(R.string.dia_req_status_successful_record)
            else
                mDialogView.txt_status.text = getString(R.string.dia_req_status_try_again)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
            val  mAlertDialog = mBuilder.show()
            mDialogView.btn_yes.setOnClickListener {
                mAlertDialog.dismiss()
                NotifyMainActivity.Open(this)
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

    private fun initTabs() {
        val fragmentList = ArrayList<SupportFragment>()

        fragmentList.add(findFragment(NotiJobMainFragment::class.java)
                ?: NotiJobMainFragment.newInstance())
        fragmentList.add(findFragment(NotiJobDetailFragment::class.java)
                ?: NotiJobDetailFragment.newInstance())
        fragmentList.add(findFragment(NotiJobRemarkFragment::class.java)
                ?: NotiJobRemarkFragment.newInstance())

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
            NotifyMainActivity.Open(this)
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
        var jobNotiJobDetailListData : List<NotiJobDetailItem>? = null
        var jobNotiJobRemarkListData : List<NotiJobRemarkItem>? = null
        var jobNotiJobProfileListData :  List<NotiJobProfileItem>? = null
        const val CONTRACT_POSITION = 0
        const val PAYOFF_POSITION = 1
        const val REFINANCE_POSITION = 2

        private const val MAIN_ID_EXTRA = "MAIN_ID_EXTRA"
        private const val TAB_SELECT_POSITION = "tablayout_current_position"
        private const val CONTRACT_STATUS = "contract_status"

        fun Open(context: Context , mainID : String ) {
            val intent = Intent(context, NotiJobMainDetailActivity::class.java).apply {
                putExtra("MAINID", mainID)
            }
            context.startActivity(intent)
        }

        fun start(context: Context?,
                  mainID: String,
                  position: Int = CONTRACT_POSITION,
                  contractStatus: String = "") {
            val intent = Intent(context, NotiJobMainDetailActivity::class.java).apply {
                putExtra(TAB_SELECT_POSITION, position)
                putExtra(MAIN_ID_EXTRA, mainID)
                putExtra(CONTRACT_STATUS, contractStatus)
            }

            context?.startActivity(intent)
        }
    }
}
