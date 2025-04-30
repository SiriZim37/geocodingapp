//package com.tlt.georepo.modules.customer
//
//import android.arch.lifecycle.MutableLiveData
//import com.tlt.georepo.common.base.BaseViewModel
//import com.tlt.georepo.common.extension.ifTrue
//import com.tlt.georepo.manager.api.tlt.TLTApiManager
//import com.tlt.georepo.model.request.BlockNotifyRequest
//import com.tlt.georepo.util.AppUtils
//
//class NoticeViewModel2 : BaseViewModel() {
//
//    val whenBlockedNotifySuccess = MutableLiveData<Boolean>()
//
//    fun sendAnswerBlockNotify(type: String, sequenceId: String) {
//
//        type.isEmpty().ifTrue {
//            whenBlockedNotifySuccess.postValue(false)
//            return
//        }
//
//        sequenceId.isEmpty().ifTrue {
//            whenBlockedNotifySuccess.postValue(false)
//            return
//        }
//
//        whenLoading.postValue(true)
//
//        val request = BlockNotifyRequest.build(type, sequenceId)
//
//        TLTApiManager.getInstance().blockNotify(request) { isError: Boolean, result: String ->
//            whenLoading.postValue(false)
//
//            if (isError) {
//                whenBlockedNotifySuccess.postValue(false)
//                return@blockNotify
//            }
//
//            whenBlockedNotifySuccess.postValue(true)
//        }
//
//    }
//
//    data class Model(
//            val type: String = "",
//            val title: String = "",
//            val imageUrl: String = "",
//            val description: String = "",
//            val isShowImage: Boolean = false,
//            val isShowIgnore: Boolean = false,
//            val isStaffApp: Boolean = AppUtils.isStaffApp()
//    )
//}