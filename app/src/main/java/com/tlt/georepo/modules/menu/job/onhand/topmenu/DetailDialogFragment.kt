//package com.tlt.georepo.modules.menu.job.onhand.topmenu
//
//import android.app.Dialog
//import android.app.DialogFragment
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentActivity
//import android.support.v4.app.FragmentManager
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import com.tlt.georepo.R
//import kotlinx.android.synthetic.main.fragment_dialog_detail.*
//
//class DetailDialogFragment : DialogFragment() {
//
//    private val listener by lazy {
//        targetFragment as DetailDialogFragment.Listener
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
//        return dialog
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_dialog_detail, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initInstances()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        dialog.window.setLayout(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        }
//
//    private fun initInstances() {
//
//        btn_close.setOnClickListener {
//            fragmentManager?.let {
//                dismiss()
////                listener.onAttachPorlorborClicked()
//            }
//        }
//    }
//
//    companion object {
//        private val TAG = this::class.java.simpleName!!
//
//        fun newInstance() = DetailDialogFragment()
//
//        fun show(fragmentManager: FragmentActivity?, targetFragment: Fragment) {
//            newInstance().apply {
//                show(getFragmentManager(), TAG)
//            }
//        }
//    }
//
//    interface Listener {
//
//        fun onCancelDialogNoClickListener()
//    }
//}