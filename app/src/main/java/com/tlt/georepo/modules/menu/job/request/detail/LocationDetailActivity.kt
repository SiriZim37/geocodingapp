package com.tlt.georepo.modules.menu.location.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_location_detail.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity

class LocationDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LocationDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        initViewModel()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
//                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun setupDataIntoViews(item: LocationDetailViewModel.Model?) {
//        txt_company_name.text = item?.cusName
//        txt_distance.text = getString(R.string.location_distance, item?.distance)
//        txt_contract.text = item?.contractNo ?: ""
//        txt_reg_no.text = item?.regNo ?: ""
//        txt_unpaid_amt.text = item?.unpaidAmt ?: ""
//        txt_address.text = item?.cusAddr ?: ""
//        txt_tel.text = item?.mobileNo ?: ""

    }

    override fun onPause() {
        super.onPause()
    }

    private fun supportForStaff(it: LocationDetailViewModel.Model) {

    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, LocationDetailActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
