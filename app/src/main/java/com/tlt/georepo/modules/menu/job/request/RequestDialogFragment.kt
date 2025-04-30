package com.tlt.georepo.modules.menu.job.request

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_dialog_sts_succes.*

class RequestDialogFragment :  BaseDialogFragment() {

    private val description by lazy {
        arguments?.getString(MESSAGE_DIALOG_EXTRA, "") ?: ""
    }
    private lateinit var listener: Listener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_sts_succes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        val statusRes =  description
        if(statusRes.equals("success")) {
            txt_status.text = "success" // activity!!.getString(R.string.location_hide)
        }else {
            txt_status.text = "fail" // activity!!.getString(R.string.location_hide)
        }
        btn_yes.setOnClickListener {
            fragmentManager?.let {
                dismiss()
                if(statusRes.equals("success"))
                    listener.onSuccessClickListener()

            }
        }
    }

    interface Listener {
        fun onSuccessClickListener()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }


    companion object {
        private val TAG = this::class.java.simpleName!!
        private val MESSAGE_DIALOG_EXTRA = "success"
        fun newInstance() = RequestDialogFragment()


        fun show(activity: Context,
                 description: String = "" ) {
                 newInstance().apply {
                    arguments = Bundle().apply {
                    putString(MESSAGE_DIALOG_EXTRA, description)
                }
                show(activity, TAG)
            }
        }
    }


}