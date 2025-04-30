//package com.tlt.georepo.modules.menu.job.collection
//
//import android.app.Dialog
//import android.app.DialogFragment
//import android.content.Intent
//import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentActivity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.Window
//import com.tlt.georepo.R
//import com.tlt.georepo.modules.menu.job.collection.subcollection.CollectionMainDetailActivity
//import kotlinx.android.synthetic.main.fragment_dialog_collection.*
//
//class CollectionDialogFragment : DialogFragment() {
//
//    private val listener by lazy {
//        targetFragment as CollectionDialogFragment.Listener
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
//        return inflater.inflate(R.layout.fragment_dialog_collection, container, false)
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
//        btn_bg_map.setOnClickListener {
//            fragmentManager?.let {
//                val mapLocation = view.tag as JobCollectionMapLocation
//                val intent = Intent(dialog.context, JobCollectionMapActivity::class.java)
//                intent.putExtra(JobCollectionMapActivity.EXTRA_LATITUDE, mapLocation.lATITUDE.toDouble())
//                intent.putExtra(JobCollectionMapActivity.EXTRA_LONGITUDE, mapLocation.lONGITUDE.toDouble())
//                startActivity(intent)
//                dismiss()
//
//            }
//        }
//
//        btnJobDetail.setOnClickListener {
//            fragmentManager?.let {
//                val mapLocation = view.tag as JobCollectionMapLocation
//                CollectionMainDetailActivity.start(dialog.context , mapLocation.mAINID)
//                dismiss()
////                listener.onAttachPorlorborClicked()
//            }
//        }
//
//        btnGotowork.setOnClickListener {
//            fragmentManager?.let {
//                dismiss()
////                listener.onAttachPorlorborClicked()
//            }
//        }
//
//        btn_cancel.setOnClickListener {
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
//        fun newInstance() = CollectionDialogFragment()
//
//        fun show(fragmentManager: FragmentActivity?, targetFragment: Fragment ) {
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