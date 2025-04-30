package com.tlt.georepo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import com.tlt.georepo.common.base.BaseActivity
import android.content.Intent
import android.net.Uri
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tlt.georepo.R
import com.tlt.georepo.util.PdfUtils
import kotlinx.android.synthetic.main.geo_upload_multifile.*


class GeoUploadFileActivity  : BaseActivity(){


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(GeoUploadFileViewModel::class.java)
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.geo_upload_multifile)

        btnUpload.setOnClickListener {
            val intent = Intent()
                .setType("application/pdf")
                .setAction(Intent.ACTION_GET_CONTENT)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            if (selectedFile != null) {
                val selectedFileURI = data.data
                openFile(selectedFileURI)
            }


        }
    }

    fun openFile(selectedFile : Uri) {

//        PdfUtils.openPdf(this@GeoUploadFileActivity , selectedFile)
        userGrantPermission(this@GeoUploadFileActivity ) {
           var str =  PdfUtils.ExportPdfbase64( this@GeoUploadFileActivity  , selectedFile )

            Strbase64.text = str
        }


    }

    private fun userGrantPermission(activity: Activity?, callback: () -> Unit) {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        return
                    }

                    callback.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
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
            val intent = Intent(context, GeoUploadFileActivity::class.java)
            context.startActivity(intent)
        }
    }

}