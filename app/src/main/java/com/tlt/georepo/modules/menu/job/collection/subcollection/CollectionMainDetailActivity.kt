package com.tlt.georepo.modules.menu.job.collection.subcollection

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Toast
import me.yokeyword.fragmentation.SupportFragment
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.collection.JobCollectionMainMapActivity
import com.tlt.georepo.modules.menu.job.collection.common.CollectionDetailItem
import com.tlt.georepo.modules.menu.job.collection.common.CollectionProfileItem
import com.tlt.georepo.modules.menu.job.collection.common.CollectionRemarkItem
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.view.*
import kotlinx.android.synthetic.main.request_main_detail_activity.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*

class CollectionMainDetailActivity : BaseActivity() {

    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CollectionMainDetailViewModel::class.java)
    }

    private val defaultTabPosition by lazy {
        intent?.getIntExtra(TAB_SELECT_POSITION, 1) ?: 1
    }

    private val mainIDExtra by lazy {
        intent?.getStringExtra(MAIN_EXTRA) ?: ""
    }

    private val contractStatus by lazy {
        intent?.getStringExtra(CONTRACT_STATUS) ?: ""
    }


    private var lastTabPosition = defaultTabPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.collection_main_detail_activity)

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

        viewModel.whenDataLoadedSuccess.observe(this, Observer {
            it?.let {
//                setupDataIntoViews(it)
                jobCollectionDetailListData = it.jobDetailList
                jobCollectionRemarkListData = it.jobRemarkList
                jobCollectionProfileListData = it.jobProfileList
                initTabs()
            }

        })

        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                viewModel.getData(mainIDExtra ,  CRRENT_LAT , CRRENT_LNG)
            }

        })

        viewModel.whenOvertime.observe(this , Observer {
            showOvertimeDialog()
        })
//        initTabs()
    }

    private fun initInstance(){

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

        viewModel.GetCurrentLocation()

    }


    private fun initTabs( ) {
        val fragmentList = ArrayList<SupportFragment>()

        fragmentList.add(findFragment(CollectionMainFragment::class.java)
                ?: CollectionMainFragment.newInstance())
        fragmentList.add(findFragment(CollectionDetailFragment::class.java)
                ?: CollectionDetailFragment.newInstance())
        fragmentList.add(findFragment(CollectionRemarkFragment::class.java)
                ?: CollectionRemarkFragment.newInstance())

        isSavedInstanceStateNotNull().ifFalse {
            loadMultipleRootFragment(content_container.id, 1, fragmentList[0], fragmentList[1],fragmentList[2])
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
            JobCollectionMainMapActivity.Open(this)
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
        var jobCollectionDetailListData : List<CollectionDetailItem>? = null
        var jobCollectionRemarkListData : List<CollectionRemarkItem>? = null
        var jobCollectionProfileListData :  List<CollectionProfileItem>? = null
        const val CONTRACT_POSITION = 0

        private const val TAB_SELECT_POSITION = "tablayout_current_position"
        private const val CONTRACT_STATUS = "contract_status"
        private const val MAIN_EXTRA = ""

        fun start(context: Context?,
                  mainID: String?,
                  position: Int = CONTRACT_POSITION
        ) {
            val intent = Intent(context, CollectionMainDetailActivity::class.java).apply {
                putExtra(TAB_SELECT_POSITION, position)
                putExtra(MAIN_EXTRA, mainID)
            }

            context?.startActivity(intent)
        }
    }
}
