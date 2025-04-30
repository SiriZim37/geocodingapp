package com.tlt.georepo.modules.main

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.tlt.georepo.Fragment.SettingsFragment
import com.tlt.georepo.Fragment.MainJobFragment
import com.tlt.georepo.Fragment.MainJobViewModel
import com.tlt.georepo.Fragment.Navigation2Fragment
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.invisible
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.modules.menu.common.DataProfileItem
import com.tlt.georepo.modules.menu.notify.NotifyMainActivity
import com.tlt.georepo.modules.pincode.IsExitDialogFragment
import com.tlt.georepo.view.SidebarMenuWidget
import kotlinx.android.synthetic.main.activity_main.*
import me.yokeyword.fragmentation.SupportActivity
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.tlt.georepo.R
import com.tlt.georepo.common.eventbus.UpdateBadgeNotificationEvent
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.modules.pincode.AuthenPincodeActivity
import java.lang.Exception
import kotlinx.android.synthetic.main.fragment_dialog_is_exit_temp.view.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainMenuActivity : SupportActivity() {

    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var actionBar: Toolbar
    private var savedInstanceState: Bundle? = null
    private var defaultMenuPosition: Int = 0
    var LAT: Double = 0.0
    var LNG: Double = 0.0
    var flagPrivate: Boolean = false
    var proDialog: ProgressDialog? = null
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainJobViewModel::class.java)
    }

    private val isflagMenuSetting by lazy {
        intent.getBooleanExtra(FLAG_SETTING, false) ?: "false"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.savedInstanceState = savedInstanceState
        setHamburgerButton()
        initInstance()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    fun setHamburgerButton() {
        flagPrivate = DatabaseManager.getInstance().getUserInfo().flagPrivate
        actionBar = findViewById(R.id.toolbar)
        if (flagPrivate) {
            actionBar.setTitle(getString(R.string.title_main_menu_home) + " (" + resources.getString(R.string.Offline_mode )+ " )" )
        } else {
            actionBar.setTitle(getString(R.string.title_main_menu_home))
        }
        setSupportActionBar(actionBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this
            , drawer_layout
            , R.string.open
            , R.string.close
        )
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initInstance() {
        viewModel.GetCurrentLocation()
        lay_unverify.gone()
        flagPrivate = DatabaseManager.getInstance().getUserInfo().flagPrivate
        val userRegis = UserManager.getInstance()
        val flagVer = "Y"
        layout_sidebar_menu.setOnSideBarMenuInteractionListener(onSidebarMenuListener)
        layout_sidebar_menu.initMenus(name = userRegis.getUser())

        drawer_layout.closeDrawers()
        layout_sidebar_menu.setOnSidebarMenuCloseClicked(View.OnClickListener {
            drawer_layout.closeDrawers()
        })
        drawer_layout.closeDrawer(GravityCompat.START)
        drawer_layout.addDrawerListener(actionBarDrawerToggle)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (UserManager.getInstance().getUnreadNotification().toInt() > 0) {
            tv_nav_drawer_count.visible()
            tv_nav_drawer_count.text = UserManager.getInstance().getUnreadNotification()
        } else {
            tv_nav_drawer_count.gone()

        }

        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                //                (it)
                if (it.latitude.toString() != "" && it.longitude.toString() != "") {
                    LAT = it.latitude
                    LNG = it.longitude
                    viewModel.getDataProfile(LAT.toString(), LNG.toString())
                }
            }

        })

        viewModel.whenDataLoadedProfileSuccess.observe(this, Observer {
            bt_checkApproval.isClickable = true
            it?.let {
                dataProfile = it
                if (proDialog != null) {
                    proDialog!!.dismiss()
                    if (dataProfile!!.flagapprove == "Y") {
                        lay_unverify.gone()
                        if (isflagMenuSetting == true) {
                            showHideFragment(
                                findFragment(MainJobFragment::class.java)
                                    ?: MainJobFragment.newsInstance()
                            )
                        } else {
                            try {
                                loadMultipleRootFragment(
                                    layout_fragment_container.id, defaultMenuPosition,
                                    findFragment(MainJobFragment::class.java) ?: MainJobFragment.newsInstance()
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        lay_unverify.visible()
                    }
                } else {
                    val isSavedInstanceStateNotNull = savedInstanceState != null
                    if (!isSavedInstanceStateNotNull) {
                        if (it.flagapprove == "Y") {
                            lay_unverify.gone()
                            if (isflagMenuSetting == false)
                                defaultMenuPosition = 0
                            else
                                defaultMenuPosition = 1

                            loadMultipleRootFragment(
                                layout_fragment_container.id, defaultMenuPosition,
                                findFragment(MainJobFragment::class.java) ?: MainJobFragment.newsInstance(),
                                findFragment(SettingsFragment::class.java) ?: SettingsFragment.newsInstance(),
                                findFragment(Navigation2Fragment::class.java) ?: Navigation2Fragment.newsInstance()
                            )

                        } else {
                            if (isflagMenuSetting == false) {
                                defaultMenuPosition = 0
                                lay_unverify.visible()
                                text_status.text = it.approvedesc
                                loadMultipleRootFragment(
                                    layout_fragment_container.id, defaultMenuPosition,
                                    findFragment(SettingsFragment::class.java) ?: SettingsFragment.newsInstance(),
                                    findFragment(Navigation2Fragment::class.java) ?: Navigation2Fragment.newsInstance()
                                )
                            } else {
                                defaultMenuPosition = 1
                                loadMultipleRootFragment(
                                    layout_fragment_container.id, defaultMenuPosition,
                                    findFragment(MainJobFragment::class.java) ?: MainJobFragment.newsInstance(),
                                    findFragment(SettingsFragment::class.java) ?: SettingsFragment.newsInstance(),
                                    findFragment(Navigation2Fragment::class.java) ?: Navigation2Fragment.newsInstance()
                                )

                            }

                        }
                    }

                }

            }
        })

        viewModel.whenDataLoadedProfileError.observe(this, Observer {
            bt_checkApproval.isClickable = false
        })

        btn_notification.setOnClickListener() {
            NotifyMainActivity.Open(this)
        }
        bt_checkApproval.setOnClickListener {
            bt_checkApproval.isClickable = false
            var title = viewModel.setLanguage(R.string.alert_loading)
            var msg = viewModel.setLanguage(R.string.alert_waiting)
            proDialog = ProgressDialog.show(this, title, msg)
            viewModel.getDataProfile(LAT.toString(), LNG.toString())
        }
    }


    private val onSidebarMenuListener = object : SidebarMenuWidget.OnSidebarMenuInteractionListener {
        override fun onHomeClick() {
            if (dataProfile!!.flagapprove == "Y") {
                lay_unverify.gone()
                showHideFragment(
                    findFragment(MainJobFragment::class.java)
                        ?: MainJobFragment.newsInstance()
                )


            } else {
                lay_unverify.visible()
            }

        }

        override fun onSettingClick() {
            Log.e("Lexi_is_in_settings", "yay before")
            lay_unverify.gone()
            showHideFragment(
                findFragment(SettingsFragment::class.java)
                    ?: SettingsFragment.newsInstance()
            )


        }

        override fun onSignOutClick() {
            exitDialog()
        }

        override fun onAfterInteraction() {
            drawer_layout.closeDrawers()
        }
    }

    private fun addBadgeNotification(amount: String) {
        tv_nav_drawer_count.visible()
        tv_nav_drawer_count.text = amount
    }

    @SuppressLint("ResourceType")
    private fun hideBadgeNotification() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBadgeNotificationReceived(event: UpdateBadgeNotificationEvent) {
        if (UserManager.getInstance().getUnreadNotification().toInt() > 0
            && !tv_nav_drawer_count.text.equals("2")
        ) {
            tv_nav_drawer_count.visible()
            addBadgeNotification(event.message)
        } else {
            UserManager.getInstance().setZeroUnreadNotification()
            tv_nav_drawer_count.gone()
            hideBadgeNotification()
        }
    }


    override fun onBackPressedSupport() {
        exitDialog()

    }

    private fun exitDialog() {
        try {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.fragment_dialog_is_exit_temp, null)
            val mBuilder = AlertDialog.Builder(this!!).setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            mDialogView.btn_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
            mDialogView.btn_confirm.setOnClickListener {
                mAlertDialog.dismiss()
                AuthenPincodeActivity.open(this@MainMenuActivity)
            }
        } catch (e: Exception) {
            e.message
        }
    }

//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.action_settings -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_camera -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }


    companion object {
        var dataProfile: DataProfileItem? = null
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"
        private const val FLAG_SETTING = "FLAG_SETTING"

        fun open(context: Context) {
            val intent = Intent(context, MainMenuActivity::class.java)
//            intent.putExtra("USER_ID" , userid)
            context.startActivity(intent)
        }

        fun openWithClearStack(
            context: Context?,
            position: Int = 0,
            data: Bundle
        ) {
            val intent = Intent(context, MainMenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, position)
                putExtra(DATA_POSITION_EXTRA, data)
            }
            context?.startActivity(intent)
        }


        fun openAfterSetting(context: Context) {
            val intent = Intent(context, MainMenuActivity::class.java)
            intent.putExtra(FLAG_SETTING, true)
            context.startActivity(intent)
        }

    }
}
