package com.tlt.georepo.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_normal.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment
import com.tlt.georepo.common.extension.gone

class NormalDialogFragment : BaseDialogFragment() {

    private var listenerActivity: Listener? = null
    private val listenerFragment by lazy {
        targetFragment?.let { it as Listener }
    }

    private val description by lazy {
        arguments?.getString(MESSAGE_DIALOG_EXTRA, "") ?: ""
    }

    private val confirmButtonMessage by lazy {
        arguments?.getString(CONFIRM_DIALOG_EXTRA, "") ?: ""
    }

    private val cancelButtonMessage by lazy {
        arguments?.getString(CANCEL_DIALOG_EXTRA, "") ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_normal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
    }

    private fun initInstances() {
        title.text = description
        btn_confirm.text = confirmButtonMessage
        btn_cancel.text = cancelButtonMessage

        if (confirmButtonMessage.isEmpty()) {
            btn_confirm.gone()
            divider.gone()
        }

        if (cancelButtonMessage.isEmpty()) {
            btn_cancel.gone()
            divider.gone()
        }

        btn_cancel.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onDialogCancelClick()
            listenerFragment?.onDialogCancelClick()
        }

        btn_confirm.setOnClickListener {
            fragmentManager?.let { dismiss() }
            listenerActivity?.onDialogConfirmClick()
            listenerFragment?.onDialogConfirmClick()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            listenerActivity = context as Listener
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    interface Listener {
        fun onDialogConfirmClick()
        fun onDialogCancelClick()
    }

    companion object {
        private const val TAG = "NormalDialogFragment"
        private const val MESSAGE_DIALOG_EXTRA = "MESSAGE_DIALOG_EXTRA"
        private const val CONFIRM_DIALOG_EXTRA = "CONFIRM_DIALOG_EXTRA"
        private const val CANCEL_DIALOG_EXTRA = "CANCEL_DIALOG_EXTRA"

        fun newInstance() = NormalDialogFragment()

        fun show(fragmentManager: FragmentManager?,
                 description: String = "",
                 confirmButtonMessage: String = "",
                 cancelButtonMessage: String = "") {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE_DIALOG_EXTRA, description)
                    putString(CONFIRM_DIALOG_EXTRA, confirmButtonMessage)
                    putString(CANCEL_DIALOG_EXTRA, cancelButtonMessage)
                }

                show(fragmentManager, TAG)
            }
        }

        fun show(fragmentManager: FragmentManager?,
                 fragment: Fragment?,
                 description: String = "",
                 confirmButtonMessage: String = "",
                 cancelButtonMessage: String = "") {
            newInstance().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE_DIALOG_EXTRA, description)
                    putString(CONFIRM_DIALOG_EXTRA, confirmButtonMessage)
                    putString(CANCEL_DIALOG_EXTRA, cancelButtonMessage)
                }

                setTargetFragment(fragment, 1)
                show(fragmentManager, TAG)
            }
        }
    }
}