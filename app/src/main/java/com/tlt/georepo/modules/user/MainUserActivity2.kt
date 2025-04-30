//package com.tlt.georepo.modules.customer
//
//import android.arch.lifecycle.Observer
//import android.arch.lifecycle.ViewModelProviders
//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.Gravity
//import android.view.View
//import kotlinx.android.synthetic.main.activity_main_customer.*
//import kotlinx.android.synthetic.main.widget_sidebar_menu.*
//import org.greenrobot.eventbus.Subscribe
//import org.greenrobot.eventbus.ThreadMode
//import com.tlt.georepo.R
//import com.tlt.georepo.common.base.BaseActivity
//import com.tlt.georepo.common.extension.ifFalse
//import com.tlt.georepo.common.extension.loadImageByUri
//import com.tlt.georepo.common.extension.showToast
//import com.tlt.georepo.common.listener.OnHambergerClickListener
//import com.tlt.georepo.manager.BusManager
//import com.tlt.georepo.manager.db.DatabaseManager
//import com.tlt.georepo.modules.jobmenu.JobMainMenuFragment
//
//class MainCustomerActivity : BaseActivity(), OnHambergerClickListener,
//        IsExitDialogFragment.Listener {
//
//    private val sidebarMenuViewModel by lazy {
//        ViewModelProviders.of(this).get(SidebarMenuViewModel::class.java)
//    }
//
//    private val defaultMenuPosition by lazy {
//        intent.getIntExtra(MENU_POSITION_EXTRA, INSTALLMENT_MENU_POSITION)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main_customer2)
//        BusManager.subscribe(this)
//
////        val sequenceList = DatabaseManager.getInstance().getSequenceList()
////        if (sequenceList!!.isNotEmpty()) {
////            CheckStatusFromBankActivity.start(this)
////        }
//
//        // check show app tutorial state
////       sidebarMenuViewModel.checkShowTutorial()
//
//        listenerViewModel()
//        initInstances()
//    }
//
//    private fun initInstances() {
//        isSavedInstanceStateNotNull().ifFalse {
//            loadMultipleRootFragment(content_container.id, defaultMenuPosition,
//                    findFragment(JobMainMenuFragment::class.java)
//                            ?: JobMainMenuFragment.newInstance(),
//                    findFragment(TaxFragment::class.java)
//                            ?: TaxFragment.newInstance(),
//                    findFragment(InsuranceFragment::class.java)
//                            ?: InsuranceFragment.newInstance(),
//                    findFragment(ContactUsMainFragment::class.java)
//                            ?: ContactUsMainFragment.newInstance(),
//                    findFragment(SettingFragment::class.java)
//                            ?: SettingFragment.newInstance(),
//                    findFragment(NewsAndPromotionFragment::class.java)
//                            ?: NewsAndPromotionFragment.newInstance(),
//                    findFragment(QRAllCarFragment::class.java)
//                            ?: QRAllCarFragment.newInstance(),
//                    findFragment(JobRequestLocationFragment::class.java)
//                            ?: JobRequestLocationFragment.newInstance(),
//                    findFragment(DocumentDownloadFragment::class.java)
//                            ?: DocumentDownloadFragment.newInstance(),
//                    findFragment(FAQFragment::class.java)
//                            ?: FAQFragment.newInstance(),
//                    findFragment(PolicyFragment::class.java)
//                            ?: PolicyFragment.newInstance()
//            )
//        }
//
//        layout_sidebar_menu.setOnSidebarMenuCloseClicked(View.OnClickListener {
////          // AnalyticsManager.trackScreenSideBar(AnalyticsScreenName.MAIN_MENU)
////          // AnalyticsManager.mainMenuCloseClicked()
//            drawer_layout.closeDrawers()
//        })
//
//        layout_sidebar_menu.setOnSidebarMenuInteractionListener(onSidebarMenuListener)
//    }
//
//    private fun listenerViewModel() {
//        sidebarMenuViewModel.whenDataLoaded.observe(this, Observer {
//            layout_sidebar_menu.initMenus(it!!)
//        })
//
//        sidebarMenuViewModel.whenLogoutSuccess.observe(this, Observer {
//            if (it!!) {
//                AuthPincodeActivity.startWithResult(this@MainCustomerActivity)
//            }
//        })
//
//        sidebarMenuViewModel.whenCheckShowTutorial.observe(this, Observer {
//            sidebarMenuViewModel.getUserProfile()
//            if (it!!) {
//                startActivity(Intent(this, TutorialActivity::class.java))
//            }
//        })
//    }
//
//    override fun onHambergerClick() {
//        drawer_layout.openDrawer(Gravity.START, true)
//    }
//
//    private val onSidebarMenuListener = object : SidebarMenuWidget.OnSidebarMenuInteractionListener {
//        override fun onVersionClicked(txt: String) {
//            if (txt == "2" || txt == "1") {
//                showToast(txt)
//            } else {
//                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                val clipData = ClipData.newPlainText("Source Text", txt)
//                clipboardManager.primaryClip = clipData
//            }
//        }
//
//        override fun onAfterInteraction() {
//            drawer_layout.closeDrawers()
//        }
//
//        override fun onInstallmentClicked() {
//          // AnalyticsManager.mainMenuInstallmentClicked()
//            showHideFragment(findFragment(JobMainMenuFragment::class.java)
//                    ?: JobMainMenuFragment.newInstance())
//        }
//
//        override fun onTaxClicked() {
//          // AnalyticsManager.mainMenuTaxClicked()
//            showHideFragment(findFragment(TaxFragment::class.java)
//                    ?: TaxFragment.newInstance())
//        }
//
//        override fun onInsuranceClicked() {
//          // AnalyticsManager.mainMenuInsuranceClicked()
//            showHideFragment(findFragment(InsuranceFragment::class.java)
//                    ?: InsuranceFragment.newInstance())
//        }
//
//        override fun onQRCodeClicked() {
//          // AnalyticsManager.mainMenuQrClicked()
//            showHideFragment(findFragment(QRAllCarFragment::class.java)
//                    ?: QRAllCarFragment.newInstance())
//        }
//
//        override fun onCalculateInstallmentClick() {
//          // AnalyticsManager.mainMenuLoanCalculationClicked()
//            WebActivity.startWebCalculate(this@MainCustomerActivity)
//        }
//
//        override fun onNewsClicked() {
//          // AnalyticsManager.mainMenuNewsClicked()
//            showHideFragment(findFragment(NewsAndPromotionFragment::class.java)
//                    ?: NewsAndPromotionFragment.newInstance())
//        }
//
//        override fun onContactUsClicked() {
//          // AnalyticsManager.mainMenuContactUsClicked()
//            showHideFragment(
//                    findFragment(ContactUsMainFragment::class.java)
//                            ?: ContactUsMainFragment.newInstance()
//            )
//        }
//
//        override fun onOfficeLocationClicked() {
//          // AnalyticsManager.mainMenuLocationClicked()
//            showHideFragment(
//                    findFragment(JobRequestLocationFragment::class.java)
//                            ?: JobRequestLocationFragment.newInstance()
//            )
//        }
//
//        override fun onLiveChatClicked() {
//          // AnalyticsManager.mainMenuChatClicked()
//            LiveChatActivity.start(this@MainCustomerActivity)
//        }
//
//        override fun onDownloadClicked() {
//          // AnalyticsManager.mainMenuDownloadDocClicked()
//            showHideFragment(
//                    findFragment(DocumentDownloadFragment::class.java)
//                            ?: DocumentDownloadFragment.newInstance()
//            )
//        }
//
//        override fun onFAQClicked() {
//          // AnalyticsManager.mainMenuFaqClicked()
//            showHideFragment(
//                    findFragment(FAQFragment::class.java)
//                            ?: FAQFragment.newInstance()
//            )
//        }
//
//        override fun onTermAndConditionClicked() {
//          // AnalyticsManager.mainMenuTermConditionClicked()
//            showHideFragment(
//                    findFragment(PolicyFragment::class.java)
//                            ?: PolicyFragment.newInstance()
//            )
//        }
//
//        override fun onSettingClicked() {
//          // AnalyticsManager.mainMenuSettingClicked()
//            showHideFragment(findFragment(SettingFragment::class.java)
//                    ?: SettingFragment.newInstance())
//        }
//
//        override fun onSignoutClicked() {
//          // AnalyticsManager.mainMenuSignoutClicked()
//            /**
//             * Call No.59 API: logoutByCustomer
//             */
//            sidebarMenuViewModel.logoutByCustomer()
////            AuthPincodeActivity.startWithResult(this@MainCustomerActivity)
//        }
//
//        override fun onDebugClicked() {
//            BusManager.observe(DeviceLogonEvent())
////            DebugActivity.start(this@MainCustomerActivity)
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onChangeImageProfileReceived(event: ChangeImageProfileEvent) {
//        ic_profile.loadImageByUri(event.uri)
//    }
//
//    override fun onBackPressedSupport() {
//        IsExitDialogFragment.show(supportFragmentManager)
//    }
//
//    override fun onIsExitCancelClicked() {
//
//    }
//
//    override fun onIsExitConfirmClicked() {
//        finishAffinity()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        BusManager.unsubscribe(this)
//    }
//
//    companion object {
//        const val DATA_POSITION_EXTRA = "DATA_POSITION_EXTRA"
//        private const val MENU_POSITION_EXTRA = "MENU_POSITION_EXTRA"
//
//        const val INSTALLMENT_MENU_POSITION = 0
//        const val TAX_MENU_POSITION = 1
//        const val INSURANCE_MENU_POSITION = 2
//        const val CONTACTUS_MENU_POSITION = 3
//        const val SETTING_MENU_POSITION = 4
//        const val NEWS_PROMOTION_MENU_POSITION = 5
//        const val QRCODE_MENU_POSITION = 6
//        const val OFFICE_LOCATION_MENU_POSITION = 7
//        const val DOCUMENT_DOWNLOAD_MENU_POSITION = 8
//        const val FAQ_MENU_POSITION = 9
//        const val POLICY_MENU_POSITION = 10
//
//        fun start(context: Context) {
//            val intent = Intent(context, MainCustomerActivity::class.java).apply {
//                putExtra(MENU_POSITION_EXTRA, INSTALLMENT_MENU_POSITION)
//            }
//
//            context.startActivity(intent)
//        }
//
//        fun startWithClearStack(context: Context?) {
//            startWithClearStack(context, INSTALLMENT_MENU_POSITION)
//        }
//
//        fun startWithClearStack(context: Context?, position: Int = INSTALLMENT_MENU_POSITION) {
//            startWithClearStack(context, position, Bundle())
//        }
//
//        fun startWithClearStack(context: Context?,
//                                position: Int = INSTALLMENT_MENU_POSITION,
//                                data: Bundle) {
//            val intent = Intent(context, MainCustomerActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                putExtra(MENU_POSITION_EXTRA, position)
//                putExtra(DATA_POSITION_EXTRA, data)
//            }
//            context?.startActivity(intent)
//        }
//
//        fun startWithClearStackByDeeplink(context: Context?,
//                                          position: Int = INSTALLMENT_MENU_POSITION,
//                                          data: Bundle) {
//            val intent = Intent(context, MainCustomerActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                putExtra(MENU_POSITION_EXTRA, position)
//                putExtra(DATA_POSITION_EXTRA, data)
//            }
//            context?.startActivity(intent)
//        }
//    }
//}
