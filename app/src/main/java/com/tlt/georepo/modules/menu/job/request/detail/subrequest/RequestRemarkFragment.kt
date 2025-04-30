package com.tlt.georepo.modules.menu.job.request.detail.subrequest

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
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestDetailItem
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestProfileItem
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestRemarkAdapter
import com.tlt.georepo.modules.menu.job.request.detail.common.RequestRemarkItem
import kotlinx.android.synthetic.main.fragment_req_remark.*

class RequestRemarkFragment : BaseFragment() {

    var RemarkListData : List<RequestRemarkItem>? = null
    var ProfileListData: List<RequestProfileItem>? = null
    var DetailListData: List<RequestDetailItem>? = null
    private val SCROLL_POSITION_STATE = "scroll_position"

    private val contractStatus by lazy {
        arguments?.getString(CONTRACT_STATUS) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RequestMainViewModel::class.java)
    }

    override fun onSupportInvisible() {

        super.onSupportInvisible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_req_remark, container, false)
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
            RemarkListData =
                RequestMainDetailActivity.jobRequestRemarkListData
            ProfileListData = RequestMainDetailActivity.jobRequestProfileListData
            DetailListData = RequestMainDetailActivity.jobRequestDetailListData
            txt_cust_name.text = this!!.ProfileListData!![0].cUSTOMERNAME
            txt_reg_no.text = this!!.DetailListData!![0].rEGISTERNO + " " +  this!!.DetailListData!![0].rEGISTERPROVINCE
            if (RemarkListData!!.count()>0) {

                if (RemarkListData!!.isNotEmpty()) {
                    recycler_view.visible()
                    recycler_view.adapter = RequestRemarkAdapter(onNotifyListener)
                    val adapter = recycler_view.adapter as RequestRemarkAdapter
                    adapter.updateItems(RemarkListData!!)
                } else {
                    recycler_view.gone()
                }

            }
        }catch (e : Exception){

            Log.e("RemarkFragment e" ,  e.stackTrace.toString())
        }
    }

    private val onNotifyListener = object : RequestRemarkAdapter.Listener {
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        private const val CONTRACT_STATUS = "contractStatus"

        fun newInstance() = RequestRemarkFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }
}
