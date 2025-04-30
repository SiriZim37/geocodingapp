package com.tlt.georepo.modules.menu.notify

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseActivity
import com.tlt.georepo.common.extension.ifFalse
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.model.response.NotificationJsonResponse
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.pincode.IsExitDialogFragment
import kotlinx.android.synthetic.main.notify_main_activity.*
import kotlinx.android.synthetic.main.widget_toolbar.view.*


class NotifyMainActivity : BaseActivity(),
    IsExitDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NotifyMainViewModel::class.java)
    }

    private val defaultMenuPosition by lazy {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notify_main_activity)

        initInstances()
    }

    private fun initInstances() {

        toolbar.widget_toolbar_navigation.setOnClickListener{
            OnClickBacktoLastActivity()
        }

        isSavedInstanceStateNotNull().ifFalse {
            loadMultipleRootFragment(content_container.id, 0,
                findFragment(NotifyMainFragment::class.java)
                    ?: NotifyMainFragment.newInstance()
            )
        }

    }

    override fun onBackPressedSupport() {
//        try {
//            MainMenuActivity.open(this)
//        }catch (e : Exception){
//            e.message
//        }
    }

    fun OnClickBacktoLastActivity(){
        try {
            MainMenuActivity.open(this)
        }catch (e : Exception){
            e.message
        }
    }

    override fun onIsExitCancelClicked() {

    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        BusManager.unsubscribe(this)
    }

    companion object {
        const val DATA_BY_DEEPLINK_DIALOG = "DATA_BY_DEEPLINK_DIALOG"
        const val IS_SHOW_BUTTON_IN_DIALOG = "IS_SHOW_BUTTON_IN_DIALOG"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK = "IS_SHOW_DIALOG_FROM_DEEPLINK"
        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"

        fun Open(context: Context) {
            val intent = Intent(context, NotifyMainActivity::class.java).apply {
            }
            context.startActivity(intent)
        }


        fun startByDeeplinkDialog(context: Context?,
                                  item: NotificationJsonResponse,
                                  isShowButtonDialog: Boolean = false,
                                  isShowDialog: Boolean = false
        ) {
            val intent = Intent(context, NotifyMainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MENU_POSITION_EXTRA, 0)
                putExtra(DATA_POSITION_EXTRA, Bundle().apply {
                                             putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                                             putBoolean(IS_SHOW_BUTTON_IN_DIALOG, isShowButtonDialog)
                                             putBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK, isShowDialog)})
            }
            context?.startActivity(intent)
        }
    }
}
