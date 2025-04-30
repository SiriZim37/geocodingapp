package com.tlt.georepo.modules.menu.notify

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_notice.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.ifTrue
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.model.response.NotificationJsonResponse
import com.tlt.georepo.modules.menu.notify.subnotify.NotiJobMainDetailActivity
import com.tlt.georepo.util.CalendarUtils
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit


class NoticeDialogFragment : BaseDialogFragment() {

    private var listenerActivity: Listener? = null
    private var mainID: String = ""

    private val listenerFragment by lazy {
        targetFragment?.let { it as Listener }
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NoticeViewModel::class.java)
    }

    private val isShowButton by lazy {
        arguments?.getBoolean(IS_SHOW_BUTTTON, false)
    }

    private lateinit var item: NotificationJsonResponse

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onPause() {
        super.onPause()
//        AnalyticsManager.trackScreenDialog(AnalyticsScreenName.PUSH_POPUP)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_notice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    private fun initViewModel() {
        viewModel.whenBlockedNotifySuccess.observe(this, Observer {
            if (it!!) {

            }
        })


    }

    private fun setDataIntoView(it: NotificationJsonResponse) {
        it.title.isNotEmpty().ifTrue {
            txt_title.visible()
            txt_title.text = it.title

        }
        mainID = it.jobId


//        it.imageUrl.isNotEmpty().ifTrue {
//            thumbnail_popup.visible()
//            thumbnail_popup.loadImageByUrl(it.imageUrl)
//        }

        it.flagCheckBox.isNotEmpty().ifTrue {
            if (it.flagCheckBox.toLowerCase() == "y") {
                checkbox_ignore.visible()
            }
        }

        if("".equals(it.jobId) && "APPROVE".equals(it.notifyType)){
            btn_detail.gone()
            count_time.gone()

        }

        it.message.isNotEmpty().ifTrue {
            txt_description.visible()
            txt_description.autoLinkMask = Linkify.PHONE_NUMBERS
            txt_description.movementMethod = LinkMovementMethod.getInstance()
            txt_description.text = it.message
        }

        checkbox_ignore.gone()
    }



    private fun initInstances() {
        item = arguments!!.getParcelable(NOTIFICATION_ITEM)
        setDataIntoView(item)

        form_quotation_popup_btn_close.bringToFront()
        form_quotation_popup_btn_close.setOnClickListener {
            try {
                fragmentManager?.let { dismiss() }
                listenerActivity?.onCloseButtonClicked()
                listenerFragment?.onCloseButtonClicked()
            }catch (e: Exception ){
                Log.e("close_popup" , e.stackTrace.toString())
            }
        }

        btn_detail.setOnClickListener {
            try {
                    fragmentManager?.let {
                        NotiJobMainDetailActivity.Open(context!! ,mainID)
                        dismiss()
                    }

                    listenerActivity?.onDetailButtonClicked()
                    listenerFragment?.onDetailButtonClicked()
            }catch (e: Exception ){
                Log.e("view_detail" , e.stackTrace.toString())
            }
        }

        try {
            if( item.expDatetime >  CalendarUtils.getCurrentDateTime()){
                val expTime =  item.expDatetime.split(" ")
                val expSplit =   expTime[1].split(":")
                val currTime =  CalendarUtils.getCurrentDateTime().split(" ")
                val currSplit =  currTime[1].split(":")
                val start = Time(expSplit[0].toInt(), expSplit[1].toInt(), expSplit[2].toInt())
                val stop = Time(currSplit[0].toInt(), currSplit[1].toInt(), currSplit[2].toInt())
                val diff: Time
                diff = difference(start, stop)
                val timeDiff = "${diff.hours}:${diff.minutes}:${diff.seconds}"

                // 60 seconds (1 minute)
                val minute:Long = 1000 * 60 // 1000 milliseconds = 1 second
                // 1 day 2 hours 35 minutes 50 seconds
                val millisInFuture:Long = ( minute * diff.minutes)  + (1000 * diff.seconds)
                // Count down interval 1 second
                val countDownInterval:Long = 1000

//                val millisInFuture  = CalendarUtils.ConvertStringTimeToMillsecond(timeDiff)

                if (millisInFuture != null) {
                    timer(millisInFuture,countDownInterval).start()
                }
                btn_detail.visible()
            }else{
                count_time.text = "Time Out!"
                btn_detail.gone()
            }
        }catch (e : Exception){
            Log.e("expire_count_time" , e.stackTrace.toString())
        }

    }

    fun difference(start: Time, stop: Time): Time {
        val diff = Time(0, 0, 0)
        if (stop.seconds > start.seconds) {
            --start.minutes
            start.seconds += 60
        }
        diff.seconds = start.seconds - stop.seconds
        if (stop.minutes > start.minutes) {
            --start.hours
            start.minutes += 60
        }
        diff.minutes = start.minutes - stop.minutes
        diff.hours = start.hours - stop.hours
        return diff
    }


    // Method to configure and return an instance of CountDownTimer object
    private fun timer(millisInFuture:Long,countDownInterval:Long): CountDownTimer {
        return object: CountDownTimer(millisInFuture,countDownInterval){
            override fun onTick(millisUntilFinished: Long){
                try {
                    val timeRemaining = timeString(millisUntilFinished)
                    count_time.text = timeRemaining
                }catch (e:Exception){
                    Log.e("counting" , e.stackTrace.toString())
                }
            }
            override fun onFinish() {
                try {
                count_time.text = "Time Out!"
                }catch (e:Exception){
                    Log.e("counting" , e.stackTrace.toString())
                }
            }
        }
    }

    // Method to get days hours minutes seconds from milliseconds
    private fun timeString(millisUntilFinished:Long):String{
        var millisUntilFinished:Long = millisUntilFinished

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "%02d min: %02d sec",
             minutes,seconds
        )
    }



    interface Listener {
        fun onDetailButtonClicked()
        fun onCloseButtonClicked()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listenerActivity = context as Listener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    companion object {
        val TAG = this::class.java.simpleName

        const val NOTIFICATION_ITEM = "itemNotificationData"
        const val IS_SHOW_BUTTTON = "isShowButton"

        fun newInstance() = NoticeDialogFragment()

        fun show(fragmentManager: FragmentManager, item: NotificationJsonResponse,
                 isShowButton: Boolean) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTIFICATION_ITEM, item)
                    putBoolean(IS_SHOW_BUTTTON, isShowButton)
                }
                show(fragmentManager, TAG)
            }
        }

        fun show(fragmentManager: FragmentManager?,
                 fragment: Fragment?,
                 item: NotificationJsonResponse,
                 isShowButton: Boolean) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTIFICATION_ITEM, item)
                    putBoolean(IS_SHOW_BUTTTON, isShowButton)
                }

                setTargetFragment(fragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }
}