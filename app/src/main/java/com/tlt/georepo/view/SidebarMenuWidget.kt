package com.tlt.georepo.view

import android.annotation.TargetApi
import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tlt.georepo.Fragment.SettingsFragment
import com.tlt.georepo.modules.sidebar.SidebarMenuAdapter
import com.tlt.georepo.dataclass.SidebarMenu
import com.tlt.georepo.R
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.loadImageByBase64
import com.tlt.georepo.common.extension.loadImageByUri
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.common.DataProfileItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.widget_sidebar_menu.view.*
import java.lang.NullPointerException

class SidebarMenuWidget : FrameLayout {


    private var onSidebarMenuInteractionListener: OnSidebarMenuInteractionListener? = null

    @JvmOverloads
    constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleArr: Int = 0
    ) : super(context, attrs, defStyleArr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.widget_sidebar_menu, this, true)
    }

    fun initMenus(name: String = "") {
        Log.e("SidebarMenuWidget", "SidebarMenuWidget")
        val sidebarList = ArrayList<SidebarMenu>()
//        sidebarList.add(SidebarMenu(SidebarMenu.Type.EMPTY))
        sidebarList.add(SidebarMenu(resources.getString(R.string.setting_home)) {
            onSidebarMenuInteractionListener?.onHomeClick()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(resources.getString(R.string.setting_setting)) {
            onSidebarMenuInteractionListener?.onSettingClick()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(resources.getString(R.string.setting_logout)) {
            onSidebarMenuInteractionListener?.onSignOutClick()
            onSidebarMenuInteractionListener?.onAfterInteraction()
        })
        sidebarList.add(SidebarMenu(SidebarMenu.Type.EMPTY))
        btn_register.text = DatabaseManager.getInstance().getUserInfo().userName
        val adapter = SidebarMenuAdapter(sidebarList = sidebarList)
        recycler_view.adapter = adapter


        if (DatabaseManager.getInstance().getUserInfo().flagLogin) {
            if (DatabaseManager.getInstance().getUserInfo().imgProfile.isEmpty()) {
                ic_profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile))
            } else {
                ic_profile.loadImageByBase64(DatabaseManager.getInstance().getUserInfo().imgProfile)
            }
        } else {
            ic_profile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_lock))
        }
    }

    interface OnSidebarMenuInteractionListener {
        fun onHomeClick()
        fun onSettingClick()
        fun onSignOutClick()
        fun onAfterInteraction()
    }

    fun setOnSideBarMenuInteractionListener(listener: OnSidebarMenuInteractionListener) {
        onSidebarMenuInteractionListener = listener
    }



    fun setOnSidebarMenuCloseClicked(listener: OnClickListener) {
        btn_close_sidebar.setOnClickListener(listener)

    }

}