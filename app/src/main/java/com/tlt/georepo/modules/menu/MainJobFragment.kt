package com.tlt.georepo.Fragment

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.tlt.georepo.R
import com.tlt.georepo.activity.GeoLabTestActivity
import com.tlt.georepo.activity.GeoUploadFileActivity
import com.tlt.georepo.manager.db.DatabaseManager
import com.tlt.georepo.model.response.NotificationJsonResponse
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.collection.JobCollectionMainMapActivity
import com.tlt.georepo.modules.menu.job.extension.JobExtensionMainMapActivity
import com.tlt.georepo.modules.menu.job.onhand.JobOnHandBtnSheetActivity
import com.tlt.georepo.modules.menu.job.request.JobRequestMapActivity
import kotlinx.android.synthetic.main.fragment_main_job_menu.*

import me.yokeyword.fragmentation.SupportFragment

class MainJobFragment : SupportFragment() {
    private var CRRENT_LAT: String = ""
    private var CRRENT_LNG: String = ""
    private var flagprivate: Boolean = false
    lateinit var actionBar: Toolbar

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainJobViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_job_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstance()
        FirebaseInstanceId.getInstance().token!!
    }

    fun initDatabase(isPrivate: Boolean) {
        flagprivate = isPrivate
    }

    fun initInstance() {
        viewModel.GetCurrentLocation()

    }

    fun initViewModel() {
        flagprivate = DatabaseManager.getInstance().getUserInfo().flagPrivate
        actionBar = activity!!.findViewById(R.id.toolbar)
        if (flagprivate) {
            actionBar.setTitle(getString(R.string.title_main_menu_home) + " (" + resources.getString(R.string.Offline_mode )+ ")" )
        } else {
            actionBar.setTitle(getString(R.string.title_main_menu_home))
        }
        btn_all.setOnClickListener {
//            if (flagprivate) {
//                showAlert(R.string.alert_offine)
//            } else {
                JobRequestMapActivity.Open(context!!)
//            }

        }

        btn_onhand.setOnClickListener {
//            if (flagprivate) {
//                showAlert(R.string.alert_offine)
//            } else {
                JobOnHandBtnSheetActivity.Open(context)
//            }


        }

//        btn_cancel.setOnClickListener{
//
//            JobCancelMapListActivityImpl.Open(context)
//        }

        btn_history.setOnClickListener {
//            if (flagprivate) {
//                showAlert(R.string.alert_offine)
//            } else {

//                JobExtensionMainMapActivity.Open(context!!)

//            }

            // Test Upload file
            GeoLabTestActivity.open(this!!.context!!)
        }

        btn_newjob.setOnClickListener {
//            if (flagprivate) {
//                showAlert(R.string.alert_offine)
//            } else {
                JobCollectionMainMapActivity.Open(context!!)
//            }
        }


        viewModel.whenDataLoadedLocationSuccess.observe(this, Observer {
            it?.let {
                CRRENT_LAT = it.latitude.toString()
                CRRENT_LNG = it.longitude.toString()

                // - do something
            }

        })
    }

    companion object {

        const val DATA_BY_DEEPLINK_DIALOG = "DATA_BY_DEEPLINK_DIALOG"
        const val IS_SHOW_BUTTON_IN_DIALOG = "IS_SHOW_BUTTON_IN_DIALOG"
        const val IS_SHOW_DIALOG_FROM_DEEPLINK = "IS_SHOW_DIALOG_FROM_DEEPLINK"

        fun newsInstance() = MainJobFragment()

        fun startByDeeplinkDialog(
            context: Context?,
            item: NotificationJsonResponse,
            isShowButtonDialog: Boolean = false,
            isShowDialog: Boolean = false
        ) {
            MainMenuActivity.openWithClearStack(
                context = context,
                position = 0,
                data = Bundle().apply {
                    putParcelable(DATA_BY_DEEPLINK_DIALOG, item)
                    putBoolean(IS_SHOW_BUTTON_IN_DIALOG, isShowButtonDialog)
                    putBoolean(IS_SHOW_DIALOG_FROM_DEEPLINK, isShowDialog)
                }
            )
        }
    }


    fun showAlert(id: Int) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val error = viewModel.setLanguage(id)
        val bt = viewModel.setLanguage(R.string.bt_ok)
        dialogBuilder.setMessage(error)
            .setCancelable(false)
            .setPositiveButton(bt, DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
//            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
//                    dialog, id -> dialog.cancel()
//            })
        val alert = dialogBuilder.create()
//        alert.setTitle("Please ensure that you you have entered all the required information")
        alert.show()
    }


}
