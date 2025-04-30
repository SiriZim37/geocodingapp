package com.tlt.georepo.modules.menu.notify

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tlt.georepo.common.base.BaseViewModel
import com.tlt.georepo.common.extension.getYears
import com.tlt.georepo.common.extension.toTwitterDatetime
import com.tlt.georepo.manager.LocalizeManager
import com.tlt.georepo.manager.db.NotifyManager
import com.tlt.georepo.util.AppUtils
import java.util.*

class NotifyMainFragmentViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenGetNotifyList = MutableLiveData<List<Notify>>()

    private val filterList by lazy {
        Array(10) { i ->
            if (LocalizeManager.isThai()) {
                if (i != 0) {
                    val currentThaiYear = Calendar.getInstance().get(Calendar.YEAR) + 543
                    (currentThaiYear - i + 1).toString()
                } else {
                    "ทั้งหมด"
                }
            } else {
                if (i != 0) {
                    val currentThaiYear = Calendar.getInstance().get(Calendar.YEAR)
                    (currentThaiYear - i + 1).toString()
                } else {
                    "All"
                }
            }
        }.toList()
    }

//    fun getCarData(contractNumber: String = "", yearFilter: String = "") {
//        val defaultCar = if (contractNumber.isEmpty()) {
////            CacheManager.getCacheCar()
//        } else {
//            DatabaseManager.getInstance()
//                .getMyCarList()
//                .first { it.contractNumber == contractNumber }
//        }
//
//        val model = Model(
//            carLicense = "${defaultCar?.regNumber} - ${defaultCar?.cREGPROVINCE}",
//            currentDate = defaultCar?.cURRENTDATE?.toDatetime() ?: "",
//            filterList = filterList
//        )
//
//        whenDataLoaded.postValue(model)
//
//        getNotifyList(yearFilter)
//    }

    fun getNotifyList(filterByYear: String = "") {
        whenLoading.postValue(true)

        val items = NotifyManager.getNotifyList()
            .map {
                val notifyType = if (it.image.isNotEmpty()) {
                    Notify.Type.FOUR
                } else {
                    Notify.Type.FIRST
                }
                Notify(
                    it.notifyKey,
                    it.description,
                    it.image,
                    it.navigation,
                    it.datetime.toString().toTwitterDatetime(),
                    it.iconColor,
                    notifyType,
                    it.datetime.getYears(),
                    it.navigation
                )
            }
            .filter {
                filterByYear.isEmpty() || filterByYear == it.year
            }

        whenGetNotifyList.postValue(items)
        whenLoading.postValue(false)
    }

    data class Model(
        val carLicense: String = "",
        val currentDate: String = "",
        val filterList: List<String> = listOf()
//        val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}