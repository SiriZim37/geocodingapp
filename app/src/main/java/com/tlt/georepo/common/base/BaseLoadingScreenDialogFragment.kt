package com.tlt.georepo.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.fragment_loading_screen_popup.*
import com.tlt.georepo.R

class BaseLoadingScreenDialogFragment : BaseDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loading_screen_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    private fun initInstance() {
        loading_screen_lottie.scale = 0.5f
        loading_screen_lottie.playAnimation()
        isCancelable = false
    }
//
//    fun showDialogActivity(fragmentManager: FragmentManager) {
//        show(fragmentManager, TAG)
//    }
//
//    fun showDialogFragment(fragmentManager: FragmentManager?,
//                           fragment: Fragment?) {
//        showFragment(fragmentManager!!, fragment)
//    }

    fun dismissDialog() {
        fragmentManager?.let {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    companion object {
        private const val TAG = "BaseLoadingScreenDialogFragment"

        fun newInstance() = BaseLoadingScreenDialogFragment()
    }
}