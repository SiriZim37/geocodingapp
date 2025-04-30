package com.tlt.georepo.activity


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.tlt.georepo.common.base.BaseActivity
import android.content.Intent
import android.view.View
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.geo_scanner.*


class GeoCaptureScannerQRcodeActivity  : CaptureActivity(){

//    var scannedResult: String = ""
//    @SuppressLint("MissingPermission")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.geo_custom_scan)
//
//
//    }

//    class SmallCaptureActivity : CaptureActivity() {
        override fun initializeContent(): DecoratedBarcodeView {
            setContentView(R.layout.geo_custom_dialog_scan)
            return findViewById<View>(R.id.zxing_barcode_scanner) as DecoratedBarcodeView
        }
//    }


    companion object {
        fun open( context: Context) {
            val intent = Intent(context, GeoCaptureScannerQRcodeActivity::class.java)
            context.startActivity(intent)
        }
    }

}