package com.tlt.georepo.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sangcomz.fishbun.define.Define

import com.tlt.georepo.R
import com.tlt.georepo.common.eventbus.ChangeImageProfileEvent
import com.tlt.georepo.common.extension.loadImageByBase64
import com.tlt.georepo.common.extension.loadImageByUri
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.manager.security.ImageManager
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.SettingsViewModel
import com.tlt.georepo.modules.menu.common.DataProfileItem
import com.tlt.georepo.modules.pincode.SetupPincodeActivity
import com.tlt.georepo.modules.pincode.SetupPincodeViewModel
import com.tlt.georepo.util.ImageUtils
import com.tlt.georepo.util.LocaleManager
import com.tlt.georepo.view.SidebarMenuWidget
import kotlinx.android.synthetic.main.setting_layout.*
import me.yokeyword.fragmentation.SupportFragment
import android.provider.Settings
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.LocationRequest
import com.tlt.georepo.model.entity.UserInfo


class SettingsFragment : SupportFragment() {

    var LAT: Double = 0.0
    var LNG: Double = 0.0
    var title = SetupPincodeViewModel().setLanguage(R.string.alert_loading)
    var msg = SetupPincodeViewModel().setLanguage(R.string.alert_waiting)
    var proDialog: ProgressDialog? = null
    var DetailListData: DataProfileItem? = null
    private var isChangedImage: Boolean = false


    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.setting_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
        initViewModel()
    }


    private fun initInstance() {
        viewModel.GetCurrentLocation()
        DetailListData = MainMenuActivity.dataProfile!!
        setUpdateViewModel()
        if (DatabaseManager.getInstance().getUserInfo().flagPrivate) {
            switch_private.isChecked = true
            MainJobFragment().initDatabase(true)
        } else {
            switch_private.isChecked = false
            MainJobFragment().initDatabase(true)
        }
        Log.e("sharedPref_lang", LocaleManager.getLanguagePref(context!!))
        tv_en.setOnClickListener {
            tv_en.setTextColor(resources.getColor(R.color.greenbtn))
            tv_th.setTextColor(resources.getColor(R.color.geo_black))
            localization("en")
        }

        tv_th.setOnClickListener {
            tv_en.setTextColor(resources.getColor(R.color.geo_black))
            tv_th.setTextColor(resources.getColor(R.color.greenbtn))
            localization("th")
        }

        imageView_upper.setOnClickListener {
            ImageManager.open(this@SettingsFragment)
        }

        switch_private.setOnCheckedChangeListener { buttonView, isChecked ->

            var email = DatabaseManager.getInstance().getUserInfo().userEmail
            var phone = DatabaseManager.getInstance().getUserInfo().userPhone
            var lang = LocaleManager.getLanguagePref(activity!!)
            if (isChecked) {
                val item = UserInfo().apply {
                    flagPrivate = true
                }
                DatabaseManager.getInstance().updateFlagPrivate(item)
                Log.e("isChecked  ", DatabaseManager.getInstance().getUserInfo().flagPrivate.toString())
                proDialog = ProgressDialog.show(context, title, msg)
                viewModel.updateUserProfile(LAT.toString(), LNG.toString(), email, phone, lang, "Y")
            } else {
                val item = UserInfo().apply {
                    flagPrivate = false
                }
                DatabaseManager.getInstance().updateFlagPrivate(item)
                Log.e("isNotChecked  ", DatabaseManager.getInstance().getUserInfo().flagPrivate.toString())
                proDialog = ProgressDialog.show(context, title, msg)
                viewModel.updateUserProfile(LAT.toString(), LNG.toString(), email, phone, lang, "N")
            }
        }

        if (DatabaseManager.getInstance().getUserInfo().flagLogin) {
            if (DatabaseManager.getInstance().getUserInfo().imgProfile.isEmpty()) {
                imageView_upper.setImageDrawable(resources.getDrawable(R.drawable.ic_profile))
            } else {
                imageView_upper.loadImageByBase64(DatabaseManager.getInstance().getUserInfo().imgProfile)
            }
        } else {
            imageView_upper.setImageDrawable(resources.getDrawable(R.drawable.ic_lock))
        }
    }

    private fun initViewModel() {
        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                //                (it)
                if (it.latitude.toString() != "" && it.longitude.toString() != "") {
                    LAT = it.latitude
                    LNG = it.longitude
                    Log.e("settings_latitude", LAT.toString())
                    Log.e("settings_longitude", LNG.toString())
                }
            }
        })

        viewModel.updateProfileSuccess.observe(this, Observer {
            it?.let {
                proDialog!!.dismiss()

                MainMenuActivity.openAfterSetting(context!!)
            }
        })
    }


    fun setUpdateViewModel() {
        tv_name.text = DetailListData!!.regisname + " " + DetailListData!!.regissurname
        tv_email.text = DetailListData!!.Email
        tv_tel.text = DetailListData!!.Mobile
        tv_com.text = DetailListData!!.companyth
        tv_com_code.text = DetailListData!!.compcode

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK
            && requestCode == Define.ALBUM_REQUEST_CODE
        ) {
            val uri = data!!.getParcelableArrayListExtra<Uri>(Define.INTENT_PATH).firstOrNull()
            imageView_upper.loadImageByUri(uri)
            proDialog = ProgressDialog.show(context, title, msg)
            viewModel.updateProfileImage(uri)
            viewModel.whenDataLoaded.observe(this, Observer {
                val base64 = ImageUtils.encodeToBase64(uri!!)
                val user = UserManager().getProfile().apply {
                    this.imgProfile = base64
                }
                DatabaseManager.getInstance()
                    .updateImgProfile(user)
                MainMenuActivity.openAfterSetting(context!!)
                proDialog!!.dismiss()
            })
            return
        }
    }

    companion object {
        fun newsInstance() = SettingsFragment()
    }

    fun localization(lang: String) {
        if (lang.equals("en")) {
            LocaleManager.setNewLocale(activity!!, LocaleManager.LANGUAGE_KEY_ENGLISH)
        } else {
            LocaleManager.setNewLocale(activity!!, LocaleManager.LANGUAGE_KEY_THAI)
        }
        MainMenuActivity.openAfterSetting(context!!)
//        activity!!.finish()
//        activity!!.recreate()
    }

}


