package com.tlt.georepo.activity


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.tlt.georepo.common.base.BaseActivity
import android.content.Intent
import android.util.Log
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.tlt.georepo.R
import kotlinx.android.synthetic.main.geo_scanner.*
import com.readystatesoftware.chuck.internal.ui.MainActivity
import android.widget.Toast
import me.dm7.barcodescanner.zxing.ZXingScannerView


class GeoScannerQRcodeActivity  : BaseActivity(){

    var scannedResult: String = ""


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_scanner)


        btnScan.setOnClickListener {
            try {
//                GeoScannerDialogFragment.show(
//                    fragmentManager = supportFragmentManager
//                )
             run {
                     IntentIntegrator(this@GeoScannerQRcodeActivity)
                    .setCaptureActivity(GeoCaptureScannerQRcodeActivity::class.java)
                    .setPrompt("Scan a QR code")
                    .setOrientationLocked(false)
                    .setTimeout(20000)
                    .initiateScan()
              }



            }catch (e : Exception){
                e.message
            }
//            run {
//                IntentIntegrator(this@GeoScannerQRcodeActivity)
////                    .setCaptureActivity(GeoCaptureScannerQRcodeActivity.javaClass)
//                    .setPrompt("Scan a QR code")
//                    .setOrientationLocked(false)
//                    .setTimeout(20000)
//                    .initiateScan()
//            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)


        if(result != null){

            if(result.contents != null){
                scannedResult = result.contents
                txtValue.text = scannedResult
            } else {
                txtValue.text = "scan failed"
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult")

            txtValue.text = scannedResult
        }
    }
    override fun onBackPressedSupport() {
        try {
            GeoLabTestActivity.open(this)
        }catch (e : Exception){
            e.message
        }
    }

    companion object {
        fun open( context: Context) {
            val intent = Intent(context, GeoScannerQRcodeActivity::class.java)
            context.startActivity(intent)
        }
    }

}