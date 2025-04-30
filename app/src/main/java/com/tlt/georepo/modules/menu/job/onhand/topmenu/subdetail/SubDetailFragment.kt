package com.tlt.georepo.modules.menu.job.onhand.topmenu.subdetail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseFragment
import com.tlt.georepo.modules.menu.job.onhand.common.OnHandDetailItem
import com.tlt.georepo.modules.menu.job.onhand.topmenu.SubDetailMainActivity
import kotlinx.android.synthetic.main.fragment_req_detail.*

class SubDetailFragment : BaseFragment(){
    var DetailListData : List<OnHandDetailItem>? = null
    protected var mGoogleMap: GoogleMap? = null


    private val SCROLL_POSITION_STATE = "scroll_position"

    private val contractStatus by lazy {
        arguments?.getString(CONTRACT_STATUS) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SubDetailViewModel::class.java)
    }

    override fun onSupportInvisible() {

        super.onSupportInvisible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sub_onhand_detail, container, false)
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
            DetailListData = SubDetailMainActivity.jobOnHandDetailListData
            if (DetailListData!!.count()>0){
                var items  = DetailListData!![0]
//                txt_branch.text = items.bRANCH
                txt_chasis_no.text = items.cHASISNO
                txt_car_color.text = items.cOLORDESC
                txt_contract_no.text = items.cONTRACTNO
                txt_contract_status.text = items.cONTRACTSTATUS
                txt_engine_no.text = items.eNGINENO
                txt_installment_amt.text = items.iNSTALLMENTAMT
                txt_installment_due_amt.text = items.iNSTALLMENTDUEAMT
                txt_last_due_date.text = items.lASTDUEDATE
                txt_car_model.text = items.mODELDESC
                txt_other_amount.text = items.oTHERSAMT
                txt_overdue_date_from.text = items.oVERDUEDATEFROM
                txt_overdue_date_to.text = items.oVERDUEDATETO
                txt_overdue_day.text = items.oVERDUEDAYS
                txt_overdue_item.text = items.oVERDUEITEM
                txt_overdue_item_from.text = items.oVERDUEITEMFROM
                txt_overdue_item_to.text = items.oVERDUEITEMTO
                txt_paid_date.text = items.pAIDDATE
                txt_paid_to_item.text = items.pAIDTOITEM
                txt_penalty_amount.text = items.pENALTYAMT
                txt_penalty_date.text = items.pENALTYDATE
                txt_reg_no.text = items.rEGISTERNO + " " + items.rEGISTERPROVINCE
                txt_total_ovd_amt.text = items.tOTALOVDAMT
                txt_expect_sts_date.text = items.eXPECTSUINGDATE
            }

        }catch (e : Exception){
            Log.e("DetailFragment e" , e.stackTrace.toString())
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        private const val CONTRACT_STATUS = "contractStatus"

        fun newInstance() = SubDetailFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }
}
