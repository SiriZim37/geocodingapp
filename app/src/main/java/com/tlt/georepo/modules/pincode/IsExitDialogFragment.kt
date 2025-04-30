package com.tlt.georepo.modules.pincode

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_dialog_is_exit.*
import com.tlt.georepo.R
import com.tlt.georepo.common.base.BaseDialogFragment

class IsExitDialogFragment : BaseDialogFragment() {

    private lateinit var listener: Listener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_is_exit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_cancel.setOnClickListener {
            fragmentManager?.let {
                dismiss()
                listener.onIsExitCancelClicked()
            }
        }

        btn_confirm.setOnClickListener {
            fragmentManager?.let {
                dismiss()
                listener.onIsExitConfirmClicked()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    interface Listener {
        fun onIsExitCancelClicked()
        fun onIsExitConfirmClicked()
    }

    companion object {
        val TAG = this::class.java.simpleName

        fun newInstance() = IsExitDialogFragment()

        fun show(fragmentManager: FragmentManager) {
            newInstance().show(fragmentManager, TAG)
        }
    }
}