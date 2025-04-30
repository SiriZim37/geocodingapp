package com.tlt.georepo.manager.security

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.tlt.georepo.R


object ImageManager {

    fun open(activity: Activity,
             maxSelect : Int = 1,
             selectImageUri : ArrayList<Uri> = arrayListOf()) {
        userGrantPermission(activity) {
            FishBun.with(activity)
                    .setImageAdapter(GlideAdapter())
                    .setMaxCount(maxSelect)
                    .setSelectedImages(selectImageUri)
                    .setPickerSpanCount(4)
                    .setCamera(true)
                    .setActionBarColor(getColorPrimaryDark(activity),
                            getColorPrimaryDark(activity), false)
                    .setActionBarTitleColor(getWhiteColor(activity))
                    .startAlbum()
        }
    }

    fun open(fragment: Fragment,
             maxSelect : Int = 1,
             selectImageUri : ArrayList<Uri> = arrayListOf()) {
        userGrantPermission(fragment.activity) {
            FishBun.with(fragment)
                    .setImageAdapter(GlideAdapter())
                    .setMaxCount(maxSelect)
                    .setSelectedImages(selectImageUri)
                    .setPickerSpanCount(4)
                    .setCamera(true)
                    .setActionBarColor(getColorPrimaryDark(fragment.context),
                            getColorPrimaryDark(fragment.context), false)
                    .setActionBarTitleColor(getWhiteColor(fragment.context))
                    .startAlbum()
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

    private fun getColorPrimaryDark(context: Context?): Int {
        return ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
    }

    private fun getWhiteColor(context: Context?): Int {
        return ContextCompat.getColor(context!!, android.R.color.white)
    }
}