//package com.tlt.georepo.modules.customer
//
//import android.app.Dialog
//import android.arch.lifecycle.Observer
//import android.arch.lifecycle.ViewModelProviders
//import android.content.Context
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
//import android.text.method.LinkMovementMethod
//import android.text.util.Linkify
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import kotlinx.android.synthetic.main.fragment_dialog_notice.*
//import com.tlt.georepo.R
//import com.tlt.georepo.AnalyticsManager
//import com.tlt.georepo.AnalyticsScreenName
//import com.tlt.georepo.common.base.BaseDialogFragment
//import com.tlt.georepo.common.extension.ifTrue
//import com.tlt.georepo.common.extension.loadImageByUrl
//import com.tlt.georepo.common.extension.visible
//import com.tlt.georepo.model.response.NotificationJsonResponse
//
//class NoticeDialogFragment2 : BaseDialogFragment() {
//
//    private var listenerActivity: Listener? = null
//
//    private val listenerFragment by lazy {
//        targetFragment?.let { it as Listener }
//    }
//
//    private val viewModel by lazy {
//        ViewModelProviders.of(this).get(NoticeViewModel2::class.java)
//    }
//
//    private val isShowButton by lazy {
//        arguments?.getBoolean(IS_SHOW_BUTTTON, false)
//    }
//
//    private lateinit var item: NotificationJsonResponse
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
//        return dialog
//    }
//
//    override fun onPause() {
//        super.onPause()
//      // AnalyticsManager.trackScreenDialog(AnalyticsScreenName.PUSH_POPUP)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_dialog_notice, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        initViewModel()
//        initInstances()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
//    }
//
//    private fun initViewModel() {
//        viewModel.whenBlockedNotifySuccess.observe(this, Observer {
//            if (it!!) {
//
//            }
//        })
//    }
//
//    private fun setDataIntoView(it: NotificationJsonResponse) {
//        it.title.isNotEmpty().ifTrue {
//            txt_title.visible()
//            txt_title.text = it.title
//        }
//
//        it.message.isNotEmpty().ifTrue {
//            txt_description.visible()
//            txt_description.autoLinkMask = Linkify.PHONE_NUMBERS
//            txt_description.movementMethod = LinkMovementMethod.getInstance()
//            txt_description.text = it.message
//        }
//
//        it.imageUrl.isNotEmpty().ifTrue {
//            thumbnail_popup.visible()
//            thumbnail_popup.loadImageByUrl(it.imageUrl)
//        }
//
//        it.flagCheckBox.isNotEmpty().ifTrue {
//            if (it.flagCheckBox.toLowerCase() == "y") {
//                checkbox_ignore.visible()
//            }
//        }
//
//        isShowButton?.ifTrue {
//            btn_detail.visible()
//        }
//    }
//
//    private fun initInstances() {
//        item = arguments!!.getParcelable(NOTIFICATION_ITEM)
//        setDataIntoView(item)
//
//        form_quotation_popup_btn_close.bringToFront()
//        form_quotation_popup_btn_close.setOnClickListener {
//          // AnalyticsManager.popupNotifyClose()
//            checkbox_ignore.isChecked.ifTrue {
//              // AnalyticsManager.popupNotifyCheckbox()
//                viewModel.sendAnswerBlockNotify(item.notifyType, item.sequenceId)
//            }
//            fragmentManager?.let { dismiss() }
//            listenerActivity?.onCloseButtonClicked()
//            listenerFragment?.onCloseButtonClicked()
//        }
//
//        btn_detail.setOnClickListener {
//          // AnalyticsManager.popupNotifyDetail(adName = item.title)
//            checkbox_ignore.isChecked.ifTrue {
//              // AnalyticsManager.popupNotifyCheckbox()
//                viewModel.sendAnswerBlockNotify(item.notifyType, item.sequenceId)
//            }
//            fragmentManager?.let { dismiss() }
//            listenerActivity?.onDetailButtonClicked()
//            listenerFragment?.onDetailButtonClicked()
//        }
//    }
//
//    interface Listener {
//        fun onDetailButtonClicked()
//        fun onCloseButtonClicked()
//    }
//
//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//
//        try {
//            listenerActivity = context as Listener
//        } catch (e: ClassCastException) {
//            e.printStackTrace()
//        }
//    }
//
//    companion object {
//        val TAG = this::class.java.simpleName
//
//        const val NOTIFICATION_ITEM = "itemNotificationData"
//        const val IS_SHOW_BUTTTON = "isShowButton"
//
//        fun newInstance() = NoticeDialogFragment2()
//
//        fun show(fragmentManager: FragmentManager, item: NotificationJsonResponse,
//                 isShowButton: Boolean) {
//            newInstance().apply {
//                arguments = Bundle().apply {
//                    putParcelable(NOTIFICATION_ITEM, item)
//                    putBoolean(IS_SHOW_BUTTTON, isShowButton)
//                }
//                show(fragmentManager, TAG)
//            }
//        }
//
//        fun show(fragmentManager: FragmentManager?,
//                 fragment: Fragment?,
//                 item: NotificationJsonResponse,
//                 isShowButton: Boolean) {
//            newInstance().apply {
//                arguments = Bundle().apply {
//                    putParcelable(NOTIFICATION_ITEM, item)
//                    putBoolean(IS_SHOW_BUTTTON, isShowButton)
//                }
//
//                setTargetFragment(fragment, 1)
//                show(fragmentManager, TAG)
//            }
//        }
//    }
//}