package com.tlt.georepo.modules.menu.job.onhand

import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_sts_cancel.*
import kotlinx.coroutines.launch
import java.lang.Exception
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.tlt.georepo.modules.main.MainMenuActivity
import com.tlt.georepo.modules.menu.job.onhand.statusmenu.CancelStatusActivity

class JobCancelDialogFragment : BaseDialogFragment() {
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()

    private val mainID by lazy {
        arguments?.getString("MAINID", "")?: ""
    }

    private val dataLat by lazy {
        arguments?.getString("CRRENT_LAT", "")?: ""
    }

    private val dataLng by lazy {
        arguments?.getString("CRRENT_LNG", "")?: ""
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_sts_cancel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        initViewModel()
    }

    private fun initViewModel() {
        whenDataLoadedSuccess.observe(this, Observer {
            MainMenuActivity.open(context!!)
        })
    }


    private fun initInstances() {
       btn_no.setOnClickListener {
            fragmentManager?.let {
                dismiss()

            }
        }

        btn_yes.setOnClickListener {
            fragmentManager?.let {
                CancelStatusActivity.start(context!!, mainID )
            }
        }
    }


    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    companion object {
        private val TAG = this::class.java.simpleName!!

        fun newInstance() = JobCancelDialogFragment()

        fun show(fragmentManager: FragmentManager, mainID: String, lat: String, lng: String ) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString("MAINID", mainID)
                    putString("CRRENT_LAT", lat)
                    putString("CRRENT_LNG", lng)
                }
                show(fragmentManager,TAG)
            }
        }
    }


}