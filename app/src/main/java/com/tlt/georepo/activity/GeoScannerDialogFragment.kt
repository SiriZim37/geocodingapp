package com.tlt.georepo.activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.journeyapps.barcodescanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.annotations.Nullable


class GeoScannerDialogFragment : BaseDialogFragment()  {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.geo_custom_dialog_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        try {



            run {
                IntentIntegrator
                    .forSupportFragment(this)
                    .setCaptureActivity(GeoCaptureScannerQRcodeActivity::class.java)
                    .setPrompt("Scan a QR code")
                    .setOrientationLocked(false)
                    .setTimeout(20000)
                    .initiateScan()
            }



        }catch (e: Exception ){

        }

    }



    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }


    //////////


    companion object {
        private val TAG = this::class.java.simpleName!!

        fun newInstance() = GeoScannerDialogFragment()

        fun show(fragmentManager: FragmentManager) {
            newInstance().apply {
                show(fragmentManager, TAG)
            }
        }
    }

}

