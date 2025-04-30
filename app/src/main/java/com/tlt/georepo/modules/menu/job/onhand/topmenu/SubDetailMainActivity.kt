package com.tlt.georepo.modules.menu.job.onhand.topmenu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.widget.Toast
import me.yokeyword.fragmentation.SupportFragment
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.modules.menu.job.onhand.JobOnHandBtnSheetActivity
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandDetailItem
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandProfileItem
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandRemarkItem
import com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail.SubDetailFragment
import com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail.SubMainFragment
import com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail.SubRemarkFragment
import kotlinx.android.synthetic.main.request_main_detail_activity.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*

class SubDetailMainActivity : BaseActivity() {


    private var CRRENT_LAT  : String = ""
    private var CRRENT_LNG : String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SubDetailMainViewModel::class.java)
    }

    private val defaultTabPosition by lazy {
        intent?.getIntExtra(TAB_SELECT_POSITION, 1) ?: 1
    }

    private val mainIDExtra by lazy {
        intent?.getStringExtra(MAIN_EXTRA) ?: ""
    }

    private var lastTabPosition = defaultTabPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_main_detail_activity)

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
                jobOnHandDetailListData = it.jobDetailList
                jobOnHandRemarkListData = it.jobRemarkList
                jobOnHandProfileListData = it.jobProfileList
                initTabs()
            }

        })

        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()
                try {
                    viewModel.getData(mainIDExtra , CRRENT_LAT ,CRRENT_LNG )
                } catch (e: Exception) {
                    e.message
                }
            }

        })

//        initTabs()
    }

    private fun initInstance(){

        viewModel.GetCurrentLocation()

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }
    }



    private fun initTabs() {
        val fragmentList = ArrayList<SupportFragment>()

        fragmentList.add(findFragment(SubMainFragment::class.java)
                ?: SubMainFragment.newInstance())
        fragmentList.add(findFragment(SubDetailFragment::class.java)
                ?: SubDetailFragment.newInstance())
        fragmentList.add(findFragment(SubRemarkFragment::class.java)
                ?: SubRemarkFragment.newInstance())

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
            JobOnHandBtnSheetActivity.Open(this)
        }catch (e : Exception){
            e.message
        }
    }

    companion object {
        var jobOnHandDetailListData : List<OnHandDetailItem>? = null
        var jobOnHandRemarkListData : List<OnHandRemarkItem>? = null
        var jobOnHandProfileListData :  List<OnHandProfileItem>? = null
        const val CONTRACT_POSITION = 0
        const val PAYOFF_POSITION = 1
        const val REFINANCE_POSITION = 2

        private const val CONTRACT_NO_EXTRA = "CONTRACT_NO_EXTRA"
        private const val TAB_SELECT_POSITION = "tablayout_current_position"
        private const val CONTRACT_STATUS = "contract_status"
        private const val MAIN_EXTRA = ""

        fun start(context: Context?,
                  mainID: String ,
                  position: Int = CONTRACT_POSITION,
                  contractStatus: String = "") {
            val intent = Intent(context, SubDetailMainActivity::class.java).apply {
                putExtra(TAB_SELECT_POSITION, position)
                putExtra(MAIN_EXTRA, mainID)
                putExtra(CONTRACT_STATUS, contractStatus)
            }

            context?.startActivity(intent)
        }

        fun Open(context: Context? , mainID : String) {
            val intent = Intent(context, SubDetailMainActivity::class.java).apply {
                putExtra(MAIN_EXTRA, mainID)
            }
            context?.startActivity(intent)
        }

    }
}
