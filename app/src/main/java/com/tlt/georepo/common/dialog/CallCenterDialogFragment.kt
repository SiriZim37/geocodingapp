package com.tlt.georepo.common.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_callcenter.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.tlt.georepo.manager.ContextManager


class CallCenterDialogFragment : BaseDialogFragment() {

    private val phoneNumber by lazy {
        arguments?.getString(PHONE_NUMBER, "02-660-5555") ?: "02-660-5555"
    }

    private val displayPhoneNumber by lazy {
        arguments?.getString(DISPLAY_PHONE_NUMBER, "02-660-5555") ?: "02-660-5555"
    }

    private val openBy by lazy {
        arguments?.getInt(OPEN_BY, 0) ?: SELECT_PHONE
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_callcenter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TLTTextView.text = ContextManager.getInstance().getApplicationContext().getString(R.string.callcenter_dialog_description, displayPhoneNumber)

        btn_cancel.setOnClickListener {

            when(openBy) {
                LOCATION -> {}
            }
            fragmentManager?.let { dismiss() }
        }

        btn_call.setOnClickListener {
            when(openBy) {
                LOCATION -> {}
            }

            fragmentManager?.let {
                val intent = Intent(Intent.ACTION_DIAL,
                        Uri.fromParts("tel", phoneNumber, null))
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onPause() {
        super.onPause()
    }

    companion object {
        val TAG = this::class.java.simpleName
        private const val DISPLAY_PHONE_NUMBER = "displayPhoneNumber"
        private const val PHONE_NUMBER = "phoneNumber"
        private const val OPEN_BY = "openBy"

        const val SELECT_PHONE = 0
        const val LOCATION = 1
        const val CONTACT_US_MAIN = 2
        const val LOCATION_DETAIL = 3

        fun newInstance() = CallCenterDialogFragment()

        fun show(fragmentManager: FragmentManager?, phoneNumber: String = "02-660-5555",
                 displayPhoneNumber: String = "02-660-5555",
                 openBy: Int = SELECT_PHONE) {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString(PHONE_NUMBER, phoneNumber)
                    putString(DISPLAY_PHONE_NUMBER, displayPhoneNumber)
                    putInt(OPEN_BY, openBy)
                }
                show(fragmentManager, TAG)
            }
        }
    }
}