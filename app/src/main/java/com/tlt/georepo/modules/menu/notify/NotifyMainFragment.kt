package com.tlt.georepo.modules.menu.notify

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.gson.Gson
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseFragment
import com.tlt.georepo.common.eventbus.UpdateBadgeNotificationEvent
import com.tlt.georepo.common.extension.gone
import com.tlt.georepo.common.extension.visible
import com.tlt.georepo.manager.BusManager
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.manager.db.UserManager
import com.tlt.georepo.model.response.NotificationJsonResponse
import com.tlt.georepo.modules.main.MainMenuActivity
import kotlinx.android.synthetic.main.fragment_notify.*

class NotifyMainFragment : BaseFragment(){

    protected var mGoogleMap: GoogleMap? = null


    private var navigation: String = ""

    private val contractStatus by lazy {
        arguments?.getString(CONTRACT_STATUS) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(NotifyMainFragmentViewModel::class.java)
    }

    override fun onSupportInvisible() {

        super.onSupportInvisible()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notify, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getNotifyList()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun initViewModel() {

        viewModel.whenLoading.observe(this, Observer {

        })

        viewModel.whenDataLoaded.observe(this, Observer {
//            setupDataIntoViews(it!!)
        })

        viewModel.whenGetNotifyList.observe(this, Observer {
            swipe_refresh.isRefreshing = false
            if (it!!.isNotEmpty()) {
                text_error.gone()
                recycler_view.visible()
                val adapter = recycler_view.adapter as NotifyAdapter
                adapter.updateItems(it)
            } else {
                recycler_view.gone()
                text_error.visible()
            }
        })
    }

    private fun initInstances() {
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = NotifyAdapter(onNotifyListener)

        swipe_refresh.setOnRefreshListener {
            viewModel.getNotifyList()
        }


    }

    private val onNotifyListener = object : NotifyAdapter.Listener {
        override fun onRemoveClicked(notify: Notify) {
            DatabaseManager.getInstance().deleteNotifyByKey(notify.id)
            viewModel.getNotifyList()
        }

        override fun onSeemoreClicked(notify: Notify) {
//            AnalyticsManager.notificationDetial()
            navigation = notify.navigation
            showNotifyPopup(notify.navigation)
        }
    }

    private fun showNotifyPopup(navigation: String) {
                NoticeDialogFragment.show(
                    fragmentManager = fragmentManager,
                    fragment = this@NotifyMainFragment,
                    isShowButton = false,
                    item = getNotifyJsonDialog(navigation)
                )
    }

    fun onDetailButtonClicked() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(navigation)
        }
        startActivity(intent)
    }

    fun onCloseButtonClicked() {}


    override fun onSupportVisible() {
        UserManager.getInstance().setZeroUnreadNotification()
        BusManager.observe(UpdateBadgeNotificationEvent())
        super.onSupportVisible()
    }


    private fun getNotifyJsonDialog(data: String): NotificationJsonResponse {
        val dataForSplit = data.replace("jsonString=", "")
        val dataSplited = dataForSplit.split("&")

        val jsonString = Gson().fromJson(dataSplited[1], NotificationJsonResponse::class.java)
        return jsonString
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        private const val CONTRACT_STATUS = "contractStatus"

        fun newInstance() = NotifyMainFragment().apply {
            arguments = Bundle().apply {

            }
        }

    }
}
