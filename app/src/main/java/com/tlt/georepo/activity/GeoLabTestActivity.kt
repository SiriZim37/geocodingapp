package com.tlt.georepo.activity

import android.content.Context
import android.os.Bundle
import com.tlt.georepo.common.base.BaseActivity
import android.content.Intent
import com.tlt.georepo.R
import com.tlt.georepo.modules.main.MainMenuActivity
import kotlinx.android.synthetic.main.geo_labtest.*


class GeoLabTestActivity  : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_labtest)

        btnUpload.setOnClickListener {
            GeoUploadFileActivity.open(this)
           }

        btnScanarcode.setOnClickListener {
            GeoScannerQRcodeActivity.open(this)
        }

        btnsign.setOnClickListener {
            GeoSignatureActivity.open(this)
        }

        deepLinkWV.setOnClickListener {
            GeoDeepLinkWebViewActivity.open(this)
        }


    }

    override fun onBackPressedSupport() {
        try {
            MainMenuActivity.open(this)
        }catch (e : Exception){
            e.message
        }
    }

    companion object {
        fun open( context: Context) {
            val intent = Intent(context, GeoLabTestActivity::class.java)
            context.startActivity(intent)
        }
    }

}