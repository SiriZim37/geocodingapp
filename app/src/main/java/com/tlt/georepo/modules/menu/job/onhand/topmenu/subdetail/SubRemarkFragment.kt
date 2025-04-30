package com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseFragment
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandDetailItem
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandProfileItem
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandRemarkAdapter
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandRemarkItem
import com.tlt.georepo.modules.menu.job.onhand.topmenu.SubDetailMainActivity
import kotlinx.android.synthetic.main.fragment_collect_remark.*

class SubRemarkFragment : BaseFragment() {

    var RemarkListData : List<OnHandRemarkItem>? = null
    var ProfileListData: List<OnHandProfileItem>? = null
    var DetailListData: List<OnHandDetailItem>? = null

    private val SCROLL_POSITION_STATE = "scroll_position"

    private val contractStatus by lazy {
        arguments?.getString(CONTRACT_STATUS) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SubRemarkViewModel::class.java)
    }

    override fun onSupportInvisible() {

        super.onSupportInvisible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_collect_remark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            nestedscrollview.scrollY = savedInstanceState.getInt(SCROLL_POSITION_STATE, 0)
        }
    }

    private fun initViewModel() {

    }

    private fun initInstances() {

        try{
            recycler_view.layoutManager = LinearLayoutManager(context)
            recycler_view.gone()
            RemarkListData = SubDetailMainActivity.jobOnHandRemarkListData
            ProfileListData = SubDetailMainActivity.jobOnHandProfileListData
            DetailListData = SubDetailMainActivity.jobOnHandDetailListData
            txt_cust_name.text = this!!.ProfileListData!![0].cUSTOMERNAME
            txt_reg_no.text = this!!.DetailListData!![0].rEGISTERNO + " " +  this!!.DetailListData!![0].rEGISTERPROVINCE
            if (RemarkListData!!.count()>0) {

                if (RemarkListData!!.isNotEmpty()) {
                    recycler_view.visible()
                    recycler_view.adapter = OnHandRemarkAdapter(onNotifyListener)
                    val adapter = recycler_view.adapter as OnHandRemarkAdapter
                    adapter.updateItems(RemarkListData!!)
                } else {
                    recycler_view.gone()
                }

            }
        }catch (e : Exception){

            Log.e("RemarkFragment e" ,  e.stackTrace.toString())
        }
    }

    private val onNotifyListener = object : OnHandRemarkAdapter.Listener {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        const val DATA_DETAIL = "DATA_DETAIL"
        private const val CONTRACT_STATUS = "contractStatus"

        fun newInstance() = SubRemarkFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }
}
